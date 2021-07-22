package com.example.newproject.Videos;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import java.util.Date;

@Entity(tableName = "video_table")
public class VideoModel {

    @ColumnInfo(name = "current_date")
    @TypeConverter(DateConverter.class)
    private Date date;

    private String title;
    private String description;


    public VideoModel(Date date, String title, String description) {
        this.date = date;
        this.title = title;
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
