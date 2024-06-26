package activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.vihaan.shaktinewconcept.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import Database.DatabaseHelper;
import activity.BeanVk.MotorParamListModel;
import activity.utility.CustomUtility;
import adapter.ComAdapter;
import webservice.AllPopupUtil;
import webservice.WebURL;


public class DeviceComponentList extends AppCompatActivity implements ComAdapter.ItemclickListner {

    private RecyclerView componentList;
    DatabaseHelper databaseHelper;
    private Context mContext ;
    TextView noDataFound;
    ComAdapter comAdapter;
    ImageView imgBluetoothiconID;
    private boolean mBLTCheckValue = false;
    private ProgressDialog progressDialog;
    private List<MotorParamListModel.Response> mSettingParameterResponse;

    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_component_list);

        inti();
    }

    private void inti() {

        componentList = findViewById(R.id.componentList);
        noDataFound = findViewById(R.id.noDataFound);
        imgBluetoothiconID = (ImageView) findViewById(R.id.imgBluetoothiconID);
        mSettingParameterResponse = new ArrayList<>();
        databaseHelper = new DatabaseHelper(this);
        mContext = this;

        Log.e("Status", String.valueOf(mBLTCheckValue));


            getPairedDeviceList();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!WebURL.BT_DEVICE_NAME.isEmpty()){
            if (AllPopupUtil.isOnline(getApplicationContext())) {
                callgetCompalinAllListAPI();
            } else {
                CustomUtility.ShowToast(getResources().getString(R.string.netConnection), DeviceComponentList.this);
                noDataFound.setVisibility(View.VISIBLE);
            }
        }
    }

    private void getPairedDeviceList() {

        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.device_does_not_support_bluetooth), Toast.LENGTH_SHORT).show();
        } else {
            if(bluetoothAdapter.isEnabled()) {
                Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
                if (pairedDevices.size() > 0) {
                    pairedDeviceList = new ArrayList<>();
                    for (BluetoothDevice device : pairedDevices) {
                        pairedDeviceList.add(new PairDeviceModel(device.getName(), device.getAddress(), false));
                    }
                    bluetoothDeviceList.setVisibility(View.VISIBLE);
                    textPairedDevice.setVisibility(View.VISIBLE);
                    pairBtn.setVisibility(View.VISIBLE);
                    bluetoothState.setVisibility(View.GONE);
                    PairedDeviceAdapter pairedDeviceAdapter = new PairedDeviceAdapter(this, pairedDeviceList);
                    bluetoothDeviceList.setAdapter(pairedDeviceAdapter);
                    pairedDeviceAdapter.deviceSelection(this);
                } else {
                    textPairedDevice.setVisibility(View.GONE);
                    pairBtn.setVisibility(View.GONE);
                    bluetoothDeviceOff(getResources().getString(R.string.no_paired_device));
                }
            }else {
                bluetoothDeviceOff(getResources().getString(R.string.bluetooth_is_off));
            }

        }
    }

    public void callgetCompalinAllListAPI() {
        showProgressDialogue();
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        // String Request initialized
        Log.e("MOTOR_Par_URL=====>",CustomUtility.getSharedPreferences(this, WebURL.BaseUrl)+WebURL.MOTOR_PERSMETER_LIST + "9500001875");

        //StringRequest mStringRequest = new StringRequest(Request.Method.GET, CustomUtility.getSharedPreferences(this,WebURL.BaseUrl)+WebURL.MOTOR_PERSMETER_LIST + CustomUtility.getSharedPreferences(getApplicationContext(), Constants.MaterialPumpCode), new Response.Listener<String>() {
        StringRequest mStringRequest = new StringRequest(Request.Method.GET, CustomUtility.getSharedPreferences(this,WebURL.BaseUrl)+WebURL.MOTOR_PERSMETER_LIST + "9500001875" , response -> {

            if (!response.isEmpty()) {
                hiddeProgressDialogue();
                MotorParamListModel motorParamListModel = new Gson().fromJson(response, MotorParamListModel.class);
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
          //  Log.i(TAG, "Error :" + error.toString());
            noDataFound.setVisibility(View.VISIBLE);
             hiddeProgressDialogue();
        });
        mStringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,  // maxNumRetries = 0 means no retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(mStringRequest);
    }

    private void insertDataInLocal(List<MotorParamListModel.Response> mSettingParameterResponse) {

        for (int i = 0 ; i < mSettingParameterResponse.size(); i++){
            DatabaseRecordInsert(mSettingParameterResponse.get(i),"");
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

        if (mSettingParameterResponse != null && mSettingParameterResponse.size() > 0) {
          //  Log.e("mSettingParameterResponse", String.valueOf(mSettingParameterResponse.size()));
            comAdapter = new ComAdapter(DeviceComponentList.this, mSettingParameterResponse, noDataFound);
            componentList.setHasFixedSize(true);
            componentList.setAdapter(comAdapter);
            comAdapter.EditItemClick(this);
            noDataFound.setVisibility(View.GONE);
            componentList.setVisibility(View.VISIBLE);
        } else {
            noDataFound.setVisibility(View.VISIBLE);
            componentList.setVisibility(View.GONE);
        }

    }



    private void showProgressDialogue() {
        runOnUiThread(() -> {
            progressDialog = new ProgressDialog(DeviceComponentList.this);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading List....");
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
    public void getBtnMethod(MotorParamListModel.Response response, int plantId) {

    }

    @Override
    public void setBtnMethod(MotorParamListModel.Response response, int plantId) {

    }
}