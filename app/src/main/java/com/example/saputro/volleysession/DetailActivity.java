package com.example.saputro.volleysession;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.saputro.volleysession.com.example.saputro.library.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class DetailActivity extends ActionBarActivity {

    TextView tvJudul, tvKonten;

    ArrayList<HashMap<String, String>> blog;

    Button btnUpdate, btnDelete;

    ProgressDialog pDialog;

    String idBlog,judul,konten;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent i = getIntent();

        final HashMap<String, String> detail = (HashMap<String, String>) i.getSerializableExtra("id_blog");

        Log.d("detail", detail.toString());

        idBlog = detail.get("id");


        tvJudul = (TextView) findViewById(R.id.tvDetailJudul);
        tvKonten = (TextView) findViewById(R.id.tvDetailKonten);

        tvJudul.setText("");
        tvKonten.setText("");


        pDialog = new ProgressDialog(DetailActivity.this);
        pDialog.setMessage("wait a moment");
        pDialog.setCancelable(false);


        btnDelete = (Button) findViewById(R.id.btnDetailHapus);
        btnUpdate = (Button)findViewById(R.id.btnDetailUpdate);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent update = new Intent(getApplicationContext(),FormUpdateActivity.class);
                update.putExtra("detail",detail);
                startActivity(update);
            }
        });

        detail(idBlog);


    }

    public void confirmDelete(View v) {
        AlertDialog.Builder alert = new AlertDialog.Builder(DetailActivity.this);
        alert.setTitle("delete");
        alert.setMessage("are you sure want to delete ?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                delete(idBlog);


            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        alert.show();
    }

    public void delete(String idBlog){
        pDialog.show();
        String url_delete = "http://10.0.3.2/apigw/delete_blog.php?id="+idBlog;

        JsonObjectRequest jDelReq = new JsonObjectRequest(url_delete, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    int success = response.getInt("success");
                    if(success==1){
                        pDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"delete success",Toast.LENGTH_LONG).show();
                        Intent list = new Intent(DetailActivity.this,ListBlogActivity.class);
                        list.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(list);
                        finish();
                    }else{
                        pDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"delete failed",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MyApplication.getInstance().addToReqQueue(jDelReq);


    }


    public void detail(final String blogId) {
        pDialog.show();
        String url = "http://10.0.3.2/apigw/detail_blog.php?id=" + blogId;

        JsonObjectRequest jDetailReq = new JsonObjectRequest(Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("respon dari server", response.toString());

                try {
                    int success = response.getInt("success");
                    if (success == 1) {
                        JSONArray blogDetail = response.getJSONArray("blog_detail");
                        Log.d("detail blog", blogDetail.toString());

                        for (int i = 0; i < blogDetail.length(); i++) {
                            JSONObject jObj = blogDetail.getJSONObject(i);

                             judul = jObj.getString("judul");
                             konten = jObj.getString("konten");

                            tvJudul.setText(judul);
                            tvKonten.setText(konten);
                        }






                    }

                    pDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();

            }
        });

        MyApplication.getInstance().addToReqQueue(jDetailReq);


    }


}
