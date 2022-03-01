package com.rajasahabacademy.activity.contact_us;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.about_us.model.ContentResponse;
import com.rajasahabacademy.api.Communicator;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.api.CustomResponseListener;
import com.rajasahabacademy.support.Utils;

import org.json.JSONObject;

public class ContactUsActivity extends AppCompatActivity implements View.OnClickListener {

    Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.darkThemeForceStop();
        setContentView(R.layout.activity_contact_us);
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
        setClickListener();
        getContactUs();
    }


    private void setClickListener() {
        TextView tvSubmit = findViewById(R.id.tv_submit);
        CardView cvmenu = findViewById(R.id.cv_back);
        cvmenu.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
    }

    private void getContactUs() {
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
            communicator.post(101, mActivity, Constants.Apis.GET_CONTENT, params, new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    Utils.hideProgressBar();
                    try {
                        if (response != null && !response.equals("")) {
                            ContentResponse modelResponse = (ContentResponse) Utils.getObject(response, ContentResponse.class);
                            if (!modelResponse.getContactUs().equals("")) {
                                WebView webView = findViewById(R.id.web_view_contact);
                                String message = "<font color='black'>" + "<b>" + modelResponse.getContactUs() + "</b>" + "<font color='cyan'>" + "<font size='22'></font>";
                                webView.loadData(message, "text/html", "utf8");
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

    private void contactUs() {
        EditText etName = findViewById(R.id.et_name);
        EditText etMobile = findViewById(R.id.et_mobile_number);
        EditText etEmail = findViewById(R.id.et_email_address);
        EditText etMessage = findViewById(R.id.et_message);

        String nameStr = etName.getText().toString().trim();
        String phoneStr = etMobile.getText().toString().trim();
        String emailStr = etEmail.getText().toString().trim();
        String messageStr = etMessage.getText().toString().trim();

        if (nameStr.isEmpty())
            Utils.showToastPopup(mActivity, getString(R.string.name_empty_validation));
        else if (phoneStr.isEmpty())
            Utils.showToastPopup(mActivity, getString(R.string.phone_empty_validation));
        /*else if (emailStr.isEmpty())
            Utils.showToastPopup(mActivity, getString(R.string.email_empty_validation));
        else if (!Utils.isValidEmail(emailStr))
            Utils.showToastPopup(mActivity, getString(R.string.email_valid_validation));
        else if (messageStr.isEmpty())
            Utils.showToastPopup(mActivity, getString(R.string.message_empty_validation));*/
        else {
            if (Utils.isNetworkAvailable(mActivity)) {
                Utils.showProgressBar(mActivity);
                Utils.hideKeyboard(mActivity);
                RequestParams params = new RequestParams();
                try {
                    params.put(Constants.Params.NAME, nameStr);
                    params.put(Constants.Params.CONTACT, phoneStr);
                    params.put(Constants.Params.EMAIL, emailStr);
                    params.put(Constants.Params.MESSAGE, messageStr);
                    params.put(Constants.Params.DEVICE_ID, Utils.getDeviceId(mActivity));
                    Utils.printLog("ProfileDetailParams", params.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Communicator communicator = new Communicator();
                communicator.post(101, mActivity, Constants.Apis.CONTENT_US, params, new CustomResponseListener() {
                    @Override
                    public void onResponse(int requestCode, String response) {
                        Utils.hideProgressBar();
                        try {
                            if (response != null && !response.equals("")) {
                                JSONObject object = new JSONObject(response);
                                if (object.getBoolean("success")) {
                                    Toast.makeText(mActivity, getString(R.string.successfully), Toast.LENGTH_SHORT).show();
                                    onBackPressed();
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

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.cv_back) {
            onBackPressed();
        } else if (id == R.id.tv_submit) {
            contactUs();
        }
    }
}