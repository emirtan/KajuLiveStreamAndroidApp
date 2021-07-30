package com.tomo.tomolive.oflineModels.Filters;

public class FilterRoot {


    int filter;
    String title;

    public FilterRoot(int filter, String name) {
        this.filter = filter;
        this.title = name;
    }

    public int getFilter() {
        return filter;
    }

    public void setFilter(int filter) {
        this.filter = filter;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
