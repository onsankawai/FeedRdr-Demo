package onsankawai.feedrdr;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Streamphony on 14/05/2015.
 */
public class AppUser implements Parcelable{
    private static final int USER_ID = 0;
    private static final int DISPLAY_NAME = 1;
    private static final int IMG_URL = 2;


    private String id;
    private String displayName;
    private String profileImgUrl;

    public AppUser(String id, String name, String imgUrl){
        this.id = id;
        this.displayName = name;
        this.profileImgUrl = imgUrl.replaceFirst("sz=[0-9]+","sz=100");
    }

    public AppUser(Parcel in){
        String[] data = new String[3];

        in.readStringArray(data);
        this.id = data[USER_ID];
        this.displayName = data[DISPLAY_NAME];
        this.profileImgUrl = data[IMG_URL];
    }

    public String getId(){
        return id;
    }

    public String getDisplayName(){
        return displayName;
    }

    public String getProfileImgUrl(){
        return profileImgUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{
                this.id,
                this.displayName,
                this.profileImgUrl
        });
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public AppUser createFromParcel(Parcel in) {
            return new AppUser(in);
        }

        public AppUser[] newArray(int size) {
            return new AppUser[size];
        }
    };


}
