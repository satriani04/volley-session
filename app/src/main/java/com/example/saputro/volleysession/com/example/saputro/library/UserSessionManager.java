package com.example.saputro.volleysession.com.example.saputro.library;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.saputro.volleysession.LoginActivity;

import java.util.HashMap;

/**
 * Created by saputro on 20/08/2015.
 */
public class UserSessionManager {

    //sharedpreferences reference
    SharedPreferences pref;

    //editor untuk sharedpreferences
    SharedPreferences.Editor editor;

    //context
    Context _context;

    //sharedpref mode
    int PRIVATE_MODE = 0;

    //sharedpreferences name
    private static final String PREFER_NAME = "AndroidExamplePref";

    //all shared preference keya
    private static final String IS_USER_LOGIN = "IsUserLogedIn";

    // User name (make variable public to access from outside)
    //nama user (buat variable public untuk akses dari luar)
    public static final String KEY_NAME = "name";

    // Email address (make variable public to access from outside)
    //alamat email (buat variable public untuk akses dari luar)
    public static final String KEY_EMAIL = "email";

    public static final String KEY_ID = "iduser";


    //construktor
    public UserSessionManager(Context ctx){
        this._context = ctx;
        pref = _context.getSharedPreferences(PREFER_NAME,PRIVATE_MODE);
        editor = pref.edit();
    }

    //create login session
    //buat login session
    public void createUserLoginSession(String name,String email, String id){
        //simpan login value = true
        editor.putBoolean(IS_USER_LOGIN, true);

        //simpan nama user di pref
        editor.putString(KEY_NAME,name);

        //simpan email user di pref
        editor.putString(KEY_EMAIL,email);

        editor.putString(KEY_ID, id);

        //commit semua perubahan
        editor.commit();
    }

    //check for login
    public boolean isUserLogedIn(){
        return pref.getBoolean(IS_USER_LOGIN,false);
    }

    /**
     * Check login method will check user login status
     * If false it will redirect user to login page
     * Else do anything
     * */

    /*
    * cek login method akan mengecek status login user. jika false maka akan redirect ke login page
    * */
    public boolean checkLogin(){
        //cek login status
        if(!this.isUserLogedIn()){
            // user is not logged in redirect him to Login Activity
            // user yang belum login akan redirect ke activity login
            Intent i = new Intent(_context, LoginActivity.class);

            //closing all activity from stack
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            //add new flag to start activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            //start activity
            _context.startActivity(i);
            return true;
        }

        return false;
    }

    /*
    * get stored session data
    * */
    public HashMap<String,String> getUserDetail(){
        //gunakan hashmap untuk menyimpan user detail
        HashMap<String,String> user = new HashMap<String,String>();

        //username
        user.put(KEY_NAME,pref.getString(KEY_NAME,null));

        //email
        user.put(KEY_EMAIL,pref.getString(KEY_EMAIL,null));

        //idUser
        user.put(KEY_ID,pref.getString(KEY_ID,null));

        return user;
    }

    //hapus session
    public void logout(){
        //clear semua user data dari sharedpreferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Login Activity
        //setelah logout redirect ke login
        Intent i = new Intent(_context,LoginActivity.class);

        //close all activity
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //add new flag to start activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        _context.startActivity(i);
    }

}
