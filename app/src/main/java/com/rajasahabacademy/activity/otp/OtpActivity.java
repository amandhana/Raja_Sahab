package com.rajasahabacademy.activity.otp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.messaging.FirebaseMessaging;
import com.loopj.android.http.RequestParams;
import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.profile.activity.ProfileActivity;
import com.rajasahabacademy.activity.home.activity.HomeActivity;
import com.rajasahabacademy.api.Communicator;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.api.CustomResponseListener;
import com.rajasahabacademy.activity.login.model.LoginResponse;
import com.rajasahabacademy.support.Preference;
import com.rajasahabacademy.support.SmsReceiver;
import com.rajasahabacademy.support.Utils;

import org.json.JSONObject;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

public class OtpActivity extends AppCompatActivity implements View.OnClickListener {

    Activity mActivity;
    private FirebaseAuth mAuth;
    private String mVerificationId;
    String mobileNumber = "";
    String cCode = "";
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    OtpTextView otpTextView;
    String firebaseToken = "";
    Preference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.darkThemeForceStop();
        setContentView(R.layout.activity_otp);
        init();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utils.hideKeyboard(mActivity);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void init() {
        mActivity = this;
        preference = Preference.getInstance(mActivity);
        mAuth = FirebaseAuth.getInstance();
        clickListener();
        setSpnnableText();
        getMobileData();
        initCallback();
        getFirebaseToken();
        bindOtpMessageReceiver();
    }

    private void clickListener() {
        otpTextView = findViewById(R.id.otp_view);
        TextView tvNext = findViewById(R.id.tv_next);
        tvNext.setOnClickListener(this);
    }


    private void setSpnnableText() {
        TextView tvDontReceive = findViewById(R.id.tv_dont_receive);
        Spannable wordtoSpan = new SpannableString(getString(R.string.don_t_receive));
        wordtoSpan.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View v) {
                resendOtp();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setUnderlineText(false);
            }
        }, 15, 21, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordtoSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), 15, 21, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvDontReceive.setText(wordtoSpan);
        tvDontReceive.setMovementMethod(LinkMovementMethod.getInstance());
        tvDontReceive.setHighlightColor(Color.TRANSPARENT);
    }

    private void getMobileData() {
        Bundle bundle = getIntent().getBundleExtra(getString(R.string.bundle));
        mVerificationId = Objects.requireNonNull(bundle).getString(getString(R.string.verification_id));
        mobileNumber = bundle.getString(getString(R.string.phone_number));
        cCode = bundle.getString(getString(R.string.country_code));

        String otpTextPhoneStr = mobileNumber;
        otpTextPhoneStr = otpTextPhoneStr.substring(8);
        TextView tvOtpPhone = findViewById(R.id.tv_otp_phone);
        tvOtpPhone.setText(getString(R.string.otp_phone_text_format, "Please check your mobile number ********", otpTextPhoneStr, " to continue"));

        otpTextView.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {

            }

            @Override
            public void onOTPComplete(String otp) {
                Utils.hideKeyboard(mActivity);
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
                Toast.makeText(mActivity, "Invalid phone number. Please check and try again.", Toast.LENGTH_SHORT).show();
                if (e instanceof FirebaseTooManyRequestsException) {
                    Toast.makeText(mActivity, "Quota exceeded.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                Utils.hideProgressBar();
                mVerificationId = verificationId;
            }
        };
    }

    private void verifyOtp() {
        if (otpTextView.getOTP().length() < 6) {
            Utils.showToastPopup(this, "Enter OTP");
        } else {
            if (Utils.isNetworkAvailable(mActivity)) {
                verifyPhoneNumberWithCode();
            } else Utils.showToastPopup(this, getResources().getString(R.string.internet_error));
        }
    }

    private void verifyPhoneNumberWithCode() {
        if (Utils.isNetworkAvailable(mActivity)) {
            Utils.showProgressBar(mActivity);
            if (mVerificationId != null) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otpTextView.getOTP());
                signInWithPhoneAuthCredential(credential);
            }
        } else
            Toast.makeText(mActivity, "Please check your internet connection and try again", Toast.LENGTH_SHORT).show();
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    Utils.hideProgressBar();
                    if (task.isSuccessful()) {
                        Log.d("TAG", "signInWithCredential:success");
                        FirebaseUser user = Objects.requireNonNull(task.getResult()).getUser();
                        checkLoginStatus();
                    } else {
                        Log.w("TAG", "signInWithCredential:failure", task.getException());
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(mActivity, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void resendOtp() {
        otpTextView.setOTP("");
        Utils.showProgressBar(mActivity);
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91" + mobileNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .setForceResendingToken(Constants.AppSaveData.token)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void getFirebaseToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        return;
                    }
                    firebaseToken = task.getResult();
                });
    }

    private void bindOtpMessageReceiver() {
        SmsReceiver.bindListener(messageText -> {
            otpTextView.setOTP(messageText);
            verifyOtp();
        });
    }

    private void checkLoginStatus() {
        if (Utils.isNetworkAvailable(mActivity)) {
            Utils.showProgressBar(mActivity);
            Utils.hideKeyboard(mActivity);
            RequestParams params = new RequestParams();
            try {
                params.put(Constants.Params.PHONE, mobileNumber);
                params.put(Constants.Params.DEVICE_TOKEN, firebaseToken);
                params.put(Constants.Params.DEVICE_ID, Utils.getDeviceId(mActivity));
                Utils.printLog("LoginParams", params.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Communicator communicator = new Communicator();
            communicator.post(101, mActivity, Constants.Apis.LOGIN, params, new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    Utils.hideProgressBar();
                    try {
                        if (response != null && !response.equals("")) {
                            LoginResponse modelResponse = (LoginResponse) Utils.getObject(response, LoginResponse.class);
                            if (modelResponse != null && modelResponse.getMessage() != null) {
                                if (modelResponse.getMessage().equalsIgnoreCase("ok")) {
                                    preference.putString(Constants.Preference.ISLOGIN, "yes");
                                    Utils.saveLoginUser(mActivity, modelResponse);
                                    Utils.startActivityFinish(mActivity, HomeActivity.class);
                                } else if (modelResponse.getMessage().equalsIgnoreCase("deactive"))
                                    Utils.showToastPopup(mActivity, modelResponse.getNotification());
                                else
                                    registerUser();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getString("message").equalsIgnoreCase("failed"))
                                registerUser();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Throwable error) {
                    Utils.hideProgressBar();
                    Utils.showToastPopup(mActivity, error.getLocalizedMessage());
                }
            });
        } else Utils.showToastPopup(mActivity, getString(R.string.internet_error));
    }

    private void registerUser() {
        if (Utils.isNetworkAvailable(mActivity)) {
            Utils.showProgressBar(mActivity);
            Utils.hideKeyboard(mActivity);
            RequestParams params = new RequestParams();
            try {
                params.put(Constants.Params.PHONE, mobileNumber);
                params.put(Constants.Params.DEVICE_ID, Utils.getDeviceId(mActivity));
                Utils.printLog("RegisterParams", params.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Communicator communicator = new Communicator();
            communicator.post(101, mActivity, Constants.Apis.REGISTER, params, new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    Utils.hideProgressBar();
                    try {
                        if (response != null && !response.equals("")) {
                            LoginResponse modelResponse = (LoginResponse) Utils.getObject(response, LoginResponse.class);
                            if (modelResponse != null && modelResponse.getMessage() != null) {
                                if (modelResponse.getMessage().equalsIgnoreCase("ok")) {
                                    preference.putString(Constants.Preference.ISLOGIN, "yes");
                                    Utils.saveLoginUser(mActivity, modelResponse);
                                    Intent intent = new Intent(mActivity, ProfileActivity.class);
                                    intent.putExtra(Constants.Preference.FROM_WHERE, "OtpScreen");
                                    startActivity(intent);
                                    finish();
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                } else {
                                    try {
                                        JSONObject object = new JSONObject(response);
                                        String message = object.optString("notification");
                                        Utils.showToastPopup(mActivity, message);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Throwable error) {
                    Utils.hideProgressBar();
                    Utils.showToastPopup(mActivity, error.getLocalizedMessage());
                }
            });
        } else Utils.showToastPopup(mActivity, getString(R.string.internet_error));
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_next) {
            verifyOtp();
        }
    }
}