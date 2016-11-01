package com.perfect_apps.koch.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.format.DateUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mostafa_anter on 10/27/16.
 */

public class Utils {

    public static boolean isOnline(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Turn drawable into byte array.
     *
     * @param uri data
     * @return byte array
     */
    public static byte[] getFileDataFromDrawable(Context context, Uri uri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


    }

    public static void browse(Context mContext, String url){
        Intent intent= new Intent(Intent.ACTION_VIEW,Uri.parse(url));
        mContext.startActivity(intent);
    }

    // convert date to nice format as 19 hours ago
    public static String manipulateDateFormat(String post_date){

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = (Date)formatter.parse(post_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date != null) {
            // Converting timestamp into x ago format
            CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                    Long.parseLong(String.valueOf(date.getTime())),
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
            return timeAgo + "";
        }else {
            return post_date;
        }
    }
}
