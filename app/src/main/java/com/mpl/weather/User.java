package com.mpl.weather;

public class User {
    private String uid;
    private String id;
    private static User instance = null;

    public static User getInstance(String uid, String id) {
        if (instance == null) {
            instance = new User(uid, id);
        }
        return instance;
    }

    User(String uid, String id) {
        this.uid = uid;
        this.id = id;
    }

    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
}
