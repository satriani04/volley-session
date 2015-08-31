package com.example.saputro.volleysession.com.example.saputro.library;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by saputro on 20/08/2015.
 */
public class ConnectionManager {

    private Context _context;

    public ConnectionManager(Context context){
        this._context = context;
    }

    public Boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    public void notOnline(){
        Toast.makeText(_context.getApplicationContext(), "you dont have internet connection", Toast.LENGTH_LONG).show();
    }
}
