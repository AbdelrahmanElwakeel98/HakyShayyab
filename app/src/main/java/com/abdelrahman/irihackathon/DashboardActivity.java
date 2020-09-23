package com.abdelrahman.irihackathon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


import com.abdelrahman.irihackathon.Common.Global;
import com.abdelrahman.irihackathon.DictionarySection.DictionaryActivity;
import com.abdelrahman.irihackathon.ExperienceSection.ExperienceDashboardActivity;
import com.abdelrahman.irihackathon.ManualSection.ManualDashboardActivity;
import com.abdelrahman.irihackathon.QuestionSection.QuestionsActivity;
import com.google.firebase.auth.FirebaseAuth;

public class DashboardActivity extends AppCompatActivity {

    private Button btnQuestion, btnManual, btnDictionary, btnExperience;
    private Toolbar toolbar;

    private String userType, userData;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        btnDictionary = findViewById(R.id.btn_dictionary_section);
        btnExperience = findViewById(R.id.btn_experience);
        btnManual = findViewById(R.id.btn_manual_section);
        btnQuestion = findViewById(R.id.btn_questions_section);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        auth = FirebaseAuth.getInstance();

        btnQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(DashboardActivity.this, QuestionsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnExperience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.chooseManual = "Experience";
                Intent intent =  new Intent(DashboardActivity.this, ExperienceDashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnDictionary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(DashboardActivity.this, DictionaryActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.chooseManual = "Manual";
                Intent intent =  new Intent(DashboardActivity.this, ManualDashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.logout_option_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.logout:
                auth.signOut();

                SharedPreferences preferences =getSharedPreferences("shayyab_logged_user", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();

                Global.UID = "";
                Global.userType = "";

                startActivity(new Intent(DashboardActivity.this, MainActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
