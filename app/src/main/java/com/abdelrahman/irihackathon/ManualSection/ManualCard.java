package com.abdelrahman.irihackathon.ManualSection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.abdelrahman.irihackathon.Common.Global;
import com.abdelrahman.irihackathon.ExperienceSection.AdventureActivity;
import com.abdelrahman.irihackathon.ExperienceSection.BedouinFoodActivity;
import com.abdelrahman.irihackathon.R;
import com.squareup.picasso.Picasso;

public class ManualCard extends AppCompatActivity {

    private ImageView back, img;
    private TextView headline, description, location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_card);

        back = findViewById(R.id.back);
        img = findViewById(R.id.img);
        headline = findViewById(R.id.txt_headline);
        description = findViewById(R.id.txt_description);
        location = findViewById(R.id.txt_location);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Global.chooseManual.equals("Manual")){

                    if (Global.categoryManual.equals(getResources().getString(R.string.manual_dashboard_songs))){
                        startActivity(new Intent(ManualCard.this, SongsActivity.class));
                        finish();
                    } else if (Global.categoryManual.equals(getResources().getString(R.string.manual_dashboard_medicine))){
                        startActivity(new Intent(ManualCard.this, MedicineActivity.class));
                        finish();
                    } else if (Global.categoryManual.equals(getResources().getString(R.string.manual_dashboard_cooking))){
                        startActivity(new Intent(ManualCard.this, CookingActivity.class));
                        finish();
                    } else if (Global.categoryManual.equals(getResources().getString(R.string.manual_dashboard_clothing))){
                        startActivity(new Intent(ManualCard.this, ClothingActivity.class));
                        finish();
                    } else if (Global.categoryManual.equals(getResources().getString(R.string.manual_dashboard_handcrafting))){
                        startActivity(new Intent(ManualCard.this, CarpetActivity.class));
                        finish();
                    }
                } else if (Global.chooseManual.equals("Experience")){

                    if (Global.categoryExperience.equals("Adventure")){
                        startActivity(new Intent(ManualCard.this, AdventureActivity.class));
                        finish();
                    } else if (Global.categoryExperience.equals("BedouinFood")){
                        startActivity(new Intent(ManualCard.this, BedouinFoodActivity.class));
                        finish();
                    }
                }
            }
        });

        if (Global.chooseManual.equals("Experience")){
            location.setVisibility(View.VISIBLE);

            headline.setText(Global.manualObject.getTitle());
            description.setText(Global.manualObject.getLocation());
            location.setText(Global.manualObject.getLocation());
            Picasso.get().load(Global.manualObject.getMedia()).into(img);
        } else {
            headline.setText(Global.manualObject.getTitle());
            description.setText(Global.manualObject.getLocation());
            Picasso.get().load(Global.manualObject.getMedia()).into(img);
        }
    }

    @Override
    public void onBackPressed() {
        if (Global.chooseManual.equals("Manual")){

            if (Global.categoryManual.equals(getResources().getString(R.string.manual_dashboard_songs))){
                startActivity(new Intent(ManualCard.this, SongsActivity.class));
                finish();
            } else if (Global.categoryManual.equals(getResources().getString(R.string.manual_dashboard_medicine))){
                startActivity(new Intent(ManualCard.this, MedicineActivity.class));
                finish();
            } else if (Global.categoryManual.equals(getResources().getString(R.string.manual_dashboard_cooking))){
                startActivity(new Intent(ManualCard.this, CookingActivity.class));
                finish();
            } else if (Global.categoryManual.equals(getResources().getString(R.string.manual_dashboard_clothing))){
                startActivity(new Intent(ManualCard.this, ClothingActivity.class));
                finish();
            } else if (Global.categoryManual.equals(getResources().getString(R.string.manual_dashboard_handcrafting))){
                startActivity(new Intent(ManualCard.this, CarpetActivity.class));
                finish();
            }
        } else if (Global.chooseManual.equals("Experience")){

            if (Global.categoryExperience.equals("Adventure")){
                startActivity(new Intent(ManualCard.this, AdventureActivity.class));
                finish();
            } else if (Global.categoryExperience.equals("BedouinFood")){
                startActivity(new Intent(ManualCard.this, BedouinFoodActivity.class));
                finish();
            }
        }

    }
}
