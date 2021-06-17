package com.tykle.Classess;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.view.Gravity;
import android.view.Window;
import android.widget.Toast;

import com.tykle.R;

import java.util.Set;

public class HelperClass {

    private static Dialog progressDialog;


    public static void toast(Context mContext, String Message) {
        Toast.makeText(mContext, Message, Toast.LENGTH_SHORT).show();
    }


    public static void showProgressDialog(Activity activity) {
        progressDialog = new Dialog(activity);
        progressDialog.setTitle("Please wait...");
        progressDialog.setContentView(R.layout.progress_loader);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window dialogWindow = progressDialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public static void hideProgressDialog() {

        progressDialog.dismiss();
    }

    public static void setProfileImage(String profileimage, Activity sActivity) {
        SharedPreferences pref = sActivity.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("ProfileImage", profileimage);
        editor.commit();
    }

    public static String getProfileImage(Activity sActivity) {
        SharedPreferences pref = sActivity.getSharedPreferences("MyPref", 0);
        return pref.getString("ProfileImage", "");
    }

    public static void setAge(String age, Activity sActivity) {
        SharedPreferences pref = sActivity.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("Age", age);
        editor.commit();
    }

    public static void setAbout(String about, Activity sActivity) {
        SharedPreferences pref = sActivity.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("about", about);
        editor.commit();
    }

    public static String getAbout(Activity sActivity) {
        SharedPreferences pref = sActivity.getSharedPreferences("MyPref", 0);
        return pref.getString("about", "");
    }

    public static String getAge(Activity sActivity) {
        SharedPreferences pref = sActivity.getSharedPreferences("MyPref", 0);
        return pref.getString("Age", "");
    }

    public static void setPhone(String phone, Activity sActivity) {
        SharedPreferences pref = sActivity.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("Phone", phone);
        editor.commit();
    }

    public static void setInterst(String interst, Activity sActivity) {
        SharedPreferences pref = sActivity.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("interst", interst);
        editor.commit();
    }

    public static String getInterst(Activity sActivity) {
        SharedPreferences pref = sActivity.getSharedPreferences("MyPref", 0);
        return pref.getString("interst", "");
    }

    public static String getPhone(Activity sActivity) {
        SharedPreferences pref = sActivity.getSharedPreferences("MyPref", 0);
        return pref.getString("Phone", "");
    }

    public static void setEmail(String email, Activity sActivity) {
        SharedPreferences pref = sActivity.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("Email", email);
        editor.commit();
    }

    public static void setQuickBloxid(String qbid, Activity sActivity) {
        SharedPreferences pref = sActivity.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("qbid", qbid);
        editor.apply();
    }

    public static String getQuickBoxid(Activity sActivity) {
        SharedPreferences pref = sActivity.getSharedPreferences("MyPref", 0);
        return pref.getString("qbid", "");
    }


    public static void setuserInterst(String userinterest, Activity sActivity) {
        SharedPreferences pref = sActivity.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("userinterest", userinterest);
        editor.commit();
    }

    public static String getUserInterst(Activity sActivity) {
        SharedPreferences pref = sActivity.getSharedPreferences("MyPref", 0);
        return pref.getString("userinterest", "");
    }

    public static String getEmail(Activity sActivity) {
        SharedPreferences pref = sActivity.getSharedPreferences("MyPref", 0);
        return pref.getString("Email", "");
    }

    public static void setID(String id, Activity sActivity) {
        SharedPreferences pref = sActivity.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("ID", id);
        editor.commit();
    }

    public static String getID(Activity sActivity) {
        SharedPreferences pref = sActivity.getSharedPreferences("MyPref", 0);
        return pref.getString("ID", "");
    }

    public static void setUsername(String name, Activity sActivity) {
        SharedPreferences pref = sActivity.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("UserName", name);
        editor.commit();
    }

    public static String getUserName(Activity sActivity) {
        SharedPreferences pref = sActivity.getSharedPreferences("MyPref", 0);
        return pref.getString("UserName", "");
    }

    public static void setToken(String token, Activity sActivity) {
        SharedPreferences pref = sActivity.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("Token", token);
        editor.commit();
    }

    public static void setTutoiral(String tutorial, Activity sActivity) {
        SharedPreferences pref = sActivity.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("tutorial", tutorial);
        editor.commit();
    }

    public static String getTutorial(Activity sActivity) {
        SharedPreferences pref = sActivity.getSharedPreferences("MyPref", 0);
        return pref.getString("tutorial", "");
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public static String getToken(Activity sActivity) {
        SharedPreferences pref = sActivity.getSharedPreferences("MyPref", 0);
        return pref.getString("Token", "");
    }


    public static void setLatitude(String latitude, Activity sActivity) {
        SharedPreferences pref = sActivity.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("Latitude", latitude);
        editor.commit();
    }

    public static String getLatitude(Activity sActivity) {
        SharedPreferences pref = sActivity.getSharedPreferences("MyPref", 0);
        return pref.getString("Latitude", "");
    }


    public static void setLongitude(String longitude, Activity sActivity) {
        SharedPreferences pref = sActivity.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("Longitude", longitude);
        editor.commit();
    }

    public static String getLongitude(Activity sActivity) {
        SharedPreferences pref = sActivity.getSharedPreferences("MyPref", 0);
        return pref.getString("Longitude", "");
    }


    //phone number verification
    public static void setPhoneVerify(String phoneVerify, Activity sActivity) {
        SharedPreferences pref = sActivity.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("PhoneVerify", phoneVerify);
        editor.commit();
    }

    public static String getPhoneVerify(Activity sActivity) {
        SharedPreferences pref = sActivity.getSharedPreferences("MyPref", 0);
        return pref.getString("PhoneVerify", "");
    }

    public static void setCurrentLatitude(String string, Activity sActivity) {
        SharedPreferences pref = sActivity.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("setCurrentLatitude", string);
        editor.apply();
    }

    public static String getCurrentLatitude(Activity sActivity) {
        SharedPreferences pref = sActivity.getSharedPreferences("MyPref", 0);
        return pref.getString("setCurrentLatitude", "");
    }

    public static void setCurrentLongitude(String string, Activity sActivity) {
        SharedPreferences pref = sActivity.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("setCurrentLongitude", string);
        editor.apply();
    }

    public static String getCurrentLongitude(Activity sActivity) {
        SharedPreferences pref = sActivity.getSharedPreferences("MyPref", 0);
        return pref.getString("setCurrentLongitude", "");
    }


    public static void setProfileImagePath(String imagepath, Activity sActivity) {
        SharedPreferences pref = sActivity.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("ImagePath", imagepath);
        editor.commit();
    }

    public static String getProfileImagePath(Activity sActivity) {
        SharedPreferences pref = sActivity.getSharedPreferences("MyPref", 0);
        return pref.getString("ImagePath", "");
    }


    public static void setLoginToken(String logingtoken, Activity sActivity) {
        SharedPreferences pref = sActivity.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("LoginToken", logingtoken);
        editor.commit();
    }

    public static void logout(Activity activity) {

        SharedPreferences pref = activity.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();

    }

    public static String getLoginToken(Activity sActivity) {
        SharedPreferences pref = sActivity.getSharedPreferences("MyPref", 0);
        return pref.getString("LoginToken", "");
    }

    public static void setCurrentAddress(String address, Activity activity) {
        SharedPreferences pref = activity.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("Address", address);
        editor.commit();
    }

    public static String getCurrentAddress(Activity activity) {
        SharedPreferences pref = activity.getSharedPreferences("MyPref", 0);
        return pref.getString("Address", "");

    }


    public static void setUserState(String userstate, Activity activity) {
        SharedPreferences pref = activity.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("UserState", userstate);
        editor.commit();
    }

    public static String getUserState(Activity activity) {
        SharedPreferences pref = activity.getSharedPreferences("MyPref", 0);
        return pref.getString("UserState", "");

    }

    public static void setUserCity(String usercity, Activity activity) {
        SharedPreferences pref = activity.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("UserCity", usercity);
        editor.commit();
    }

    public static String getUserCity(Activity activity) {
        SharedPreferences pref = activity.getSharedPreferences("MyPref", 0);
        return pref.getString("UserCity", "");
    }

    public static void setProfileQuestionAge(String sage, Activity activity) {
        SharedPreferences pref = activity.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("Age", sage);
        editor.commit();
    }

    public static String getProfileQuestionAge(Activity activity) {
        SharedPreferences pref = activity.getSharedPreferences("MyPref", 0);
        return pref.getString("Age", "");
    }

    public static void setProfileQuestionLanguage(String slanguage, Activity activity) {
        SharedPreferences pref = activity.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("Language", slanguage);
        editor.commit();
    }

    public static String getProfileQuestionLanguage(Activity activity) {
        SharedPreferences pref = activity.getSharedPreferences("MyPref", 0);
        return pref.getString("Language", "");
    }

    public static void setProfileQuestionProfession(String sProfession, Activity activity) {
        SharedPreferences pref = activity.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("Profession", sProfession);
        editor.commit();
    }

    public static String getProfileQuestionProfession(Activity activity) {
        SharedPreferences pref = activity.getSharedPreferences("MyPref", 0);
        return pref.getString("Profession", "");
    }

    public static void setProfileQuestionIdentity(String sIdentity, Activity activity) {
        SharedPreferences pref = activity.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("Identity", sIdentity);
        editor.commit();
    }

    public static String getProfileQuestionIdentity(Activity activity) {
        SharedPreferences pref = activity.getSharedPreferences("MyPref", 0);
        return pref.getString("Identity", "");
    }

    public static void setProfileQuestionLooking_for(String sLooking_for, Activity activity) {
        SharedPreferences pref = activity.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("Identity", sLooking_for);
        editor.commit();
    }

    public static String getProfileQuestionLooking_for(Activity activity) {
        SharedPreferences pref = activity.getSharedPreferences("MyPref", 0);
        return pref.getString("Looking_for", "");
    }


    public static void setLikeImage(Set<String> likeImageList, Activity activity) {
        SharedPreferences pref = activity.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putStringSet("LikeImageList", likeImageList);
        editor.commit();
    }

    public static Set<String> getLikeImage(Activity activity) {
        SharedPreferences pref = activity.getSharedPreferences("MyPref", 0);
        return pref.getStringSet("LikeImageList", null);
    }

    public static void setFacebookLoginToken(String facebookAccessToken, Activity activity) {
        SharedPreferences pref = activity.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("facebookAccessToken", facebookAccessToken);
        editor.commit();
    }

    public static Set<String> getFacebookLoginToken(Activity activity) {
        SharedPreferences pref = activity.getSharedPreferences("MyPref", 0);
        return pref.getStringSet("facebookAccessToken", null);
    }
}
