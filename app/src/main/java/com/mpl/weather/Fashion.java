package com.mpl.weather;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Fashion {
    @PrimaryKey(autoGenerate = true)
    public int fashionId;

    public String date;
    public List<Cloth> clothList;
    public String weather;
    public String rate;
    public String photoURL;

    public Fashion(String date, List<Cloth> clothList, String weather, String rate, String photoURL) {
        this.date = date;
        this.clothList = clothList;
        this.weather = weather;
        this.rate = rate;
        this.photoURL = photoURL;
    }

    @Override
    public String toString() {
        return "Fashion{" +
                "fashionId=" + fashionId +
                ", date='" + date + '\'' +
                ", clothList=" + clothList +
                ", weather='" + weather + '\'' +
                ", rate='" + rate + '\'' +
                ", photoURL='" + photoURL + '\'' +
                '}';
    }

    public Fashion() {
        new Fashion("", new ArrayList<Cloth>(), "", "", "");
    }
}