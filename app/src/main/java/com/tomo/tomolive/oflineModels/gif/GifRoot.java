package com.tomo.tomolive.oflineModels.gif;

public class GifRoot {
    int gif;
    String title;

    public GifRoot(int gif, String name) {
        this.gif = gif;
        this.title = name;
    }

    public int getFilter() {
        return gif;
    }

    public void setFilter(int filter) {
        this.gif = filter;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
