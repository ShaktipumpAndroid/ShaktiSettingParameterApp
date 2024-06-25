package activity;

import static com.android.volley.Request.Method.GET;
import static webservice.WebURL.RetrievePumpCOde;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.gson.Gson;
import com.vihaan.shaktinewconcept.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import activity.BeanVk.PumpCodeModel;
import activity.utility.CustomUtility;
import webservice.AllPopupUtil;
import webservice.Constants;
import webservice.WebURL;

public class ScannedBarcodeActivity extends AppCompatActivity {
    private static final String TAG = "ScannedBarcodeActivity";
    SurfaceView surfaceView;
    private EditText edtBarcodeValueID, mobileNotxt,userNametxt,sapCodetxt;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;

    //Button btnAction;
    String intentData = "";
    RelativeLayout search;

    TextView pumpCodeExt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_barcode);
        initViews();
    }

    private void initViews() {
        edtBarcodeValueID = findViewById(R.id.edtBarcodeValueID);
        mobileNotxt = findViewById(R.id.mobileNotxt);
        sapCodetxt = findViewById(R.id.sapCodetxt);
        userNametxt =findViewById(R.id.userNametxt);
        RelativeLayout rlvSubmitBarcodeDataID = findViewById(R.id.rlvSubmitBarcodeDataID);
        surfaceView = findViewById(R.id.surfaceView);
        pumpCodeExt = findViewById(R.id.pumpCodeExt);
        search = findViewById(R.id.search);

        if (CustomUtility.isInternetOn(getApplicationContext())) {
            baseURLAPICall();
        } else {
            CustomUtility.setSharedPreference(getApplicationContext(), WebURL.BaseUrl, WebURL.rmsBaseURL);
        }
        search.setOnClickListener(v -> {

            if (!edtBarcodeValueID.getText().toString().isEmpty()) {
                if (AllPopupUtil.isOnline(getApplicationContext())) {
                    getPumpCode();
                } else {
                    CustomUtility.ShowToast("Please check your internet connection!", getApplicationContext());
                }
            } else {
                Toast.makeText(ScannedBarcodeActivity.this, "Please scan or enter Serial No!", Toast.LENGTH_SHORT).show();

            }


        });

        rlvSubmitBarcodeDataID.setOnClickListener(v -> {

            if(!userNametxt.getText().toString().trim().isEmpty() && !mobileNotxt.getText().toString().trim().isEmpty() && !sapCodetxt.getText().toString().trim().isEmpty())
            {
                String phone = mobileNotxt.getText().toString().trim();

                if (isValidPhone(phone))
                {
                    if (!pumpCodeExt.getText().toString().trim().isEmpty()) {

                        CustomUtility.setSharedPreference(getApplicationContext(), Constants.SerialNumber, edtBarcodeValueID.getText().toString());
                        CustomUtility.setSharedPreference(getApplicationContext(), Constants.MaterialPumpCode, pumpCodeExt.getText().toString());
                        CustomUtility.setSharedPreference(getApplicationContext(), Constants.UserName , userNametxt.getText().toString());
                        CustomUtility.setSharedPreference(getApplicationContext(), Constants.MobileNo, mobileNotxt.getText().toString());
                        CustomUtility.setSharedPreference(getApplicationContext(), Constants.SapCode , sapCodetxt.getText().toString());

                        Intent intent = new Intent(getApplicationContext(), DeviceSettingActivity.class);
                        intent.putExtra("MCode", pumpCodeExt.getText().toString().trim());
                        startActivity(intent);
                    } else {
                        Toast.makeText(ScannedBarcodeActivity.this, "Please scan or enter Serial No!", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(ScannedBarcodeActivity.this, "Please enter valid Mobile No!", Toast.LENGTH_SHORT).show();

                }
            }
            else {
                Toast.makeText(ScannedBarcodeActivity.this, "Please enter Personal Details!", Toast.LENGTH_SHORT).show();
            }


        });


    }

    public static boolean isValidPhone(String phone)
    {
        String expression = "^([\\d+]|\\(\\d{1,3}\\))[\\d\\-. ]{3,15}$";
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    private void initialiseDetectorsAndSources() {


        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(ScannedBarcodeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(ScannedBarcodeActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    edtBarcodeValueID.post(() -> {
                        if (barcodes.valueAt(0).email != null) {
                            edtBarcodeValueID.removeCallbacks(null);
                            intentData = barcodes.valueAt(0).email.address;
                            edtBarcodeValueID.setText(intentData.trim());

                        } else {
                            intentData = barcodes.valueAt(0).displayValue;
                            edtBarcodeValueID.setText(intentData.trim());

                        }

                        if (AllPopupUtil.isOnline(getApplicationContext())) {
                            getPumpCode();
                        } else {
                            CustomUtility.ShowToast("Please check your internet connection!", getApplicationContext());
                        }


                    });
                }
            }
        });

    }

    public void getPumpCode() {

        RequestQueue mRequestQueue = Volley.newRequestQueue(this);

        // String Request initialized
        StringRequest mStringRequest = new StringRequest(Request.Method.GET, RetrievePumpCOde + edtBarcodeValueID.getText().toString(), response -> {

            if (!response.isEmpty()) {

                Gson gson = new Gson();
                JsonReader reader = new JsonReader(new StringReader(response));
                reader.setLenient(true);
                PumpCodeModel pumpcodemodel = gson.fromJson(response, PumpCodeModel.class);
                if (pumpcodemodel.getStatus().equals("true")) {
                    CustomUtility.setSharedPreference(getApplicationContext(), Constants.MaterialPumpCode, pumpcodemodel.getResponse().getMaterialNumber().trim());
                    pumpCodeExt.setText(pumpcodemodel.getResponse().getMaterialNumber().trim());
                } else {
                    Toast.makeText(ScannedBarcodeActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                }
            }

        }, error -> Log.i(TAG, "Error :" + error.toString()));
        mStringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,  // maxNumRetries = 0 means no retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(mStringRequest);
    }
    /*---------------------------------------------------------------Base Url retrieve From Sap------------------------------------------------------------------------------*/
    private void baseURLAPICall() {

        RequestQueue mRequestQueue = Volley.newRequestQueue(this);

        // String Request initialized
        StringRequest mStringRequest = new StringRequest(GET, WebURL.sapBaseURL, new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                if(response!=null && !response.isEmpty()) {
                    try {
                        JSONObject Jobject = new JSONObject(response);
                        CustomUtility.setSharedPreference(getApplicationContext(), WebURL.BaseUrl, Jobject.getString("Base_url").toLowerCase().trim());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    CustomUtility.ShowToast(getResources().getString(R.string.somethingWentWrong),ScannedBarcodeActivity.this);
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i(TAG, "Error :" + error.toString());

            }
        });

        mRequestQueue.add(mStringRequest);

    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }


}