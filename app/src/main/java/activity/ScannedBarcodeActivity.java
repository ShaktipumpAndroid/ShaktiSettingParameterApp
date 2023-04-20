package activity;

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
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.gson.Gson;
import com.vihaan.shaktinewconcept.R;

import java.io.IOException;
import java.io.StringReader;

import activity.BeanVk.PumpCodeModel;
import activity.utility.CustomUtility;
import webservice.AllPopupUtil;
import webservice.Constants;

public class ScannedBarcodeActivity extends AppCompatActivity {
    private static final String TAG = "ScannedBarcodeActivity";
    SurfaceView surfaceView;
    private RelativeLayout rlvSubmitBarcodeDataID;
    private EditText edtBarcodeValueID;
    private BarcodeDetector barcodeDetector;
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
        rlvSubmitBarcodeDataID = findViewById(R.id.rlvSubmitBarcodeDataID);
        surfaceView = findViewById(R.id.surfaceView);
        pumpCodeExt = findViewById(R.id.pumpCodeExt);
        search = findViewById(R.id.search);


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!edtBarcodeValueID.getText().toString().isEmpty()) {
                    if (AllPopupUtil.isOnline(getApplicationContext())) {
                        getPumpCode();
                    } else {
                        CustomUtility.ShowToast("Please check your internet connection!", getApplicationContext());
                    }
                } else {
                    Toast.makeText(ScannedBarcodeActivity.this, "Please scan or enter Serial No!", Toast.LENGTH_SHORT).show();

                }


            }
        });

        rlvSubmitBarcodeDataID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!pumpCodeExt.getText().toString().trim().isEmpty()) {
                    CustomUtility.setSharedPreference(getApplicationContext(), Constants.SerialNumber, edtBarcodeValueID.getText().toString());
                    CustomUtility.setSharedPreference(getApplicationContext(), Constants.MaterialPumpCode, pumpCodeExt.getText().toString());
                    Intent intent = new Intent(getApplicationContext(), DeviceSettingActivity.class);
                    intent.putExtra("MCode", pumpCodeExt.getText().toString().trim());
                    startActivity(intent);
                } else {
                    Toast.makeText(ScannedBarcodeActivity.this, "Please scan or enter Serial No!", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void initialiseDetectorsAndSources() {


        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
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
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
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
                    edtBarcodeValueID.post(new Runnable() {
                        @Override
                        public void run() {
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


                        }
                    });
                }
            }
        });

    }

    public void getPumpCode() {

        RequestQueue mRequestQueue = Volley.newRequestQueue(this);

        // String Request initialized
        StringRequest mStringRequest = new StringRequest(Request.Method.GET, RetrievePumpCOde + edtBarcodeValueID.getText().toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (!response.isEmpty()) {

                    Gson gson = new Gson();
                    JsonReader reader = new JsonReader(new StringReader(response));
                    reader.setLenient(true);
                    PumpCodeModel pumpcodemodel = gson.fromJson(response.toString(), PumpCodeModel.class);
                    if (pumpcodemodel.getStatus().equals("true")) {
                        CustomUtility.setSharedPreference(getApplicationContext(), Constants.MaterialPumpCode, pumpcodemodel.getResponse().getMaterialNumber().trim());
                        pumpCodeExt.setText(pumpcodemodel.getResponse().getMaterialNumber().trim());
                    } else {
                        Toast.makeText(ScannedBarcodeActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Error :" + error.toString());

            }
        });
        mStringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,  // maxNumRetries = 0 means no retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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