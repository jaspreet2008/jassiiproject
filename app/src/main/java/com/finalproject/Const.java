package com.finalproject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Const {

    public static String SHAREDPREFERENCE = "preference";
    public static final String Mobile = "mobileKey";
    public static final String UserId = "useridKey";
    public static final String Email = "emailKey";
    public static final String Name = "nameKey";
    public static final String UserType = "typeKey";
    public static final String Longitude = "lngKey";
    public static final String Address = "addressKey";

    public static final String STORAGE_PATH_UPLOADS = "uploads/";
    public static final String DATABASE_PATH_UPLOADS = "uploads";

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
}
