package com.kuldiegor;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class XMLModel2 {
    @SerializedName("Payload")
    public Payload mPayload;

    public static class Payload {

        @SerializedName("Cities")
        public Cities mCities;

        @SerializedName("Users")
        public Users mUsers;

        public static class Cities {
            @SerializedName("City")
            public List<City> mCityList;

            public static class City {
                @SerializedName("id")
                public String mId;

                @SerializedName("content")
                public String mContent;
            }
        }

        public static class Users {
            @SerializedName("User")
            public List<User> mUserList;

            public static class User {
                @SerializedName("city")
                public String mCity;

                @SerializedName("email")
                public String mEmail;

                @SerializedName("fullName")
                public String mFullName;

                public static class Email {
                    @SerializedName("content")
                    public String mContent;
                }

                public static class FullName {
                    @SerializedName("content")
                    public String mContent;
                }
            }
        }
    }
}
