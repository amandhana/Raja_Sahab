package com.rajasahabacademy.activity.video.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.loopj.android.http.RequestParams;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.video.adapter.VideoChatAdapter;
import com.rajasahabacademy.api.Communicator;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.api.CustomResponseListener;
import com.rajasahabacademy.exo_player.TrackSelectionDialog;
import com.rajasahabacademy.activity.video.model.Result;
import com.rajasahabacademy.activity.video.model.VideoChatResponse;
import com.rajasahabacademy.support.Preference;
import com.rajasahabacademy.support.Utils;
import com.rajasahabacademy.support.ViewUtils;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class VideoActivity extends AppCompatActivity implements View.OnClickListener {

    private Activity mActivity;
    private String videoId = "";
    private String videoPath = "";
    FrameLayout frameLayout;
    private boolean isShowingTrackSelectionDialog;
    private DefaultTrackSelector trackSelector;
    String[] speed = {"0.25x", "0.5x", "Normal", "1.5x", "2x"};
    PlayerView playerView;
    private TextView tvDescription;
    SimpleExoPlayer simpleExoPlayer;
    List<Result> list = new ArrayList<>();
    VideoChatAdapter videoChatAdapter;
    RecyclerView recyclerView;
    String fromWhere = "";
    String offlineVideoUrl = "";

    TextView tvVideo;
    ImageView ivDownload;
    RelativeLayout deleteLay;
    ProgressBar progressBar;
    ScrollView scrollView;
    LinearLayout mainLayout;
    Preference preference;
    long downloadId;
    DownloadManager manager;
    int dl_progress;
    String fileName;
    TextView tvPercent;

    ImageView ivBookmark;
    String bookmarkStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.darkThemeForceStop();
        Utils.hideNavigationButton(this);
        setContentView(R.layout.activity_video);
        init();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        releasePlayer();
        Utils.hideKeyboard(mActivity);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void init() {
        mActivity = this;
        preference = Preference.getInstance(mActivity);
        clickListener();
        getBundleData();
        setUpExoPlayer();
        if (!fromWhere.equals("Bookmark")) {
            if (!fromWhere.equals("Offline")) {
                ivDownload.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                deleteLay.setVisibility(View.GONE);
                tvVideo.setVisibility(View.GONE);
                ivBookmark.setVisibility(View.VISIBLE);
                getFileName();
                checkDownloadStatus();
                setUpList();
                checkOrientation();
                getDownloadPercentStatus();
            }
            downloadCompleteNotify();
        }else findViewById(R.id.end_lay).setVisibility(View.GONE);
    }

    private void clickListener() {
        frameLayout = findViewById(R.id.frame_layout);
        tvDescription = findViewById(R.id.tv_description);
        CardView cvBack = findViewById(R.id.cv_back);
        cvBack.setOnClickListener(this);

        scrollView = findViewById(R.id.scroll_view);
        mainLayout = findViewById(R.id.main_layout);
        progressBar = findViewById(R.id.progress_bar);
        ivDownload = findViewById(R.id.iv_download);
        tvVideo = findViewById(R.id.tv_video);
        ivDownload.setOnClickListener(this);
        deleteLay = findViewById(R.id.delete_lay);
        deleteLay.setOnClickListener(this);

        RelativeLayout sendLay = findViewById(R.id.send_lay);
        sendLay.setOnClickListener(this);
        RelativeLayout descriptionLay = findViewById(R.id.description_lay);
        descriptionLay.setOnClickListener(this);

        ivBookmark = findViewById(R.id.iv_bookmark);
        ivBookmark.setOnClickListener(this);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false));
        recyclerView.addOnLayoutChangeListener((view, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (bottom < oldBottom) {
                recyclerView.scrollBy(0, oldBottom - bottom);
            }
        });
    }

    private void getBundleData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            fromWhere = bundle.getString(Constants.Course.FROM_WHERE);
            if (fromWhere.equals("Offline"))
                offlineVideoUrl = bundle.getString(Constants.Course.OFFLINE_FILE);
            else {
                videoId = bundle.getString(Constants.Course.VIDEO_ID);
                videoPath = bundle.getString(Constants.Course.VIDEO_PATH);
                String videoDescription = bundle.getString(Constants.Course.VIDEO_DESCRIPTION);
                bookmarkStr = bundle.getString(Constants.Course.VIDEO_BOOKMARK);
                if (videoDescription.isEmpty())
                    findViewById(R.id.cv_title_description).setVisibility(View.GONE);
                else {
                    findViewById(R.id.cv_title_description).setVisibility(View.VISIBLE);
                    Utils.setHtmlText(videoDescription, tvDescription);
                }
                if (bookmarkStr.equals("1"))
                    ivBookmark.setBackgroundResource(R.drawable.ic_bookmark_fill);
                else ivBookmark.setBackgroundResource(R.drawable.ic_bookmark_empty);
            }
        }
    }

    private void setUpExoPlayer() {
        trackSelector = new DefaultTrackSelector(this);
        simpleExoPlayer = new SimpleExoPlayer.Builder(this).setTrackSelector(trackSelector).build();
        playerView = findViewById(R.id.exoPlayerView);
        playerView.setPlayer(simpleExoPlayer);
        MediaItem mediaItem;
        if (!fromWhere.equals("Offline"))
            mediaItem = MediaItem.fromUri(videoPath);
        else {
            mediaItem = MediaItem.fromUri(offlineVideoUrl);
            findViewById(R.id.cv_title_description).setVisibility(View.GONE);
            findViewById(R.id.recycler_view).setVisibility(View.GONE);
            findViewById(R.id.cv_send_message).setVisibility(View.GONE);
        }
        simpleExoPlayer.addMediaItem(mediaItem);
        simpleExoPlayer.prepare();
        simpleExoPlayer.play();

        ImageView farwordBtn = playerView.findViewById(R.id.fwd);
        ImageView rewBtn = playerView.findViewById(R.id.rew);
        ImageView setting = playerView.findViewById(R.id.exo_track_selection_view);
        ImageView speedBtn = playerView.findViewById(R.id.exo_playback_speed);
        TextView speedTxt = playerView.findViewById(R.id.speed);

        speedBtn.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            builder.setTitle("Set Speed");
            builder.setItems(speed, (dialog, which) -> {
                if (which == 0) {
                    speedTxt.setVisibility(View.VISIBLE);
                    speedTxt.setText("0.25X");
                    PlaybackParameters param = new PlaybackParameters(0.5f);
                    simpleExoPlayer.setPlaybackParameters(param);
                }
                if (which == 1) {
                    speedTxt.setVisibility(View.VISIBLE);
                    speedTxt.setText("0.5X");
                    PlaybackParameters param = new PlaybackParameters(0.5f);
                    simpleExoPlayer.setPlaybackParameters(param);
                }
                if (which == 2) {
                    speedTxt.setVisibility(View.GONE);
                    PlaybackParameters param = new PlaybackParameters(1f);
                    simpleExoPlayer.setPlaybackParameters(param);
                }
                if (which == 3) {
                    speedTxt.setVisibility(View.VISIBLE);
                    speedTxt.setText("1.5X");
                    PlaybackParameters param = new PlaybackParameters(1.5f);
                    simpleExoPlayer.setPlaybackParameters(param);
                }
                if (which == 4) {
                    speedTxt.setVisibility(View.VISIBLE);
                    speedTxt.setText("2X");
                    PlaybackParameters param = new PlaybackParameters(2f);
                    simpleExoPlayer.setPlaybackParameters(param);
                }
            });
            builder.show();
        });
        farwordBtn.setOnClickListener(v -> simpleExoPlayer.seekTo(simpleExoPlayer.getCurrentPosition() + 10000));
        rewBtn.setOnClickListener(v -> {
            long num = simpleExoPlayer.getCurrentPosition() - 10000;
            if (num < 0)
                simpleExoPlayer.seekTo(0);
            else simpleExoPlayer.seekTo(simpleExoPlayer.getCurrentPosition() - 10000);
        });

        ImageView fullscreenButton = playerView.findViewById(R.id.fullscreen);
        fullscreenButton.setOnClickListener(view -> {
            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_PORTRAIT)
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            else
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        });
        findViewById(R.id.exo_play).setOnClickListener(v -> simpleExoPlayer.play());
        findViewById(R.id.exo_pause).setOnClickListener(v -> simpleExoPlayer.pause());
        simpleExoPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int state) {
            }
        });
        playerView.setControllerVisibilityListener(visibility -> {
        });

        setting.setOnClickListener(v -> {
            if (!isShowingTrackSelectionDialog
                    && TrackSelectionDialog.willHaveContent(trackSelector)) {
                isShowingTrackSelectionDialog = true;
                TrackSelectionDialog trackSelectionDialog =
                        TrackSelectionDialog.createForTrackSelector(
                                trackSelector,
                                dismissedDialog -> isShowingTrackSelectionDialog = false);
                trackSelectionDialog.show(getSupportFragmentManager(), null);
            }
        });

    }

    private void getFileName() {
        if (videoPath.contains("/")) {
            int lastIndex = videoPath.lastIndexOf("/");
            fileName = videoPath.substring(++lastIndex);
        }
    }

    private void checkDownloadStatus() {
        if (!fileName.equals("")) {
            File cDir = getApplication().getExternalFilesDir(null);
            File saveFilePath = new File(cDir.getPath() + "/" + fileName);
            if (!saveFilePath.exists()) {
                ivDownload.setVisibility(View.VISIBLE);
                deleteLay.setVisibility(View.GONE);
            } else {
                ivDownload.setVisibility(View.GONE);
                deleteLay.setVisibility(View.VISIBLE);
            }
        }
    }

    private void downloadCompleteNotify(){
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (downloadId == reference) {
                    progressBar.setVisibility(View.GONE);
                    deleteLay.setVisibility(View.VISIBLE);
                }
            }
        };
        registerReceiver(receiver, filter);
    }


    private void checkOrientation() {
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setVisibility(View.VISIBLE);
            findViewById(R.id.cv_send_message).setVisibility(View.VISIBLE);
            findViewById(R.id.header_lay).setVisibility(View.VISIBLE);
            findViewById(R.id.cv_title_description).setVisibility(View.VISIBLE);
        } else {
            FrameLayout frameLayout = findViewById(R.id.frame_layout);
            recyclerView.setVisibility(View.GONE);
            findViewById(R.id.cv_send_message).setVisibility(View.GONE);
            findViewById(R.id.header_lay).setVisibility(View.GONE);
            findViewById(R.id.cv_title_description).setVisibility(View.GONE);
            try {
                playerView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                frameLayout.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void releasePlayer() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.release();
            simpleExoPlayer = null;
            trackSelector = null;
        }
    }

    private void setUpList() {
        if (Utils.isNetworkAvailable(mActivity)) {
            Utils.hideKeyboard(mActivity);
            RequestParams params = new RequestParams();
            try {
                params.put(Constants.Params.USER_ID, Utils.getUserId(mActivity));
                params.put(Constants.Params.DEVICE_ID, Utils.getDeviceId(mActivity));
                params.put(Constants.Params.VIDEO_ID, videoId);
                Utils.printLog("ProfileDetailParams", params.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Communicator communicator = new Communicator();
            communicator.post(101, mActivity, Constants.Apis.GET_VIDEO_ALL_CHAT, params, new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    Utils.hideProgressBar();
                    try {
                        if (response != null && !response.equals("")) {
                            VideoChatResponse modelResponse = (VideoChatResponse) Utils.getObject(response, VideoChatResponse.class);
                            if (modelResponse != null && modelResponse.getMessage() != null && modelResponse.getMessage().equalsIgnoreCase("ok")) {
                                if (modelResponse.getResults() != null && modelResponse.getResults().size() > 0) {
                                    list.addAll(modelResponse.getResults());
                                    videoChatAdapter = new VideoChatAdapter(mActivity, list);
                                    recyclerView.setAdapter(videoChatAdapter);
                                    recyclerView.smoothScrollToPosition(list.size());
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

    private void sendMessage() {
        Utils.hideKeyboard(mActivity);
        EditText etMessage = findViewById(R.id.et_message);
        String messageStr = etMessage.getText().toString().trim();
        if (!messageStr.isEmpty()) {
            if (Utils.isNetworkAvailable(mActivity)) {
                Utils.showProgressBar(mActivity);
                Utils.hideKeyboard(mActivity);
                RequestParams params = new RequestParams();
                try {
                    params.put(Constants.Params.USER_ID, Utils.getUserId(mActivity));
                    params.put(Constants.Params.MESSAGE, messageStr);
                    params.put(Constants.Params.DEVICE_ID, Utils.getDeviceId(mActivity));
                    params.put(Constants.Params.VIDEO_ID, videoId);
                    Utils.printLog("ProfileDetailParams", params.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Communicator communicator = new Communicator();
                communicator.post(101, mActivity, Constants.Apis.SEND_VIDEO_MESSAGE, params, new CustomResponseListener() {
                    @Override
                    public void onResponse(int requestCode, String response) {
                        Utils.hideProgressBar();
                        try {
                            if (response != null && !response.equals("")) {
                                JSONObject object = new JSONObject(response);
                                if (object.getString("message").equalsIgnoreCase("ok")) {
                                    Result model = new Result();
                                    model.setMessage(messageStr);
                                    model.setCreatedAt(Utils.getCurrentDate());
                                    model.setFrom(Utils.getUserId(mActivity));
                                    model.setName(Utils.getSaveLoginUser(mActivity).getResults().getName());
                                    list.add(model);
                                    if (videoChatAdapter != null) {
                                        if (list.size() > 0)
                                            videoChatAdapter.notifyItemInserted(list.size() - 1);
                                        else videoChatAdapter.updateList(list);
                                    } else {
                                        videoChatAdapter = new VideoChatAdapter(mActivity, list);
                                        recyclerView.setAdapter(videoChatAdapter);
                                    }
                                    etMessage.setText("");
                                    recyclerView.smoothScrollToPosition(list.size());
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
    }

    private void downloadPermission() {
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        Permissions.check(mActivity, permissions, null, null, new PermissionHandler() {
            @Override
            public void onGranted() {
                if (Utils.isNetworkAvailable(mActivity)) {
                    try {
                        String filename = "";
                        if (videoPath.contains("/")) {
                            int lastIndex = videoPath.lastIndexOf("/");
                            filename = videoPath.substring(++lastIndex);
                        }
                        downloadWithPercent(videoPath, filename);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else Toast.makeText(mActivity, "Internet required", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                super.onDenied(context, deniedPermissions);
                Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void downloadWithPercent(String url, String fileName) {
        preference.putString("fileName", fileName);
        ivDownload.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        File myDir = new File(getExternalCacheDir(), "/");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        File cDir = getApplication().getExternalFilesDir(null);
        File saveFilePath = new File(cDir.getPath() + "/" + fileName);
        if (!saveFilePath.exists()) {
            Uri Download_Uri = Uri.parse(url);
            DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
            request.setDescription("Downloading File");
            request.setTitle("Downloading");
            request.setDestinationUri(Uri.fromFile(saveFilePath));

            manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

            downloadId = manager.enqueue(request);

            tvPercent = findViewById(R.id.tv_percent);

            new Thread(() -> {
                boolean downloading = true;
                while (downloading) {
                    try {
                        DownloadManager.Query q = new DownloadManager.Query();
                        q.setFilterById(downloadId);
                        Cursor cursor = manager.query(q);
                        cursor.moveToFirst();
                        int bytes_downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                        int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                        if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                            downloading = false;
                        }
                        dl_progress = (int) ((bytes_downloaded * 100L) / bytes_total);
                        cursor.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }).start();
        }
    }

    private void getDownloadPercentStatus() {
        getContentResolver().registerContentObserver(Uri.parse("content://downloads/my_downloads"),
                true, new ContentObserver(null) {
                    @Override
                    public void onChange(boolean selfChange, Uri uri) {
                        super.onChange(selfChange, uri);
                        if (uri.toString().matches(".*\\d+$")) {
                            long changedId = Long.parseLong(uri.getLastPathSegment());
                            if (changedId == downloadId) {
                                Log.d(TAG, "onChange: " + uri.toString() + " " + changedId + " " + downloadId);
                                try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                                    if (cursor != null && cursor.moveToFirst()) {
                                        Log.e("status", "onChange: running" + dl_progress);
                                        ivDownload.setVisibility(View.GONE);
                                        progressBar.setVisibility(View.VISIBLE);
                                        tvPercent.setText(String.valueOf(dl_progress));
                                        deleteLay.setVisibility(View.GONE);
                                    } else {
                                        Log.e("status", "onChange: cancel" + dl_progress);
                                    }
                                }
                            }
                        }
                    }
                });
    }

    public void deletePopup() {
        try {
            final Dialog dialog = new Dialog(mActivity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.toast_popup_exit);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            dialog.setCanceledOnTouchOutside(false);

            TextView tvMessage = dialog.findViewById(R.id.tv_message);
            tvMessage.setText(getString(R.string.delete_video_message));
            CardView cvCancel = dialog.findViewById(R.id.cv_cancel);
            CardView cvOk = dialog.findViewById(R.id.cv_ok);

            cvCancel.setOnClickListener(v -> dialog.dismiss());
            cvOk.setOnClickListener(v -> {
                File cDir = getApplication().getExternalFilesDir(null);
                File saveFilePath = new File(cDir.getPath() + "/" + fileName);
                if (saveFilePath.exists()) {
                    saveFilePath.delete();
                    try {
                        manager.remove(downloadId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                deleteLay.setVisibility(View.GONE);
                ivDownload.setVisibility(View.VISIBLE);
                ivDownload.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));
                dialog.dismiss();
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addBookmark() {
        if (Utils.isNetworkAvailable(mActivity)) {
            Utils.showProgressBar(mActivity);
            Utils.hideKeyboard(mActivity);
            RequestParams params = new RequestParams();
            try {
                params.put(Constants.Params.VIDEO_ID, videoId);
                params.put(Constants.Params.USER_ID, Utils.getUserId(mActivity));
                Utils.printLog("ProfileDetailParams", params.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Communicator communicator = new Communicator();
            communicator.post(101, mActivity, Constants.Apis.ADD_BOOKMARK_VIDEO, params, new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    Utils.hideProgressBar();
                    try {
                        if (response != null && !response.equals("")) {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optBoolean("success"))
                                ivBookmark.setBackgroundResource(R.drawable.ic_bookmark_fill);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Throwable error) {
                    Utils.hideProgressBar();
                    Utils.showToastPopup(mActivity, getString(R.string.quiz_list_failure));
                }
            });
        } else Utils.showToastPopup(mActivity, getString(R.string.internet_error));
    }

    private void removeBookmark() {
        if (Utils.isNetworkAvailable(mActivity)) {
            Utils.showProgressBar(mActivity);
            Utils.hideKeyboard(mActivity);
            RequestParams params = new RequestParams();
            try {
                params.put(Constants.Params.VIDEO_ID, videoId);
                params.put(Constants.Params.USER_ID, Utils.getUserId(mActivity));
                Utils.printLog("ProfileDetailParams", params.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Communicator communicator = new Communicator();
            communicator.post(101, mActivity, Constants.Apis.REMOVE_BOOKMARK_VIDEO, params, new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    Utils.hideProgressBar();
                    try {
                        if (response != null && !response.equals("")) {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optBoolean("success"))
                                ivBookmark.setBackgroundResource(R.drawable.ic_bookmark_empty);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Throwable error) {
                    Utils.hideProgressBar();
                    Utils.showToastPopup(mActivity, getString(R.string.quiz_list_failure));
                }
            });
        } else Utils.showToastPopup(mActivity, getString(R.string.internet_error));
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.cv_back) {
            onBackPressed();
        } else if (id == R.id.send_lay) {
            sendMessage();
        } else if (id == R.id.description_lay) {
            if (tvDescription.getVisibility() == View.VISIBLE)
                ViewUtils.collapse(tvDescription);
            else ViewUtils.expand(tvDescription);
        } else if (id == R.id.iv_download)
            downloadPermission();
        else if (id == R.id.delete_lay) {
            deletePopup();
        }else if (id == R.id.iv_bookmark) {
            if (!bookmarkStr.equals("1"))
                addBookmark();
            else removeBookmark();
        }
    }
}