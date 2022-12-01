package com.mpl.weather;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Cloth {
    @PrimaryKey(autoGenerate = true)
    public int clothId;

    public String category;
    public String clothName;

    Cloth(String category, String clothName) {
        this.category = category;
        this.clothName = clothName;
    }
}