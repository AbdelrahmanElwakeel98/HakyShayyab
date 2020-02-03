package com.abdelrahman.irihackathon.ManualSection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.abdelrahman.irihackathon.Common.Global;
import com.abdelrahman.irihackathon.DashboardActivity;
import com.abdelrahman.irihackathon.R;

public class ManualDashboardActivity extends AppCompatActivity {

    private Button btnSongs, btnMedicine, btnClothing, btnCooking, btnHandCrafting;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_dashboard);

        btnClothing = findViewById(R.id.btn_clothing);
        btnCooking = findViewById(R.id.btn_fire);
        btnHandCrafting = findViewById(R.id.btn_carpet);
        btnMedicine = findViewById(R.id.btn_medicine);
        btnSongs = findViewById(R.id.btn_songs);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ManualDashboardActivity.this, DashboardActivity.class));
                finish();
            }
        });

        btnSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.categoryManual = getResources().getString(R.string.manual_dashboard_songs);
                startActivity(new Intent(ManualDashboardActivity.this, SongsActivity.class));
                finish();
            }
        });

        btnMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.categoryManual = getResources().getString(R.string.manual_dashboard_medicine);
                startActivity(new Intent(ManualDashboardActivity.this, MedicineActivity.class));
                finish();
            }
        });

        btnHandCrafting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.categoryManual = getResources().getString(R.string.manual_dashboard_handcrafting);
                startActivity(new Intent(ManualDashboardActivity.this, CarpetActivity.class));
                finish();
            }
        });

        btnCooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.categoryManual = getResources().getString(R.string.manual_dashboard_cooking);
                startActivity(new Intent(ManualDashboardActivity.this, CookingActivity.class));
                finish();
            }
        });

        btnClothing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.categoryManual = getResources().getString(R.string.manual_dashboard_clothing);
                startActivity(new Intent(ManualDashboardActivity.this, ClothingActivity.class));
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ManualDashboardActivity.this, DashboardActivity.class));
        finish();
    }
}
