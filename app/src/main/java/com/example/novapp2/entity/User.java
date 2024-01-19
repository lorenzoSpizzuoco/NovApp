package com.example.novapp2.entity;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public class User {

    public String name;
    public String email;
    public String surname;
    public String bio;
    public List<String> groupChats;
    public List<String> favourites;
    public Boolean isBiccoccaUser;
    public String profileImg;
    public String password;
    public List<String> notifications;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name, String email, String surname, String bio, List<String> groupChats, List<String> favourites, Boolean isBiccoccaUser, String profileImg, String password, List<String> notifications) {
        this.name = name;
        this.email = email;
        this.surname = surname;
        this.bio = bio;
        this.groupChats = groupChats;
        this.favourites = favourites;
        this.isBiccoccaUser = isBiccoccaUser;
        this.profileImg = profileImg;
        this.password = password;
        this.notifications = notifications;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", surname='" + surname + '\'' +
                ", bio='" + bio + '\'' +
                ", groupChats=" + groupChats +
                ", favourites=" + favourites +
                ", isBiccoccaUser=" + isBiccoccaUser +
                ", profileImg='" + profileImg + '\'' +
                ", password='" + password + '\'' +
                ", notifications=" + notifications +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<String> getGroupChats() {
        return groupChats;
    }

    public void setGroupChats(List<String> groupChats) {
        this.groupChats = groupChats;
    }

    public List<String> getFavourites() {
        return favourites;
    }

    public void setFavourites(List<String> favourites) {
        this.favourites = favourites;
    }

    public Boolean getBiccoccaUser() {
        return isBiccoccaUser;
    }

    public void setBiccoccaUser(Boolean biccoccaUser) {
        isBiccoccaUser = biccoccaUser;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<String> notifications) {
        this.notifications = notifications;
    }
}
