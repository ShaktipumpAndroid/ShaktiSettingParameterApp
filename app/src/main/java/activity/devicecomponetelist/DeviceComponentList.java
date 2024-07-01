package activity.devicecomponetelist;

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
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
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
import java.util.UUID;

import Database.DatabaseHelper;
import activity.BeanVk.MotorParamListModel;
import activity.DeviceOnOffActivity;
import activity.utility.CustomUtility;
import adapter.ComAdapter;
import webservice.AllPopupUtil;
import webservice.Constants;
import webservice.WebURL;

//reference of DeviceSettingActivity
//Get All data from drive code is not added.
public class DeviceComponentList extends AppCompatActivity implements ComAdapter.ItemclickListner, View.OnClickListener {

    private RecyclerView componentList;
    int mGlobalPosition = 0,mGlobalPositionSet = 0, mWriteAllCounterValue = 0,counterValue = 0;
    String myVersionName, mobileModel, androidVersion;
    DatabaseHelper databaseHelper;
    private Context mContext ;
    private float edtValueFloat = 0;
    TextView noDataFound;
    float mTotalTimeFloatData;
    private Activity mActivity;
    private UUID mMyUDID;
    private RelativeLayout  rlvSetAllViewID;
    Toolbar toolbar;
    ComAdapter comAdapter;
    int selected_pos = 0;
    char mCRCFinalValue;
    ImageView imgBluetoothiconID;
    private BluetoothAdapter myBluetooth;
    private ProgressDialog progressDialog;
    private BluetoothSocket btSocket;
    private List<MotorParamListModel.Response> mSettingParameterResponse;
    private InputStream iStream;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_component_list);

        inIt();
        listener();

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
                    CustomUtility.ShowToast("Please check your internet connection!", getApplicationContext());
                }
                return true;

            case R.id.action_deviceOnOff:
                Intent intent = new Intent(getApplicationContext(), DeviceOnOffActivity.class);
                startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void syncOfflineData() {
        JSONArray jsonArray = null;

        showProgressDialogue(getResources().getString(R.string.action_sync_offline));
        ArrayList<MotorParamListModel.Response> motorPumpList;
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
            jsonObject.put("mobileModel", mobileModel);
            jsonObject.put("Username",CustomUtility.getSharedPreferences(getApplicationContext(),Constants.UserName));
            jsonObject.put("MobileNo",CustomUtility.getSharedPreferences(getApplicationContext(),Constants.MobileNo));
            jsonObject.put("response", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("jsonObject", jsonObject.toString());

        RequestQueue queue = Volley.newRequestQueue(DeviceComponentList.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, CustomUtility.getSharedPreferences(this,WebURL.BaseUrl)+WebURL.syncOfflineData, jsonObject,
                response -> {

                    Log.e("String Response : ", response.toString());
                    try {
                        if(response.getString("status").equalsIgnoreCase("true")){
                            CustomUtility.ShowToast(getResources().getString(R.string.datasaved), DeviceComponentList.this);
                          //  databaseHelper.deleteDatabase();
                        }else {
                            CustomUtility.ShowToast(response.getString("message").toString(), DeviceComponentList.this);
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    hiddeProgressDialogue();
                }, error -> {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    error.toString();
                });

        // below line is to make
        // a json object request.
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,  // maxNumRetries = 0 means no retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjectRequest);

    }

    private void listener() {
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        rlvSetAllViewID.setOnClickListener(this);
    }

    private void inIt() {

        mActivity = this;
        toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        componentList = findViewById(R.id.componentList);
        noDataFound = findViewById(R.id.noDataFound);
        imgBluetoothiconID = (ImageView) findViewById(R.id.imgBluetoothiconID);
        rlvSetAllViewID = findViewById(R.id.rlvSetAllViewID);
        mSettingParameterResponse = new ArrayList<>();
        databaseHelper = new DatabaseHelper(this);
        mContext = this;

            if (AllPopupUtil.isOnline(getApplicationContext())) {
                callgetCompalinAllListAPI();
            } else {
                  offlineList();
            }

            getDeviceDetail();
    }

    private void getDeviceDetail() {

        try {
            myVersionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            mobileModel = Build.MODEL;
            androidVersion =  Build.VERSION.RELEASE;

            Log.e("VERSION_App",androidVersion);
            Log.e("VERSION_NAME", myVersionName);
            Log.e("MOBILE_MODEL", mobileModel);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void offlineList() {
        mSettingParameterResponse = databaseHelper.getRecordDetails();
        setAdapter();
    }

    public void callgetCompalinAllListAPI() {
        showProgressDialogue(getResources().getString(R.string.getComData));
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        // String Request initialized
        Log.e("MOTOR_Par_URL=====>",CustomUtility.getSharedPreferences(this, WebURL.BaseUrl)+WebURL.MOTOR_PERSMETER_LIST + "9500001875");

        //StringRequest mStringRequest = new StringRequest(Request.Method.GET, CustomUtility.getSharedPreferences(this,WebURL.BaseUrl)+WebURL.MOTOR_PERSMETER_LIST + CustomUtility.getSharedPreferences(getApplicationContext(), Constants.MaterialPumpCode), new Response.Listener<String>() {
        StringRequest mStringRequest = new StringRequest(Request.Method.GET, CustomUtility.getSharedPreferences(this,WebURL.BaseUrl)+WebURL.MOTOR_PERSMETER_LIST + "9500001875" , response -> {
            Log.e("response===>", String.valueOf(response.toString()));
            if (!response.isEmpty()) {
                hiddeProgressDialogue();
                MotorParamListModel motorParamListModel = new Gson().fromJson(response, MotorParamListModel.class);
                Log.e("response===>", String.valueOf(motorParamListModel.getStatus().equals("true")));
                if (motorParamListModel.getStatus().equals("true")) {

                    mSettingParameterResponse = motorParamListModel.getResponse();
                    insertDataInLocal(mSettingParameterResponse);
                    Log.e("size", String.valueOf(mSettingParameterResponse.size()));

                    setAdapter();

                } else {
                     hiddeProgressDialogue();
                    noDataFound.setVisibility(View.VISIBLE);
                }

            } else {
                 hiddeProgressDialogue();
                noDataFound.setVisibility(View.VISIBLE);
            }

        }, error -> {
             hiddeProgressDialogue();
             noDataFound.setVisibility(View.VISIBLE);
        });

        mStringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,  // maxNumRetries = 0 means no retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(mStringRequest);
    }

    private void insertDataInLocal(List<MotorParamListModel.Response> mSettingParameterResponse) {
        databaseHelper.deleteDatabase();
        for (int i = 0 ; i < mSettingParameterResponse.size(); i++){
            DatabaseRecordInsert(mSettingParameterResponse.get(i), String.valueOf(mSettingParameterResponse.get(i).getpValue()*mSettingParameterResponse.get(i).getFactor()));
        }
    }

    private void DatabaseRecordInsert(MotorParamListModel.Response response, String pValue) {
        databaseHelper.insertRecordAlternate(String.valueOf(response.getPmId()),
                response.getParametersName(),
                response.getModbusaddress(),
                response.getMobBTAddress(),
                String.valueOf(response.getFactor()),
                pValue.toString(),
                response.getMaterialCode(),
                response.getUnit(),
                String.valueOf(response.getOffset()));
    }

    private void setAdapter() {
        Log.e("Setting","List");
        if (mSettingParameterResponse != null && mSettingParameterResponse.size() > 0) {
            comAdapter = new ComAdapter(DeviceComponentList.this, mSettingParameterResponse, noDataFound);
            componentList.setHasFixedSize(true);
            componentList.setAdapter(comAdapter);
            comAdapter.EditItemClick(this);
            noDataFound.setVisibility(View.GONE);
            componentList.setVisibility(View.VISIBLE);
            Log.e("Set","List");
        } else {
            noDataFound.setVisibility(View.VISIBLE);
            componentList.setVisibility(View.GONE);
        }
    }

    private void showProgressDialogue(String message) {
        runOnUiThread(() -> {
            progressDialog = new ProgressDialog(DeviceComponentList.this);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(message);
            progressDialog.show();
        });
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

    @Override
    public void getBtnMethod(MotorParamListModel.Response response,String editvalue, int position) {

        selected_pos = position;
        showProgressDialogue(getResources().getString(R.string.fetchingData));
        int pos = position;
        mGlobalPosition = position;

        String spsp = mSettingParameterResponse.get(pos).getOffset() + "";
        if (!spsp.equalsIgnoreCase("") && !spsp.equalsIgnoreCase("0") && !spsp.equalsIgnoreCase("0.0")) {
            edtValueFloat = Float.parseFloat(mSettingParameterResponse.get(pos).getOffset() + "");
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
        String mMobADR = mSettingParameterResponse.get(pos).getMobBTAddress();
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
            Toast.makeText(mContext, getResources().getString(R.string.addressnotfound), Toast.LENGTH_SHORT).show();
        }
        final String modeBusCommand = "0103" + mMOBADDRESS + v1 + v2 + v3 + v4 + v5;//write
        System.out.println("mTotalTime==>>get=modeBusCommand=>>" + modeBusCommand);
        mTotalTimeFloatData = 0;

        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        new DeviceComponentList.BluetoothCommunicationForDynamicParameterRead().execute(modeBusCommand, modeBusCommand, "OK");
                    }
                }, 3000);

    }

    @SuppressLint("StaticFieldLeak")
    private class BluetoothCommunicationForDynamicParameterRead extends AsyncTask<String, Void, Boolean>  // UI thread
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
                        sleep(100);

                        iStream = btSocket.getInputStream();
                        System.out.println("iStream==>>iStream  " + iStream + " " );

                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }

                    int[] bytesReaded = new int[4];

                    int mTotalTime;
                    int jjj = 0;

                    for (int i = 0; i < 1; i++) {
                        try {
                            for (int j=0; j<6; j++){

                                int mCharOne1 = iStream.read();
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

                            Log.e("bytesReaded", bytesReaded.toString());

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
                            Log.e("READ=====>", String.valueOf(mTotalTimeFloatData));
                            mActivity.runOnUiThread(new Runnable() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void run() {
                                    try {
                                         ShowAlertResponse("0");

                                        mSettingParameterResponse.get(mGlobalPosition).setpValue((float) mTotalTimeFloatData);
                                        comAdapter.notifyDataSetChanged();
                                        System.out.println("mGlobalPosition==>>" + mGlobalPosition + "\nmTotalTimeFloatData==>>" + mTotalTimeFloatData);
                                        databaseHelper.updateRecordAlternate(mSettingParameterResponse.get(mGlobalPosition));

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                            jjj++;
                            int mCharOne11;
                            //needed to cancel out the extra buffer
                            for (int ii = 0; ii < 4; ii++) {
                                  mCharOne11 = iStream.read();
                                Log.e("mCharOne11===>ii>>>>>", String.valueOf(mCharOne11)+"====="+ii);
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



    @Override
    public void setBtnMethod(MotorParamListModel.Response response,String editvalue, int position) {

        try {
            selected_pos = position;
            int pos = position;
            mGlobalPosition = pos;
            String mStringCeck = editvalue.trim();
            if (!mStringCeck.equalsIgnoreCase("") && !mStringCeck.equalsIgnoreCase("0.0")) {
                edtValueFloat = Float.parseFloat(editvalue);
            } else {
                edtValueFloat = Float.parseFloat(mSettingParameterResponse.get(pos).getOffset() + "");
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
                String mMobADR = mSettingParameterResponse.get(pos).getMobBTAddress();
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
                    new DeviceComponentList.BluetoothCommunicationForDynamicParameterWrite().execute(modeBusCommand, modeBusCommand, "OK");
                } else {
                    Toast.makeText(mContext, getResources().getString(R.string.addressnotfound), Toast.LENGTH_SHORT).show();
                }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class BluetoothCommunicationForDynamicParameterWrite extends AsyncTask<String, Void, Boolean>  // UI thread
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialogue(getResources().getString(R.string.sendingData));
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

                       // Log.e("iStream===>set", String.valueOf(iStream.read()));
                    } catch (InterruptedException e1) {

                        e1.printStackTrace();
                    }


                    int[] bytesReaded = new int[4];


                    int mTotalTime;

                    int jjj = 0;

                    for (int i = 0; i < 1; i++) {
                        try {
                            for (int j=0; j<6; j++){
                                int mCharOne1 = iStream.read();
                                Log.e("istream====>","" + (char) mCharOne1 );
                            }


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

                            System.out.println("byte0==>  " + jjj + " " +   bytesReaded[i]);
                            System.out.println("byte1==>  " + jjj + " " +   bytesReaded[i + 1]);
                            System.out.println("byte2==>  " + jjj + " " +   bytesReaded[i + 2]);
                            System.out.println("byte3=>  " + jjj + " " +   bytesReaded[i + 3]);


                            mTotalTime = bytesReaded[i];
                            System.out.println("mTotalTime==>>vvv1  " + jjj + " " + mTotalTime);
                            mTotalTime |= bytesReaded[i + 1] << 8;
                            System.out.println("mTotalTime==>>vvv2  " + jjj + " " + mTotalTime);
                            mTotalTime |= bytesReaded[i + 2] << 16;
                            System.out.println("mTotalTime==>>vvv3  " + jjj + " " + mTotalTime);
                            mTotalTime |= bytesReaded[i + 3] << 24;

                            mTotalTimeFloatData = 0;
                            mTotalTimeFloatData = Float.intBitsToFloat(mTotalTime);
                            Log.e("Write=====>", String.valueOf(mTotalTimeFloatData));

                            mActivity.runOnUiThread(() -> {

                                if(mTotalTimeFloatData == edtValueFloat ){
                                    ShowAlertResponse("1");
                                    mSettingParameterResponse.get(mGlobalPosition).setisSet(true);
                                } else{
                                    ShowAlertResponse("-1");
                                    mSettingParameterResponse.get(mGlobalPosition).setisSet(false);
                                }

                                mSettingParameterResponse.get(mGlobalPosition).setpValue((float) edtValueFloat);
                                comAdapter.notifyDataSetChanged();
                                databaseHelper.updateRecordAlternate(mSettingParameterResponse.get(selected_pos));


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
    
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rlvSetAllViewID:
                setAllTheComParameter();
                break;
        }
    }

    private void setAllTheComParameter() {
        mGlobalPositionSet = 0;
        mWriteAllCounterValue = 0;
        if (mSettingParameterResponse.size() > 0) {

            try {
                String mStringCeck = mSettingParameterResponse.get(mWriteAllCounterValue).getpValue().toString().trim();

                System.out.println("Vikas!@#==>>" + mStringCeck);

                if (!mStringCeck.equalsIgnoreCase("") && !mStringCeck.equalsIgnoreCase("0.0")) {
                    edtValueFloat = Float.parseFloat(mSettingParameterResponse.get(mWriteAllCounterValue).getpValue().toString().trim());
                } else {
                    edtValueFloat = Float.parseFloat(mSettingParameterResponse.get(mWriteAllCounterValue).getOffset() + "");
                }

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
                //  String mMobADR = mSettingParameterResponse.get(pp).getModbusaddress();
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

                    new DeviceComponentList.BluetoothCommunicationForDynamicParameterWriteAll().execute(modeBusCommand, modeBusCommand, "OK");

                } else {
                    Toast.makeText(mContext, "MOB address not found!", Toast.LENGTH_SHORT).show();
                }

            } catch (NumberFormatException e) {
                e.printStackTrace();
            }


        } else {
            CustomUtility.ShowToast(getResources().getString(R.string.somethingWentWrong), DeviceComponentList.this);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class BluetoothCommunicationForDynamicParameterWriteAll extends AsyncTask<String, Void, Boolean>  // UI thread
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialogue(getResources().getString(R.string.setalldata));
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
                            for (int j=0; j<6; j++){
                                int mCharOne1 = iStream.read();
                                Log.e("istream====>","" + (char) mCharOne1 );
                            }

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

                            Log.e("WriteAll====>", String.valueOf(mTotalTimeFloatData));

                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    if(mTotalTimeFloatData == -1.0){
                                        mSettingParameterResponse.get(mWriteAllCounterValue).setisSet(false);
                                        comAdapter.notifyDataSetChanged();
                                    }
                                    Log.e("edtValueFloat===>", String.valueOf(edtValueFloat));

                                    mSettingParameterResponse.get(mWriteAllCounterValue).setpValue((float) edtValueFloat);
                                    databaseHelper.updateRecordAlternate(mSettingParameterResponse.get(mGlobalPosition));

                                }
                            });
                            jjj++;

                        } catch (IOException e) {
                            hiddeProgressDialogue();
                            e.printStackTrace();
                        }
                    }

                    //needed to cancel out extra buffer
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
                    String mStringCeck =  mSettingParameterResponse.get(mWriteAllCounterValue).getpValue().toString().trim();
                    System.out.println("Vikas!@#==>>" + mStringCeck);
                    System.out.println("Sumit====>" + mSettingParameterResponse.get(mWriteAllCounterValue).getParametersName());

                    databaseHelper.updateRecordAlternate(mSettingParameterResponse.get(mGlobalPosition));

                    if (!mStringCeck.equalsIgnoreCase("") && !mStringCeck.equalsIgnoreCase("0.0")) {
                        edtValueFloat = Float.parseFloat( mSettingParameterResponse.get(mWriteAllCounterValue).getpValue().toString().trim());
                    } else {
                        edtValueFloat = Float.parseFloat(mSettingParameterResponse.get(mWriteAllCounterValue).getOffset() + "");
                    }

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

                        new DeviceComponentList.BluetoothCommunicationForDynamicParameterWriteAll().execute(modeBusCommand, modeBusCommand, "OK");

                    } else {
                        Toast.makeText(mContext, getResources().getString(R.string.addressnotfound), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    System.out.println("222");
                }

                displayNotSetData();

            } catch (NumberFormatException e) {
                e.printStackTrace();
                System.out.println("111");
            }

        }
    }

    private void displayNotSetData() {

    }

    private void ShowAlertResponse(String value) {
        LayoutInflater inflater = (LayoutInflater) DeviceComponentList.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.devicealert,
                null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(DeviceComponentList.this, R.style.MyDialogTheme);

        builder.setView(layout);
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();

        ImageView icon = layout.findViewById(R.id.user_img);
        TextView OK_txt = layout.findViewById(R.id.OK_txt);
        TextView title_txt = layout.findViewById(R.id.title_txt);

        if(value.equals("-1")){
            icon.setImageDrawable(getDrawable(R.drawable.cross));
        }

        if (value.equals("0")) {
            title_txt.setText(getResources().getString(R.string.alertGetMessage));
        } else if(value.equals("1")) {
            title_txt.setText(getResources().getString(R.string.alertSetMessage));
        }else {
            title_txt.setText(getResources().getString(R.string.alertNotSetMessage));
        }

        OK_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }


}
