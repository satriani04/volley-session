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
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.saputro.volleysession.com.example.saputro.library.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class FormUpdateActivity extends ActionBarActivity {


    private Button btnFormUpdate;
    private EditText etFormTitle, etFormKonten;
    private String url_update = "http://10.0.3.2/apigw/update_blog.php";
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_update);

        Intent i = getIntent();
        final HashMap<String, String> detail = (HashMap<String, String>) i.getSerializableExtra("detail");
        Log.d("detail_update", detail.toString());

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("updating your data");


        etFormTitle = (EditText) findViewById(R.id.etFormUpdateBlogTitle);
        etFormKonten = (EditText) findViewById(R.id.etFormUpdateBlogContent);

        etFormTitle.setText(detail.get("judul"));
        etFormKonten.setText(detail.get("konten"));

        btnFormUpdate = (Button) findViewById(R.id.btnFormUpdateBlog);
        btnFormUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTitle = etFormTitle.getText().toString();
                String newKonten = etFormKonten.getText().toString();
                String blogId = detail.get("id");

                if (newTitle.trim().length() > 0 && newKonten.trim().length() > 0) {
                    //do update
                    update(newTitle, newKonten, blogId);
                } else {
                    Toast.makeText(getApplicationContext(), "fill all input", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public void update(final String judul, final String konten, final String idBlog) {
        pDialog.show();
        StringRequest strUpdateReq = new StringRequest(Request.Method.POST, url_update, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("respon server update", response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                    if(success==1){
                        //update sukses
                        pDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"update sukses",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), ListBlogActivity.class));
                        finish();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"update gagal",Toast.LENGTH_LONG).show();
                pDialog.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                } else if (error instanceof AuthFailureError) {
                    //TODO
                } else if (error instanceof ServerError) {
                    //TODO
                } else if (error instanceof NetworkError) {
                    //TODO
                } else if (error instanceof ParseError) {
                    //TODO
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("judul", judul);
                param.put("konten", konten);
                param.put("id", idBlog);
                return param;
            }
        };

        MyApplication.getInstance().addToReqQueue(strUpdateReq);

    }


}
