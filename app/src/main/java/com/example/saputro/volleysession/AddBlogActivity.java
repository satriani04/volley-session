package com.example.saputro.volleysession;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class AddBlogActivity extends ActionBarActivity {


    Button btnAdd;
    EditText etTitle, etKonten;

    UserSessionManager session;

    private String url = "http://10.0.3.2/apigw/add_blog.php";

    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_blog);

        pDialog = new ProgressDialog(AddBlogActivity.this);
        pDialog.setMessage("adding your blog");
        pDialog.setCancelable(false);

        session = new UserSessionManager(getApplicationContext());

        HashMap<String, String> detail = session.getUserDetail();

        final String uId = detail.get(UserSessionManager.KEY_ID);

        etTitle = (EditText) findViewById(R.id.etAddBlogTitle);
        etKonten = (EditText) findViewById(R.id.etAddBlogContent);

        btnAdd = (Button) findViewById(R.id.btnAddBlog);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputTitle = etTitle.getText().toString();
                String inputKonten = etKonten.getText().toString();
                if (inputTitle.trim().length() > 0 && inputKonten.trim().length() > 0) {
                    //cek koneksi
                    ConnectionManager cm = new ConnectionManager(getApplicationContext());
                    if (cm.isOnline()) {
                        //doLogin
                        addBlog(inputTitle, inputKonten, uId);
                    } else {
                        AlertDialog.Builder alert = new AlertDialog.Builder(AddBlogActivity.this);
                        alert.setTitle("Ups....");
                        alert.setMessage("seem's like you dont have internet connection");
                        alert.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alert.show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "please enter credential", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void addBlog(final String judul, final String konten, final String idUser) {
        pDialog.show();

        StringRequest sAddReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                Log.d("respon", response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                    if (success == 1) {
                        Toast.makeText(getApplicationContext(), "add data success", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getApplicationContext(), ListBlogActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "add data failed", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "ada yang salah", Toast.LENGTH_LONG).show();

            }
        }) {
//

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("title", judul);
                param.put("konten", konten);
                param.put("id_user", idUser);
                return param;
            }
        };

        MyApplication.getInstance().addToReqQueue(sAddReq);
    }


}
