package activity;

import static java.lang.Thread.sleep;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.vihaan.shaktinewconcept.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.UUID;

import Database.DatabaseHelper;
import activity.BeanVk.MotorParamListModel;
import activity.utility.CustomUtility;
import webservice.AllPopupUtil;
import webservice.Constants;
import webservice.WebURL;


public class DeviceSettingActivity extends AppCompatActivity {
    private static final String TAG = "DeviceSettingActivity";
    Timer timer;
    int counterValue = 0;
    int mWriteAllCounterValue = 0;
    int mReadAllCounterValue = 0;
    private List<MotorParamListModel.Response> mSettingParameterResponse;
    Context mContext;
    private ProgressDialog progressDialog;
    private List<EditText> mEditTextList;
    private List<TextView> mTextViewSetIDtList;

    JSONArray jsonArray = null;
    private BluetoothSocket btSocket;
    private BluetoothAdapter myBluetooth;

    private UUID mMyUDID;
    private InputStream iStream;
    private Activity mActivity;

    RelativeLayout iv_sub_linearlayout12;
    LinearLayout lvlMainParentLayoutID;

    RelativeLayout rlvMainDynamicViewID;

    private int edtValue = 0;
    private float edtValueFloat = 0;
    private String old_data = "1";

    float mTotalTimeFloatData;
    char mCRCFinalValue;
    char mCRCFinalValueWrite;
    int i = 0;
    Intent myIntent;
    String myVersionName, mobilemodel, androidVersion;
    int mGlobalPosition = 0;
    int mGlobalPositionSet = 0;

    ImageView imgRefreshiconID;
    ImageView imgBluetoothiconID;


    CardView cardViewAddDynamicViewID;
    private boolean mBLTCheckValue = false;

    private RelativeLayout rlvGetAllViewID, rlvSetAllViewID;

    TextView noDataFound;

    DatabaseHelper databaseHelper;

    Toolbar toolbar;
    StringBuilder sb = new StringBuilder();
    String value="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_setting);


        mSettingParameterResponse = new ArrayList<>();

        mEditTextList = new ArrayList<>();
        mTextViewSetIDtList = new ArrayList<>();
        databaseHelper = new DatabaseHelper(this);

        rlvGetAllViewID = findViewById(R.id.rlvGetAllViewID);
        rlvSetAllViewID = findViewById(R.id.rlvSetAllViewID);
        imgBluetoothiconID = (ImageView) findViewById(R.id.imgBluetoothiconID);
        noDataFound = findViewById(R.id.noDataFound);
        mContext = this;
        mActivity = this;
        try {
            myVersionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            mobilemodel = Build.MODEL;
            androidVersion =  Build.VERSION.RELEASE;

            Log.e("VERSION_App",androidVersion);
            Log.e("VERSION_NAME", myVersionName);
            Log.e("MOBILE_MODEL", mobilemodel);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        initView();

    }

    private void initView() {
        lvlMainParentLayoutID = (LinearLayout) findViewById(R.id.lvlMainParentLayoutID);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.getOverflowIcon().setColorFilter(Color.WHITE , PorterDuff.Mode.SRC_ATOP);
        imgBluetoothiconID.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                //finish();
                if (mBLTCheckValue) {
                    mBLTCheckValue = false;
                    imgBluetoothiconID.setImageResource(R.drawable.ic_bluetooth_gray);
                } else {
                    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (mBluetoothAdapter.isEnabled()) {
                        if (AllPopupUtil.pairedDeviceListGloable(mContext)) {

                            if (WebURL.BT_DEVICE_NAME.equalsIgnoreCase("") || WebURL.BT_DEVICE_NAME.equalsIgnoreCase(null) || WebURL.BT_DEVICE_MAC_ADDRESS.equalsIgnoreCase("") || WebURL.BT_DEVICE_MAC_ADDRESS.equalsIgnoreCase(null)) {
                                Intent intent = new Intent(mContext, PairedDeviceActivity.class);
                                mContext.startActivity(intent);
                            }
                        } else {
                            mContext.startActivity(new Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS));
                        }
                    } else {
                        mContext.startActivity(new Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS));
                    }

                    mBLTCheckValue = true;
                    imgBluetoothiconID.setImageResource(R.drawable.ic_bluetooth_blue);
                }
            }
        });

        imgBluetoothiconID.setVisibility(View.VISIBLE);
        rlvGetAllViewID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGlobalPosition = 0;
                mReadAllCounterValue = 0;

                String sspp = mSettingParameterResponse.get(mReadAllCounterValue).getOffset() + "";

                if (!sspp.equalsIgnoreCase("") && !sspp.equalsIgnoreCase("0") && !sspp.equalsIgnoreCase("0.0")) {
                    edtValueFloat = Float.parseFloat(mSettingParameterResponse.get(mReadAllCounterValue).getOffset() + "");
                } else {
                    edtValueFloat = 1;
                }

                char[] datar = new char[4];
                int a = Float.floatToIntBits((float) edtValueFloat);
                datar[0] = (char) (a & 0x000000FF);
                datar[1] = (char) ((a & 0x0000FF00) >> 8);
                datar[2] = (char) ((a & 0x00FF0000) >> 16);
                datar[3] = (char) ((a & 0xFF000000) >> 24);
                int crc = CRC16_MODBUS(datar, 4);
                char reciverbyte1 = (char) ((crc >> 8) & 0x00FF);
                char reciverbyte2 = (char) (crc & 0x00FF);

                mCRCFinalValue = (char) (reciverbyte1 + reciverbyte2);

                String v1 = String.format("%02x", (0xff & datar[0]));
                String v2 = String.format("%02x", (0xff & datar[1])); //String v2 =Integer.toHexString(datar[1]);
                String v3 = String.format("%02x", (0xff & datar[2]));
                String v4 = String.format("%02x", (0xff & datar[3]));
                String v5 = Integer.toHexString(mCRCFinalValue);

                String mMOBADDRESS = "";
                String mMobADR = mSettingParameterResponse.get(0).getMobBTAddress();
                if (!mMobADR.equalsIgnoreCase("")) {
                    int mLenth = mMobADR.length();
                    if (mLenth == 1) {
                        mMOBADDRESS = "000" + mMobADR;
                    } else if (mLenth == 2) {
                        mMOBADDRESS = "00" + mMobADR;
                    }
                    if (mLenth == 3) {
                        mMOBADDRESS = "0" + mMobADR;
                    } else {
                        mMOBADDRESS = mMobADR;
                    }
                } else {
                    Toast.makeText(mContext, "MOB address not found!", Toast.LENGTH_SHORT).show();
                }
                String modeBusCommand = "0103" + mMOBADDRESS + v1 + v2 + v3 + v4 + v5;//write

                System.out.println("mTotalTime==>>vvv=>>offset==>>" + edtValueFloat);
                System.out.println("mTotalTime==>>vvv=>>Read_Input==>>" + modeBusCommand);
                mTotalTimeFloatData = 0;
                new BluetoothCommunicationForDynamicParameterReadAll().execute(modeBusCommand, modeBusCommand, "OK");
            }
        });

        rlvSetAllViewID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGlobalPositionSet = 0;
                mWriteAllCounterValue = 0;
                if (mSettingParameterResponse.size() > 0) {
                    Log.e("mWriteAllCounterValue", String.valueOf(mWriteAllCounterValue));
                    try {

                        String mStringCeck = mEditTextList.get(mWriteAllCounterValue).getText().toString().trim();

                        System.out.println("Vikas!@#==>>" + mStringCeck);

                        if (!mStringCeck.equalsIgnoreCase("") && !mStringCeck.equalsIgnoreCase("0.0")) {
                            edtValueFloat = Float.parseFloat(mEditTextList.get(mWriteAllCounterValue).getText().toString().trim());
                        } else {
                            edtValueFloat = Float.parseFloat(mSettingParameterResponse.get(mWriteAllCounterValue).getOffset() + "");
                        }
                        {
                            counterValue = 0;
                            char[] datar = new char[4];
                            int a = Float.floatToIntBits((float) edtValueFloat);
                            datar[0] = (char) (a & 0x000000FF);
                            datar[1] = (char) ((a & 0x0000FF00) >> 8);
                            datar[2] = (char) ((a & 0x00FF0000) >> 16);
                            datar[3] = (char) ((a & 0xFF000000) >> 24);
                            int crc = CRC16_MODBUS(datar, 4);
                            char reciverbyte1 = (char) ((crc >> 8) & 0x00FF);
                            char reciverbyte2 = (char) (crc & 0x00FF);
                            mCRCFinalValue = (char) (reciverbyte1 + reciverbyte2);
                            String v1 = String.format("%02x", (0xff & datar[0]));
                            String v2 = String.format("%02x", (0xff & datar[1])); //String v2 =Integer.toHexString(datar[1]);
                            String v3 = String.format("%02x", (0xff & datar[2]));
                            String v4 = String.format("%02x", (0xff & datar[3]));
                            String v5 = Integer.toHexString(mCRCFinalValue);
                            String mMOBADDRESS = "";
                            String mMobADR = mSettingParameterResponse.get(mWriteAllCounterValue).getMobBTAddress();
                            if (!mMobADR.isEmpty()) {
                                int mLenth = mMobADR.length();
                                if (mLenth == 1) {
                                    mMOBADDRESS = "000" + mMobADR;
                                } else if (mLenth == 2) {
                                    mMOBADDRESS = "00" + mMobADR;
                                }
                                if (mLenth == 3) {
                                    mMOBADDRESS = "0" + mMobADR;
                                } else {
                                    mMOBADDRESS = mMobADR;
                                }
                                String modeBusCommand = "0106" + mMOBADDRESS + v1 + v2 + v3 + v4 + v5;//write
                                System.out.println("mTotalTime==>>vvv==>> " + modeBusCommand);

                                new BluetoothCommunicationForDynamicParameterWriteAll().execute(modeBusCommand, modeBusCommand, "OK");

                            } else {
                                Toast.makeText(mContext, "MOB address not found!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }

                } else {
                    CustomUtility.ShowToast(getResources().getString(R.string.somethingWentWrong), DeviceSettingActivity.this);
                }
            }
        });

        if (AllPopupUtil.isOnline(getApplicationContext())) {
            callgetCompalinAllListAPI();
        } else {

           CustomUtility.ShowToast(getResources().getString(R.string.netConnection), DeviceSettingActivity.this);
            noDataFound.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_sync_offline:
                if (AllPopupUtil.isOnline(getApplicationContext())) {

                    Log.e("DatabaseCount", String.valueOf(databaseHelper.getRecordCount()));
                    if(databaseHelper.getRecordCount()>0) {
                        syncOfflineData();
                    }else {
                        CustomUtility.ShowToast(getResources().getString(R.string.pleasesetdatafirst),getApplicationContext());
                    }
                } else {
                    CustomUtility.ShowToast(getResources().getString(R.string.netConnection), getApplicationContext());
                }
               break;
            case android.R.id.home:
                return true;
            case R.id.action_deviceOnOff:
                      Intent intent = new Intent(getApplicationContext(),DeviceOnOffActivity.class);
                      startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void syncOfflineData() {
        showProgressDialogue();
        ArrayList<MotorParamListModel.Response> motorPumpList = new ArrayList<MotorParamListModel.Response>();
        motorPumpList = databaseHelper.getRecordDetails();
        if (motorPumpList.size() > 0) {
            jsonArray = new JSONArray();
            for (int i = 0; i < motorPumpList.size(); i++) {
                JSONObject jsonObj = new JSONObject();
                try {
                    jsonObj.put("pmId", motorPumpList.get(i).getPmId());
                    jsonObj.put("parametersName", motorPumpList.get(i).getParametersName());
                    jsonObj.put("pValue", motorPumpList.get(i).getpValue());
                    jsonArray.put(jsonObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        JSONObject jsonObject = new JSONObject();
        try {
            //input your API parameters
            jsonObject.put("phoneDeviceID", Settings.Secure.getString(getContentResolver(),
                    Settings.Secure.ANDROID_ID).toString().trim());
            jsonObject.put("DeviceSerialNumber", CustomUtility.getSharedPreferences(getApplicationContext(), Constants.SerialNumber));
            jsonObject.put("AppVersion", myVersionName);
            jsonObject.put("AndroidVersion", androidVersion);
            jsonObject.put("MobileModel", mobilemodel);
            jsonObject.put("Username",CustomUtility.getSharedPreferences(getApplicationContext(),Constants.UserName));
            jsonObject.put("MobileNo",CustomUtility.getSharedPreferences(getApplicationContext(),Constants.MobileNo));
            jsonObject.put("response", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("jsonObject", jsonObject.toString());

        RequestQueue queue = Volley.newRequestQueue(DeviceSettingActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, CustomUtility.getSharedPreferences(this,WebURL.BaseUrl)+WebURL.syncOfflineData, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hiddeProgressDialogue();
                        databaseHelper.deleteDatabase();
                        Log.e("String Response : ", response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                error.toString();
            }
        });

        // below line is to make
        // a json object request.
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,  // maxNumRetries = 0 means no retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjectRequest);

    }



    public void callgetCompalinAllListAPI() {
        showProgressDialogue();
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        // String Request initialized

        Log.e("MOTOR_Par_URL=====>",CustomUtility.getSharedPreferences(this,WebURL.BaseUrl)+WebURL.MOTOR_PERSMETER_LIST + CustomUtility.getSharedPreferences(getApplicationContext(), Constants.MaterialPumpCode));

        //StringRequest mStringRequest = new StringRequest(Request.Method.GET, CustomUtility.getSharedPreferences(this,WebURL.BaseUrl)+WebURL.MOTOR_PERSMETER_LIST + CustomUtility.getSharedPreferences(getApplicationContext(), Constants.MaterialPumpCode), new Response.Listener<String>() {
        StringRequest mStringRequest = new StringRequest(Request.Method.GET, CustomUtility.getSharedPreferences(this,WebURL.BaseUrl)+WebURL.MOTOR_PERSMETER_LIST + CustomUtility.getSharedPreferences(getApplicationContext(), Constants.MaterialPumpCode) , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                if (!response.isEmpty()) {
                    hiddeProgressDialogue();
                    MotorParamListModel motorParamListModel = new Gson().fromJson(response.toString(), MotorParamListModel.class);
                    if (motorParamListModel.getStatus().equals("true")) {

                        mSettingParameterResponse = motorParamListModel.getResponse();
                        addDynamicViewProNew(mSettingParameterResponse);
                        noDataFound.setVisibility(View.GONE);

                        /*if (String.valueOf(databaseHelper.getRecordCount()).equals("0")) {
                            for (int i = 0; i < motorParamListModel.getResponse().size(); i++) {
                                databaseHelper.insertRecordAlternate(String.valueOf(motorParamListModel.getResponse().get(i).getPmId()),
                                        motorParamListModel.getResponse().get(i).getParametersName(),
                                        motorParamListModel.getResponse().get(i).getModbusaddress(),
                                        motorParamListModel.getResponse().get(i).getMobBTAddress(),
                                        String.valueOf(motorParamListModel.getResponse().get(i).getFactor()),
                                        String.valueOf(motorParamListModel.getResponse().get(i).getpValue()),
                                        motorParamListModel.getResponse().get(i).getMaterialCode(),
                                        motorParamListModel.getResponse().get(i).getUnit(),
                                        String.valueOf(motorParamListModel.getResponse().get(i).getOffset()));
                            }
                        }*/

                    } else {
                        hiddeProgressDialogue();
                        noDataFound.setVisibility(View.VISIBLE);
                    }

                } else {
                    hiddeProgressDialogue();
                    noDataFound.setVisibility(View.VISIBLE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Error :" + error.toString());
                noDataFound.setVisibility(View.VISIBLE);
                hiddeProgressDialogue();
            }
        });
        mStringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,  // maxNumRetries = 0 means no retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(mStringRequest);
    }

    @Override
    public void onDestroy() {
        try {
            progressDialog.dismiss();

            if (progressDialog != null)
                progressDialog = null;

        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    private void addDynamicViewProNew(final List<MotorParamListModel.Response> mSettingParameterResponse) {
        try {
            if (mEditTextList.size() > 0) {
                mEditTextList.clear();
            }
            if (mTextViewSetIDtList.size() > 0) {
                mTextViewSetIDtList.clear();
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    lvlMainParentLayoutID.removeAllViews();
                }
            });


        } catch (Exception exp) {
            exp.printStackTrace();
        }

        for (i = 0; i < mSettingParameterResponse.size(); i++) {

            cardViewAddDynamicViewID = new CardView(this);
            CardView.LayoutParams cardViewAddDynamicViewIDoutparams12 = new CardView.LayoutParams
                    ((int) CardView.LayoutParams.MATCH_PARENT, (int) CardView.LayoutParams.WRAP_CONTENT);
            cardViewAddDynamicViewIDoutparams12.setMarginEnd((int) getResources().getDimension(R.dimen._1sdp));
            cardViewAddDynamicViewIDoutparams12.setMarginStart((int) getResources().getDimension(R.dimen._1sdp));
            cardViewAddDynamicViewIDoutparams12.setMargins((int) getResources().getDimension(R.dimen._1sdp), (int) getResources().getDimension(R.dimen._1sdp), (int) getResources().getDimension(R.dimen._1sdp), (int) getResources().getDimension(R.dimen._1sdp));
            cardViewAddDynamicViewID.setCardBackgroundColor(getResources().getColor(R.color.white));
            cardViewAddDynamicViewID.setRadius(15);
            cardViewAddDynamicViewID.setElevation(5);
            cardViewAddDynamicViewID.setLayoutParams(cardViewAddDynamicViewIDoutparams12);

            rlvMainDynamicViewID = new RelativeLayout(this);
            RelativeLayout.LayoutParams rlvMainDynamicViewIDParam = new RelativeLayout.LayoutParams
                    ((int) RelativeLayout.LayoutParams.WRAP_CONTENT, (int) getResources().getDimension(R.dimen._50sdp));

            rlvMainDynamicViewIDParam.setMargins((int) getResources().getDimension(R.dimen._1sdp), (int) getResources().getDimension(R.dimen._1sdp), (int) getResources().getDimension(R.dimen._1sdp), (int) getResources().getDimension(R.dimen._1sdp));
            rlvMainDynamicViewIDParam.addRule(RelativeLayout.ALIGN_PARENT_START);
            rlvMainDynamicViewID.setLayoutParams(rlvMainDynamicViewIDParam);

            iv_sub_linearlayout12 = new RelativeLayout(this);
            RelativeLayout.LayoutParams iv_outparams12 = new RelativeLayout.LayoutParams((int) RelativeLayout.LayoutParams.MATCH_PARENT, (int) RelativeLayout.LayoutParams.MATCH_PARENT);
            iv_outparams12.setMarginEnd((int) getResources().getDimension(R.dimen._1sdp));
            iv_outparams12.setMarginStart((int) getResources().getDimension(R.dimen._1sdp));
            iv_outparams12.setMargins((int) getResources().getDimension(R.dimen._1sdp), (int) getResources().getDimension(R.dimen._1sdp), (int) getResources().getDimension(R.dimen._1sdp), (int) getResources().getDimension(R.dimen._1sdp));
            iv_sub_linearlayout12.setBackgroundColor(getResources().getColor(R.color.white));

            iv_sub_linearlayout12.setLayoutParams(iv_outparams12);

            TextView txtPeraNameID = new TextView(this);
            RelativeLayout.LayoutParams txtFromTextHeadParam = new RelativeLayout.LayoutParams
                    ((int) RelativeLayout.LayoutParams.WRAP_CONTENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT);

            txtFromTextHeadParam.addRule(RelativeLayout.CENTER_HORIZONTAL);
            txtPeraNameID.setGravity(Gravity.CENTER_HORIZONTAL);

            txtPeraNameID.setText(mSettingParameterResponse.get(i).getParametersName());
            txtPeraNameID.setTextSize((int) getResources().getDimension(R.dimen._4ssp));
            txtPeraNameID.setId(i + 1);
            txtPeraNameID.setTypeface(null, Typeface.BOLD);
            txtPeraNameID.setTextColor(getResources().getColor(R.color.black));
            txtPeraNameID.setLayoutParams(txtFromTextHeadParam);
            iv_sub_linearlayout12.addView(txtPeraNameID);

            RelativeLayout rlvMainViewLayoutIN = new RelativeLayout(this);
            RelativeLayout.LayoutParams rlvMainParamIN = new RelativeLayout.LayoutParams
                    ((int) RelativeLayout.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen._45sdp));
            rlvMainParamIN.setMargins((int) getResources().getDimension(R.dimen._1sdp), (int) getResources().getDimension(R.dimen._1sdp), (int) getResources().getDimension(R.dimen._1sdp), (int) getResources().getDimension(R.dimen._1sdp));
            rlvMainParamIN.addRule(RelativeLayout.BELOW, txtPeraNameID.getId());
            rlvMainViewLayoutIN.setLayoutParams(rlvMainParamIN);
            iv_sub_linearlayout12.addView(rlvMainViewLayoutIN);

            TextView txtGetID = new TextView(this);
            RelativeLayout.LayoutParams txtGetIDParam = new RelativeLayout.LayoutParams
                    ((int) (int) getResources().getDimension(R.dimen._60sdp), (int) getResources().getDimension(R.dimen._45sdp));

            txtGetIDParam.addRule(RelativeLayout.ALIGN_PARENT_START);
            txtGetID.setGravity(Gravity.CENTER);
            txtGetID.setText(getResources().getString(R.string.Get_text));
            txtGetID.setTextSize((int) getResources().getDimension(R.dimen._6ssp));
            txtGetID.setId(i + 2);
            txtGetID.setTypeface(null, Typeface.BOLD);

            txtGetID.setTextColor(getResources().getColor(R.color.white));
            txtGetID.setBackground(getResources().getDrawable(R.drawable.rounded_shape));
            txtGetID.setLayoutParams(txtGetIDParam);
            rlvMainViewLayoutIN.addView(txtGetID);

            txtGetID.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mBLTCheckValue) {
                        int iii = v.getId();
                        int pp = iii - 2;
                        mGlobalPosition = pp;

                        String spsp = mSettingParameterResponse.get(pp).getOffset() + "";
                        if (!spsp.equalsIgnoreCase("") && !spsp.equalsIgnoreCase("0") && !spsp.equalsIgnoreCase("0.0")) {
                            edtValueFloat = Float.parseFloat(mSettingParameterResponse.get(pp).getOffset() + "");
                        } else {
                            edtValueFloat = 1;
                        }

                        System.out.println("mTotalTime==>>vvv=offset=>>" + edtValueFloat);

                        char[] datar = new char[4];
                        int a = Float.floatToIntBits((float) edtValueFloat);
                        //  int a= (int) edtValueFloat;
                        datar[0] = (char) (a & 0x000000FF);
                        datar[1] = (char) ((a & 0x0000FF00) >> 8);
                        datar[2] = (char) ((a & 0x00FF0000) >> 16);
                        datar[3] = (char) ((a & 0xFF000000) >> 24);
                        int crc = CRC16_MODBUS(datar, 4);
                        char reciverbyte1 = (char) ((crc >> 8) & 0x00FF);
                        char reciverbyte2 = (char) (crc & 0x00FF);

                        mCRCFinalValue = (char) (reciverbyte1 + reciverbyte2);

                        String v1 = String.format("%02x", (0xff & datar[0]));
                        String v2 = String.format("%02x", (0xff & datar[1])); //String v2 =Integer.toHexString(datar[1]);
                        String v3 = String.format("%02x", (0xff & datar[2]));
                        String v4 = String.format("%02x", (0xff & datar[3]));
                        String v5 = Integer.toHexString(mCRCFinalValue);

                        String mMOBADDRESS = "";
                        String mMobADR = mSettingParameterResponse.get(pp).getMobBTAddress();
                        if (!mMobADR.equalsIgnoreCase("")) {
                            int mLenth = mMobADR.length();
                            if (mLenth == 1) {
                                mMOBADDRESS = "000" + mMobADR;
                            } else if (mLenth == 2) {
                                mMOBADDRESS = "00" + mMobADR;
                            } else if (mLenth == 3) {
                                mMOBADDRESS = "0" + mMobADR;
                            } else {
                                mMOBADDRESS = mMobADR;
                            }
                        } else {
                            Toast.makeText(mContext, "MOB address not found!", Toast.LENGTH_SHORT).show();
                        }
                        final String modeBusCommand = "0103" + mMOBADDRESS + v1 + v2 + v3 + v4 + v5;//write
                        System.out.println("mTotalTime==>>vvv=modeBusCommand=>>" + modeBusCommand);
                        mTotalTimeFloatData = 0;

                        // baseRequest.showLoader();
                        new Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        new BluetoothCommunicationForDynamicParameterRead().execute(modeBusCommand, modeBusCommand, "OK");
                                    }
                                }, 3000);
                    } else {
                        mBLTCheckValue = true;
                        imgBluetoothiconID.setImageResource(R.drawable.ic_bluetooth_blue);

                        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                        if (mBluetoothAdapter.isEnabled()) {
                            if (AllPopupUtil.pairedDeviceListGloable(mContext)) {

                                if (WebURL.BT_DEVICE_NAME.equalsIgnoreCase("") || WebURL.BT_DEVICE_NAME.equalsIgnoreCase(null) || WebURL.BT_DEVICE_MAC_ADDRESS.equalsIgnoreCase("") || WebURL.BT_DEVICE_MAC_ADDRESS.equalsIgnoreCase(null)) {
                                    Intent intent = new Intent(mContext, PairedDeviceActivity.class);
                                    mContext.startActivity(intent);
                                } else {
                                    int iii = v.getId();
                                    int pp = iii - 2;
                                    mGlobalPosition = pp;

                                    String spsp = mSettingParameterResponse.get(pp).getOffset() + "";
                                    if (!spsp.equalsIgnoreCase("") && !spsp.equalsIgnoreCase("0") && !spsp.equalsIgnoreCase("0.0")) {
                                        edtValueFloat = Float.parseFloat(mSettingParameterResponse.get(pp).getOffset() + "");
                                    } else {
                                        edtValueFloat = 1;
                                    }

                                    System.out.println("mTotalTime==>>vvv=offset=>>" + edtValueFloat);

                                    char[] datar = new char[4];
                                    int a = Float.floatToIntBits((float) edtValueFloat);
                                    //  int a= (int) edtValueFloat;
                                    datar[0] = (char) (a & 0x000000FF);
                                    datar[1] = (char) ((a & 0x0000FF00) >> 8);
                                    datar[2] = (char) ((a & 0x00FF0000) >> 16);
                                    datar[3] = (char) ((a & 0xFF000000) >> 24);
                                    int crc = CRC16_MODBUS(datar, 4);
                                    char reciverbyte1 = (char) ((crc >> 8) & 0x00FF);
                                    char reciverbyte2 = (char) (crc & 0x00FF);

                                    mCRCFinalValue = (char) (reciverbyte1 + reciverbyte2);

                                    String v1 = String.format("%02x", (0xff & datar[0]));
                                    String v2 = String.format("%02x", (0xff & datar[1])); //String v2 =Integer.toHexString(datar[1]);
                                    String v3 = String.format("%02x", (0xff & datar[2]));
                                    String v4 = String.format("%02x", (0xff & datar[3]));
                                    String v5 = Integer.toHexString(mCRCFinalValue);

                                    String mMOBADDRESS = "";
                                    //  String mMobADR = mSettingParameterResponse.get(pp).getModbusaddress();
                                    String mMobADR = mSettingParameterResponse.get(pp).getMobBTAddress();
                                    if (!mMobADR.equalsIgnoreCase("")) {
                                        int mLenth = mMobADR.length();
                                        if (mLenth == 1) {
                                            mMOBADDRESS = "000" + mMobADR;
                                        } else if (mLenth == 2) {
                                            mMOBADDRESS = "00" + mMobADR;
                                        } else if (mLenth == 3) {
                                            mMOBADDRESS = "0" + mMobADR;
                                        } else {
                                            mMOBADDRESS = mMobADR;
                                        }
                                    } else {
                                        Toast.makeText(mContext, "MOB address not found!", Toast.LENGTH_SHORT).show();
                                    }
                                    final String modeBusCommand = "0103" + mMOBADDRESS + v1 + v2 + v3 + v4 + v5;//write
                                    //  String modeBusCommand = "0103"+mSettingParameterResponse.get(pp).getMobBTAddress()+v1+v2+v3+v4+v5;//write
                                    System.out.println("mTotalTime==>>vvv=modeBusCommand=>>" + modeBusCommand);
                                    mTotalTimeFloatData = 0;

                                    //  baseRequest.showLoader();
                                    new Handler().postDelayed(
                                            new Runnable() {
                                                public void run() {
                                                    new BluetoothCommunicationForDynamicParameterRead().execute(modeBusCommand, modeBusCommand, "OK");
                                                    //Log.i("tag","A Kiss after 5 seconds");
                                                }
                                            }, 100);
                                }
                            } else {
                                mContext.startActivity(new Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS));
                            }
                        } else {
                            mContext.startActivity(new Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS));
                        }

                    }

                }
            });

            TextView txtSetID = new TextView(this);
            RelativeLayout.LayoutParams txtSetIDParam = new RelativeLayout.LayoutParams
                    ((int) getResources().getDimension(R.dimen._60sdp), (int) getResources().getDimension(R.dimen._45sdp));
            txtSetIDParam.addRule(RelativeLayout.ALIGN_PARENT_END);
            txtSetID.setGravity(Gravity.CENTER);
            txtSetID.setText(getResources().getString(R.string.Set_text));
            txtSetID.setTextSize((int) getResources().getDimension(R.dimen._6ssp));
            txtSetID.setId(i + 3);
            txtSetID.setTypeface(null, Typeface.BOLD);
            txtSetID.setTextColor(getResources().getColor(R.color.white));
            txtSetID.setBackground(getResources().getDrawable(R.drawable.rounded_shape));
            txtSetID.setLayoutParams(txtSetIDParam);
            mTextViewSetIDtList.add(txtSetID);
            String sssss = String.valueOf(mSettingParameterResponse.get(i).getpValue());

            if (!sssss.equalsIgnoreCase("0.0") && String.valueOf(mSettingParameterResponse.get(i).getpValue()) != null && !String.valueOf(mSettingParameterResponse.get(i).getpValue()).equalsIgnoreCase("")) {
                changeButtonVisibility(true, 1.0f, mTextViewSetIDtList.get(i));
            } else {
                changeButtonVisibility(false, 0.5f, mTextViewSetIDtList.get(i));
            }
            rlvMainViewLayoutIN.addView(txtSetID);
            //yo leno hai
            txtSetID.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mBLTCheckValue) {
                        try {
                            int iii = v.getId();
                            int pp = iii - 3;
                            mGlobalPosition = pp;
                            String mStringCeck = mEditTextList.get(pp).getText().toString().trim();
                            if (!mStringCeck.equalsIgnoreCase("") && !mStringCeck.equalsIgnoreCase("0.0")) {
                                edtValueFloat = Float.parseFloat(mEditTextList.get(pp).getText().toString().trim());
                            } else {
                                edtValueFloat = Float.parseFloat(mSettingParameterResponse.get(pp).getOffset() + "");
                            }
                            // edtValueFloat = Float.parseFloat(mEditTextList.get(pp).getText().toString().trim());
                            //  edtValue = Integer.parseInt(mEditTextList.get(pp).getText().toString().trim());
                            //   if (edtValueFloat >= mSettingParameterResponse.get(pp).getPMin() && edtValueFloat <= mSettingParameterResponse.get(pp).getPMax())
                            {
                                /*  setDeviceMode(mSettingModelResponse.get(position).getAddress());*/
                                //changeButtonVisibility(true, 1.0f, holder);
                                char[] datar = new char[4];
                                // int a=Float.floatToIntBits((float) edtValue);
                                int a = Float.floatToIntBits((float) edtValueFloat);
                                datar[0] = (char) (a & 0x000000FF);
                                datar[1] = (char) ((a & 0x0000FF00) >> 8);
                                datar[2] = (char) ((a & 0x00FF0000) >> 16);
                                datar[3] = (char) ((a & 0xFF000000) >> 24);
                                int crc = CRC16_MODBUS(datar, 4);
                                char reciverbyte1 = (char) ((crc >> 8) & 0x00FF);
                                char reciverbyte2 = (char) (crc & 0x00FF);
                                mCRCFinalValue = (char) (reciverbyte1 + reciverbyte2);
                                String v1 = String.format("%02x", (0xff & datar[0]));
                                String v2 = String.format("%02x", (0xff & datar[1])); //String v2 =Integer.toHexString(datar[1]);
                                String v3 = String.format("%02x", (0xff & datar[2]));
                                String v4 = String.format("%02x", (0xff & datar[3]));
                                String v5 = Integer.toHexString(mCRCFinalValue);
                                String mMOBADDRESS = "";
                                //  String mMobADR = mSettingParameterResponse.get(pp).getModbusaddress();
                                String mMobADR = mSettingParameterResponse.get(pp).getMobBTAddress();
                                if (!mMobADR.equalsIgnoreCase("")) {
                                    int mLenth = mMobADR.length();
                                    if (mLenth == 1) {
                                        mMOBADDRESS = "000" + mMobADR;
                                    } else if (mLenth == 2) {
                                        mMOBADDRESS = "00" + mMobADR;
                                    }
                                    if (mLenth == 3) {
                                        mMOBADDRESS = "0" + mMobADR;
                                    } else {
                                        mMOBADDRESS = mMobADR;
                                    }
                                    String modeBusCommand = "0106" + mMOBADDRESS + v1 + v2 + v3 + v4 + v5;//write
                                    System.out.println("mTotalTime==>>vvvSet==>> " + modeBusCommand);
                                    //  String modeBusCommand1 = "0103"+mSettingModelResponse.get(position).getMobBTAddress()+""+"crc";
                                    new BluetoothCommunicationForDynamicParameterWrite().execute(modeBusCommand, modeBusCommand, "OK");
                                } else {
                                    Toast.makeText(mContext, "MOB address not found!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    } else {
                        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                        if (mBluetoothAdapter.isEnabled()) {
                            if (AllPopupUtil.pairedDeviceListGloable(mContext)) {

                                if (WebURL.BT_DEVICE_NAME.equalsIgnoreCase("") || WebURL.BT_DEVICE_NAME.equalsIgnoreCase(null) || WebURL.BT_DEVICE_MAC_ADDRESS.equalsIgnoreCase("") || WebURL.BT_DEVICE_MAC_ADDRESS.equalsIgnoreCase(null)) {
                                    Intent intent = new Intent(mContext, PairedDeviceActivity.class);
                                    mContext.startActivity(intent);
                                } else {
                                    try {
                                        int iii = v.getId();
                                        int pp = iii - 3;
                                        mGlobalPosition = pp;
                                        //Toast.makeText(mContext, "jai hooo...==>>  "+pp, Toast.LENGTH_SHORT).show();
                                        String mStringCeck = mEditTextList.get(pp).getText().toString().trim();
                                        if (!mStringCeck.equalsIgnoreCase("") && !mStringCeck.equalsIgnoreCase("0.0")) {
                                            edtValueFloat = Float.parseFloat(mEditTextList.get(pp).getText().toString().trim());
                                        } else {
                                            edtValueFloat = Float.parseFloat(mSettingParameterResponse.get(pp).getOffset() + "");
                                        }
                                        {
                                            char[] datar = new char[4];
                                            int a = Float.floatToIntBits((float) edtValueFloat);
                                            datar[0] = (char) (a & 0x000000FF);
                                            datar[1] = (char) ((a & 0x0000FF00) >> 8);
                                            datar[2] = (char) ((a & 0x00FF0000) >> 16);
                                            datar[3] = (char) ((a & 0xFF000000) >> 24);
                                            int crc = CRC16_MODBUS(datar, 4);
                                            char reciverbyte1 = (char) ((crc >> 8) & 0x00FF);
                                            char reciverbyte2 = (char) (crc & 0x00FF);
                                            mCRCFinalValue = (char) (reciverbyte1 + reciverbyte2);
                                            String v1 = String.format("%02x", (0xff & datar[0]));
                                            String v2 = String.format("%02x", (0xff & datar[1])); //String v2 =Integer.toHexString(datar[1]);
                                            String v3 = String.format("%02x", (0xff & datar[2]));
                                            String v4 = String.format("%02x", (0xff & datar[3]));
                                            String v5 = Integer.toHexString(mCRCFinalValue);
                                            String mMOBADDRESS = "";
                                            String mMobADR = mSettingParameterResponse.get(pp).getMobBTAddress();
                                            if (!mMobADR.equalsIgnoreCase("")) {
                                                int mLenth = mMobADR.length();
                                                if (mLenth == 1) {
                                                    mMOBADDRESS = "000" + mMobADR;
                                                } else if (mLenth == 2) {
                                                    mMOBADDRESS = "00" + mMobADR;
                                                }
                                                if (mLenth == 3) {
                                                    mMOBADDRESS = "0" + mMobADR;
                                                } else {
                                                    mMOBADDRESS = mMobADR;
                                                }
                                                String modeBusCommand = "0106" + mMOBADDRESS + v1 + v2 + v3 + v4 + v5;//write
                                                System.out.println("mTotalTime==>>vvv==>> " + modeBusCommand);
                                                //  String modeBusCommand1 = "0103"+mSettingModelResponse.get(position).getMobBTAddress()+""+"crc";
                                                new BluetoothCommunicationForDynamicParameterWrite().execute(modeBusCommand, modeBusCommand, "OK");
                                            } else {
                                                Toast.makeText(mContext, "MOB address not found!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                mContext.startActivity(new Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS));
                            }
                        } else {
                            mContext.startActivity(new Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS));
                        }

                        mBLTCheckValue = true;
                        imgBluetoothiconID.setImageResource(R.drawable.ic_bluetooth_blue);
                    }
                }
            });
            RelativeLayout rlvEDITLayout = new RelativeLayout(this);
            RelativeLayout.LayoutParams rlvEDITParam = new RelativeLayout.LayoutParams
                    ((int) RelativeLayout.LayoutParams.MATCH_PARENT, (int) (int) getResources().getDimension(R.dimen._45sdp));
            rlvEDITParam.setMarginStart((int) getResources().getDimension(R.dimen._61sdp));
            rlvEDITParam.setMarginEnd((int) getResources().getDimension(R.dimen._61sdp));
            rlvEDITParam.addRule(RelativeLayout.CENTER_IN_PARENT);
            rlvEDITLayout.setLayoutParams(rlvEDITParam);

            rlvEDITLayout.setBackground(getResources().getDrawable(R.drawable.gray_round_corner));
            rlvMainViewLayoutIN.addView(rlvEDITLayout);
            //iv_sub_linearlayout12.addView(rlvEDITLayout);

            EditText edtValueID = new EditText(this);
            RelativeLayout.LayoutParams edtValueIDParam = new RelativeLayout.LayoutParams
                    ((int) RelativeLayout.LayoutParams.MATCH_PARENT, (int) RelativeLayout.LayoutParams.MATCH_PARENT);
            edtValueIDParam.addRule(RelativeLayout.ALIGN_PARENT_START);
            edtValueID.setGravity(Gravity.CENTER_VERTICAL);
            String ssssss = String.valueOf(mSettingParameterResponse.get(i).getpValue() * mSettingParameterResponse.get(i).getFactor());
            edtValueID.setText(ssssss);

            edtValueID.setTextColor(getResources().getColor(R.color.black));
            edtValueID.setBackgroundColor(getResources().getColor(R.color.black));
            edtValueID.setTextSize((int) getResources().getDimension(R.dimen._4ssp));
            edtValueID.setId(i + 4);
            edtValueID.setMaxLines(1);
            edtValueID.setInputType(InputType.TYPE_CLASS_NUMBER);
            edtValueID.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
            edtValueID.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
            edtValueID.setTypeface(null, Typeface.NORMAL);
            edtValueID.setTextColor(getResources().getColor(R.color.white));
            edtValueID.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            edtValueID.setLayoutParams(edtValueIDParam);

            rlvEDITLayout.addView(edtValueID);

            mEditTextList.add(edtValueID);

            rlvMainDynamicViewID.addView(iv_sub_linearlayout12);
            cardViewAddDynamicViewID.addView(rlvMainDynamicViewID);
            lvlMainParentLayoutID.addView(cardViewAddDynamicViewID);

        }

    }


    @SuppressLint("StaticFieldLeak")
    private class BluetoothCommunicationForDynamicParameterReadAll extends AsyncTask<String, Void, Boolean>  // UI thread
    {
        public int RetryCount = 0;
        int bytesRead = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
                   showProgressDialogue();
            mMyUDID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        }

        @SuppressLint("MissingPermission")
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Boolean doInBackground(String... requests) //while the progress dialog is shown, the connection is done in background
        {
            try {

                if (btSocket != null) {
                    if (!btSocket.isConnected()) {
                        btSocket.connect();//start connection
                    }
                } else {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device

                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(WebURL.BT_DEVICE_MAC_ADDRESS);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createRfcommSocketToServiceRecord(mMyUDID);//create a RFCOMM (SPP) connection
                    myBluetooth.cancelDiscovery();

                    if (!btSocket.isConnected()) {
                        btSocket.connect();//start connection
                    }
                }

                if (btSocket.isConnected()) {
                    byte[] STARTRequest = requests[0].getBytes(StandardCharsets.US_ASCII);

                    try {
                        btSocket.getOutputStream().write(STARTRequest);
                        sleep(1000);
                        iStream = btSocket.getInputStream();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }

                    int[] bytesReaded = new int[4];
                    int mTotalTime;

                    int jjj = 0;
                    for (int i = 0; i < 1; i++) {
                        try {
                             int mCharOne = iStream.read();
                            int mCharTwo = iStream.read();
                            bytesReaded[i] = Integer.parseInt("" + (char) mCharOne + (char) mCharTwo, 16);
                            mCharOne = iStream.read();
                            mCharTwo = iStream.read();
                            bytesReaded[i + 1] = Integer.parseInt("" + (char) mCharOne + (char) mCharTwo, 16);
                            mCharOne = iStream.read();
                            mCharTwo = iStream.read();
                            bytesReaded[i + 2] = Integer.parseInt("" + (char) mCharOne + (char) mCharTwo, 16);
                            mCharOne = iStream.read();
                            mCharTwo = iStream.read();
                            bytesReaded[i + 3] = Integer.parseInt("" + (char) mCharOne + (char) mCharTwo, 16);
                            mTotalTime = bytesReaded[i];
                            System.out.println("mTotalTime==>>vvv1  " + jjj + " " + mTotalTime);
                            mTotalTime |= bytesReaded[i + 1] << 8;
                            System.out.println("mTotalTime==>>vvv2  " + jjj + " " + mTotalTime);
                            mTotalTime |= bytesReaded[i + 2] << 16;
                            System.out.println("mTotalTime==>>vvv3  " + jjj + " " + mTotalTime);
                            mTotalTime |= bytesReaded[i + 3] << 24;
                            System.out.println("mTotalTime==>>vvv4  " + jjj + " " + mTotalTime);
                            mTotalTimeFloatData = 0;
                            mTotalTimeFloatData = Float.intBitsToFloat(mTotalTime);

                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mSettingParameterResponse.get(mReadAllCounterValue).setpValue((float) mTotalTimeFloatData);
                                     mEditTextList.get(mReadAllCounterValue).setText("" + mTotalTimeFloatData);
                                    System.out.println("mGlobalPosition==>>" + mReadAllCounterValue + "\nmTotalTimeFloatData==>>" + mTotalTimeFloatData);
                                    changeButtonVisibility(true, 1.0f, mTextViewSetIDtList.get(mReadAllCounterValue));

                                    DatabaseRecordInsert(mSettingParameterResponse.get(mReadAllCounterValue), mEditTextList.get(mReadAllCounterValue));

                                }
                            });

                            jjj++;


                        } catch (IOException e) {
                            hiddeProgressDialogue();
                            e.printStackTrace();
                        }
                    }
                    while (iStream.available()>0){
                        iStream.read();
                    }
                }
            } catch (Exception e) {
                //baseRequest.hideLoader();
                e.printStackTrace();
                return false;
            }

            //  baseRequest.hideLoader();
            return false;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Boolean result) //after the doInBackground, it checks if everything went fine
        {
            // baseRequest.hideLoader();
            super.onPostExecute(result);
               hiddeProgressDialogue();
            mReadAllCounterValue = mReadAllCounterValue + 1;

            if (mReadAllCounterValue < mSettingParameterResponse.size()) {

                String sspp = mSettingParameterResponse.get(mReadAllCounterValue).getOffset() + "";

                if (!sspp.equalsIgnoreCase("") && !sspp.equalsIgnoreCase("0") && !sspp.equalsIgnoreCase("0.0")) {
                    edtValueFloat = Float.parseFloat(mSettingParameterResponse.get(mReadAllCounterValue).getOffset() + "");
                } else {
                    edtValueFloat = 1;
                }

                char[] datar = new char[4];
                int a = Float.floatToIntBits((float) edtValueFloat);
                // int a= (int) edtValueFloat;
                datar[0] = (char) (a & 0x000000FF);
                datar[1] = (char) ((a & 0x0000FF00) >> 8);
                datar[2] = (char) ((a & 0x00FF0000) >> 16);
                datar[3] = (char) ((a & 0xFF000000) >> 24);
                int crc = CRC16_MODBUS(datar, 4);
                char reciverbyte1 = (char) ((crc >> 8) & 0x00FF);
                char reciverbyte2 = (char) (crc & 0x00FF);

                mCRCFinalValue = (char) (reciverbyte1 + reciverbyte2);

                String v1 = String.format("%02x", (0xff & datar[0]));
                String v2 = String.format("%02x", (0xff & datar[1])); //String v2 =Integer.toHexString(datar[1]);
                String v3 = String.format("%02x", (0xff & datar[2]));
                String v4 = String.format("%02x", (0xff & datar[3]));
                String v5 = Integer.toHexString(mCRCFinalValue);

                String mMOBADDRESS = "";
                String mMobADR = mSettingParameterResponse.get(0).getMobBTAddress();
                if (!mMobADR.equalsIgnoreCase("")) {
                    int mLenth = mMobADR.length();
                    if (mLenth == 1) {
                        mMOBADDRESS = "000" + mMobADR;
                    } else if (mLenth == 2) {
                        mMOBADDRESS = "00" + mMobADR;
                    }
                    if (mLenth == 3) {
                        mMOBADDRESS = "0" + mMobADR;
                    } else {
                        mMOBADDRESS = mMobADR;
                    }
                } else {
                    Toast.makeText(mContext, "MOB address not found!", Toast.LENGTH_SHORT).show();
                }
                String modeBusCommand = "0103" + mMOBADDRESS + v1 + v2 + v3 + v4 + v5;//write

                //  String modeBusCommand = "0103"+mSettingModelResponse.get(pp).getMobBTAddress()+v1+v2+v3+v4+v5;//write
                System.out.println("mTotalTime==>>vvv=>>offset==>>" + edtValueFloat);
                System.out.println("mTotalTime==>>vvv=>>Read_Input==>>" + modeBusCommand);
                mTotalTimeFloatData = 0;
                new BluetoothCommunicationForDynamicParameterReadAll().execute(modeBusCommand, modeBusCommand, "OK");

            }

        }
    }

    private void DatabaseRecordInsert(MotorParamListModel.Response response, EditText pValue) {
        databaseHelper.insertRecordAlternate(String.valueOf(response.getPmId()),
                response.getParametersName(),
                response.getModbusaddress(),
                response.getMobBTAddress(),
                String.valueOf(response.getFactor()),
                pValue.getText().toString(),
                response.getMaterialCode(),
                response.getUnit(),
                String.valueOf(response.getOffset()));
    }

    /////////////////////////bt read write
    @SuppressLint("StaticFieldLeak")
    private class BluetoothCommunicationForDynamicParameterWrite extends AsyncTask<String, Void, Boolean>  // UI thread
    {
        public int RetryCount = 0;
        int bytesRead = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        showProgressDialogue();
            mMyUDID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

        }

        @SuppressLint("MissingPermission")
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Boolean doInBackground(String... requests) //while the progress dialog is shown, the connection is done in background
        {
            try {
                if (btSocket != null) {
                    if (btSocket.isConnected()) {

                    }
                } else {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(WebURL.BT_DEVICE_MAC_ADDRESS);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createRfcommSocketToServiceRecord(mMyUDID);//create a RFCOMM (SPP) connection
                    myBluetooth.cancelDiscovery();

                }

                if (!btSocket.isConnected())
                    btSocket.connect();//start connection


                if (btSocket.isConnected()) {
                    byte[] STARTRequest = requests[0].getBytes(StandardCharsets.US_ASCII);

                    try {
                        btSocket.getOutputStream().write(STARTRequest);
                        sleep(1000);
                        iStream = btSocket.getInputStream();
                    } catch (InterruptedException e1) {

                        e1.printStackTrace();
                    }


                    int[] bytesReaded = new int[4];


                    int mTotalTime;

                    int jjj = 0;

                    for (int i = 0; i < 1; i++) {
                        try {

                            int mCharOne = iStream.read();
                            int mCharTwo = iStream.read();
                            bytesReaded[i] = Integer.parseInt("" + (char) mCharOne + (char) mCharTwo, 16);
                            mCharOne = iStream.read();
                            mCharTwo = iStream.read();
                            bytesReaded[i + 1] = Integer.parseInt("" + (char) mCharOne + (char) mCharTwo, 16);
                            mCharOne = iStream.read();
                            mCharTwo = iStream.read();
                            bytesReaded[i + 2] = Integer.parseInt("" + (char) mCharOne + (char) mCharTwo, 16);
                            mCharOne = iStream.read();
                            mCharTwo = iStream.read();
                            bytesReaded[i + 3] = Integer.parseInt("" + (char) mCharOne + (char) mCharTwo, 16);

                            mTotalTime = bytesReaded[i];
                            System.out.println("mTotalTime==>>vvv1  " + jjj + " " + mTotalTime);
                            mTotalTime |= bytesReaded[i + 1] << 8;
                            System.out.println("mTotalTime==>>vvv2  " + jjj + " " + mTotalTime);
                            mTotalTime |= bytesReaded[i + 2] << 16;
                            System.out.println("mTotalTime==>>vvv3  " + jjj + " " + mTotalTime);
                            mTotalTime |= bytesReaded[i + 3] << 24;

                            System.out.println("mTotalTime==>>vvv4  " + jjj + " " + Float.intBitsToFloat(mTotalTime));
                            mTotalTimeFloatData = 0;
                            mTotalTimeFloatData = Float.intBitsToFloat(mTotalTime);
                            // mTotalTimeFloatData = (float) mTotalTime;

                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mSettingParameterResponse.get(mGlobalPosition).setpValue((float) mTotalTimeFloatData);
                                   // mEditTextList.get(mGlobalPosition).setText("" + mTotalTimeFloatData);

                                    Log.e("mGlobalPosition", String.valueOf(mGlobalPosition));
                                    Log.e("mEditTextList", mEditTextList.get(mGlobalPosition).getText().toString());
                                     DatabaseRecordInsert(mSettingParameterResponse.get(mGlobalPosition), mEditTextList.get(mGlobalPosition));
                                }
                            });

                            jjj++;



                        } catch (IOException e) {
                          hiddeProgressDialogue();
                            e.printStackTrace();
                        }
                    }

                    while (iStream.available()>0){
                        iStream.read();
                    }
                }
            } catch (Exception e) {

                return false;
            }


            return false;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Boolean result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);
            hiddeProgressDialogue();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class BluetoothCommunicationForDynamicParameterWriteAll extends AsyncTask<String, Void, Boolean>  // UI thread
    {
        public int RetryCount = 0;
        int bytesRead = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialogue();
            mMyUDID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

        }

        @SuppressLint("MissingPermission")
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Boolean doInBackground(String... requests) //while the progress dialog is shown, the connection is done in background
        {
            try {
                if (btSocket != null) {
                    if (btSocket.isConnected()) {

                    }
                } else {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(WebURL.BT_DEVICE_MAC_ADDRESS);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createRfcommSocketToServiceRecord(mMyUDID);//create a RFCOMM (SPP) connection
                    myBluetooth.cancelDiscovery();
                }

                if (!btSocket.isConnected())
                    btSocket.connect();//start connection


                if (btSocket.isConnected()) {
                    byte[] STARTRequest = requests[0].getBytes(StandardCharsets.US_ASCII);

                    try {
                        btSocket.getOutputStream().write(STARTRequest);
                        sleep(1000);

                        iStream = btSocket.getInputStream();
                    } catch (InterruptedException e1) {
                        // baseRequest.hideLoader();
                        e1.printStackTrace();
                    }

                    int[] bytesReaded = new int[4];
                    int mTotalTime;

                    int jjj = 0;

                    for (int i = 0; i < 1; i++) {
                        try {

                            int mCharOne = iStream.read();
                            int mCharTwo = iStream.read();
                            bytesReaded[i] = Integer.parseInt("" + (char) mCharOne + (char) mCharTwo, 16);
                            mCharOne = iStream.read();
                            mCharTwo = iStream.read();
                            bytesReaded[i + 1] = Integer.parseInt("" + (char) mCharOne + (char) mCharTwo, 16);
                            mCharOne = iStream.read();
                            mCharTwo = iStream.read();
                            bytesReaded[i + 2] = Integer.parseInt("" + (char) mCharOne + (char) mCharTwo, 16);
                            mCharOne = iStream.read();
                            mCharTwo = iStream.read();
                            bytesReaded[i + 3] = Integer.parseInt("" + (char) mCharOne + (char) mCharTwo, 16);

                            mTotalTime = bytesReaded[i];
                            System.out.println("mTotalTime==>>vvv1  " + jjj + " " + mTotalTime);
                            mTotalTime |= bytesReaded[i + 1] << 8;
                            System.out.println("mTotalTime==>>vvv2  " + jjj + " " + mTotalTime);
                            mTotalTime |= bytesReaded[i + 2] << 16;
                            System.out.println("mTotalTime==>>vvv3  " + jjj + " " + mTotalTime);
                            mTotalTime |= bytesReaded[i + 3] << 24;

                            System.out.println("mTotalTime==>>vvv4  " + jjj + " " + Float.intBitsToFloat(mTotalTime));
                            System.out.println("mTotalTime==>>vikasVihu==>  " + mWriteAllCounterValue + " " + Float.intBitsToFloat(mTotalTime));

                            mTotalTimeFloatData = 0;
                            mTotalTimeFloatData = Float.intBitsToFloat(mTotalTime);


                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    mSettingParameterResponse.get(mWriteAllCounterValue).setpValue((float) mTotalTimeFloatData);

                                 //   mEditTextList.get(mWriteAllCounterValue).setText("" + mTotalTimeFloatData);
                                     DatabaseRecordInsert(mSettingParameterResponse.get(mWriteAllCounterValue), mEditTextList.get(mWriteAllCounterValue));

                                }
                            });
                            jjj++;



                        } catch (IOException e) {
                            hiddeProgressDialogue();
                            e.printStackTrace();
                        }
                    }

                    while (iStream.available()>0){
                        iStream.read();
                    }
                }
            } catch (Exception e) {

                return false;
            }

            return false;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Boolean result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            mWriteAllCounterValue = mWriteAllCounterValue + 1;
            try {
                hiddeProgressDialogue();
                if (mWriteAllCounterValue < mSettingParameterResponse.size()) {
                      String mStringCeck = mEditTextList.get(mWriteAllCounterValue).getText().toString().trim();
                    System.out.println("Vikas!@#==>>" + mStringCeck);
                    System.out.println("Sumit====>" + mSettingParameterResponse.get(mWriteAllCounterValue).getParametersName());
                    DatabaseRecordInsert(mSettingParameterResponse.get(mWriteAllCounterValue), mEditTextList.get(mWriteAllCounterValue));
                    if (!mStringCeck.equalsIgnoreCase("") && !mStringCeck.equalsIgnoreCase("0.0")) {
                        edtValueFloat = Float.parseFloat(mEditTextList.get(mWriteAllCounterValue).getText().toString().trim());
                    } else {
                        edtValueFloat = Float.parseFloat(mSettingParameterResponse.get(mWriteAllCounterValue).getOffset() + "");
                    }


                    counterValue = 0;
                    char[] datar = new char[4];
                    // int a=Float.floatToIntBits((float) edtValue);
                    int a = Float.floatToIntBits((float) edtValueFloat);
                    datar[0] = (char) (a & 0x000000FF);
                    datar[1] = (char) ((a & 0x0000FF00) >> 8);
                    datar[2] = (char) ((a & 0x00FF0000) >> 16);
                    datar[3] = (char) ((a & 0xFF000000) >> 24);
                    int crc = CRC16_MODBUS(datar, 4);
                    char reciverbyte1 = (char) ((crc >> 8) & 0x00FF);
                    char reciverbyte2 = (char) (crc & 0x00FF);
                    mCRCFinalValue = (char) (reciverbyte1 + reciverbyte2);
                    String v1 = String.format("%02x", (0xff & datar[0]));
                    String v2 = String.format("%02x", (0xff & datar[1])); //String v2 =Integer.toHexString(datar[1]);
                    String v3 = String.format("%02x", (0xff & datar[2]));
                    String v4 = String.format("%02x", (0xff & datar[3]));
                    String v5 = Integer.toHexString(mCRCFinalValue);
                    String mMOBADDRESS = "";
                    String mMobADR = mSettingParameterResponse.get(mWriteAllCounterValue).getMobBTAddress();
                    if (!mMobADR.equalsIgnoreCase("")) {
                        int mLenth = mMobADR.length();
                        if (mLenth == 1) {
                            mMOBADDRESS = "000" + mMobADR;
                        } else if (mLenth == 2) {
                            mMOBADDRESS = "00" + mMobADR;
                        }
                        if (mLenth == 3) {
                            mMOBADDRESS = "0" + mMobADR;
                        } else {
                            mMOBADDRESS = mMobADR;
                        }
                        String modeBusCommand = "0106" + mMOBADDRESS + v1 + v2 + v3 + v4 + v5;//write
                        System.out.println("mTotalTime==>>vvv==>> " + modeBusCommand);

                        //  String modeBusCommand1 = "0103"+mSettingModelResponse.get(position).getMobBTAddress()+""+"crc";
                        new BluetoothCommunicationForDynamicParameterWriteAll().execute(modeBusCommand, modeBusCommand, "OK");

                    } else {
                        Toast.makeText(mContext, "MOB address not found!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    //baseRequest.hideLoader();
                    System.out.println("222");
                }



            } catch (NumberFormatException e) {
                e.printStackTrace();
                System.out.println("111");
            }

        }
    }

    @SuppressLint("StaticFieldLeak")
    private class BluetoothCommunicationForDynamicParameterRead extends AsyncTask<String, Void, Boolean>  // UI thread
    {
        public int RetryCount = 0;
        int bytesRead = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mMyUDID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
          showProgressDialogue();
        }

        @SuppressLint("MissingPermission")
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Boolean doInBackground(String... requests) //while the progress dialog is shown, the connection is done in background
        {
            try {
                if (btSocket != null) {
                    if (!btSocket.isConnected()) {
                        btSocket.connect();//start connection
                    }
                } else {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(WebURL.BT_DEVICE_MAC_ADDRESS);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createRfcommSocketToServiceRecord(mMyUDID);//create a RFCOMM (SPP) connection
                    myBluetooth.cancelDiscovery();

                    if (!btSocket.isConnected()) {
                        btSocket.connect();//start connection
                    }
                }
                if (btSocket.isConnected()) {
                    byte[] STARTRequest = requests[0].getBytes(StandardCharsets.US_ASCII);

                    try {
                        btSocket.getOutputStream().write(STARTRequest);
                        sleep(100);

                        iStream = btSocket.getInputStream();
                    } catch (InterruptedException e1) {
                        // baseRequest.hideLoader();
                        e1.printStackTrace();
                    }

                    int[] bytesReaded = new int[4];


                    int mTotalTime;
                    int jjj = 0;

                   // String value = getCharArrayFromStream(iStream);
                     //Log.e("value====>",value);

                    for (int i = 0; i < 1; i++) {
                        try {
                            for (int j=0; j<6; j++){

                                int mCharOne1 = iStream.read();
                               // int mCharTwo = iStream.read();

                                Log.e("istream====>","" + (char) mCharOne1 );
                            }

                            int mCharOne = iStream.read();
                            int mCharTwo = iStream.read();
                            bytesReaded[i] = Integer.parseInt("" + (char) mCharOne +""+ (char) mCharTwo, 16);
                            mCharOne = iStream.read();
                            mCharTwo = iStream.read();
                            bytesReaded[i + 1] = Integer.parseInt("" + (char) mCharOne +""+ (char) mCharTwo, 16);
                            mCharOne = iStream.read();
                            mCharTwo = iStream.read();
                            bytesReaded[i + 2] = Integer.parseInt("" + (char) mCharOne +""+ (char) mCharTwo, 16);
                            mCharOne = iStream.read();
                            mCharTwo = iStream.read();
                            bytesReaded[i + 3] = Integer.parseInt("" + (char) mCharOne +""+ (char) mCharTwo, 16);


                            Log.e("byteread0====>", String.valueOf(bytesReaded[i])
                                    +"byteread1====>"+String.valueOf(bytesReaded[i+1])
                                    +"byteread2====>"+String.valueOf(bytesReaded[i+2])
                                    +"byteread3====>"+String.valueOf(bytesReaded[i+3]));
                            mTotalTime = bytesReaded[i];
                            System.out.println("mTotalTime==>>vvv1  " + jjj + " " + mTotalTime);
                            mTotalTime |= bytesReaded[i + 1] << 8;
                            System.out.println("mTotalTime==>>vvv2  " + jjj + " " + mTotalTime);
                            mTotalTime |= bytesReaded[i + 2] << 16;
                            System.out.println("mTotalTime==>>vvv3  " + jjj + " " + mTotalTime);
                            mTotalTime |= bytesReaded[i + 3] << 24;
                            System.out.println("mTotalTime==>>vvv4  " + jjj + " " + mTotalTime);


                            mTotalTimeFloatData = 0;
                            mTotalTimeFloatData = Float.intBitsToFloat(mTotalTime);
                            mActivity.runOnUiThread(new Runnable() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void run() {
                                    try {
                                        mSettingParameterResponse.get(mGlobalPosition).setpValue((float) mTotalTimeFloatData);
                                        Float fgfg;
                                        if ((mSettingParameterResponse.get(mGlobalPosition).getOffset() != 0) || (mSettingParameterResponse.get(mGlobalPosition).getOffset() != 0.0)) {
                                            fgfg = mTotalTimeFloatData;

                                        } else {
                                            fgfg = mTotalTimeFloatData;
                                        }
                                        Log.e("fgfg=====>", String.valueOf(fgfg));

                                        mEditTextList.get(mGlobalPosition).setText("" + fgfg);
                                        System.out.println("mGlobalPosition==>>" + mGlobalPosition + "\nmTotalTimeFloatData==>>" + mTotalTimeFloatData);
                                        changeButtonVisibility(true, 1.0f, mTextViewSetIDtList.get(mGlobalPosition));
                                        Log.e("medit_txt=====>", String.valueOf( mEditTextList.get(mGlobalPosition)));
                                        DatabaseRecordInsert(mSettingParameterResponse.get(mGlobalPosition), mEditTextList.get(mGlobalPosition));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                            jjj++;

                            for (int ii = 0; ii < 4; ii++) {
                                int mCharOne11 = iStream.read();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
                hiddeProgressDialogue();
                return false;
            }

            return false;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Boolean result) //after the doInBackground, it checks if everything went fine
        {

            super.onPostExecute(result);
            hiddeProgressDialogue();
        }
    }


    public static int CRC16_MODBUS(char[] buf, int len) {

        int crc = 0xFFFF;
        int pos = 0, i = 0;
        for (pos = 0; pos < len; pos++) {
            crc ^= (int) buf[pos];    // XOR byte into least sig. byte of crc

            for (i = 8; i != 0; i--) {    // Loop over each bit
                if ((crc & 0x0001) != 0) {      // If the LSB is set
                    crc >>= 1;                    // Shift right and XOR 0xA001
                    crc ^= 0xA001;
                } else                            // Else LSB is not set
                    crc >>= 1;                    // Just shift right
            }
        }

        return crc;
    }

    private void changeButtonVisibility(boolean state, float alphaRate, TextView text) {
        text.setEnabled(state);
        text.setAlpha(alphaRate);
    }

    private void hiddeProgressDialogue() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(progressDialog!=null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        });
    }

    private void showProgressDialogue() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog = new ProgressDialog(DeviceSettingActivity.this);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Sync Data Offline....");
                progressDialog.show();
            }
        });
    }

}
