package com.mahmoudgalal.mindvalley_mahmoudgalal_android_test.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mahmoud Galal on 30/09/2016.
 */
public class PinModel implements Parcelable {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private  String id;
    @SerializedName("created_at")
    private  String  creationTime;
    private int height;
    private int width;
    private User user;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(id);
        parcel.writeString(creationTime);
        parcel.writeInt(height);
        parcel.writeInt(width);
        parcel.writeParcelable(user,i);
    }
    public static final Creator<PinModel> CREATOR
            = new Creator<PinModel>() {
        public PinModel createFromParcel(Parcel in) {
            return new PinModel(in);
        }

        public PinModel[] newArray(int size) {
            return new PinModel[size];
        }
    };
    private PinModel(Parcel in){
        id = in.readString();
        creationTime = in.readString();
        height = in.readInt();
        width = in.readInt();
        user = in.readParcelable(User.class.getClassLoader());
    }


    public  static  class User implements Parcelable{
        private String id;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ProfileImages getProfileImage() {
            return profileImage;
        }

        public void setProfileImage(ProfileImages profileImage) {
            this.profileImage = profileImage;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @SerializedName("username")
        private String userName;
        private  String name ;
        @SerializedName("profile_image")
        private ProfileImages profileImage;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(id);
            parcel.writeString(userName);
            parcel.writeString(name);
            parcel.writeParcelable(profileImage,i);
        }
        public static final Creator<User> CREATOR
                = new Creator<User>() {
            public User createFromParcel(Parcel in) {
                return new User(in);
            }

            public User[] newArray(int size) {
                return new User[size];
            }
        };
        private User(Parcel in){
            id = in.readString();
            userName = in.readString();
            name = in.readString();
            profileImage = in.readParcelable(ProfileImages.class.getClassLoader());
        }

        public static  class ProfileImages implements Parcelable{
            public String getSmall() {
                return small;
            }

            public void setSmall(String small) {
                this.small = small;
            }

            public String getMedium() {
                return medium;
            }

            public void setMedium(String medium) {
                this.medium = medium;
            }

            public String getLarge() {
                return large;
            }

            public void setLarge(String large) {
                this.large = large;
            }

            private String small;
            private String medium;
            private String large;

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel parcel, int i) {
                    parcel.writeString(small);
                parcel.writeString(medium);
                parcel.writeString(large);
            }
            public static final Creator<ProfileImages> CREATOR
                    = new Creator<ProfileImages>() {
                public ProfileImages createFromParcel(Parcel in) {
                    return new ProfileImages(in);
                }

                public ProfileImages[] newArray(int size) {
                    return new ProfileImages[size];
                }
            };
            private ProfileImages(Parcel in){
                small = in.readString();
                medium = in.readString();
                large = in.readString();

            }
        }
    }
}
