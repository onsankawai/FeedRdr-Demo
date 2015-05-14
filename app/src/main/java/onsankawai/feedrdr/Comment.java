package onsankawai.feedrdr;

import android.util.Log;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Streamphony on 14/05/2015.
 */
public class Comment implements JsonParsable{
    private static final int ADD_COMMENT_URL_RES = R.string.add_comment_url;
    private static final int UPDATE_COMMENT_URL_RES = R.string.update_comment_url;

    private String id;
    private String userId;
    private String content;
    private String date;
    private int votes;

    public Comment(String id, String userId, String content, String date, int votes) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.date = date;
        this.votes = votes;
    }

    public String getUserName(){
        return "";
    }

    public String getContent() {
        return content;
    }

    public String getUserImageUrl(){
        return "";
    }

    public void updateVotes() {
        String queryString = "id="+id+"&votes="+votes;
        //doPostRequest(queryString);
    }



}
