package com.example.novapp2.entity.ad;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ad_table")
public class Ad implements Parcelable{
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String author;

    private String title;

    private String content;

    private int image;

    private String date;

    public Ad() {}

    public Ad(String author, String title, String content, int image, String Date) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.image = image;
        this.date = Date;
    }

    protected Ad(Parcel in) {
        author = in.readString();
        title = in.readString();
        content = in.readString();
        image = in.readInt();
        date = in.readString();
    }

    public static final Creator<Ad> CREATOR = new Creator<Ad>() {
        @Override
        public Ad createFromParcel(Parcel in) {
            return new Ad(in);
        }

        @Override
        public Ad[] newArray(int size) {
            return new Ad[size];
        }
    };

    public void setId(long id) {
        this.id = id;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public long getId() {
        return id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUrlToImage(int image) {
        this.image = image;
    }

    public void setDate(String date) {
        date = date;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getImage() {
        return image;
    }

    public String getDate() {
        return date;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeInt(image);
        dest.writeString(date);
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public String toString() {
        return author +
                " " +
                title +
                " " +
                content +
                " " +
                date;
    }
}