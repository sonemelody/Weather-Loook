package com.mpl.weather;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Cloth {
    @PrimaryKey(autoGenerate = true)
    public int clothId;

    public String category;
    public String clothName;

    @Override
    public String toString() {
        return "Cloth{" +
                "category='" + category + '\'' +
                ", clothName='" + clothName + '\'' +
                '}';
    }

    Cloth(String category, String clothName) {
        this.category = category;
        this.clothName = clothName;
    }

    Cloth(String clothName) {
        this.clothName = clothName;
    }

    public Cloth() {
        new Cloth("", "");
    }
}