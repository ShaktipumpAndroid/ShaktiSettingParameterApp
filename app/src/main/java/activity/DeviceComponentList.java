package activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.vihaan.shaktinewconcept.R;

import java.util.ArrayList;
import java.util.List;

import activity.BeanVk.MotorParamListModel;
import activity.utility.CustomUtility;
import adapter.ComAdapter;
import webservice.AllPopupUtil;
import webservice.WebURL;


public class DeviceComponentList extends AppCompatActivity implements ComAdapter.ItemclickListner {

    private RecyclerView componentList;
    TextView noDataFound;
    ComAdapter comAdapter;
    private ProgressDialog progressDialog;
    private List<MotorParamListModel.Response> mSettingParameterResponse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_component_list);

        inti();
    }

    private void inti() {

        componentList = findViewById(R.id.componentList);
        noDataFound = findViewById(R.id.noDataFound);
        mSettingParameterResponse = new ArrayList<>();

        if (AllPopupUtil.isOnline(getApplicationContext())) {
            callgetCompalinAllListAPI();
        } else {
            CustomUtility.ShowToast(getResources().getString(R.string.netConnection), DeviceComponentList.this);
             noDataFound.setVisibility(View.VISIBLE);
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

    private void setAdapter() {

        if (mSettingParameterResponse != null && mSettingParameterResponse.size() > 0) {
          //  Log.e("mSettingParameterResponse", String.valueOf(mSettingParameterResponse.size()));
            comAdapter = new ComAdapter(DeviceComponentList.this, mSettingParameterResponse, noDataFound);
            componentList.setHasFixedSize(true);
            componentList.setAdapter(comAdapter);
            //comAdapter.EditItemClick(this);
            noDataFound.setVisibility(View.GONE);
            componentList.setVisibility(View.VISIBLE);
        } else {
            noDataFound.setVisibility(View.VISIBLE);
            componentList.setVisibility(View.GONE);
        }

    }

    @Override
    public void itemClick(MotorParamListModel.Response response, String plantId) {

    }

    private void showProgressDialogue() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog = new ProgressDialog(DeviceComponentList.this);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Loading List....");
                progressDialog.show();
            }
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
}