package de.gimik.app.allpresanapp.model;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by Dang on 22.09.2015.
 */
@DatabaseTable(tableName = "event")
public class Event extends AuditableEntity {

    @SerializedName("name")
    @DatabaseField
    private String name;

    @SerializedName("place")
    @DatabaseField
    private String place;

    @SerializedName("starts_at")
    @DatabaseField
    private Date starts_at;

    @SerializedName("ends_at")
    @DatabaseField
    private Date ends_at;

    @SerializedName("description")
    @DatabaseField
    private String description;

    @SerializedName("image")
    @DatabaseField
    private String image;

    @SerializedName("date")
    @DatabaseField
    private String date;

    @SerializedName("pushText")
    @DatabaseField
    private String pushText;

    @SerializedName("summary")
    @DatabaseField
    private String summary;

    @SerializedName("news")
    @DatabaseField
    private int news;

    @SerializedName("push")
    @DatabaseField
    private int push;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Date getStarts_at() {
        return starts_at;
    }

    public void setStarts_at(Date starts_at) {
        this.starts_at = starts_at;
    }

    public Date getEnds_at() {
        return ends_at;
    }

    public void setEnds_at(Date ends_at) {
        this.ends_at = ends_at;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPushText() {
        return pushText;
    }

    public void setPushText(String pushText) {
        this.pushText = pushText;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getNews() {
        return news;
    }

    public void setNews(int news) {
        this.news = news;
    }

    public int getPush() {
        return push;
    }

    public void setPush(int push) {
        this.push = push;
    }
}
