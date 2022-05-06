package com.rajasahabacademy.activity.home.fragment;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.loopj.android.http.RequestParams;
import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.home.activity.HomeActivity;
import com.rajasahabacademy.activity.home.adapter.LiveClassAdapter;
import com.rajasahabacademy.activity.home.model.live_class.LiveClassResponse;
import com.rajasahabacademy.activity.home.model.live_class.UserDetails;
import com.rajasahabacademy.api.Communicator;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.api.CustomResponseListener;
import com.rajasahabacademy.support.Utils;

import java.util.ArrayList;
import java.util.List;

import us.zoom.sdk.JoinMeetingOptions;
import us.zoom.sdk.JoinMeetingParams;
import us.zoom.sdk.MeetingService;
import us.zoom.sdk.ZoomSDK;
import us.zoom.sdk.ZoomSDKInitParams;
import us.zoom.sdk.ZoomSDKInitializeListener;

public class LiveClassFragment extends Fragment implements View.OnClickListener {
    @SuppressLint("StaticFieldLeak")
    private static LiveClassFragment fragment;
    private Activity mActivity;
    private View rootView;
    private String filterStatus = "Pending";
    TextView tvStatus;
    UserDetails userDetails = null;
    RecyclerView recyclerView;
    ShimmerFrameLayout liveClassShimmer;

    public static LiveClassFragment newInstance() {
        fragment = new LiveClassFragment();
        Constants.FragmentReference.liveClassFragment = fragment;
        return fragment;
    }

    public static LiveClassFragment getInstance() {
        if (fragment == null)
            return new LiveClassFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_live_class, container, false);
        init();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity) mActivity).showHideBottomNavigation(false);
        ((HomeActivity) mActivity).showHideCart(false);
        ((HomeActivity) mActivity).resetAllBottom("Live Classes");
    }

    private void init() {
        mActivity = getActivity();
        setUpClickListener();
        setUpLiveVideo();
    }
    private void setUpClickListener() {
        RelativeLayout filterLay = rootView.findViewById(R.id.filter_lay);
        filterLay.setOnClickListener(this);
        liveClassShimmer = rootView.findViewById(R.id.live_class_shimmer);
        tvStatus = rootView.findViewById(R.id.tv_class_status);
        recyclerView = rootView.findViewById(R.id.recycler_live_video);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false));
    }


    private void setUpLiveVideo() {
        tvStatus.setText(filterStatus + " Classes");
        liveClassShimmer.startShimmer();
        if (Utils.isNetworkAvailable(mActivity)) {
            Utils.hideKeyboard(mActivity);
            RequestParams params = new RequestParams();
            try {
                params.put(Constants.Params.USER_ID, Utils.getUserId(mActivity));
                Utils.printLog("ProfileDetailParams", params.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Communicator communicator = new Communicator();
            communicator.post(101, mActivity, Constants.Apis.GET_LIVE, params, new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    try {
                        liveClassShimmer.setVisibility(View.GONE);
                        if (response != null && !response.equals("")) {
                            LiveClassResponse modelResponse = (LiveClassResponse) Utils.getObject(response, LiveClassResponse.class);
                            if (modelResponse != null && modelResponse.getCode() == 200) {
                                if (modelResponse.getUserDetails() != null) {
                                    userDetails = modelResponse.getUserDetails();
                                    if (userDetails.getUpcoming().size() > 0) {
                                        recyclerView.setVisibility(View.VISIBLE);
                                        rootView.findViewById(R.id.tv_no_data).setVisibility(View.GONE);
                                        List<Object> list = new ArrayList<>(userDetails.getUpcoming());
                                        recyclerView.setAdapter(new LiveClassAdapter(mActivity, list, LiveClassFragment.this));
                                    } else {
                                        recyclerView.setVisibility(View.GONE);
                                        rootView.findViewById(R.id.tv_no_data).setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    recyclerView.setVisibility(View.GONE);
                                    rootView.findViewById(R.id.tv_no_data).setVisibility(View.VISIBLE);
                                }
                            } else {
                                recyclerView.setVisibility(View.GONE);
                                rootView.findViewById(R.id.tv_no_data).setVisibility(View.VISIBLE);
                            }
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            rootView.findViewById(R.id.tv_no_data).setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        recyclerView.setVisibility(View.GONE);
                        rootView.findViewById(R.id.tv_no_data).setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(int statusCode, Throwable error) {
                    recyclerView.setVisibility(View.GONE);
                    rootView.findViewById(R.id.tv_no_data).setVisibility(View.VISIBLE);
                }
            });
        } else Utils.showToastPopup(mActivity, getString(R.string.internet_error));
    }


    public void zoomInit(String zoomApiKey, String zoomScretKey, String meetingId, String meetingPassword) {
        if (zoomApiKey.equals("") || zoomScretKey.equals("")) {
            Toast.makeText(mActivity, "Invalid zoom api key and secret key", Toast.LENGTH_SHORT).show();
            return;
        }
        ZoomSDK sdk = ZoomSDK.getInstance();
        ZoomSDKInitParams params = new ZoomSDKInitParams();
        params.appKey = zoomApiKey;
        params.appSecret = zoomScretKey;
        params.domain = "zoom.us";
        params.enableLog = true;
        ZoomSDKInitializeListener listener = new ZoomSDKInitializeListener() {
            @Override
            public void onZoomSDKInitializeResult(int errorCode, int internalErrorCode) {
                Log.e("errorCode", String.valueOf(errorCode));
                zoomMeeting(meetingId, meetingPassword);
            }

            @Override
            public void onZoomAuthIdentityExpired() {
                Log.e("errorCode", "errorCode");
            }

        };

        try {
            sdk.initialize(mActivity, listener, params);
        } catch (Exception e) {
            e.printStackTrace();
            sdk.initialize(mActivity, listener, params);
        }
    }

    public void zoomMeeting(String meetingId, String meetingPassword) {
        Toast.makeText(mActivity, "Please wait..", Toast.LENGTH_SHORT).show();
        if (!meetingId.equals("") && !meetingPassword.equals("")) {
            joinMeeting(meetingId, meetingPassword, Utils.getSaveLoginUser(mActivity).getResults().getName());
        } else Toast.makeText(mActivity, "Meeting id is blank", Toast.LENGTH_SHORT).show();
    }

    private void joinMeeting(String meetingId, String meetingPassword, String name) {
        try {
            MeetingService meetingService = ZoomSDK.getInstance().getMeetingService();
            JoinMeetingOptions options = new JoinMeetingOptions();
            JoinMeetingParams params = new JoinMeetingParams();
            params.displayName = name;
            params.meetingNo = meetingId;
            params.password = meetingPassword;
            if (meetingService != null)
                meetingService.joinMeetingWithParams(mActivity, params, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void filterPopup() {
        try {
            Dialog dialogPopup = new Dialog(mActivity);
            dialogPopup.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogPopup.setContentView(R.layout.dialog_live_class_filter);
            dialogPopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogPopup.setCanceledOnTouchOutside(false);
            RelativeLayout pendingLay = dialogPopup.findViewById(R.id.pending_lay);
            RelativeLayout onGoingLay = dialogPopup.findViewById(R.id.ongoing_lay);
            RelativeLayout completedLay = dialogPopup.findViewById(R.id.completed_lay);

            pendingLay.setBackgroundResource(R.drawable.filter_live_class_bg_5);
            ((TextView) dialogPopup.findViewById(R.id.tv_pending)).setTextColor(mActivity.getResources().getColor(R.color.primary_text_color));
            onGoingLay.setBackgroundResource(R.drawable.filter_live_class_bg_5);
            ((TextView) dialogPopup.findViewById(R.id.tv_ongoing)).setTextColor(mActivity.getResources().getColor(R.color.primary_text_color));
            completedLay.setBackgroundResource(R.drawable.filter_live_class_bg_5);
            ((TextView) dialogPopup.findViewById(R.id.tv_completed)).setTextColor(mActivity.getResources().getColor(R.color.primary_text_color));
            switch (filterStatus) {
                case "Pending":
                    pendingLay.setBackgroundResource(R.drawable.bg_button_5);
                    ((TextView) dialogPopup.findViewById(R.id.tv_pending)).setTextColor(mActivity.getResources().getColor(R.color.white));
                    break;
                case "Ongoing":
                    onGoingLay.setBackgroundResource(R.drawable.bg_button_5);
                    ((TextView) dialogPopup.findViewById(R.id.tv_ongoing)).setTextColor(mActivity.getResources().getColor(R.color.white));
                    break;
                case "Completed":
                    completedLay.setBackgroundResource(R.drawable.bg_button_5);
                    ((TextView) dialogPopup.findViewById(R.id.tv_completed)).setTextColor(mActivity.getResources().getColor(R.color.white));
                    break;
            }

            pendingLay.setOnClickListener(view -> {
                filterStatus = "Pending";
                dialogPopup.dismiss();
                init();
            });
            onGoingLay.setOnClickListener(view -> {
                filterStatus = "Ongoing";
                dialogPopup.dismiss();
                init();
            });
            completedLay.setOnClickListener(view -> {
                filterStatus = "Completed";
                dialogPopup.dismiss();
                init();
            });

            ImageView ivClose = dialogPopup.findViewById(R.id.iv_close);
            ivClose.setOnClickListener(v -> {
                dialogPopup.dismiss();
            });

            dialogPopup.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.filter_lay)
            filterPopup();
    }
}