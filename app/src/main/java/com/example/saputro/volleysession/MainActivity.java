package com.example.saputro.volleysession;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saputro.volleysession.com.example.saputro.library.ConnectionManager;
import com.example.saputro.volleysession.com.example.saputro.library.UserSessionManager;

import java.util.HashMap;


public class MainActivity extends ActionBarActivity {


    TextView tvWellcome;
    Button btnLogout,btnList,btnAdd,btnAndroidhive;

    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new UserSessionManager(getApplicationContext());

        if (session.checkLogin())
            finish();

        tvWellcome = (TextView) findViewById(R.id.tvDashboardName);

        HashMap<String, String> user = session.getUserDetail();

        String fullname = user.get(UserSessionManager.KEY_NAME);
        String email = user.get(UserSessionManager.KEY_EMAIL);
        final String id = user.get(UserSessionManager.KEY_ID);

        Log.d("detail user",user.toString());

        tvWellcome.setText("Welcome " + fullname);

        btnLogout = (Button)findViewById(R.id.btnDashboardLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logout();
                finish();
            }
        });

        btnList = (Button)findViewById(R.id.btnDashboardListBlog);
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConnectionManager cm = new ConnectionManager(getApplicationContext());
                if(cm.isOnline()){
                    Intent i = new Intent(getApplicationContext(),ListBlogActivity.class);
                    i.putExtra("id_user",id);
                    startActivity(i);

                }else{
                    Toast.makeText(getApplicationContext(),"you dont have internet access",Toast.LENGTH_LONG).show();
                }


            }
        });

        btnAdd = (Button)findViewById(R.id.btnDashboardAddBlog);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add = new Intent(getApplicationContext(),AddBlogActivity.class );
                startActivity(add);
            }
        });

        btnAndroidhive=(Button)findViewById(R.id.btnDashboardVolley);
        btnAndroidhive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent volley = new Intent(getApplicationContext(),VolleyListActivity.class);
                startActivity(volley);
            }
        });

    }


}
