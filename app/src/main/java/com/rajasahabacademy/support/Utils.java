package com.rajasahabacademy.support;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDelegate;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.loopj.android.http.BuildConfig;
import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.LoginActivity;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.model.login.LoginResponse;
import com.rajasahabacademy.model.profile_detail.Success;
import com.razorpay.Checkout;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static Dialog dialog;
    static Dialog dialogPopup;

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void darkThemeForceStop() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    public static void hideNavigationButton(Activity context){
        context.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY );
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        context.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    public static void setImageUsingGlide(Activity context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error_placholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
                .dontAnimate()
                .dontTransform();

        Glide.with(context)
                .load(url)
                .apply(options)
                .into(imageView);
    }

    public static void setProfileImageUsingGlide(Activity context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_user_placeholder)
                .error(R.drawable.ic_user_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
                .dontAnimate()
                .dontTransform();

        Glide.with(context)
                .load(url)
                .apply(options)
                .into(imageView);
    }


    public static void clearGlide(Activity context, ImageView imageView) {
        Glide.with(context)
                .clear(imageView);
    }

    public static void setHtmlText(String text, TextView textView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT));
        } else {
            textView.setText(Html.fromHtml(text));
        }
    }

    public static void startPayment(Activity mActivity, int paybleAmount) {
        Checkout co = new Checkout();
        try {
            JSONObject options = new JSONObject();
            options.put("name", Utils.getSaveLoginUser(mActivity).getResults().getName());
            options.put("description", "Demoing Charges");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", paybleAmount * 100);

            JSONObject preFill = new JSONObject();
            preFill.put("email", Utils.getSaveLoginUser(mActivity).getResults().getEmail());
            preFill.put("contact", Utils.getSaveLoginUser(mActivity).getResults().getPhone());
            options.put("prefill", preFill);
            co.open(mActivity, options);
        } catch (Exception e) {
            Toast.makeText(mActivity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    public static boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public static void printLog(String key, String message) {
        if (BuildConfig.DEBUG) {
            Log.e(key, message);
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static Uri getImageUri(Activity context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static void showSoftKeyboard(View view, Activity context) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public static void hideSoftKeyboard(View view, Activity context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public static void saveLoginUser(Activity context, LoginResponse loginResponse) {
        Gson gson = new Gson();
        Preference preference = Preference.getInstance(context);
        preference.putString("loginUser", gson.toJson(loginResponse));
    }
    public static void saveProfileDetail(Activity context, Success success) {
        Gson gson = new Gson();
        Preference preference = Preference.getInstance(context);
        preference.putString("profile_detail", gson.toJson(success));
    }

    public static LoginResponse getSaveLoginUser(Activity context) {
        Preference preference = Preference.getInstance(context);
        String resposne = preference.getString("loginUser");
        return (LoginResponse) getObject(resposne, LoginResponse.class);
    }
    public static Success getProfileDetail(Activity context) {
        Preference preference = Preference.getInstance(context);
        String resposne = preference.getString("profile_detail");
        return (Success) getObject(resposne, Success.class);
    }

    public static String getUserId(Activity context) {
        Preference preference = Preference.getInstance(context);
        String resposne = preference.getString("loginUser");
        LoginResponse loginResponse = (LoginResponse) getObject(resposne, LoginResponse.class);
        return loginResponse.getResults().getId();
    }

    public static String getImage(Activity context) {
        Preference preference = Preference.getInstance(context);
        String resposne = preference.getString("loginUser");
        LoginResponse loginResponse = (LoginResponse) getObject(resposne, LoginResponse.class);
        return loginResponse.getResults().getImage();
    }

    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }


    public static void startActivity(Activity activity, Class<?> aClass) {
        activity.startActivity(new Intent(activity, aClass));
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public static void startActivityBundle(Activity activity, Class<?> aClass, Bundle bundle) {
        Intent intent = new Intent(activity, aClass);
        intent.putExtras(bundle);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public static void startActivityBundleFinish(Activity activity, Class<?> aClass, Bundle bundle) {
        Intent intent = new Intent(activity, aClass);
        intent.putExtras(bundle);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        activity.finish();
    }

    public static void startActivityFinish(Activity activity, Class<?> aClass) {
        activity.startActivity(new Intent(activity, aClass));
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        activity.finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void showProgressBar(Activity activity) {
        try {
            if (dialog != null && dialog.isShowing()) {
                return;
            }
            dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.progress_bar);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideProgressBar() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public static String getCurrentDate() {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getCurrentTime() {
        //"2020-02-04T00:00:00"
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        Date currentTime = Calendar.getInstance().getTime();
        return dateFormat.format(currentTime);
    }

    public static String getFormattedDate(String date) {
        if (date.contains(" ")) {
            StringTokenizer tokenizer = new StringTokenizer(date, " ");
            return tokenizer.nextToken();
        } else return date;
    }

    public static void showToastPopup(Context context, String message) {
        try {
            if (dialogPopup == null) {
                dialogPopup = new Dialog(context);
                dialogPopup.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogPopup.setContentView(R.layout.toast_popup);
                dialogPopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialogPopup.setCanceledOnTouchOutside(false);

                TextView messageTv = dialogPopup.findViewById(R.id.message_tv);
                TextView okBtn = dialogPopup.findViewById(R.id.ok_btn);

                messageTv.setText(message);


                okBtn.setOnClickListener(v -> {
                    dialogPopup.dismiss();
                    dialogPopup = null;
                });

                dialogPopup.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void manageSingleDeviceLogin(Activity context) {
        Preference preference = Preference.getInstance(context);
        context.finishAffinity();
        preference.clearPreference();
        Utils.hideKeyboard(context);
        Utils.startActivity(context, LoginActivity.class);
    }

    public static Object getObject(String name, Class<?> aClass) {
        Gson gson = new Gson();
        return gson.fromJson(name, aClass);
    }
}
