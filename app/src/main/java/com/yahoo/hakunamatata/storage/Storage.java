package com.yahoo.hakunamatata.storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by jonaswu on 2015/7/30.
 */
public class Storage {
    private static String MyPREFERENCES = "LOSERS!!";
    private static SharedPreferences sharedpreferences;

    public static void write(Context ctx, String key, String value) {
        if (sharedpreferences == null)
            sharedpreferences = ctx.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String read(Context ctx, String key, String defaultVal) {
        if (sharedpreferences == null)
            sharedpreferences = ctx.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString(key, defaultVal);
    }
}
