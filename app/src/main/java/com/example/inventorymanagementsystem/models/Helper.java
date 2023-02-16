package com.example.inventorymanagementsystem.models;

import android.text.TextUtils;
import android.util.Patterns;

public class Helper {
    public Helper() {

    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
