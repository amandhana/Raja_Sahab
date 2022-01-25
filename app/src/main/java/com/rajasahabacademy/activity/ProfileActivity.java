package com.rajasahabacademy.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.loopj.android.http.RequestParams;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.rajasahabacademy.R;
import com.rajasahabacademy.adapter.CityAdapter;
import com.rajasahabacademy.adapter.StateAdapter;
import com.rajasahabacademy.api.Communicator;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.api.CustomResponseListener;
import com.rajasahabacademy.model.CityResponse.CityResponse;
import com.rajasahabacademy.model.CityResponse.ResultCity;
import com.rajasahabacademy.model.StateResponse.Result;
import com.rajasahabacademy.model.StateResponse.StateResponse;
import com.rajasahabacademy.model.login.LoginResponse;
import com.rajasahabacademy.model.profile_detail.ProfileDetailResponse;
import com.rajasahabacademy.model.profile_detail.Success;
import com.rajasahabacademy.support.FileUtils;
import com.rajasahabacademy.support.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    Activity mActivity;
    EditText etName;
    EditText etEmail;
    EditText etPhone;
    EditText etCity;
    EditText etState;
    EditText etAddress;
    EditText etEducation;
    EditText etRefBy;
    ImageView ivImage;
    ActivityResultLauncher<Intent> cameraActivityResultLauncher;
    ActivityResultLauncher<Intent> galleryActivityResultLauncher;
    File imageFile = null;
    String fromWhere = "";

    Spinner stateSpinn;
    Spinner citySpinn;
    List<Result> stateList = new ArrayList<>();
    List
            <ResultCity> cityList = new ArrayList<>();
    StateAdapter stateAdapter;
    CityAdapter cityAdapter;
    String stateId = "";
    String selectedStateNameStr = "";
    String cityId = "";
    String selectedCityNameStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.darkThemeForceStop();
        setContentView(R.layout.activity_profile);
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
        onActivityCamera();
        onActivityGallery();
        getProfileDetail();
        getStateList();
    }



    private void clickListener() {
        CardView cvBack = findViewById(R.id.cv_back);
        cvBack.setOnClickListener(this);
        TextView tvSkip = findViewById(R.id.tv_skip);
        tvSkip.setOnClickListener(this);
        cvBack.setVisibility(View.GONE);
        tvSkip.setVisibility(View.GONE);
        if (getIntent().getStringExtra(Constants.Preference.FROM_WHERE) != null) {
            cvBack.setVisibility(View.GONE);
            tvSkip.setVisibility(View.VISIBLE);
            RelativeLayout cameraLay = findViewById(R.id.camera_lay);
            cameraLay.setVisibility(View.GONE);
            RelativeLayout nameEditLay = findViewById(R.id.name_edit_lay);
            nameEditLay.setVisibility(View.GONE);
            RelativeLayout emailEditLay = findViewById(R.id.email_edit_lay);
            emailEditLay.setVisibility(View.GONE);
            RelativeLayout phoneEditLay = findViewById(R.id.mobile_number_edit_lay);
            phoneEditLay.setVisibility(View.GONE);
            RelativeLayout addressEditLay = findViewById(R.id.address_edit_lay);
            addressEditLay.setVisibility(View.GONE);
            RelativeLayout educationEditLay = findViewById(R.id.education_edit_lay);
            educationEditLay.setVisibility(View.GONE);
            tvSkip.setVisibility(View.GONE);
            TextView tvRefferalCode = findViewById(R.id.tv_refferal_code);
            tvRefferalCode.setVisibility(View.GONE);
            fromWhere = "guest";
        } else {
            cvBack.setVisibility(View.VISIBLE);
            tvSkip.setVisibility(View.GONE);
           /* RelativeLayout refByLay = findViewById(R.id.ref_by_lay);
            refByLay.setVisibility(View.GONE);*/
            fromWhere = "user";
        }
        ivImage = findViewById(R.id.iv_profile_image);
        etName = findViewById(R.id.et_profile_name);
        etEmail = findViewById(R.id.et_profile_email);
        etCity = findViewById(R.id.et_profile_city);
        etState = findViewById(R.id.et_profile_state);
        etEducation = findViewById(R.id.et_profile_education);
        etPhone = findViewById(R.id.et_profile_phone_number);
        etAddress = findViewById(R.id.et_profile_address);
        etRefBy = findViewById(R.id.et_ref_by);

        RelativeLayout cameraLay = findViewById(R.id.camera_lay);
        RelativeLayout nameEditLay = findViewById(R.id.name_edit_lay);
        RelativeLayout emailEditLay = findViewById(R.id.email_edit_lay);
        RelativeLayout phoneEditLay = findViewById(R.id.mobile_number_edit_lay);
        RelativeLayout addressEditLay = findViewById(R.id.address_edit_lay);
        TextView tvUpdate = findViewById(R.id.tv_update);
        nameEditLay.setOnClickListener(this);
        emailEditLay.setOnClickListener(this);
        phoneEditLay.setOnClickListener(this);
        addressEditLay.setOnClickListener(this);
        cameraLay.setOnClickListener(this);
        tvUpdate.setOnClickListener(this);

        stateSpinn = findViewById(R.id.state_spinn);
        citySpinn = findViewById(R.id.city_spinn);

        stateSpinn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (stateList.get(position).getId() != null) {
                    stateId = stateList.get(position).getId();
                    selectedStateNameStr = stateList.get(position).getName();
                    getCity();
                } else {
                    stateId = "";
                    selectedStateNameStr = "";
                    cityList.clear();
                    if (cityAdapter != null)
                        cityAdapter.notifyDataSetChanged();
                    cityId = "";
                    selectedCityNameStr = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });
        citySpinn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (cityList.get(position).getId() != null) {
                    cityId = cityList.get(position).getId();
                    selectedCityNameStr = cityList.get(position).getName();
                } else {
                    cityId = "";
                    selectedCityNameStr = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void getProfileDetail() {
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
            communicator.post(101, mActivity, Constants.Apis.PROFILE_DETAIL, params, new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    Utils.hideProgressBar();
                    findViewById(R.id.top_lay).setVisibility(View.VISIBLE);
                    try {
                        if (response != null && !response.equals("")) {
                            ProfileDetailResponse modelResponse = (ProfileDetailResponse) Utils.getObject(response, ProfileDetailResponse.class);
                            if (modelResponse != null && modelResponse.getSuccess() != null) {
                                setUpData(modelResponse.getSuccess());
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

    private void setUpData(Success model) {
        try {
            Utils.setProfileImageUsingGlide(mActivity, model.getImage(), ivImage);
            etName.setText(model.getName());
            etName.setSelection(etName.getText().toString().length());
            etName.requestFocus();
            etEmail.setText(model.getEmail());
            etEmail.setSelection(etEmail.getText().toString().length());
            etEmail.requestFocus();
            etPhone.setText(model.getPhone());
            etPhone.setSelection(etPhone.getText().toString().length());
            etPhone.requestFocus();
            etCity.setText(model.getCity());
            etCity.setSelection(etCity.getText().toString().length());
            etCity.requestFocus();
            etState.setText(model.getState());
            etState.setSelection(etState.getText().toString().length());
            etState.requestFocus();
            etEducation.setText(model.getEducation());
            etEducation.setSelection(etEducation.getText().toString().length());
            etEducation.requestFocus();
            etAddress.setText(model.getAddress());
            etAddress.setSelection(etAddress.getText().toString().length());
            etAddress.requestFocus();

            TextView tvUserName = findViewById(R.id.tv_profile_username);
            TextView tvUserPhone = findViewById(R.id.tv_profile_user_phone);
            TextView tvRefferalCode = findViewById(R.id.tv_refferal_code);
            tvUserName.setText(model.getName());
            tvUserPhone.setText(model.getPhone());
            tvRefferalCode.setText("Refferal Code : " + model.getRefCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void photoSelect() {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        Permissions.check(mActivity, permissions, null, null, new PermissionHandler() {
            @Override
            public void onGranted() {
                showToastPopup();
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                super.onDenied(context, deniedPermissions);
            }
        });
    }

    public void showToastPopup() {
        try {
            final Dialog dialog = new Dialog(mActivity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.toast_popup_photo);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            dialog.setCanceledOnTouchOutside(false);

            TextView tvCamera = dialog.findViewById(R.id.tv_camera);
            TextView tvGallery = dialog.findViewById(R.id.tv_gallery);

            tvCamera.setOnClickListener(v -> {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraActivityResultLauncher.launch(takePicture);
                dialog.dismiss();
            });

            tvGallery.setOnClickListener(v -> {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryActivityResultLauncher.launch(pickPhoto);
                dialog.dismiss();
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onActivityCamera() {
        cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                        Uri resultUri = Utils.getImageUri(mActivity, bitmap);
                        String filePath = FileUtils.getPath(mActivity, resultUri);
                        if (filePath != null) {
                            imageFile = new File(filePath);
                            Utils.clearGlide(mActivity, ivImage);
                            ivImage.setImageURI(resultUri);
                        }

                    }
                });
    }

    private void onActivityGallery() {
        galleryActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Uri resultUri = Objects.requireNonNull(data).getData();
                        String filePath = FileUtils.getPath(mActivity, resultUri);
                        if (filePath != null) {
                            imageFile = new File(filePath);
                            Utils.clearGlide(mActivity, ivImage);
                            ivImage.setImageURI(resultUri);
                        }

                    }
                });
    }

    private void updateProfile() {
        String nameStr = etName.getText().toString().trim();
        String emailStr = etEmail.getText().toString().trim();
        String phoneStr = etPhone.getText().toString().trim();
        String addressStr = etAddress.getText().toString().trim();
        String educationStr = etEducation.getText().toString().trim();
        String refByStr = etRefBy.getText().toString().trim();


        if (nameStr.isEmpty()) {
            Utils.showToastPopup(mActivity, getString(R.string.name_empty_validation));
            return;
        } else if (!Utils.isValidEmail(emailStr)) {
            Utils.showToastPopup(mActivity, getString(R.string.email_valid_validation));
            return;
        } else if (fromWhere.equalsIgnoreCase("guest")) {
            if (stateId.isEmpty()) {
                Utils.showToastPopup(mActivity, getString(R.string.select_state_validation));
                return;
            } else if (cityId.isEmpty()) {
                Utils.showToastPopup(mActivity, getString(R.string.select_city_validation));
                return;
            }
        }
        if (Utils.isNetworkAvailable(mActivity)) {
            Utils.showProgressBar(mActivity);
            Utils.hideKeyboard(mActivity);
            RequestParams params = new RequestParams();
            try {
                params.put(Constants.Params.NAME, nameStr);
                params.put(Constants.Params.EMAIL, emailStr);
                params.put(Constants.Params.PHONE, phoneStr);
                params.put(Constants.Params.CITY, selectedCityNameStr);
                params.put(Constants.Params.STATE, selectedStateNameStr);
                params.put(Constants.Params.EDUCATION, educationStr);
                params.put(Constants.Params.ADDRESS, addressStr);
                params.put(Constants.Params.REF_BY, refByStr);
                if (imageFile != null)
                    params.put(Constants.Params.IMAGE, imageFile);
                params.put(Constants.Params.USER_ID, Utils.getUserId(mActivity));
                params.put(Constants.Params.DEVICE_ID, Utils.getDeviceId(mActivity));
                Utils.printLog("ProfileDetailParams", params.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Communicator communicator = new Communicator();
            communicator.post(101, mActivity, Constants.Apis.UPDATE_PROFILE, params, new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    Utils.hideProgressBar();
                    try {
                        if (response != null && !response.equals("")) {
                            LoginResponse modelResponse = (LoginResponse) Utils.getObject(response, LoginResponse.class);
                            if (modelResponse != null && modelResponse.getMessage() != null) {
                                if (modelResponse.getMessage().equalsIgnoreCase("ok")) {
                                    Utils.saveLoginUser(mActivity, modelResponse);
                                    if (fromWhere.equals("guest")) {
                                        Utils.startActivityFinish(mActivity, HomeActivity.class);
                                        Toast.makeText(mActivity, modelResponse.getNotification(), Toast.LENGTH_SHORT).show();
                                    } else
                                        Utils.showToastPopup(mActivity, modelResponse.getNotification());
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


    private void getStateList() {
        if (Utils.isNetworkAvailable(mActivity)) {
            Utils.showProgressBar(mActivity);
            Utils.hideKeyboard(mActivity);
            Communicator communicator = new Communicator();
            communicator.post(101, mActivity, Constants.Apis.STATES, new RequestParams(), new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    Utils.hideProgressBar();
                    try {
                        if (response != null && !response.equals("")) {
                            StateResponse modelResponse = (StateResponse) Utils.getObject(response, StateResponse.class);
                            if (modelResponse != null) {
                                if (modelResponse.getNotification().equalsIgnoreCase("success")) {
                                    if (modelResponse.getResults().size() > 0) {
                                        Result result = new Result();
                                        result.setName("Select state");
                                        stateList = modelResponse.getResults();
                                        stateList.add(0, result);
                                        stateAdapter = new StateAdapter(mActivity, stateList);
                                        stateSpinn.setAdapter(stateAdapter);
                                    }
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

    private void getCity() {
        if (Utils.isNetworkAvailable(mActivity)) {
            Utils.showProgressBar(mActivity);
            Utils.hideKeyboard(mActivity);
            RequestParams params = new RequestParams();
            try {
                params.put(Constants.Params.STATE_ID, stateId);
                Utils.printLog("ProfileDetailParams", params.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Communicator communicator = new Communicator();
            communicator.post(101, mActivity, Constants.Apis.CITY, params, new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    Utils.hideProgressBar();
                    try {
                        if (response != null && !response.equals("")) {
                            CityResponse modelResponse = (CityResponse) Utils.getObject(response, CityResponse.class);
                            if (modelResponse != null) {
                                if (modelResponse.getNotification().equalsIgnoreCase("success")) {
                                    if (modelResponse.getResults().size() > 0) {
                                        ResultCity result = new ResultCity();
                                        result.setName("Select city");
                                        cityList = modelResponse.getResults();
                                        cityList.add(0, result);
                                        cityAdapter = new CityAdapter(mActivity, cityList);
                                        citySpinn.setAdapter(cityAdapter);
                                    }
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


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.cv_back) {
            onBackPressed();
        } else if (id == R.id.tv_skip) {
            Utils.startActivityFinish(mActivity, HomeActivity.class);
        } else if (id == R.id.name_edit_lay) {
            etName.setEnabled(true);
            etName.requestFocus();
            if (etName.requestFocus())
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            Utils.showSoftKeyboard(etName, mActivity);
        } else if (id == R.id.email_edit_lay) {
            etEmail.setEnabled(true);
            etEmail.requestFocus();
            if (etEmail.requestFocus())
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            Utils.showSoftKeyboard(etEmail, mActivity);
        } else if (id == R.id.mobile_number_edit_lay) {
        } else if (id == R.id.address_edit_lay) {
            etAddress.setEnabled(true);
            etAddress.requestFocus();
            if (etAddress.requestFocus())
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            Utils.showSoftKeyboard(etAddress, mActivity);
        } else if (id == R.id.camera_lay) {
            photoSelect();
        } else if (id == R.id.tv_update) {
            updateProfile();
        }
    }
}