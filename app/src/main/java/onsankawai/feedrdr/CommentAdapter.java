package onsankawai.feedrdr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by Streamphony on 14/05/2015.
 */
public class CommentAdapter extends ArrayAdapter<JsonParsable> {
    static class ViewHolder {
        TextView userName;
        TextView content;
        ImageView userImage;
    }

    public CommentAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(getContext());
            convertView = layoutInflator.inflate(R.layout.row_comment, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.userName = (TextView)convertView.findViewById(R.id.commentUserName);
            viewHolder.content = (TextView)convertView.findViewById(R.id.commentContent);
            viewHolder.userImage = (ImageView) convertView .findViewById(R.id.commentProfileImg);

            convertView.setTag(viewHolder);
        }

        ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        //mLoader.DisplayImage(getItem(position), holder.imageView);
        Comment comment = (Comment)this.getItem(position);

        /*
        viewHolder.userName.setText(comment);
        viewHolder.description.setText(feed.getDesc());
        //Log.d("DEBUG", feed.getThumbnailUrl());
        dataSourceLoader.lazyLoadImage(this, feed.getThumbnailUrl(), viewHolder.thumbnail);
*/
        return convertView;
    }

}
