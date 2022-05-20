package com.rajasahabacademy.activity.short_video.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager2.widget.ViewPager2;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;

import com.loopj.android.http.RequestParams;
import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.short_video.adapter.ShortVideoViewPagerAdapter;
import com.rajasahabacademy.api.Communicator;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.api.CustomResponseListener;
import com.rajasahabacademy.activity.short_video.model.Datum;
import com.rajasahabacademy.activity.short_video.model.ShortVideosResponse;
import com.rajasahabacademy.support.Utils;

import java.util.List;

public class ShortVideoActivity extends AppCompatActivity implements View.OnClickListener {
    Activity mActivity;
    ViewPager2 viewPagerShortVideo;

    ShortVideoViewPagerAdapter viewPagerAdapter;
    List<Datum> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_short_video);
        init();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utils.hideKeyboard(mActivity);
        if (viewPagerAdapter != null)
            viewPagerAdapter.releasePlayer();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Utils.hideKeyboard(mActivity);
        if (viewPagerAdapter != null)
            viewPagerAdapter.releasePlayer();
    }

    private void init() {
        mActivity = this;
        setClickListener();
        setUpShortVideo();
        viewPagerShortVideo = findViewById(R.id.view_pager_2_short_video);
        viewPagerShortVideo.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                int currentPosition = viewPagerShortVideo.getCurrentItem();
                list.get(currentPosition).setFlag(true);
                new Handler(Looper.getMainLooper()).postDelayed(() -> viewPagerAdapter.notifyItemChanged(position), 800);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

    public void loadNextPage(int position){
        viewPagerShortVideo.setCurrentItem(position);
    }

    private void setClickListener() {
        CardView cvmenu = findViewById(R.id.cv_back);
        cvmenu.setOnClickListener(this);
    }

    private void setUpShortVideo() {
        if (Utils.isNetworkAvailable(mActivity)) {
            Utils.showProgressBar(mActivity);
            Utils.hideKeyboard(mActivity);
            RequestParams params = new RequestParams();
            try {
                params.put(Constants.Params.USER_ID, Utils.getUserId(mActivity));
                Utils.printLog("ProfileDetailParams", params.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Communicator communicator = new Communicator();
            communicator.post(101, mActivity, Constants.Apis.SHORT_VIDEOS, params, new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    Utils.hideProgressBar();
                    try {
                        if (response != null && !response.equals("")) {
                            ShortVideosResponse modelResponse = (ShortVideosResponse) Utils.getObject(response, ShortVideosResponse.class);
                            if (modelResponse != null && modelResponse.getMessage() != null && modelResponse.getMessage().equalsIgnoreCase("ok")) {
                                if (modelResponse.getMessage().equalsIgnoreCase("ok")) {
                                    if (modelResponse.getResults() != null) {
                                        if (modelResponse.getResults().getData().size() > 0) {
                                            list = modelResponse.getResults().getData();
                                            viewPagerAdapter = new ShortVideoViewPagerAdapter(mActivity, list);
                                            viewPagerShortVideo.setAdapter(viewPagerAdapter);
                                        }
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

    public void likeVideo(int position,String videoId) {
        if (Utils.isNetworkAvailable(mActivity)) {
            Utils.hideKeyboard(mActivity);
            RequestParams params = new RequestParams();
            try {
                params.put(Constants.Params.USER_ID, Utils.getUserId(mActivity));
                params.put(Constants.Params.VIDEO_ID, videoId);
                Utils.printLog("ProfileDetailParams", params.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Communicator communicator = new Communicator();
            communicator.post(101, mActivity, Constants.Apis.ADD_LIKE_VIDEO, params, new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    Utils.hideProgressBar();
                    list.get(position).setLike(1);
                    viewPagerAdapter.setLikeDislikeImage(position);
                }

                @Override
                public void onFailure(int statusCode, Throwable error) {
                    Utils.hideProgressBar();
                }
            });
        } else Utils.showToastPopup(mActivity, getString(R.string.internet_error));
    }

    public void unlikeVideo(int position,String videoId) {
        if (Utils.isNetworkAvailable(mActivity)) {
            Utils.hideKeyboard(mActivity);
            RequestParams params = new RequestParams();
            try {
                params.put(Constants.Params.USER_ID, Utils.getUserId(mActivity));
                params.put(Constants.Params.VIDEO_ID, videoId);
                Utils.printLog("ProfileDetailParams", params.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Communicator communicator = new Communicator();
            communicator.post(101, mActivity, Constants.Apis.REMOVE_LIKE_VIDEO, params, new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    Utils.hideProgressBar();
                    list.get(position).setLike(0);
                    viewPagerAdapter.setLikeDislikeImage(position);
                }

                @Override
                public void onFailure(int statusCode, Throwable error) {
                    Utils.hideProgressBar();
                }
            });
        } else Utils.showToastPopup(mActivity, getString(R.string.internet_error));
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.cv_back) {
            onBackPressed();
        }
    }
}