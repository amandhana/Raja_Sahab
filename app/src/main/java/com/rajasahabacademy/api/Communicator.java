package com.rajasahabacademy.api;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.rajasahabacademy.support.Utils;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class Communicator {

    public void postLogin(final int reqCode, final Activity context, String url, RequestParams params, final CustomResponseListener responseListener) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(5 * 60000);
        client.setResponseTimeout(5 * 60000);
        client.post(context, url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Utils.printLog("Response", response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optBoolean("userexistno"))
                        Utils.manageSingleDeviceLogin(context);
                    else responseListener.onResponse(reqCode, response.trim());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Utils.printLog("Error", error.toString());
                Utils.hideProgressBar();
                responseListener.onFailure(reqCode, error);
            }
        });

        Utils.printLog("URL", url);

    }



    public void post(final int reqCode, final Activity context, String url, RequestParams params, final CustomResponseListener responseListener) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(5 * 60000);
        client.setResponseTimeout(5 * 60000);
        client.post(context, url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Utils.printLog("Response", response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optBoolean("userexistno"))
                        Utils.manageSingleDeviceLogin(context);
                    else
                        responseListener.onResponse(reqCode, response.trim());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Utils.printLog("Error", error.toString());
                Utils.hideProgressBar();
                responseListener.onFailure(reqCode, error);
            }

        });

        Utils.printLog("URL", url);


    }


    public void postWithoutHeader(final int reqCode, final Context context, String url, RequestParams params, final CustomResponseListener responseListener) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(5 * 60000);
        client.setResponseTimeout(5 * 60000);
        client.post(context, url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Utils.printLog("Response", response);
                responseListener.onResponse(reqCode, response.trim());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Utils.printLog("Error", error.toString());
                Utils.hideProgressBar();
                responseListener.onFailure(reqCode, error);
            }

        });

        Utils.printLog("URL", url);


    }


    public void postMultipart(final int reqCode, final Context context, String url, RequestParams params, final CustomResponseListener responseListener) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Content-Type", "multipart/form-data");
        client.setTimeout(5 * 60000);
        client.setResponseTimeout(5 * 60000);
        client.post(context, url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                responseListener.onResponse(reqCode, response.trim());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                responseListener.onFailure(reqCode, error);
            }

        });

    }

    public void get(final int reqCode, final Context context, String url, final CustomResponseListener responseListener) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setMaxRetriesAndTimeout(1, 60000);
        client.get(context, url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Utils.printLog("Response", response);
                responseListener.onResponse(reqCode, response.trim());

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Utils.hideProgressBar();
                Utils.printLog("Error", error.toString());
                responseListener.onFailure(reqCode, error);
            }

        });
        Utils.printLog("URL", url);

    }

    public void get(final int reqCode, final Context context, String url, RequestParams params, final CustomResponseListener responseListener) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setMaxRetriesAndTimeout(2, 5 * 60000);
        client.get(context, url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Utils.printLog("Response", response);
                responseListener.onResponse(reqCode, response.trim());

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Utils.printLog("Error", error.toString());
                Utils.hideProgressBar();
                responseListener.onFailure(reqCode, error);
            }
        });
        Utils.printLog("URL", url);

    }

    public void post(final int reqCode, final Context context, String url, StringEntity entity, final CustomResponseListener responseListener) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(5 * 60000);
        client.post(context, url, entity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Utils.printLog("Response", response);
                responseListener.onResponse(reqCode, response.trim());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Utils.printLog("Error", error.toString());
                Utils.hideProgressBar();
                responseListener.onFailure(reqCode, error);
            }

        });

        Utils.printLog("URL", url);

    }

}