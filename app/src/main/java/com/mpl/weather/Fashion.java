package com.mpl.weather;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

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
}