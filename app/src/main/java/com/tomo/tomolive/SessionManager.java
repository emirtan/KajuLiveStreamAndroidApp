package com.tomo.tomolive;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tomo.tomolive.models.User;
import com.tomo.tomolive.retrofit.Const;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class SessionManager {
    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context) {
        this.pref = context.getSharedPreferences(Const.PREF_NAME, MODE_PRIVATE);
        this.editor = this.pref.edit();
    }


    public void saveBooleanValue(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBooleanValue(String key) {
        return pref.getBoolean(key, false);
    }

    public void saveStringValue(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public String getStringValue(String key) {
        return pref.getString(key, "");
    }

    public void saveUser(User user) {
        editor.putString(Const.USER, new Gson().toJson(user));
        editor.apply();
    }

    public User getUser() {
        String userString = pref.getString(Const.USER, "");
        if (userString != null && !userString.isEmpty()) {
            return new Gson().fromJson(userString, User.class);
        }
        return null;
    }

    public void addToHistory(String id) {
        ArrayList<String> fav = getHistory();
        if (fav != null) {
            if (fav.contains(id)) {
                fav.remove(id);
            }

        } else {
            fav = new ArrayList<>();

        }
        fav.add(id);
        editor.putString(Const.HISTORY, new Gson().toJson(fav));
        editor.apply();
    }

    public void removefromHistory(String id) {
        ArrayList<String> fav = getHistory();
        if (fav != null) {
            if (fav.contains(id)) {
                fav.remove(id);

            } else {

            }
        } else {
            fav = new ArrayList<>();

        }
        editor.putString(Const.HISTORY, new Gson().toJson(fav));
        editor.apply();
    }

    public ArrayList<String> getHistory() {
        String userString = pref.getString(Const.HISTORY, "");
        if (!userString.isEmpty()) {
            return new Gson().fromJson(userString, new TypeToken<ArrayList<String>>() {
            }.getType());
        }
        return new ArrayList<>();
    }

    public void removeAllHistory() {
        ArrayList<String> fav = new ArrayList<>();
        editor.putString(Const.HISTORY, new Gson().toJson(fav));
        editor.apply();

    }


   /* public void saveAds(AdvertisementRoot ads) {
        editor.putString(Const.ADS, new Gson().toJson(ads));
        editor.apply();
    }

    public AdvertisementRoot getAdsKeys() {
        String userString = pref.getString(Const.ADS, "");
        if (userString != null && !userString.isEmpty()) {
            return new Gson().fromJson(userString, AdvertisementRoot.class);
        }
        return null;
    }*/
}
