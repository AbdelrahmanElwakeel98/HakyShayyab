package com.abdelrahman.irihackathon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.abdelrahman.irihackathon.Common.Constants;
import com.abdelrahman.irihackathon.Common.Global;
import com.abdelrahman.irihackathon.Common.HttpsTrustManager;
import com.abdelrahman.irihackathon.Model.User;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class BedouinQuestionsActivity extends AppCompatActivity {

    private Button back, finish;
    private User user;
    private String userType, userData;
    private FirebaseAuth auth;

    private RadioGroup groupOne;
    private RadioGroup groupTwo;
    private RadioGroup groupThree;


    private RadioButton selectedRadio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bedouin_questions);

        back = findViewById(R.id.btn_back);
        finish = findViewById(R.id.btn_finish);

        groupOne = findViewById(R.id.group_one);
        groupTwo = findViewById(R.id.group_two);
        groupThree = findViewById(R.id.group_three);

        userType = Global.userType;


        auth = FirebaseAuth.getInstance();

        // Listeners
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BedouinQuestionsActivity.this, RegistrationActivity.class);
                startActivity(intent);
                finish();
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAnswers()){
                    registerUser();
                } else {
                    Intent intent = new Intent(BedouinQuestionsActivity.this, WrongBedouin.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private boolean checkAnswers(){
        int selectedIdOne = groupOne.getCheckedRadioButtonId();
        int selectedIdTwo = groupTwo.getCheckedRadioButtonId();
        int selectedIdThree = groupThree.getCheckedRadioButtonId();

        if (selectedIdOne == R.id.q1_a2 && selectedIdTwo == R.id.q2_a1 && selectedIdThree == R.id.q3_a2){
            return  true;
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(BedouinQuestionsActivity.this, RegistrationActivity.class);
        startActivity(intent);
        finish();
    }

    private void registerUser(){
        HttpsTrustManager.allowAllSSL();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.API_URL + "users", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(BedouinQuestionsActivity.this, "Done", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(BedouinQuestionsActivity.this, DashboardActivity.class);
                startActivity(intent);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BedouinQuestionsActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("Error", error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("fullName", Global.user.getUsername());
                params.put("phoneNumber", Global.user.getPhone());
                params.put("uid", auth.getCurrentUser().getUid());
                params.put("gender", "");
                params.put("birthday", "");
                params.put("bedouin", userType.equals("Bedouin")? "true" : "false");

                // Inquiry
                params.put("tribe", user.getTribe());

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
