package com.rajasahabacademy.activity.course_detail.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.loopj.android.http.RequestParams;
import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.course_detail.adapter.BookmarkedPdfAdapter;
import com.rajasahabacademy.activity.course_detail.model.bookmarked_pdf.BookmarkedPdfResponse;
import com.rajasahabacademy.api.Communicator;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.api.CustomResponseListener;
import com.rajasahabacademy.support.Utils;

public class BookmarkPdfActivity extends AppCompatActivity {
    Activity mActivity;
    RecyclerView recyclerViewBookmarkedPdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.darkThemeForceStop();
        setContentView(R.layout.activity_bookmark_pdf);
        init();
    }

    private void init() {
        mActivity = this;
        getBookmarkedPdf();
    }



    private void getBookmarkedPdf(){
        recyclerViewBookmarkedPdf = findViewById(R.id.recycler_view_bookmarked_notes);
        recyclerViewBookmarkedPdf.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
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
            communicator.post(101, mActivity, Constants.Apis.GET_BOOKMARK_EBOOK, params, new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    Utils.hideProgressBar();
                    try {
                        if (response != null && !response.equals("")) {
                            BookmarkedPdfResponse modelResponse = (BookmarkedPdfResponse) Utils.getObject(response, BookmarkedPdfResponse.class);
                            if (modelResponse != null) {
                                if (modelResponse.getSuccess()) {
                                    if (modelResponse.getData().size() > 0) {
                                        recyclerViewBookmarkedPdf.setVisibility(View.VISIBLE);
                                        findViewById(R.id.tv_no_course_pdf).setVisibility(View.GONE);
                                        recyclerViewBookmarkedPdf.setAdapter(new BookmarkedPdfAdapter(mActivity, modelResponse.getData()));
                                    } else {
                                        recyclerViewBookmarkedPdf.setVisibility(View.GONE);
                                        findViewById(R.id.tv_no_course_pdf).setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    recyclerViewBookmarkedPdf.setVisibility(View.GONE);
                                    findViewById(R.id.tv_no_course_pdf).setVisibility(View.VISIBLE);
                                }
                            } else {
                                recyclerViewBookmarkedPdf.setVisibility(View.GONE);
                                findViewById(R.id.tv_no_course_pdf).setVisibility(View.VISIBLE);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        recyclerViewBookmarkedPdf.setVisibility(View.GONE);
                        findViewById(R.id.tv_no_course_pdf).setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(int statusCode, Throwable error) {
                    Utils.hideProgressBar();
                    recyclerViewBookmarkedPdf.setVisibility(View.GONE);
                    findViewById(R.id.tv_no_course_pdf).setVisibility(View.VISIBLE);
                }
            });
        } else Utils.showToastPopup(mActivity, getString(R.string.internet_error));
    }


}