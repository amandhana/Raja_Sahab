package com.rajasahabacademy.activity.pdf_view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
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
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.barteksc.pdfviewer.PDFView;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.rajasahabacademy.R;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.support.Preference;
import com.rajasahabacademy.support.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class PdfViewActivity extends AppCompatActivity implements View.OnClickListener {

    Activity mActivity;
    WebView webView;
    String ebookPath = "";
    String ebookName = "";
    ShimmerFrameLayout pdfViewShimmer;
    RelativeLayout pdfLay;
    PDFView pdfView;

    ImageView ivDownload;
    RelativeLayout deleteLay;
    ProgressBar progressBar;
    ScrollView scrollView;
    LinearLayout mainLayout;
    Preference preference;
    long downloadId;
    DownloadManager manager;
    int dl_progress;
    TextView tvPercent;
    String fromWhere = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.darkThemeForceStop();
        setContentView(R.layout.activity_pdf_view);
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
    }

    private void clickListener() {
        pdfViewShimmer = findViewById(R.id.pdf_view_shimmer);
        webView = findViewById(R.id.web_view);
        pdfLay = findViewById(R.id.pdf_lay);
        pdfView = findViewById(R.id.pdfView);
        TextView tvName = findViewById(R.id.tv_ebook_name);
        tvName.setText(ebookName);
        CardView cvBack = findViewById(R.id.cv_back);
        cvBack.setOnClickListener(this);

        scrollView = findViewById(R.id.scroll_view);
        mainLayout = findViewById(R.id.main_layout);
        progressBar = findViewById(R.id.progress_bar);
        ivDownload = findViewById(R.id.iv_download);
        ivDownload.setOnClickListener(this);
        deleteLay = findViewById(R.id.delete_lay);
        deleteLay.setOnClickListener(this);

    }

    private void getBundleData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            fromWhere = bundle.getString(Constants.Course.FROM_WHERE);
            ebookPath = bundle.getString(Constants.Course.EBOOK_PATH);
            ebookName = bundle.getString(Constants.Course.EBOOK_NAME);
            if (fromWhere.equals("Offline")) {
                ivDownload.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                deleteLay.setVisibility(View.GONE);
                pdfLay.setVisibility(View.GONE);
                pdfView.setVisibility(View.VISIBLE);
                setUpLocalEbook();
            } else {
                pdfLay.setVisibility(View.VISIBLE);
                pdfView.setVisibility(View.GONE);
                setUpEbook();
                checkDownloadStatus();
                getDownloadPercentStatus();
                downloadCompleteNotify();
            }
        }
    }

    private void checkDownloadStatus() {
        if (!ebookName.equals("")) {
            String path = Environment.getExternalStorageDirectory() + "/Ebook";
            File fileDir = new File(path);
            File saveFilePath = new File(fileDir, ebookName+".pdf");
            if (!saveFilePath.exists()) {
                ivDownload.setVisibility(View.VISIBLE);
                deleteLay.setVisibility(View.GONE);
            } else {
                ivDownload.setVisibility(View.GONE);
                deleteLay.setVisibility(View.VISIBLE);
            }
        }
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

    @SuppressLint("SetJavaScriptEnabled")
    private void setUpEbook() {
        pdfViewShimmer.startShimmer();
        try {
            if (ebookPath != null && !ebookPath.equals("")) {
                webView.setWebChromeClient(new WebChromeClient() {
                    public void onProgressChanged(WebView view, int newProgress) {
                        if (newProgress == 100) {
                            pdfViewShimmer.setVisibility(View.GONE);
                        }
                    }
                });
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        if (view.getTitle().equals(""))
                            view.reload();
                        else pdfViewShimmer.setVisibility(View.GONE);
                    }
                });
                webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                webView.getSettings().setLoadWithOverviewMode(true);
                webView.getSettings().setBuiltInZoomControls(true);
                webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
                webView.clearCache(true);
                webView.clearHistory();
                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                webView.loadUrl(Constants.EBOOK_PATH + ebookPath);

            } else
                Toast.makeText(this, getString(R.string.ebook_path_valid), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.ebook_path_error) + e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setUpLocalEbook() {
        File file = new File(Environment.getExternalStorageDirectory() + "/Ebook/" + ebookName);
        pdfView.fromUri(Uri.fromFile(file))
                .defaultPage(0)
                .spacing(10)
                .load();
    }

    private void downloadPermission() {
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        Permissions.check(mActivity, permissions, null, null, new PermissionHandler() {
            @Override
            public void onGranted() {
                if (Utils.isNetworkAvailable(mActivity)) {
                    try {
                        downloadWithPercent(ebookPath, ebookName + ".pdf");
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
        String path = Environment.getExternalStorageDirectory() + "/Ebook";
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        File saveFilePath = new File(fileDir, fileName);

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
                String path = Environment.getExternalStorageDirectory() + "/Ebook";
                File fileDir = new File(path);
                File saveFilePath = new File(fileDir, ebookName+".pdf");
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


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.cv_back) {
            onBackPressed();
        } else if (view.getId() == R.id.iv_download)
            downloadPermission();
        else if (view.getId() == R.id.delete_lay) {
            deletePopup();
        }
    }
}