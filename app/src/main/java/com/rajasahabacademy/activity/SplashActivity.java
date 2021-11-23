package com.rajasahabacademy.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.rajasahabacademy.R;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.support.Preference;
import com.rajasahabacademy.support.Utils;

public class SplashActivity extends AppCompatActivity {
    Activity mActivity;
    Preference preference;
    Handler handler = new Handler();
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.darkThemeForceStop();
        setContentView(R.layout.activity_splash);
        init();
    }

    private void init() {
        mActivity = this;
        preference = Preference.getInstance(mActivity);
        setAnimation();
        String isLogin = preference.getString(Constants.Preference.ISLOGIN);
        runnable = () -> {
            if (isLogin.equals("yes"))
                Utils.startActivityFinish(mActivity, HomeActivity.class);
            else Utils.startActivityFinish(mActivity, LoginActivity.class);
        };
        handler.postDelayed(runnable, 4500);

        findViewById(R.id.start_now_lay).setOnClickListener(view -> {
            handler.removeCallbacks(runnable);
            if (isLogin.equals("yes"))
                Utils.startActivityFinish(mActivity, HomeActivity.class);
            else Utils.startActivityFinish(mActivity, LoginActivity.class);
        });
    }

    private void setAnimation() {
        try {
            ImageView ivImage = findViewById(R.id.iv_splash_logo);
            RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.1f, Animation.RELATIVE_TO_SELF, 0.1f);
            anim.setInterpolator(new LinearInterpolator());
            anim.setRepeatCount(Animation.INFINITE);
            anim.setDuration(1000);
            ivImage.startAnimation(anim);
            new Handler().postDelayed(() -> {
                ivImage.setAnimation(null);
                startTextAnimation();
                fadeInFadeOutAnimation();
            }, 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startTextAnimation() {
        try {
            TextView tvWelcome = findViewById(R.id.tv_splash_welcome);
            TextView tvWelcomeText = findViewById(R.id.tv_splash_welcome_txt);
            tvWelcome.setVisibility(View.VISIBLE);
            tvWelcomeText.setVisibility(View.VISIBLE);
            Animation welcomeAnimation = AnimationUtils.loadAnimation(mActivity, R.anim.splash_text_scale);
            Animation welcomeAnimationText = AnimationUtils.loadAnimation(mActivity, R.anim.splash_text_scale);
            welcomeAnimation.reset();
            welcomeAnimationText.reset();
            tvWelcome.clearAnimation();
            tvWelcome.startAnimation(welcomeAnimation);
            tvWelcomeText.clearAnimation();
            tvWelcomeText.startAnimation(welcomeAnimationText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fadeInFadeOutAnimation() {
        try {
            TextView tvEasyText = findViewById(R.id.tv_easy_text);
            TextView tvPlatform = findViewById(R.id.tv_platform);
            tvEasyText.setVisibility(View.VISIBLE);
            tvPlatform.setVisibility(View.VISIBLE);
            AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
            tvEasyText.startAnimation(fadeIn);
            tvPlatform.startAnimation(fadeIn);
            fadeIn.setDuration(2000);
            fadeIn.setFillAfter(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}