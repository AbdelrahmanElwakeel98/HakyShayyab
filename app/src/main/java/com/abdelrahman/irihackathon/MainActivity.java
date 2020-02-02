package com.abdelrahman.irihackathon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.abdelrahman.irihackathon.Common.Global;

public class MainActivity extends AppCompatActivity {

    private Button bedouin, user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bedouin = findViewById(R.id.btn_register_bedouin);
        user = findViewById(R.id.btn_register_user);

        bedouin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                Global.userType = "Bedouin";
                startActivity(intent);
                finish();
            }
        });

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                Global.userType = "User";
                startActivity(intent);
                finish();
            }
        });

    }
}
