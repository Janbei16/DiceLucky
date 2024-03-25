package com.example.dicelucky;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class GameDataHandler {

    private static final String PREF_NAME = "com.example.dicelucky.GameDataHandler";
    private static final String KEY_MONEY = "money";

    public static int loadTotalMoney(Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_MONEY, 100); // Default start Coins: 100
    }

    public static void saveTotalMoney(Activity activity, int totalMoney) {
        SharedPreferences.Editor editor = activity.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(KEY_MONEY, totalMoney);
        editor.apply();
    }
}
