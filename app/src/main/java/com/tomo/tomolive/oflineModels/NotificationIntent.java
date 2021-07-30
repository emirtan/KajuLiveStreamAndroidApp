package com.tomo.tomolive.oflineModels;

import com.tomo.tomolive.models.Thumb;

public class NotificationIntent {
    public static int TYPE = 0;
    public static int CHAT = 1;
    public static int LIVE = 2;


    int type;
    String hostid;
    String image;
    String name;
    Thumb thumb;


    public static int getTYPE() {
        return TYPE;
    }

    public static void setTYPE(int TYPE) {
        NotificationIntent.TYPE = TYPE;
    }

    public static int getCHAT() {
        return CHAT;
    }

    public static void setCHAT(int CHAT) {
        NotificationIntent.CHAT = CHAT;
    }

    public static int getLIVE() {
        return LIVE;
    }

    public static void setLIVE(int LIVE) {
        NotificationIntent.LIVE = LIVE;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getHostid() {
        return hostid;
    }

    public void setHostid(String hostid) {
        this.hostid = hostid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Thumb getThumb() {
        return thumb;
    }

    public void setThumb(Thumb thumb) {
        this.thumb = thumb;
    }
}
