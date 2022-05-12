package com.rajasahabacademy.activity.youtube;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.loopj.android.http.RequestParams;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.video.adapter.VideoChatAdapter;
import com.rajasahabacademy.api.Communicator;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.api.CustomResponseListener;
import com.rajasahabacademy.activity.video.model.Result;
import com.rajasahabacademy.activity.video.model.VideoChatResponse;
import com.rajasahabacademy.support.Preference;
import com.rajasahabacademy.support.Utils;
import com.rajasahabacademy.support.ViewUtils;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

import static android.content.ContentValues.TAG;

public class YoutubeVideoPlayActivity extends YouTubeBaseActivity implements View.OnClickListener,
        YouTubePlayer.OnInitializedListener {

    private Activity mActivity;
    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer mPlayer;
    private SeekBar mSeekBar;
    private Handler mHandler = null;
    private TextView mPlayTimeTextView;
    private TextView tvDescription;
    private View mPlayButtonLayout;
    private ImageButton play_video, pause_video;
    private String videTitle = "";
    private String videoId = "";
    private String videoPath = "";
    private String videoDescription = "";
    List<Result> list = new ArrayList<>();
    VideoChatAdapter videoChatAdapter;
    RecyclerView recyclerView;

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
    private TextView tvTitle;

    ImageView ivBookmark;
    String bookmarkStr = "";
    String fromWhereStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.darkThemeForceStop();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_youtube_video_play);
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
        clickListener();
        getBundleData();
        setUpYoutubePlayerView();
        setUpList();
        getFileName();
        getDownloadPercentStatus();
        downloadCompleteNotify();
    }

    private void clickListener() {
        tvTitle = findViewById(R.id.tv_title_video);
        tvTitle.setSelected(true);
        tvDescription = findViewById(R.id.tv_description);
        mPlayTimeTextView = findViewById(R.id.play_time);
        mSeekBar = findViewById(R.id.video_seekbar);
        youTubePlayerView = findViewById(R.id.youtube_view);
        mPlayButtonLayout = findViewById(R.id.video_control);
        play_video = findViewById(R.id.play_video);
        pause_video = findViewById(R.id.pause_video);
        CardView cvBack = findViewById(R.id.cv_back);


        scrollView = findViewById(R.id.scroll_view);
        mainLayout = findViewById(R.id.main_layout);
        progressBar = findViewById(R.id.progress_bar);
        ivDownload = findViewById(R.id.iv_download);
        ivDownload.setOnClickListener(this);
        deleteLay = findViewById(R.id.delete_lay);
        deleteLay.setOnClickListener(this);

        RelativeLayout sendLay = findViewById(R.id.send_lay);
        sendLay.setOnClickListener(this);
        TextView marqText = findViewById(R.id.tv_title_video);
        marqText.setSelected(true);
        marqText.setText("This code worked for me - although I had to make sure the android");
        RelativeLayout descriptionLay = findViewById(R.id.description_lay);
        descriptionLay.setOnClickListener(this);

        ivBookmark = findViewById(R.id.iv_bookmark);
        ivBookmark.setOnClickListener(this);

        cvBack.setOnClickListener(this);
        play_video.setOnClickListener(this);
        pause_video.setOnClickListener(this);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false));
        recyclerView.addOnLayoutChangeListener((view, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (bottom < oldBottom) {
                recyclerView.scrollBy(0, oldBottom - bottom);
            }
        });
    }

    private void getBundleData() {
        try {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                if (bundle.getString(Constants.Course.FROM_WHERE) != null)
                    fromWhereStr = bundle.getString(Constants.Course.FROM_WHERE);
                if (bundle.getString(Constants.Course.VIDEO_ID) != null)
                    videoId = bundle.getString(Constants.Course.VIDEO_ID);
                if (bundle.getString(Constants.Course.VIDEO_PATH) != null)
                    videoPath = bundle.getString(Constants.Course.VIDEO_PATH);
                if (bundle.getString(Constants.Course.VIDEO_DESCRIPTION) != null)
                    videoDescription = bundle.getString(Constants.Course.VIDEO_DESCRIPTION);
                if (bundle.getString(Constants.Course.VIDEO_BOOKMARK) != null)
                    bookmarkStr = bundle.getString(Constants.Course.VIDEO_BOOKMARK);
                if (bundle.getString(Constants.Course.VIDEO_TITLE) != null)
                    videTitle = bundle.getString(Constants.Course.VIDEO_TITLE);
            }
            if (videoDescription.isEmpty())
                findViewById(R.id.cv_title_description).setVisibility(View.GONE);
            else {
                findViewById(R.id.cv_title_description).setVisibility(View.VISIBLE);
                Utils.setHtmlText(videoDescription, tvDescription);
            }

            if (videTitle.isEmpty())
                tvTitle.setVisibility(View.GONE);
            else {
                tvTitle.setVisibility(View.VISIBLE);
                Utils.setHtmlText(videTitle, tvTitle);
            }

            if (!fromWhereStr.equals("Bookmark")) {
                if (bookmarkStr.equals("1"))
                    ivBookmark.setBackgroundResource(R.drawable.ic_bookmark_fill);
                else ivBookmark.setBackgroundResource(R.drawable.ic_bookmark_empty);
            } else findViewById(R.id.end_lay).setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUpYoutubePlayerView() {
        youTubePlayerView.initialize(getString(R.string.youtube_api_key), this);
        mSeekBar.setOnSeekBarChangeListener(mVideoSeekBarChangeListener);
        mHandler = new Handler(Looper.getMainLooper());
    }

    private void downloadCompleteNotify() {
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

    private void getFileName() {
        final ProgressDialog progressDialog = new ProgressDialog(mActivity);
        progressDialog.setTitle("Please wait");
        progressDialog.show();
        new YouTubeExtractor(this) {
            @Override
            public void onExtractionComplete(SparseArray<YtFile> ytFiles, final VideoMeta vMeta) {
                progressDialog.cancel();
                if (ytFiles == null) {
                    finish();
                    return;
                }
                for (int i = 0, itag; i < ytFiles.size(); i++) {
                    itag = ytFiles.keyAt(i);
                    final YtFile ytFile = ytFiles.get(itag);
                    if (ytFile.getFormat().getHeight() == -1 || ytFile.getFormat().getHeight() >= 360) {
                        if (vMeta.getTitle().length() > 55) {
                            fileName = vMeta.getTitle().substring(0, 55) + "." + ytFile.getFormat().getExt();
                        } else {
                            fileName = vMeta.getTitle() + "." + ytFile.getFormat().getExt();
                        }
                        fileName = fileName.replaceAll("[\\\\><\"|*?%:#/]", "");
                    }
                    if (!fileName.equals(""))
                        break;
                }
                checkDownloadStatus();
            }
        }.extract(getValidVideoId());
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

    SeekBar.OnSeekBarChangeListener mVideoSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            long lengthPlayed = (mPlayer.getDurationMillis() * progress) / 100;
            mPlayer.seekToMillis((int) lengthPlayed);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            Log.e("youtube_player", "start");
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            Log.e("youtube_player", "stop");
        }
    };

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            if (null == player) return;
            mPlayer = player;
            displayCurrentTime();
            player.cueVideo(getValidVideoId());

            player.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
            mPlayButtonLayout.setVisibility(View.VISIBLE);

            player.setPlayerStateChangeListener(mPlayerStateChangeListener);
            player.setPlaybackEventListener(mPlaybackEventListener);


        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, 1).show();
        } else {
            String error = String.format("%s %s", "Could not initialize video player", errorReason.toString());
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        }
    }

    YouTubePlayer.PlayerStateChangeListener mPlayerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {
        @Override
        public void onAdStarted() {
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason arg0) {
        }

        @Override
        public void onLoaded(String arg0) {
        }

        @Override
        public void onLoading() {
        }

        @Override
        public void onVideoEnded() {
            Log.e("Status", "Video End");
            mPlayer.seekToMillis(0);
            mPlayer.pause();
            mSeekBar.setProgress(0);
        }

        @Override
        public void onVideoStarted() {
            displayCurrentTime();
        }
    };

    YouTubePlayer.PlaybackEventListener mPlaybackEventListener = new YouTubePlayer.PlaybackEventListener() {
        @Override
        public void onBuffering(boolean arg0) {
        }

        @Override
        public void onPaused() {
            play_video.setVisibility(View.VISIBLE);
            pause_video.setVisibility(View.GONE);
            mHandler.removeCallbacks(runnable);
        }

        @Override
        public void onPlaying() {
            play_video.setVisibility(View.GONE);
            pause_video.setVisibility(View.VISIBLE);

            mHandler.postDelayed(runnable, 100);
            displayCurrentTime();
        }

        @Override
        public void onSeekTo(int arg0) {
            play_video.setVisibility(View.VISIBLE);
            pause_video.setVisibility(View.GONE);
            mHandler.postDelayed(runnable, 100);
        }

        @Override
        public void onStopped() {
            play_video.setVisibility(View.VISIBLE);
            pause_video.setVisibility(View.GONE);
            mHandler.removeCallbacks(runnable);
        }
    };

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            displayCurrentTime();
            mHandler.postDelayed(this, 100);
        }
    };

    private void displayCurrentTime() {
        if (null == mPlayer) return;
        String formattedTime = formatTime(mPlayer.getDurationMillis() - mPlayer.getCurrentTimeMillis());
        mPlayTimeTextView.setText(formattedTime);
    }

    @SuppressLint("DefaultLocale")
    private String formatTime(int millis) {
        int seconds = millis / 1000;
        int minutes = seconds / 60;
        int hours = minutes / 60;

        return (hours == 0 ? "--:" : hours + ":") + String.format("%02d:%02d", minutes % 60, seconds % 60);
    }

    private String getValidVideoId() {
        if (videoPath.contains(getString(R.string.youtube_video_format))) {
            int index = videoPath.indexOf("=");
            index = index + 1;
            return videoPath.substring(index);
        } else return videoPath;
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
                        getDownloadYoutubeVideoLink();
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

    private void getDownloadYoutubeVideoLink() {
        progressBar.setVisibility(View.VISIBLE);
        new YouTubeExtractor(this) {
            @Override
            public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta) {
                progressBar.setVisibility(View.GONE);
                if (ytFiles == null) {
                    finish();
                    return;
                }
                for (int i = 0, itag; i < ytFiles.size(); i++) {
                    itag = ytFiles.keyAt(i);
                    YtFile ytFile = ytFiles.get(itag);
                    if (ytFile.getFormat().getHeight() == -1 || ytFile.getFormat().getHeight() >= 360) {
                        addButtonToMainLayout(vMeta.getTitle(), ytFile);
                    }
                }
                scrollView.setVisibility(View.VISIBLE);
            }
        }.extract(getValidVideoId());
    }

    private void addButtonToMainLayout(final String videoTitle, final YtFile ytfile) {
        String btnText = (ytfile.getFormat().getHeight() == -1) ? "Audio " +
                ytfile.getFormat().getAudioBitrate() + " kbit/s" :
                ytfile.getFormat().getHeight() + "p";
        btnText += (ytfile.getFormat().isDashContainer()) ? " dash" : "";
        Button btn = new Button(this);
        btn.setText(btnText);
        btn.setOnClickListener(v -> {
            String filename;
            if (videoTitle.length() > 55) {
                filename = videoTitle.substring(0, 55) + "." + ytfile.getFormat().getExt();
            } else {
                filename = videoTitle + "." + ytfile.getFormat().getExt();
            }
            filename = filename.replaceAll("[\\\\><\"|*?%:#/]", "");
            scrollView.setVisibility(View.GONE);
            downloadWithPercent(ytfile.getUrl(), videoTitle, filename);
        });
        mainLayout.addView(btn);
    }

    private void downloadWithPercent(String url, String videoTitle, String fileName) {
        preference.putString("fileName", fileName);
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
            final CircularProgressBar progressBar = findViewById(R.id.progress_circular);

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
                        runOnUiThread(() -> {
                            tvPercent.setText(String.valueOf(dl_progress));
                            progressBar.setProgress((float) dl_progress);
                        });
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
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.cv_back) {
            onBackPressed();
        } else if (id == R.id.play_video) {
            if (null != mPlayer && !mPlayer.isPlaying())
                mPlayer.play();
        } else if (id == R.id.pause_video) {
            if (null != mPlayer && mPlayer.isPlaying())
                mPlayer.pause();
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
        } else if (id == R.id.iv_bookmark) {
            if (!bookmarkStr.equals("1"))
                addBookmark();
            else removeBookmark();
        }
    }
}