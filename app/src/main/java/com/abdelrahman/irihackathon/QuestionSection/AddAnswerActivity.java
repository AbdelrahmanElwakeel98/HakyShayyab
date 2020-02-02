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

public class AddAnswerActivity extends AppCompatActivity {

    private Button add;
    private EditText edtAnswer;
    private ImageView back;

    private String addedBy, answer, questionId;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_answer);

        add = findViewById(R.id.btn_done_answer);
        edtAnswer = findViewById(R.id.edt_answer);
        back = findViewById(R.id.back);

        auth = FirebaseAuth.getInstance();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(edtAnswer.getText().toString())){
                    addAnswer(auth.getCurrentUser().getUid(), edtAnswer.getText().toString(), Global.selectedQuestion.getId());
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddAnswerActivity.this, QuestionCardActivity.class));
                finish();
            }
        });
    }

    private void addAnswer(final String addedBy, final String answer, final String questionId){
        HttpsTrustManager.allowAllSSL();
        String url = Constants.API_URL + "answers/addAnswer/" + questionId;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(AddAnswerActivity.this, response, Toast.LENGTH_LONG).show();
                Log.e("Error", response);
                startActivity(new Intent(AddAnswerActivity.this, QuestionCardActivity.class));
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddAnswerActivity.this, error+"", Toast.LENGTH_LONG).show();
                Log.e("Error", error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("addedBy", addedBy);
                params.put("answer", answer);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(AddAnswerActivity.this, QuestionCardActivity.class));
        finish();
    }
}
