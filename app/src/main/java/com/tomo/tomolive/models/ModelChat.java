package com.tomo.tomolive.models;

import android.net.Uri;

public class ModelChat {

    boolean isUser;
    String message;
    Uri imageuri;
    String imagename;

    public ModelChat(boolean b, String msg, Uri selectedImageUri, String s) {

        this.isUser = b;
        message = msg;
        this.imageuri = selectedImageUri;
        this.imagename = s;
    }

    public String getImagename() {
        return imagename;
    }

    public void setImagename(String imagename) {
        this.imagename = imagename;
    }

    public Uri getImageuri() {
        return imageuri;
    }

    public void setImageuri(Uri imageuri) {
        this.imageuri = imageuri;
    }

    public boolean isUser() {
        return isUser;
    }

    public void setUser(boolean user) {
        isUser = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
