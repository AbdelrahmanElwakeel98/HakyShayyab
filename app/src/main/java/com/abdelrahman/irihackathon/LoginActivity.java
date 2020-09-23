package com.abdelrahman.irihackathon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.abdelrahman.irihackathon.Common.CustomDialogBox;
import com.abdelrahman.irihackathon.Common.Global;
import com.abdelrahman.irihackathon.Interface.IDialogBoxListener;
import com.abdelrahman.irihackathon.Model.User;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity implements IDialogBoxListener {

    private Button back, login;
    private EditText edtPhone;
    private String userType, phoneNumber;

    private CustomDialogBox customDialogBox;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseAuth auth;
    private String verificationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Buttons
        back = findViewById(R.id.btn_back);
        login = findViewById(R.id.btn_login);

        // Edit texts
        edtPhone = findViewById(R.id.edt_phone_reg);

        customDialogBox = CustomDialogBox.getInstance();

        userType = Global.userType;


        StartFireBaseLogin();

        // Listeners on buttons
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneNumber = "+2" + edtPhone.getText().toString();

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phoneNumber,                     // Phone number to verify
                        60,                           // Timeout duration
                        TimeUnit.SECONDS,                // Unit of timeout
                        LoginActivity.this,        // Activity (for callback binding)
                        mCallback);
            }
        });
    }

    private void StartFireBaseLogin() {

        auth = FirebaseAuth.getInstance();
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(LoginActivity.this, R.string.verification_success, Toast.LENGTH_SHORT).show();

                Global.UID = auth.getCurrentUser().getUid();
                Log.e("UID", auth.getCurrentUser().getUid());

                if (userType.equals("Bedouin")){
                    saveLoggedUser(true, auth.getCurrentUser().getUid());
                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    customDialogBox.dismissDialog();
                    finish();
                } else {
                    saveLoggedUser(false, auth.getCurrentUser().getUid());
                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    customDialogBox.dismissDialog();
                    finish();
                }

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.e("error", e.getMessage());
                Toast.makeText(LoginActivity.this, R.string.verification_fail, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                mResendToken = forceResendingToken;
                showVerifyDialog();
                Toast.makeText(LoginActivity.this, R.string.code_sent, Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void showVerifyDialog(){
        customDialogBox.showLoginDialog(LoginActivity.this, this);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onClickPositiveButton(DialogInterface dialogInterface, String otp) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otp);
        SigninWithPhone(credential, dialogInterface);
    }

    private void SigninWithPhone(PhoneAuthCredential credential, final DialogInterface dialogInterface) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Global.UID = auth.getCurrentUser().getUid();

                            if (userType.equals("Bedouin")){
                                saveLoggedUser(true, auth.getCurrentUser().getUid());
                                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                startActivity(intent);
                                customDialogBox.dismissDialog();
                                finish();
                            } else {
                                saveLoggedUser(false, auth.getCurrentUser().getUid());
                                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                startActivity(intent);
                                customDialogBox.dismissDialog();
                                finish();
                            }

                            dialogInterface.dismiss();
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, R.string.incorrect_otp, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallback,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    private void saveLoggedUser(boolean isBedouin, String user_id){

        SharedPreferences sp;
        sp = getSharedPreferences("shayyab_logged_user", MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();

        editor.putBoolean("isLogged",true);
        editor.putBoolean("isBedouin", isBedouin);
        editor.putString("user_id", user_id);

        editor.apply();

    }

    @Override
    public void onClickNegativeButton(DialogInterface dialogInterface) {
        phoneNumber = "+2" + edtPhone.getText().toString();

        resendVerificationCode(phoneNumber, mResendToken);
    }
}
