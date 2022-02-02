package com.rajasahabacademy.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.loopj.android.http.RequestParams;
import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.HomeActivity;
import com.rajasahabacademy.adapter.CourseAdapter;
import com.rajasahabacademy.adapter.LiveQuizSubjectAdapter;
import com.rajasahabacademy.adapter.NotesSubjectAdapter;
import com.rajasahabacademy.adapter.NotesViewAdapter;
import com.rajasahabacademy.adapter.NotificationAdapter;
import com.rajasahabacademy.api.Communicator;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.api.CustomResponseListener;
import com.rajasahabacademy.model.notes.NotesResponse;
import com.rajasahabacademy.model.notes.ResultNotes;
import com.rajasahabacademy.model.notification.NotificationResponse;
import com.rajasahabacademy.model.quiz.ResultLiveQuiz;
import com.rajasahabacademy.support.Utils;

import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

public class NotesFragment extends Fragment implements View.OnClickListener {
    @SuppressLint("StaticFieldLeak")
    private static NotesFragment fragment;
    private Activity mActivity;
    private View rootView;
    ShimmerFrameLayout notesShimmer;
    RecyclerView recyclerView;
    NotesViewAdapter notesViewAdapter;
    List<ResultNotes> list;
    String categoryId = "";

    public static NotesFragment newInstance() {
        fragment = new NotesFragment();
        Constants.FragmentReference.notesFragment = fragment;
        return fragment;
    }

    public static NotesFragment getInstance() {
        if (fragment == null)
            return new NotesFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_notes, container, false);
        init();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity) mActivity).showHideBottomNavigation(false);
        ((HomeActivity) mActivity).showHideCart(true);
        ((HomeActivity) mActivity).applyFilter();
        setUpNotesList();
    }

    private void init() {
        mActivity = getActivity();
        clickListener();
        ((HomeActivity) mActivity).applyFilter();
        getBundleData();
    }

    private void clickListener() {
        notesShimmer = rootView.findViewById(R.id.notes_shimmer);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false));
    }

    private void getBundleData() {
        if (getArguments() != null && getArguments().getString(Constants.Params.CATEGORY_ID) != null)
            categoryId = getArguments().getString(Constants.Params.CATEGORY_ID);
    }

    private void setUpNotesList() {
        if (Utils.isNetworkAvailable(mActivity)) {
            notesShimmer.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            notesShimmer.startShimmer();
            Utils.hideKeyboard(mActivity);
            RequestParams params = new RequestParams();
            try {
                params.put(Constants.Params.USER_ID, Utils.getUserId(mActivity));
                params.put(Constants.Params.CATEGORY_ID, categoryId);
                params.put(Constants.Params.DEVICE_ID, Utils.getDeviceId(mActivity));
                Utils.printLog("ProfileDetailParams", params.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Communicator communicator = new Communicator();
            communicator.post(101, mActivity, Constants.Apis.NOTES_EBOOK, params, new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    notesShimmer.setVisibility(View.GONE);
                    ((HomeActivity) getActivity()).showCartCount();
                    try {
                        if (response != null && !response.equals("")) {
                            NotesResponse modelResponse = (NotesResponse) Utils.getObject(response, NotesResponse.class);
                            if (modelResponse != null && modelResponse.getMessage() != null && modelResponse.getMessage().equalsIgnoreCase("ok")) {
                                if (modelResponse.getResults() != null && modelResponse.getResults().size() > 0) {
                                    recyclerView.setVisibility(View.VISIBLE);
                                    rootView.findViewById(R.id.tv_no_data).setVisibility(View.GONE);
                                    list = modelResponse.getResults();
                                    notesViewAdapter = new NotesViewAdapter(mActivity, list, NotesFragment.this);
                                    recyclerView.setAdapter(notesViewAdapter);
                                    setCartCount();
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
                    notesShimmer.setVisibility(View.GONE);
                    Utils.showToastPopup(mActivity, error.getLocalizedMessage());
                }
            });
        } else Utils.showToastPopup(mActivity, getString(R.string.internet_error));
    }


    public List<ResultNotes> getList() {
        return list;
    }

    public void setCourseFilterList(List<ResultNotes> list) {
        if (recyclerView != null) {
            if (list != null && list.size() > 0) {
                recyclerView.setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.tv_no_data).setVisibility(View.GONE);
                if (notesViewAdapter != null)
                    notesViewAdapter.updateList(list);
            } else {
                recyclerView.setVisibility(View.GONE);
                rootView.findViewById(R.id.tv_no_data).setVisibility(View.VISIBLE);
            }
        }
    }

    public void addToCart(String noteId, int position) {
        if (Utils.isNetworkAvailable(mActivity)) {
            Utils.showProgressBar(mActivity);
            Utils.hideKeyboard(mActivity);
            RequestParams params = new RequestParams();
            try {
                params.put(Constants.Params.NOTE_ID, noteId);
                params.put(Constants.Params.USER_ID, Utils.getUserId(mActivity));
                params.put(Constants.Params.DEVICE_ID, Utils.getDeviceId(mActivity));
                Utils.printLog("ProfileDetailParams", params.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Communicator communicator = new Communicator();
            communicator.post(101, mActivity, Constants.Apis.ADD_NOTE_CART, params, new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    Utils.hideProgressBar();
                    try {
                        if (response != null && !response.equals("")) {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("notification").equalsIgnoreCase("Success")) {
                                list.get(position).setIsCart("1");
                                if (notesViewAdapter != null)
                                    notesViewAdapter.notifyDataSetChanged();
                                setCartCount();
                            }
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

    public void removeCart(String noteId, int position) {
        if (Utils.isNetworkAvailable(mActivity)) {
            Utils.showProgressBar(mActivity);
            Utils.hideKeyboard(mActivity);
            RequestParams params = new RequestParams();
            try {
                params.put(Constants.Params.NOTE_ID, noteId);
                params.put(Constants.Params.USER_ID, Utils.getUserId(mActivity));
                params.put(Constants.Params.DEVICE_ID, Utils.getDeviceId(mActivity));
                Utils.printLog("ProfileDetailParams", params.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Communicator communicator = new Communicator();
            communicator.post(101, mActivity, Constants.Apis.REMOVE_NOTE_CART, params, new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    Utils.hideProgressBar();
                    try {
                        if (response != null && !response.equals("")) {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optBoolean("success")) {
                                list.get(position).setIsCart("0");
                                if (notesViewAdapter != null)
                                    notesViewAdapter.notifyDataSetChanged();
                                setCartCount();
                            }
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

    private void setCartCount() {
        if (list != null) {
            if (list.size() > 0) {
                int count = 0;
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getIsCart().equalsIgnoreCase("1"))
                        count = count + 1;
                }
                Constants.AppSaveData.homeCartCount = String.valueOf(count);
                ((HomeActivity) getActivity()).showCartCount();
            }
        }
    }

    @Override
    public void onClick(View view) {

    }
}
