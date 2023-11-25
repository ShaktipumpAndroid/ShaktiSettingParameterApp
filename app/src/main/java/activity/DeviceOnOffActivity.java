package activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.vihaan.shaktinewconcept.R;

import org.json.JSONException;
import org.json.JSONObject;

import activity.BeanVk.MotorOnOffModel;
import activity.utility.AppController;
import activity.utility.CustomUtility;
import webservice.WebURL;

public class DeviceOnOffActivity extends AppCompatActivity {

    public static String TAG = "DeviceOnOffActivity";
    private Toolbar mToolbar;
    SearchView searchDevice;
    RelativeLayout searchRelative;
    LinearLayout startMotor,stopMotor,startStopLinear;

    EditText ControllerIDExt;
    TextView searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_on_off);

        Init();
        listner();
    }



    private void Init() {
        mToolbar = findViewById(R.id.toolbar);
        ControllerIDExt = findViewById(R.id.ControllerIDExt);
        searchBtn = findViewById(R.id.searchBtn);
        startMotor = findViewById(R.id.startMotor);
        stopMotor = findViewById(R.id.stopMotor);
        startStopLinear= findViewById(R.id.startStopLinear);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.deviceOnOff));

    }

    private void listner() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ControllerIDExt.getText().toString().isEmpty()){
                    CustomUtility.ShowToast("Please Enter Device Controller ID First!",getApplicationContext());

                }else {
                    if(CustomUtility.isInternetOn(DeviceOnOffActivity.this)) {
                        verifyDevice();
                    }else {
                        CustomUtility.ShowToast(getResources().getString(R.string.netConnection),DeviceOnOffActivity.this);
                    }
                }
            }
        });


        startMotor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ControllerIDExt.getText().toString().isEmpty()){
                    CustomUtility.ShowToast("Please Enter Device Controller ID First!",getApplicationContext());
                }else {

                    String deviceType = ControllerIDExt.getText().toString().substring(0,1)+ControllerIDExt.getText().toString().substring(1,2);
                    if(CustomUtility.isInternetOn(DeviceOnOffActivity.this)) {
                        startStopBtn("1", "0", deviceType,true);
                    }else {
                        CustomUtility.ShowToast(getResources().getString(R.string.netConnection),DeviceOnOffActivity.this);
                    }

                }
            }
        });
        stopMotor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ControllerIDExt.getText().toString().isEmpty()){
                    CustomUtility.ShowToast("Please Enter Device Controller ID First!",getApplicationContext());
                }else {
                    String deviceType = ControllerIDExt.getText().toString().substring(0,1)+ControllerIDExt.getText().toString().substring(1,2);
                   if(CustomUtility.isInternetOn(DeviceOnOffActivity.this)) {
                       startStopBtn("0", "1", deviceType,false);

                   }else {
                        CustomUtility.ShowToast(getResources().getString(R.string.netConnection),DeviceOnOffActivity.this);
                    }
                }
            }
        });
    }

    public void verifyDevice(){

        CustomUtility.showProgressDialogue(DeviceOnOffActivity.this);
       StringRequest mStringRequest = new StringRequest(Request.Method.GET, CustomUtility.getSharedPreferences(this,WebURL.BaseUrl)+WebURL.VerifyDeviceID+ControllerIDExt.getText().toString().trim()+"-0", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    if(!response.isEmpty()){
                        CustomUtility.hideProgressDialogue();
                    JSONObject   jsonObject = new JSONObject(response);
                    if(jsonObject.getString("status").equals("true")) {
                        if(jsonObject.getString("onlineStatus").equals("Online")) {
                            startStopLinear.setVisibility(View.VISIBLE);
                        }else {
                            CustomUtility.ShowToast(getResources().getString(R.string.motorOffline),getApplicationContext());
                            startStopLinear.setVisibility(View.GONE);
                        }
                    }else {
                        CustomUtility.ShowToast(getResources().getString(R.string.something_went_wrong),getApplicationContext());
                        startStopLinear.setVisibility(View.GONE);
                    }
                    }
                } catch (JSONException e) {
                  e.printStackTrace();
                    CustomUtility.hideProgressDialogue();
                    CustomUtility.ShowToast(getResources().getString(R.string.something_went_wrong),getApplicationContext());
                    startStopLinear.setVisibility(View.GONE);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Error :" + error.toString());
                CustomUtility.hideProgressDialogue();
                startStopLinear.setVisibility(View.GONE);
                CustomUtility.ShowToast(getResources().getString(R.string.something_went_wrong),getApplicationContext());
            }
        });

        AppController.getInstance().addToRequestQueue(mStringRequest);
        mStringRequest.setShouldCache(false);
        mStringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }




   public void startStopBtn(String data1, String OldData, String deviceType, boolean isMotorStart){
       JSONObject mainObject = new JSONObject();
       try {
           //mainObject.put("user_id", sessionParam.user_id);
           mainObject.put("address1", "501");
           mainObject.put("did1", ControllerIDExt.getText().toString().trim()+"-0");


           mainObject.put("RW", "1");
           mainObject.put("data1", data1);
           mainObject.put("OldData", OldData);
           mainObject.put("UserId", "22");
           mainObject.put("DeviceType", deviceType);
           mainObject.put("offset1", "1");
           mainObject.put("IPAddress", CustomUtility.getDeviceId(getApplicationContext()));

            Log.e("mainObject", String.valueOf(mainObject));
       } catch (
               JSONException e) {
           e.printStackTrace();
       }

          CustomUtility.showProgressDialogue(DeviceOnOffActivity.this);
       JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
               CustomUtility.getSharedPreferences(this,WebURL.BaseUrl)+ WebURL.deviceOnOffAPI, mainObject,
               new Response.Listener<JSONObject>() {
                   @Override
                   public void onResponse(JSONObject response) {
                       Log.d(TAG, "Get Acknow list response=" + response.toString());
                       CustomUtility.hideProgressDialogue();

                       MotorOnOffModel motorOnOffModel = new Gson().fromJson(response.toString(),MotorOnOffModel.class);
                           if (motorOnOffModel.getStatus().equals("true")) {
                              if(motorOnOffModel.getResponse()!=null && !motorOnOffModel.getResponse().isEmpty()){
                                  if(!motorOnOffModel.getResponse().get(0).getResult().equals("Failed")){
                                      if(isMotorStart) {
                                          showProgressDialogue(getResources().getString(R.string.motorStartedSuccessFully));
                                      }else {
                                          showProgressDialogue(getResources().getString(R.string.motorStopSuccessFully));
                                      }
                                  }else {
                                      motorNotRespond(isMotorStart);
                                  }
                              }else {
                                  motorNotRespond(isMotorStart);
                              }
                           } else {
                              motorNotRespond(isMotorStart);

                           }

                   }
               }, new Response.ErrorListener() {

           @Override
           public void onErrorResponse(VolleyError error) {
               VolleyLog.d(TAG, "Error: " + error.getMessage());
               CustomUtility.hideProgressDialogue();

               CustomUtility.ShowToast(getResources().getString(R.string.something_went_wrong),getApplicationContext());
               VolleyLog.d(TAG, "Error: " + error.getMessage());

           }
       });
// Adding request to request queue
       AppController.getInstance().addToRequestQueue(jsonObjReq);
       jsonObjReq.setShouldCache(false);
       jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
               10000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
   }

    private void motorNotRespond(boolean isMotorStart) {
        if(isMotorStart) {
            showProgressDialogue(getResources().getString(R.string.MotorNotStarted));
        }else {
            showProgressDialogue(getResources().getString(R.string.MotorNotStop));
        }

    }

    public void showProgressDialogue(String msg){
       LayoutInflater inflater = (LayoutInflater) DeviceOnOffActivity.this
               .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       View layout = inflater.inflate(R.layout.send_successfully_layout,
               null);
       final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(DeviceOnOffActivity.this, R.style.MyDialogTheme);

       builder.setView(layout);
       builder.setCancelable(false);
       android.app.AlertDialog alertDialog = builder.create();
       alertDialog.setCanceledOnTouchOutside(false);
       alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
       alertDialog.show();


       TextView OK_txt = layout.findViewById(R.id.OK_txt);
       TextView messageTxt = layout.findViewById(R.id.messageTxt);

       messageTxt.setText(msg);

       OK_txt.setOnClickListener(v -> {
           alertDialog.dismiss();
       });
   }

}