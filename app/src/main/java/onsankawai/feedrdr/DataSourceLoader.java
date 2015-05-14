package onsankawai.feedrdr;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import org.apache.shiro.util.SoftHashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Streamphony on 09/05/2015.
 */
public class DataSourceLoader {
    private static final String DATE_FORMAT = "E, dd MMM yyyy k:m:s z";
    private SimpleDateFormat mDateFormat;

    private static DataSourceLoader mInstance = null;

    // ID identifying the link of the source data
    private int mSourceId = 1;

    // Handler updating adapter UI events
    private Handler mUIUpdateHandler;
    private XmlPullParserFactory mXMLFactoryObject;
    private Resources resources;

    // Softreference on Bitmap objects
    private SoftHashMap<String,Bitmap> imageCache;

    private DataSourceLoader() {
        resources = MainActivity.ApplicationContext.getResources();
        mDateFormat = new SimpleDateFormat(DATE_FORMAT);
        mDateFormat.setTimeZone(TimeZone.getDefault());
        mUIUpdateHandler = new Handler();
        imageCache = new SoftHashMap<String,Bitmap>(50);
        try {
            mXMLFactoryObject = XmlPullParserFactory.newInstance();
            mXMLFactoryObject.setNamespaceAware(true);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    public static DataSourceLoader getDataSourceLoader() {
        if( mInstance == null) {
            mInstance = new DataSourceLoader();
        }
        return mInstance;
    }

    /**
     * Set the source id, which indeed switching to the corresponding source link, see the loadDataSource method
     * @param sourceId
     */
    public void setSource(int sourceId) {
        mSourceId = sourceId;
    }

    private String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }

    /**
     * Parse XML when it is ready, update UI via adapter, called on a separate thread
     * see loadDataSource method
     * @param parser
     * @param adapter
     */
    private void onXMLReady(XmlPullParser parser, final ArrayAdapter<JsonParsable> adapter) {
        //mAdapter.clear();
        final List<NewsFeed> feedList = new ArrayList<NewsFeed>();
        NewsFeed feed;
        int eventType = 0;
        try {
            /*
            while (!parser.getName().equalsIgnoreCase("item")) {
                parser.next();
            }
*/
            eventType = parser.getEventType();
            String title = "", desc = "", sourceUrl = "", date = "", thumbnailUrl = "";
            while (eventType != XmlPullParser.END_DOCUMENT) {

                if (eventType == XmlPullParser.START_TAG) {
                    if (parser.getName().equalsIgnoreCase("title")) {
                        title = parser.nextText();
                    } else if (parser.getName().equalsIgnoreCase("description")) {
                        desc = parser.nextText();
                    } else if (parser.getName().equalsIgnoreCase("link")) {
                        sourceUrl = parser.nextText();
                    } else if (parser.getName().equalsIgnoreCase("pubDate")) {

                        date = mDateFormat.format(mDateFormat.parse(parser.nextText()));
                    } else if (parser.getName().equalsIgnoreCase("thumbnail")) {
                        thumbnailUrl = parser.getAttributeValue(null, "url");
                    }

                } else if(eventType == XmlPullParser.END_TAG) {
                    if (parser.getName().equalsIgnoreCase("item")) {
                        feedList.add(new NewsFeed(title,desc,date,sourceUrl,thumbnailUrl));
                        title = "";
                        desc = "";
                        date = "";
                        sourceUrl = "";
                        thumbnailUrl = "";
                    }
                }

                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mUIUpdateHandler.post(new Runnable() {
            public void run() {
                adapter.clear();
                adapter.addAll(feedList);
                adapter.notifyDataSetChanged();
            }
        });


    }

    private void onJsonReady(String jsonResult, final ArrayAdapter<JsonParsable> adapter) {
        try {
            final List<JsonParsable> itemList = new ArrayList<JsonParsable>();
            JSONObject jsonObj = new JSONObject(jsonResult);
            JSONArray items = null;
            if(jsonObj.has("threads")) {
                items = jsonObj.getJSONArray("threads");
                for(int i = 0; i < items.length(); i++) {
                    JSONObject item = items.getJSONObject(i);
                    String title = item.getString("title");
                    String desc = item.getString("desc");
                    String date = item.getString("pub_date");
                    String srcLink = item.getString("src_link");
                    String imgLink = item.getString("img_link");
                    itemList.add(new NewsFeed(title, desc, date, srcLink, imgLink));
                }

            } else if(jsonObj.has("comments")) {
                items = jsonObj.getJSONArray("comments");
                for(int i = 0; i < items.length(); i++) {
                    JSONObject item = items.getJSONObject(i);
                    String title = item.getString("title");
                    String desc = item.getString("desc");
                    String date = item.getString("pub_date");
                    String srcLink = item.getString("src_link");
                    String imgLink = item.getString("img_link");
                    itemList.add(new NewsFeed(title, desc, date, srcLink, imgLink));
                }
            }


            mUIUpdateHandler.post(new Runnable() {
                public void run() {
                    adapter.clear();
                    adapter.addAll(itemList);
                    adapter.notifyDataSetChanged();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load selected data source to adapter, on a separate thread
     * @param adapter
     */
    public void loadDataSource(final ArrayAdapter<JsonParsable> adapter,final String datasrcUrl, final String queryString) {

        new Thread(new Runnable(){
            @Override
            public void run() {
                InputStream stream = null;
                XmlPullParser xmlParser = null;
                try {
                    stream = NetworkHelper.doRequest(datasrcUrl, queryString, "GET");
                    if(stream != null) {
                        //String result = getStringFromInputStream(stream);
                        xmlParser = mXMLFactoryObject.newPullParser();
                        xmlParser.setInput(stream, null);
                        onXMLReady(xmlParser, adapter);
                        stream.close();

                        //onJsonReady(result, adapter);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    /**
     * Lazy loading an image on a separate thread, via cache if hit, via network if miss,
     * then update the referenced imageView
     * @param adapter
     * @param imgUrl
     * @param imageView
     */
    public void lazyLoadImage(final ArrayAdapter<JsonParsable> adapter, final String imgUrl, final ImageView imageView) {
        if(imageCache.containsKey(imgUrl)) { // HIT
            imageView.setImageBitmap(imageCache.get(imgUrl));
            return;
        }

        new Thread(new Runnable(){

            @Override
            public void run() {
                InputStream is = NetworkHelper.doRequest(imgUrl, null, "GET");
                if(is != null) {
                    final Bitmap image = BitmapFactory.decodeStream(is);
                    imageCache.put(imgUrl, image);

                    mUIUpdateHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(image);
                        }
                    });
                }

            }
        }).start();


    }
    
    public void loadImage(final String imgUrl, final ImageView imageView) {
        new Thread(new Runnable(){

            @Override
            public void run() {
                InputStream is = NetworkHelper.doRequest(imgUrl, null, "GET");
                if(is != null) {
                    final Bitmap image = BitmapFactory.decodeStream(is);
                    imageCache.put(imgUrl, image);

                    mUIUpdateHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(image);
                        }
                    });
                }

            }
        }).start();
    }


}
