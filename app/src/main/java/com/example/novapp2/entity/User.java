package com.example.novapp2.entity;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public class User {

    public String userId;
    public String name;
    public String email;
    public String surname;
    public String bio;
    public List<String> groupChats;
    public List<String> favourites;
    public Boolean isBicoccaUser;
    public String profileImg;
    public List<String> notifications;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String userId, String name, String email, String surname, String bio, List<String> groupChats, List<String> favourites, Boolean isBicoccaUser, String profileImg, List<String> notifications) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.surname = surname;
        this.bio = bio;
        this.groupChats = groupChats;
        this.favourites = favourites;
        this.isBicoccaUser = isBicoccaUser;
        this.profileImg = profileImg;
        this.notifications = notifications;
    }

    public String getID() {
        return userId;
    }

    public void setID(String userId) {
        this.userId = userId;
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
        return isBicoccaUser;
    }

    public void setBiccoccaUser(Boolean biccoccaUser) {
        isBicoccaUser = biccoccaUser;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public List<String> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<String> notifications) {
        this.notifications = notifications;
    }
}
