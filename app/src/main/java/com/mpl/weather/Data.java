package com.mpl.weather;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Data {
    @PrimaryKey(autoGenerate = true)
    public int dataId;

    public String date;

    public void setDate(String date) {
        this.date = date;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public String photoURL;
    public String weather;
    public Integer rate;

    @Override
    public String toString() {
        return "Data{" +
                "dataId=" + dataId +
                ", date='" + date + '\'' +
                ", photoURL='" + photoURL + '\'' +
                ", weather='" + weather + '\'' +
                ", rate=" + rate +
                '}';
    }
}
