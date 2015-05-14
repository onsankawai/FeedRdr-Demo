package onsankawai.feedrdr;

import java.util.Date;

/**
 * Created by Streamphony on 09/05/2015.
 */
public class NewsFeed implements JsonParsable{
    private String mTitle;
    private String mDesc;
    private String mSourceUrl;
    private String mThumbnailUrl;
    private String mLocalDate;

    public NewsFeed(String title, String desc, String date, String sourceUrl, String thumbnailUrl) {
        mTitle = title;
        mDesc = desc;
        mLocalDate = date;
        mSourceUrl = sourceUrl;
        mThumbnailUrl = thumbnailUrl;
    }

    String getTitle() {
        return mTitle;
    }

    String getDesc() {
        return mDesc;
    }

    String getDate() {
        return mLocalDate;
    }

    String getSourceUrl() {
        return mSourceUrl;
    }

    String getThumbnailUrl() {
        return mThumbnailUrl;
    }
}
