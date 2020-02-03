package com.abdelrahman.irihackathon.DictionarySection;

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
import com.abdelrahman.irihackathon.Common.HttpsTrustManager;
import com.abdelrahman.irihackathon.DashboardActivity;
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

public class AddDictionaryActivity extends AppCompatActivity {

    private EditText edtWord, edtMeaning;
    private Button btnAdd;
    private ImageView back;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dictionary);

        edtWord = findViewById(R.id.edt_word);
        edtMeaning = findViewById(R.id.edt_word_translated);
        btnAdd = findViewById(R.id.btn_done_add);
        back = findViewById(R.id.back);

        auth = FirebaseAuth.getInstance();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddDictionaryActivity.this, DictionaryActivity.class));
                finish();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(edtWord.getText().toString()) &&
                        !TextUtils.isEmpty(edtMeaning.getText().toString())){
                    addWord(auth.getCurrentUser().getUid(), edtWord.getText().toString(), edtMeaning.getText().toString());
                }
            }
        });
    }

    private void addWord(final String addedBy, final String word, final String translatedWord){
        HttpsTrustManager.allowAllSSL();
        String url = Constants.API_URL + "dictionary/addWord";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(AddDictionaryActivity.this, response, Toast.LENGTH_LONG).show();
                Log.e("Error", response);
                startActivity(new Intent(AddDictionaryActivity.this, DictionaryActivity.class));
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddDictionaryActivity.this, error+"", Toast.LENGTH_LONG).show();
                Log.e("Error", error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("addedBy", addedBy);
                params.put("word", word);
                params.put("arabicWord", translatedWord);
                params.put("englishWord", "");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AddDictionaryActivity.this, DictionaryActivity.class));
        finish();
    }
}
