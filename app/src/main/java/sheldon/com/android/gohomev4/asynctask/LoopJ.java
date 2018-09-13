package sheldon.com.android.gohomev4.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import org.json.*;

import com.loopj.android.http.*;

import cz.msebera.android.httpclient.Header;
import sheldon.com.android.gohomev4.R;

public class LoopJ {
    private static final String TAG = "LOOPJ";
    private static final String TAG_LOGIN = "LOOPJ_LOGIN";
    private static final String TAG_SYNC = "LOOPJ_SYNC";
    private static final String TAG_SWITCH = "LOOPJ_SWITCH";
    private static final String AUTH_URL = "user/submitLogin/";
    private static final String SYNC_URL = "device/getListSensor/";
    private static final String DO_SETTER_URL = "device/setDO/";

    private Context context;
    private LoopJListener loopJListener;
    public static JSONObject syncResponse;
    public static boolean isSyncingData, isChangingSwitchState;

    public LoopJ() {
    }

    public LoopJ(LoopJListener loopJListener) {
        this.loopJListener = loopJListener;
    }

    public LoopJ(Context context, LoopJListener loopJListener) {
        this.context = context;
        this.loopJListener = loopJListener;
    }

    public void sendLoginRequest(String username, String hashPassword) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("username", username);
        requestParams.put("hashpassword", hashPassword);

        LoopJRestClient.post(AUTH_URL, requestParams, new JsonHttpResponseHandler() {
            ProgressDialog progressDialog;

            @Override
            public void onStart() {
                progressDialog = new ProgressDialog(context,
                        R.style.AppTheme_Dark_Dialog);

                progressDialog.setMessage("Authenticating...");
                progressDialog.setIndeterminate(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d(TAG_LOGIN, "onFailure#1: " + errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d(TAG_LOGIN, "onFailure#2: " + responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(TAG_LOGIN, "onSuccess: " + response);

                loopJListener.authenticate(response);
            }

            @Override
            public void onFinish() {
                //Do something with the response
                progressDialog.dismiss();
            }
        });
    }

    public static void synchronize(String token, String username) {
//        Log.d("USERNAME", "synchronize: " + username);
//        Log.d("TOKEN", "synchronize: " + token);

        LoopJRestClient.addHeader("username", username);
        LoopJRestClient.addHeader("tokenid", token);

        LoopJRestClient.get(SYNC_URL, null, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                isSyncingData = true;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                  JSONObject errorResponse) {
                Log.d(TAG_SYNC, "onFailure#1: " + errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString,
                                  Throwable throwable) {
                Log.d(TAG_SYNC, "onFailure#2: " + responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject response) {
                syncResponse = response;
            }

            @Override
            public void onFinish() {
                isSyncingData = false;
            }
        });
    }

    public static void setSwitchState(String username, int switchID, int switchState) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("username", username);
        requestParams.put("switchID", switchID);
        requestParams.put("switchVal", switchState);

        LoopJRestClient.post(DO_SETTER_URL, requestParams, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                isChangingSwitchState = true;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d(TAG_SWITCH, "onFailure#2: " + responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(TAG_SWITCH, "onSuccess: " + response.toString());
            }

            @Override
            public void onFinish() {
                isChangingSwitchState = false;
            }
        });
    }
}