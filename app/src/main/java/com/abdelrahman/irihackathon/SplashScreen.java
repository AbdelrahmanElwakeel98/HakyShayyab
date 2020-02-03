package com.abdelrahman.irihackathon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.abdelrahman.irihackathon.Common.Constants;
import com.abdelrahman.irihackathon.Common.Global;
import com.abdelrahman.irihackathon.Model.User;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

public class SplashScreen extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 2000;

    private FirebaseAuth auth;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        auth = FirebaseAuth.getInstance();
        mQueue = Volley.newRequestQueue(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (auth.getCurrentUser() != null){
                    jsonParse(auth.getCurrentUser().getUid());

                } else {
                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                    SplashScreen.this.finish();
                }
            }
        },SPLASH_DISPLAY_LENGTH);
    }

    private void jsonParse(String uid) {

        String url = Constants.API_URL + "users/user/" + uid;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject userJson = response.getJSONObject("data");

                            User currentUser = new User();

                            currentUser.setUsername(userJson.getString("fullName"));
                            currentUser.setPhone(userJson.getString("phoneNumber"));

                            boolean checkUserType = userJson.getBoolean("isBedouin");

                            if (checkUserType){
                                Global.userType = "Bedouin";
                            } else {
                                Global.userType = "User";
                            }

                            Global.user = currentUser;

                            startActivity(new Intent(SplashScreen.this, DashboardActivity.class));
                            SplashScreen.this.finish();

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
}
