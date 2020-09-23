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
import android.widget.Toast;

import com.abdelrahman.irihackathon.Common.CustomDialogBox;
import com.abdelrahman.irihackathon.Common.Global;
import com.abdelrahman.irihackathon.Common.HttpsTrustManager;
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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RegistrationActivity extends AppCompatActivity implements IDialogBoxListener {

    private Button back, signUp, login;
    private EditText edtUserName, edtPhone,
            edtTribe;
    private String userType, phoneNumber, userDataObject;
    private String URL_POST = "https://hakysheyab.herokuapp.com/users";

    private CustomDialogBox customDialogBox;

    private User newUser;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseAuth auth;
    private String verificationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Buttons
        back = findViewById(R.id.btn_back);
        signUp = findViewById(R.id.btn_sign_up);
        login = findViewById(R.id.btn_login);

        // Edit texts
        edtUserName = findViewById(R.id.edt_user_reg);
        edtPhone = findViewById(R.id.edt_phone_reg);
        edtTribe = findViewById(R.id.edt_tribe_reg);

        customDialogBox = CustomDialogBox.getInstance();

        // Get the user type
        userType = Global.userType;

        if (userType.equals("Bedouin")) {
            edtTribe.setVisibility(View.VISIBLE);
        }

        StartFireBaseLogin();

        // Listeners on buttons
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                finish();
            }
        });


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user = edtUserName.getText().toString().trim();
                String phoneNum = edtPhone.getText().toString().trim();
                String code = "+2";
                String tribe = edtTribe.getText().toString().trim();

                if (checkFields(user, phoneNum, code, tribe)) {
                    if (userType.equals("Bedouin")) {
                        newUser = new User(user, code + phoneNum, tribe);

                        Global.user = newUser;

                        phoneNumber = "+2" + edtPhone.getText().toString();

                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                phoneNumber,                     // Phone number to verify
                                60,                           // Timeout duration
                                TimeUnit.SECONDS,                // Unit of timeout
                                RegistrationActivity.this,        // Activity (for callback binding)
                                mCallback);

                    } else {
                        newUser = new User(user, code + phoneNum);

                        Global.user = newUser;

                        phoneNumber = "+2" + edtPhone.getText().toString();

                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                phoneNumber,                     // Phone number to verify
                                60,                           // Timeout duration
                                TimeUnit.SECONDS,                // Unit of timeout
                                RegistrationActivity.this,        // Activity (for callback binding)
                                mCallback);
                    }
                }

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private boolean checkFields(String user, String phoneNum
            , String code, String tribe) {

        if (TextUtils.isEmpty(user)) {
            Toast.makeText(getApplicationContext(), R.string.empty_user, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(phoneNum)) {
            Toast.makeText(getApplicationContext(), R.string.empty_phone, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(code)) {
            Toast.makeText(getApplicationContext(), R.string.empty_code, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (phoneNum.length() < 11) {
            Toast.makeText(getApplicationContext(), R.string.invalid_phone, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (userType.equals("Bedouin")) {
            if (TextUtils.isEmpty(tribe)) {
                Toast.makeText(getApplicationContext(), R.string.empty_tribe, Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private void StartFireBaseLogin() {

        auth = FirebaseAuth.getInstance();
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                //Toast.makeText(RegistrationActivity.this, R.string.verification_success, Toast.LENGTH_SHORT).show();

                /*if (userType.equals("Bedouin")) {
                    Intent intent = new Intent(RegistrationActivity.this, BedouinQuestionsActivity.class);
                    startActivity(intent);
                    customDialogBox.dismissDialog();
                    finish();
                } else {
                    Intent intent = new Intent(RegistrationActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    customDialogBox.dismissDialog();
                    finish();
                }*/

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(RegistrationActivity.this, R.string.verification_fail, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                mResendToken = forceResendingToken;
                showVerifyDialog();
                Toast.makeText(RegistrationActivity.this, R.string.code_sent, Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void showVerifyDialog() {
        customDialogBox.showLoginDialog(RegistrationActivity.this, this);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
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
                            if (Global.userType.equals("Bedouin")) {
                                Intent intent = new Intent(RegistrationActivity.this, BedouinQuestionsActivity.class);
                                startActivity(intent);
                                dialogInterface.dismiss();
                                finish();
                            } else {
                                registerUser(dialogInterface);
                            }
                        } else {
                            Toast.makeText(RegistrationActivity.this, R.string.incorrect_otp, Toast.LENGTH_SHORT).show();
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

    private void registerUser(final DialogInterface dialogInterface) {
        HttpsTrustManager.allowAllSSL();
        String tag_string_req = "string_req";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_POST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(RegistrationActivity.this, "Done", Toast.LENGTH_LONG).show();

                Global.UID = auth.getCurrentUser().getUid();
                saveLoggedUser(false, auth.getCurrentUser().getUid());

                Intent intent = new Intent(RegistrationActivity.this, DashboardActivity.class);
                startActivity(intent);
                dialogInterface.dismiss();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegistrationActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                //Log.e("Error", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("fullName", Global.user.getUsername());
                params.put("phoneNumber", "+2" + Global.user.getPhone());
                params.put("uid", auth.getCurrentUser().getUid());
                params.put("gender", "");
                params.put("birthday", "");
                params.put("bedouin", userType.equals("Bedouin") ? "true" : "false");
                if (Global.userType.equals("Bedouin")) {
                    params.put("tribeID", Global.user.getTribe());
                }
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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
        phoneNumber = Global.user.getPhone();

        Toast.makeText(RegistrationActivity.this, phoneNumber, Toast.LENGTH_LONG).show();

        resendVerificationCode(phoneNumber, mResendToken);
    }
}
