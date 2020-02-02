package com.abdelrahman.irihackathon.DictionarySection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.abdelrahman.irihackathon.Common.Constants;
import com.abdelrahman.irihackathon.Common.Global;
import com.abdelrahman.irihackathon.DashboardActivity;
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

public class DictionaryActivity extends AppCompatActivity {

    private EditText search;
    private TextView searchedTxt;
    private FloatingActionButton add;
    private ImageView back;

    private ArrayList<String> translated;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        search = findViewById(R.id.edt_search);
        searchedTxt = findViewById(R.id.searched_txt);
        add = findViewById(R.id.btn_add);
        back = findViewById(R.id.back);

        if (Global.userType.equals("Bedouin")){
            add.show();

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(DictionaryActivity.this, AddDictionaryActivity.class));
                    finish();
                }
            });
        }

        mQueue = Volley.newRequestQueue(this);
        translated = new ArrayList<>();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DictionaryActivity.this, DashboardActivity.class));
                finish();
            }
        });

        search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (search.getRight() - search.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())){
                        if (!TextUtils.isEmpty(search.getText().toString())){
                            jsonParse(search.getText().toString());
                        }
                    }

                }
                return false;
            }
        });
    }
    private void jsonParse(String word) {

        String url = Constants.API_URL + "dictionary/searchWord/" + word;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            String w = "";

                            for (int i = 0; i < jsonArray.length(); i++) {
                                w = "";
                                JSONObject words = jsonArray.getJSONObject(i);

                                w += " " + words.getString("word") + " " + words.getString("arabicWord");
                                translated.add(w);
                            }

                            String result = "";
                            for (int j = 0; j < translated.size(); j++){
                                result += "\n" + translated.get(j);
                            }
                            searchedTxt.setText(result);

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
        startActivity(new Intent(DictionaryActivity.this, DashboardActivity.class));
        finish();
    }
}
