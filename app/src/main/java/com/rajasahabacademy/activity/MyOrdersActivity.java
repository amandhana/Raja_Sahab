package com.rajasahabacademy.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.loopj.android.http.RequestParams;
import com.rajasahabacademy.R;
import com.rajasahabacademy.adapter.CourseAdapter;
import com.rajasahabacademy.adapter.HomeLatestCourseAdapter;
import com.rajasahabacademy.adapter.MyOrderAdapter;
import com.rajasahabacademy.api.Communicator;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.api.CustomResponseListener;
import com.rajasahabacademy.model.course.CourseResponse;
import com.rajasahabacademy.model.order_history.OrderHistoryResponse;
import com.rajasahabacademy.model.order_history.Result;
import com.rajasahabacademy.support.Utils;

import java.util.ArrayList;
import java.util.List;

public class MyOrdersActivity extends AppCompatActivity implements View.OnClickListener {
    Activity mActivity;
    ShimmerFrameLayout orderHistoryShimmer;
    int pageNo = 0;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    MyOrderAdapter myOrderAdapter;
    private boolean loading = true;
    ProgressBar progressBar;
    List<Result> list = new ArrayList<>();
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.darkThemeForceStop();
        setContentView(R.layout.activity_my_orders);
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
        clickListener();
        setUpMyOrderList("First");
        pagination();
    }

    private void clickListener() {
        orderHistoryShimmer = findViewById(R.id.order_history_shimmer);
        progressBar = findViewById(R.id.progress_bar);
        recyclerView = findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        CardView cvBack = findViewById(R.id.cv_back);
        CardView cvCart = findViewById(R.id.cv_cart);
        cvBack.setOnClickListener(this);
        cvCart.setOnClickListener(this);
    }

    private void pagination() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount = linearLayoutManager.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();
                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            pageNo++;
                            setUpMyOrderList("Pagination");
                        }
                    }
                }
            }
        });

    }

    private void setUpMyOrderList(String type) {
        if (Utils.isNetworkAvailable(mActivity)) {
            if (type.equals("First"))
                orderHistoryShimmer.startShimmer();
            else progressBar.setVisibility(View.VISIBLE);
            Utils.hideKeyboard(mActivity);
            RequestParams params = new RequestParams();
            try {
                params.put(Constants.Params.USER_ID, Utils.getUserId(mActivity));
                params.put(Constants.Params.DEVICE_ID, Utils.getDeviceId(mActivity));
                params.put(Constants.Params.PAGE, pageNo);
                Utils.printLog("ProfileDetailParams", params.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Communicator communicator = new Communicator();
            communicator.post(101, mActivity, Constants.Apis.ORDER_HISTORY, params, new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    try {
                        orderHistoryShimmer.setVisibility(View.GONE);
                        if (response != null && !response.equals("")) {
                            OrderHistoryResponse modelResponse = (OrderHistoryResponse) Utils.getObject(response, OrderHistoryResponse.class);
                            if (modelResponse != null && modelResponse.getMessage() != null && modelResponse.getMessage().equalsIgnoreCase("ok")) {
                                if (modelResponse.getResults() != null && modelResponse.getResults().size() > 0) {
                                    if (progressBar.getVisibility() == View.VISIBLE) {
                                        progressBar.setVisibility(View.GONE);
                                        loading = true;
                                        list.addAll(modelResponse.getResults());
                                        myOrderAdapter.updateList(list);
                                    } else {
                                        list.addAll(modelResponse.getResults());
                                        myOrderAdapter = new MyOrderAdapter(mActivity, list);
                                        recyclerView.setAdapter(myOrderAdapter);
                                    }
                                    recyclerView.setVisibility(View.VISIBLE);
                                    findViewById(R.id.tv_no_data).setVisibility(View.GONE);
                                } else {
                                    if (progressBar.getVisibility() == View.VISIBLE) {
                                        progressBar.setVisibility(View.GONE);
                                    }
                                    if (type.equals("First"))
                                        findViewById(R.id.tv_no_data).setVisibility(View.VISIBLE);
                                }
                            } else {
                                if (progressBar.getVisibility() == View.VISIBLE) {
                                    progressBar.setVisibility(View.GONE);
                                }
                                recyclerView.setVisibility(View.GONE);
                                findViewById(R.id.tv_no_data).setVisibility(View.VISIBLE);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (progressBar.getVisibility() == View.VISIBLE) {
                            progressBar.setVisibility(View.GONE);
                        }
                        recyclerView.setVisibility(View.GONE);
                        findViewById(R.id.tv_no_data).setVisibility(View.VISIBLE);

                    }
                }

                @Override
                public void onFailure(int statusCode, Throwable error) {
                    orderHistoryShimmer.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    findViewById(R.id.tv_no_data).setVisibility(View.VISIBLE);
                    if (progressBar.getVisibility() == View.VISIBLE) {
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
        } else Utils.showToastPopup(mActivity, getString(R.string.internet_error));
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.cv_back) {
            onBackPressed();
        } else if (id == R.id.cv_cart) {
            Utils.startActivity(mActivity, CartActivity.class);
        }
    }
}