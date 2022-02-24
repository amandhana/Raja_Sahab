package com.rajasahabacademy.activity.chat.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.loopj.android.http.RequestParams;
import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.chat.adapter.ChatAdapter;
import com.rajasahabacademy.api.Communicator;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.api.CustomResponseListener;
import com.rajasahabacademy.activity.chat.model.ChatResponse;
import com.rajasahabacademy.activity.chat.model.Result;
import com.rajasahabacademy.activity.chat.model.chat_update.ChatUpdateResponse;
import com.rajasahabacademy.support.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {
    private Activity mActivity;
    RecyclerView recyclerView;
    ChatAdapter chatAdapter;
    List<Result> list = new ArrayList<>();
    Handler handler = new Handler(Looper.getMainLooper());
    String lastId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.darkThemeForceStop();
        setContentView(R.layout.activity_chat);
        init();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handler.removeCallbacksAndMessages(null);
        Utils.hideKeyboard(mActivity);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void init() {
        mActivity = this;
        setClickListener();
        setUpList();
    }

    private void setClickListener() {
        CardView cvmenu = findViewById(R.id.cv_back);
        cvmenu.setOnClickListener(this);
        RelativeLayout sendLay = findViewById(R.id.send_lay);
        sendLay.setOnClickListener(this);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false));
        recyclerView.addOnLayoutChangeListener((view, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (bottom < oldBottom) {
                recyclerView.scrollBy(0, oldBottom - bottom);
            }
        });
    }

    private void setUpList() {
        if (Utils.isNetworkAvailable(mActivity)) {
            Utils.showProgressBar(mActivity);
            Utils.hideKeyboard(mActivity);
            RequestParams params = new RequestParams();
            try {
                params.put(Constants.Params.USER_ID, Utils.getUserId(mActivity));
                params.put(Constants.Params.DEVICE_ID, Utils.getDeviceId(mActivity));
                Utils.printLog("ProfileDetailParams", params.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Communicator communicator = new Communicator();
            communicator.post(101, mActivity, Constants.Apis.CHATS, params, new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    Utils.hideProgressBar();
                    try {
                        if (response != null && !response.equals("")) {
                            ChatResponse modelResponse = (ChatResponse) Utils.getObject(response, ChatResponse.class);
                            if (modelResponse != null && modelResponse.getMessage() != null && modelResponse.getMessage().equalsIgnoreCase("ok")) {
                                if (modelResponse.getResults() != null && modelResponse.getResults().size() > 0) {
                                    list.addAll(modelResponse.getResults());
                                    chatAdapter = new ChatAdapter(mActivity, list);
                                    recyclerView.setAdapter(chatAdapter);
                                    recyclerView.smoothScrollToPosition(list.size());
                                    updateChatStart();
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

    private void getUpdateChatData() {
        if (Utils.isNetworkAvailable(mActivity)) {
            RequestParams params = new RequestParams();
            try {
                params.put(Constants.Params.USER_ID, Utils.getUserId(mActivity));
                params.put(Constants.Params.LAST_ID, getLastMessageId());
                Utils.printLog("ProfileDetailParams", params.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Communicator communicator = new Communicator();
            communicator.post(101, mActivity, Constants.Apis.GET_UPDATE_CHAT_DATA, params, new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    Utils.hideProgressBar();
                    try {
                        if (response != null && !response.equals("")) {
                            ChatUpdateResponse modelResponse = (ChatUpdateResponse) Utils.getObject(response, ChatUpdateResponse.class);
                            if (modelResponse != null && modelResponse.getMessage() != null && modelResponse.getMessage().equalsIgnoreCase("ok")) {
                                if (modelResponse.getResults() != null && modelResponse.getResults().size() > 0) {
                                    for (int i = 0; i < modelResponse.getResults().size(); i++) {
                                        Result model = new Result();
                                        model.setSenderId(modelResponse.getResults().get(i).getSenderId());
                                        model.setCreatedAt(modelResponse.getResults().get(i).getCreatedAt());
                                        model.setMessage(modelResponse.getResults().get(i).getMessage());
                                        model.setId(modelResponse.getResults().get(i).getId());
                                        model.setSentToUser(modelResponse.getResults().get(i).getSentToUser());
                                        list.add(model);
                                    }
                                    if (chatAdapter != null)
                                        chatAdapter.notifyItemInserted(list.size() - 1);
                                    recyclerView.smoothScrollToPosition(list.size());
                                    lastId = getLastMessageId();
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


    private void updateChatStart() {
        handler.postDelayed(new Runnable() {
            public void run() {
                getUpdateChatData();
                handler.postDelayed(this, 3000);
            }
        }, 3000);
    }

    private String getLastMessageId() {
        try {
            if (list.size() > 0) {
                int index = -1;
                for (int i = 0; i < list.size(); i++) {
                    index = i;
                }
                lastId = list.get(index).getId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lastId;
    }

    private void sendMessage() {
        Utils.hideKeyboard(mActivity);
        EditText etMessage = findViewById(R.id.et_message);
        String messageStr = etMessage.getText().toString().trim();
        if (!messageStr.isEmpty()) {
            if (Utils.isNetworkAvailable(mActivity)) {
                handler.removeCallbacksAndMessages(null);
                Utils.showProgressBar(mActivity);
                Utils.hideKeyboard(mActivity);
                RequestParams params = new RequestParams();
                try {
                    params.put(Constants.Params.USER_ID, Utils.getUserId(mActivity));
                    params.put(Constants.Params.MESSAGE, messageStr);
                    params.put(Constants.Params.DEVICE_ID, Utils.getDeviceId(mActivity));
                    Utils.printLog("ProfileDetailParams", params.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Communicator communicator = new Communicator();
                communicator.post(101, mActivity, Constants.Apis.SEND_MESSAGE, params, new CustomResponseListener() {
                    @Override
                    public void onResponse(int requestCode, String response) {
                        Utils.hideProgressBar();
                        try {
                            if (response != null && !response.equals("")) {
                                JSONObject object = new JSONObject(response);
                                if (object.getString("message").equalsIgnoreCase("ok")) {
                                    ChatUpdateResponse modelResponse = (ChatUpdateResponse) Utils.getObject(response, ChatUpdateResponse.class);
                                    if (modelResponse.getResults() != null && modelResponse.getResults().size() > 0) {
                                        for (int i = 0; i < modelResponse.getResults().size(); i++) {
                                            Result model = new Result();
                                            model.setMessage(modelResponse.getResults().get(i).getMessage());
                                            model.setCreatedAt(modelResponse.getResults().get(i).getCreatedAt());
                                            model.setSenderId(modelResponse.getResults().get(i).getSenderId());
                                            model.setId(modelResponse.getResults().get(i).getId());
                                            model.setSentToUser(modelResponse.getResults().get(i).getSentToUser());
                                            list.add(model);
                                        }
                                        if (chatAdapter != null) {
                                            if (list.size() > 0)
                                                chatAdapter.notifyItemInserted(list.size() - 1);
                                            else chatAdapter.updateList(list);
                                        } else {
                                            chatAdapter = new ChatAdapter(mActivity, list);
                                            recyclerView.setAdapter(chatAdapter);
                                        }
                                        etMessage.setText("");
                                        recyclerView.smoothScrollToPosition(list.size());
                                    }

                                }
                                updateChatStart();
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

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.cv_back) {
            onBackPressed();
        } else if (id == R.id.send_lay) {
            sendMessage();
        }
    }
}