package com.abdelrahman.irihackathon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WrongBedouin extends AppCompatActivity {

    private Button backToUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrong_bedouin);

        backToUser = findViewById(R.id.btn_back_user_reg);

        backToUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WrongBedouin.this, RegistrationActivity.class);
                intent.putExtra("UserType", "User");
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(WrongBedouin.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
