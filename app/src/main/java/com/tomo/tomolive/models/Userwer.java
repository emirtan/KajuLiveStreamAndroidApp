package com.tomo.tomolive.models;

import com.google.gson.annotations.SerializedName;

public class Userwer {

    @SerializedName("nModified")
    private int nModified;

    @SerializedName("ok")
    private int ok;

    @SerializedName("n")
    private int N;

    public int getNModified() {
        return nModified;
    }

    public int getOk() {
        return ok;
    }

    public int getN() {
        return N;
    }
}