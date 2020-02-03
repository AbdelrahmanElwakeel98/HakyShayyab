package com.abdelrahman.irihackathon.ExperienceSection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.abdelrahman.irihackathon.Common.Global;
import com.abdelrahman.irihackathon.DashboardActivity;
import com.abdelrahman.irihackathon.R;

public class ExperienceDashboardActivity extends AppCompatActivity {

    private ImageView adventure, cooking, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experience_dashboard);

        adventure = findViewById(R.id.btn_adventure_exp);
        cooking = findViewById(R.id.btn_cooking_exp);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ExperienceDashboardActivity.this, DashboardActivity.class));
                finish();
            }
        });

        adventure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.categoryExperience = "Adventure";
                startActivity(new Intent(ExperienceDashboardActivity.this, AdventureActivity.class));
                finish();
            }
        });

        cooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.categoryExperience = "BedouinFood";
                startActivity(new Intent(ExperienceDashboardActivity.this, BedouinFoodActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ExperienceDashboardActivity.this, DashboardActivity.class));
        finish();
    }
}
