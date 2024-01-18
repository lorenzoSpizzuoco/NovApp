package com.example.novapp2.ui.post;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.novapp2.ui.ad.Ad;

@Entity(tableName = "post_table")
public class Post implements Parcelable{

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String author;
    private String title;

    private String date;

    private String content;

    private int category;

    private int image;

    protected Post(Parcel in) {
        author = in.readString();
        content = in.readString();
        date = in.readString();
        category = in.readInt();
        title = in.readString();
        image = in.readInt();
    }

    public static final Parcelable.Creator<Post> CREATOR = new Parcelable.Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public void setId(long id) {
        this.id = id;
    }
    public long getId() {
        return id;
    }
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }



    // post category
    public Post(String title, String author, int image, String content, int category, String date) {
        this.title = title;
        this.author = author;
        this.content = content;
        this.category = category;
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(date);
        dest.writeInt(category);
        dest.writeString(title);
        dest.writeInt(image);
    }
}
