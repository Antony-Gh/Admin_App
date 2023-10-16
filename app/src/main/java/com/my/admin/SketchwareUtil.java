package com.my.admin;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.net.URLDecoder;

public class SketchwareUtil {

    public static void hideKeyboard(Context context) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(1, 0);
    }

    public static void showMessage(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    public static void setloc(Context c, String g) {
        SharedPreferences s = c.getSharedPreferences("app_default", Activity.MODE_PRIVATE);
        s.edit().putString("loc", g).apply();
    }

    public static String getloc(Context c) {
        SharedPreferences s = c.getSharedPreferences("app_default", Activity.MODE_PRIVATE);
        return s.getString("loc", "");
    }

}
