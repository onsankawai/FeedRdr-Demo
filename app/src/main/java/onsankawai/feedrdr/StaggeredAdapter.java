package onsankawai.feedrdr;

import android.content.Context;
import android.content.res.Resources;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import origamilabs.library.views.StaggeredGridView;

//import com.example.staggeredgridviewdemo.loader.ImageLoader;
//import com.example.staggeredgridviewdemo.views.ScaleImageView;

public class StaggeredAdapter extends ArrayAdapter<JsonParsable> {

	static class ViewHolder {
		TextView title;
		TextView description;
		TextView publishDate;
		ImageView thumbnail;
	}

	private DataSourceLoader dataSourceLoader;


	public StaggeredAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		dataSourceLoader = DataSourceLoader.getDataSourceLoader();
	}

	public void switchSection(int s) {
		Resources resources = MainActivity.ApplicationContext.getResources();
		String datasrcUrl = null;
		switch(s) {
			case 1:
				datasrcUrl = resources.getString(R.string.datasrc_section1);
				break;
			case 2:
				datasrcUrl = resources.getString(R.string.datasrc_section2);
				break;
			case 3:
				datasrcUrl = resources.getString(R.string.datasrc_section3);
				break;
			case 4:
				datasrcUrl = resources.getString(R.string.datasrc_section4);
				break;
			case 5:
				datasrcUrl = resources.getString(R.string.datasrc_section5);
				break;
			default:
				datasrcUrl = resources.getString(R.string.datasrc_section1);
				break;
		}


		dataSourceLoader.loadDataSource(this,datasrcUrl,null);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater layoutInflator = LayoutInflater.from(getContext());
			convertView = layoutInflator.inflate(R.layout.grid_staggered, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.title = (TextView)convertView.findViewById(R.id.gridTitle);
			viewHolder.description = (TextView)convertView.findViewById(R.id.gridDesc);
			viewHolder.thumbnail = (ImageView) convertView .findViewById(R.id.gridImageView);

			convertView.setTag(viewHolder);
		}

		ViewHolder viewHolder = (ViewHolder) convertView.getTag();

		//mLoader.DisplayImage(getItem(position), holder.imageView);
		NewsFeed feed = (NewsFeed)this.getItem(position);

		viewHolder.title.setText(feed.getTitle());
		viewHolder.description.setText(feed.getDesc());
		//Log.d("DEBUG", feed.getThumbnailUrl());
		dataSourceLoader.lazyLoadImage(this,feed.getThumbnailUrl(),viewHolder.thumbnail);

		return convertView;
	}

}
