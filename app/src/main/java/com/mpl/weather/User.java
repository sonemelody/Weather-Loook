package com.mpl.weather;

public class User {
    private String uid;
    private static User instance = null;

    public static User getInstance(String uid) {
        if (instance == null) {
            instance = new User(uid);
        }
        return instance;
    }

    User(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
