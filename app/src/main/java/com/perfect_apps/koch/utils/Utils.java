package com.perfect_apps.koch.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateUtils;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.perfect_apps.koch.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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


    public static String returnDate(){
        DateFormat df = new SimpleDateFormat("EEE, MMM d, ''yy");
        String date = df.format(Calendar.getInstance().getTime());
        return date;

//        "yyyy.MM.dd G 'at' HH:mm:ss z" ---- 2001.07.04 AD at 12:08:56 PDT
//        "hh 'o''clock' a, zzzz" ----------- 12 o'clock PM, Pacific Daylight Time
//        "EEE, d MMM yyyy HH:mm:ss Z"------- Wed, 4 Jul 2001 12:08:56 -0700
//        "yyyy-MM-dd'T'HH:mm:ss.SSSZ"------- 2001-07-04T12:08:56.235-0700
//        "yyMMddHHmmssZ"-------------------- 010704120856-0700
//        "K:mm a, z" ----------------------- 0:08 PM, PDT
//        "h:mm a" -------------------------- 12:08 PM
//        "EEE, MMM d, ''yy" ---------------- Wed, Jul 4, '01
    }

    public static String returnTime(){
        DateFormat df = new SimpleDateFormat("h:mm a");
        String date = df.format(Calendar.getInstance().getTime());

        return date;

        //        "yyyy.MM.dd G 'at' HH:mm:ss z" ---- 2001.07.04 AD at 12:08:56 PDT
//        "hh 'o''clock' a, zzzz" ----------- 12 o'clock PM, Pacific Daylight Time
//        "EEE, d MMM yyyy HH:mm:ss Z"------- Wed, 4 Jul 2001 12:08:56 -0700
//        "yyyy-MM-dd'T'HH:mm:ss.SSSZ"------- 2001-07-04T12:08:56.235-0700
//        "yyMMddHHmmssZ"-------------------- 010704120856-0700
//        "K:mm a, z" ----------------------- 0:08 PM, PDT
//        "h:mm a" -------------------------- 12:08 PM
//        "EEE, MMM d, ''yy" ---------------- Wed, Jul 4, '01
    }

    public static void sleepAndSuccess(final FragmentActivity mContext, final String message){
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                new SweetDialogHelper(mContext)
                        .showSuccessfulMessage(mContext.getString(R.string.done),
                                message);
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mContext.finish();


            }
        }.execute();
    }

    public static Long LongNationalNumber(String mNumberStr){
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber mNumberProto = null;
        try {
            mNumberProto = phoneUtil.parse(mNumberStr, "");
        } catch (NumberParseException e) {
            e.printStackTrace();
        }
        if(mNumberProto == null){
            // there is no number show error message
            return null;
        }
        return mNumberProto.getNationalNumber();
    }
}
