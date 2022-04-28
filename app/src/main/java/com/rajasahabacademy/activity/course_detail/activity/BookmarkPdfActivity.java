package com.rajasahabacademy.activity.course_detail.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;

import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.course_detail.adapter.BookmarkedPdfAdapter;
import com.rajasahabacademy.activity.course_detail.adapter.CourseDetailPdfAdapter;
import com.rajasahabacademy.activity.course_detail.adapter.CourseDetailVideoAdapter;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.support.Utils;

public class BookmarkPdfActivity extends AppCompatActivity {
    Activity mActivity;
    RecyclerView recyclerViewBookmarkedPdf;
    BookmarkedPdfAdapter bookmarkedPdfAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.darkThemeForceStop();
        setContentView(R.layout.activity_bookmark_pdf);
        init();
    }

    private void init() {
        mActivity = this;
        setBookmarkedPdf();
    }

    private void setBookmarkedPdf() {
        recyclerViewBookmarkedPdf = findViewById(R.id.recycler_view_bookmarked_notes);
        recyclerViewBookmarkedPdf.setLayoutManager(new LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false));
        recyclerViewBookmarkedPdf.setAdapter(bookmarkedPdfAdapter);

    }

}