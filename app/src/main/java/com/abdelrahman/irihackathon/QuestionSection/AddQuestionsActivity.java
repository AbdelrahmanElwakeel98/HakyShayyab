package com.abdelrahman.irihackathon.QuestionSection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.abdelrahman.irihackathon.Common.Constants;
import com.abdelrahman.irihackathon.Common.Global;
import com.abdelrahman.irihackathon.Common.HttpsTrustManager;
import com.abdelrahman.irihackathon.Model.User;
import com.abdelrahman.irihackathon.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

public class AddQuestionsActivity extends AppCompatActivity {

    private Button add;
    private EditText edtQuestion;
    private ImageView back;

    private String userType, userData, categoryID;
    private User user;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_questions);

        add = findViewById(R.id.btn_done_add);
        edtQuestion = findViewById(R.id.edt_asked_question);
        back = findViewById(R.id.back);

        userType = Global.userType;
        categoryID = Global.categoryQuestion;

        /*Gson gson = new Gson();
        user = gson.fromJson(userData, User.class);*/

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(edtQuestion.getText().toString())){
                    auth = FirebaseAuth.getInstance();

                    if (Global.UID == ""){
                        Toast.makeText(AddQuestionsActivity.this, R.string.auth_null_alert, Toast.LENGTH_LONG).show();
                    } else {
                        askQuestion(Global.UID, categoryID, edtQuestion.getText().toString());
                    }
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddQuestionsActivity.this, QuestionsActivity.class));
                finish();
            }
        });
    }

    private void askQuestion(final String addedBy, final String categoryID, final String question){
        HttpsTrustManager.allowAllSSL();
        String url = Constants.API_URL + "questions/addQuestion";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(AddQuestionsActivity.this, getString(R.string.add_done), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(AddQuestionsActivity.this, QuestionsActivity.class);
                startActivity(intent);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddQuestionsActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("Error", error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("addedBy", addedBy);
                params.put("categoryID", categoryID);
                params.put("question", question);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AddQuestionsActivity.this, QuestionsActivity.class));
        finish();
    }
}
