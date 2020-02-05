package com.abdelrahman.irihackathon.ManualSection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.abdelrahman.irihackathon.Adapter.ManualAdapter;
import com.abdelrahman.irihackathon.Common.Constants;
import com.abdelrahman.irihackathon.Common.Global;
import com.abdelrahman.irihackathon.Common.RecyclerItemClickListener;
import com.abdelrahman.irihackathon.Model.Manual;
import com.abdelrahman.irihackathon.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CookingActivity extends AppCompatActivity {

    private ImageView back;
    private FloatingActionButton add;

    private RecyclerView list;
    private RequestQueue mQueue;
    private ArrayList<Manual> manuals;
    private ManualAdapter manualAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooking);

        list = findViewById(R.id.cooking_list);
        back = findViewById(R.id.back);
        add = findViewById(R.id.btn_add);

        if (Global.userType.equals("Bedouin")){
            add.show();

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(CookingActivity.this, AddActivity.class));
                    finish();
                }
            });
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CookingActivity.this, ManualDashboardActivity.class));
                finish();
            }
        });

        mQueue = Volley.newRequestQueue(this);
        manuals = new ArrayList<>();

        jsonParse(Global.categoryManual);

    }
    private void jsonParse(String categoryId) {

        String url = Constants.API_URL + "blogs/getBlogs/" + categoryId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");


                            manuals.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject manual = jsonArray.getJSONObject(i);

                                Manual m = new Manual();
                                m.setTitle(manual.getString("title"));
                                m.setDescription(manual.getString("body"));
                                m.setAddedBy(manual.getString("addedBy"));
                                m.setBlogID(manual.getString("blogID"));
                                m.setMedia(manual.getString("media"));
                                m.setCategoryID(manual.getString("categoryID"));

                                manuals.add(m);
                            }

                            manualAdapter = new ManualAdapter(CookingActivity.this, manuals);
                            list.setLayoutManager(new LinearLayoutManager(CookingActivity.this));
                            list.setAdapter(manualAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(CookingActivity.this, ManualDashboardActivity.class));
        finish();
    }
}
