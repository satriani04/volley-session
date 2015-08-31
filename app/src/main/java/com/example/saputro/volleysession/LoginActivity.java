package com.example.saputro.volleysession;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.saputro.volleysession.com.example.saputro.library.ConnectionManager;
import com.example.saputro.volleysession.com.example.saputro.library.MyApplication;
import com.example.saputro.volleysession.com.example.saputro.library.UserSessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends ActionBarActivity {


    String url = "http://10.0.3.2/apigw/login_post.php";
    Button btnLogin;
    EditText etInputUsername, etInputPassword;
    UserSessionManager session;
    String username, password;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new UserSessionManager(getApplicationContext());

        Toast.makeText(getApplicationContext(), "user login status : " + session.isUserLogedIn(), Toast.LENGTH_SHORT).show();

        etInputUsername = (EditText) findViewById(R.id.etLoginUsername);
        etInputPassword = (EditText) findViewById(R.id.etLoginpassword);

        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("please wait....");
        pDialog.setCancelable(false);


        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = etInputUsername.getText().toString();
                password = etInputPassword.getText().toString();

                if (username.trim().length() > 0 && password.trim().length() > 0) {
                    //cek internet connection
                    ConnectionManager cm = new ConnectionManager(getApplicationContext());
                    if (cm.isOnline()) {
                        //do login
                        login(username, password);
                    } else {
                        Toast.makeText(getApplicationContext(), "check internet access", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "please enter credential", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public void login(final String username, final String password) {
        pDialog.show();

        StringRequest sReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                Log.d("respon", response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                    if (success == 1) {
                        Toast.makeText(getApplicationContext(), "login sukses", Toast.LENGTH_LONG).show();
                        JSONArray hasil = jObj.getJSONArray("login");

                        for (int i = 0; i < hasil.length(); i++) {
                            JSONObject c = hasil.getJSONObject(i);
                            String nama = c.getString("nama").trim();
                            String email = c.getString("email").trim();
                            String id_user = c.getString("id").trim();
                            //simpan ke session
                            session.createUserLoginSession(nama, email, id_user);

                            Log.d("info", "data berhasil");


                        }

                        Intent dashboard = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(dashboard);
                        finish();

                    } else {
                        Toast.makeText(getApplicationContext(), "username dan password salah", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "error "+error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> param = new HashMap<String, String>();
                param.put("email", username);
                param.put("password", password);
                return param;
            }
        };

        MyApplication.getInstance().addToReqQueue(sReq);


    }


}
