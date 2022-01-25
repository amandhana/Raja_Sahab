package com.rajasahabacademy.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.rajasahabacademy.R;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.support.Utils;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Activity mActivity;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    FirebaseAuth mAuth;
    EditText etPhone;
    ActivityResultLauncher<Intent> loginActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.darkThemeForceStop();
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        mActivity = this;
        mAuth = FirebaseAuth.getInstance();
        clickListener();
        initCallback();
        onActivityResultLauncher();
    }

    private void clickListener() {
        etPhone = findViewById(R.id.et_phone);
        TextView tvSendOtp = findViewById(R.id.tv_send_otp);
        tvSendOtp.setOnClickListener(this);
    }

    private void askForPermission() {
        String[] permissions = {Manifest.permission.RECEIVE_SMS};
        Permissions.check(mActivity, permissions, null, null, new PermissionHandler() {
            @Override
            public void onGranted() {

            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                super.onDenied(context, deniedPermissions);
            }
        });
    }

    private void initCallback() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                Log.e("LoginActivity", "onVerificationCompleted:" + credential);
                Utils.hideProgressBar();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.e("LoginActivity", "onVerificationFailed " + e, e);
                Utils.hideProgressBar();
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(mActivity, "Quota exceeded", Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Toast.makeText(mActivity, "Quota exceeded.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                mVerificationId = verificationId;
                mResendToken = token;
                proceedToVerifyOtp(mVerificationId, etPhone.getText().toString().trim());
            }
        };
    }

    private void onActivityResultLauncher() {
        loginActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData() != null) {
                            Intent data = result.getData();
                            String message = data.getStringExtra("message");
                            Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void sendOtp() {
        String phoneStr = etPhone.getText().toString();
        if (phoneStr.isEmpty())
            Utils.showToastPopup(mActivity, getString(R.string.phone_empty_validation));
        else if (phoneStr.length() < 10)
            Utils.showToastPopup(mActivity, getString(R.string.phone_valid_validation));
        else startPhoneNumberVerification("+91" + phoneStr);
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        Utils.showProgressBar(mActivity);
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void proceedToVerifyOtp(String mVerificationId, String mobileNumber) {
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.verification_id), mVerificationId);
        bundle.putString(getString(R.string.phone_number), mobileNumber);
        bundle.putString(getString(R.string.country_code), "+91");

        Constants.AppSaveData.token = mResendToken;
        Utils.hideProgressBar();
        Intent intent = new Intent(this, OtpActivity.class);
        intent.putExtra(getString(R.string.bundle), bundle);
        loginActivityResultLauncher.launch(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_send_otp) {
            sendOtp();
        }
    }
}