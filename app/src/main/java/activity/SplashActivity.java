package activity;


import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.BLUETOOTH;
import static android.Manifest.permission.BLUETOOTH_CONNECT;
import static android.Manifest.permission.BLUETOOTH_SCAN;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.vihaan.shaktinewconcept.R;

import activity.utility.CustomUtility;
import webservice.Constants;


@SuppressWarnings("deprecation")
public class SplashActivity extends AppCompatActivity {

    private final int REQUEST_CODE_PERMISSION = 123;
    LinearLayout descimage,desctxt;
    Animation uptodown,downtoup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView mLogo = findViewById(R.id.imageView2);
        descimage =  findViewById(R.id.titleimage);
        desctxt =  findViewById(R.id.titletxt);
        uptodown = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        downtoup = AnimationUtils.loadAnimation(this,R.anim.downtoup);
        descimage.setAnimation(downtoup);
        desctxt.setAnimation(uptodown);
        RotateAnimation rotate = new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(3000);
        rotate.setInterpolator(new LinearInterpolator());
        mLogo.startAnimation(rotate);

        if(checkPermission()){
            checkLogin();
        }
        else {
            requestPermission();
        }

    }

    private void checkLogin() {
        new Handler().postDelayed(() -> {
            if(CustomUtility.getSharedPreferences(getApplicationContext(), Constants.MaterialPumpCode)!=null && !CustomUtility.getSharedPreferences(getApplicationContext(), Constants.MaterialPumpCode).isEmpty()){
                Intent i = new Intent(SplashActivity.this, DeviceSettingActivity.class);
                startActivity(i); // invoke the SecondActivity.
                finish();
            }else {
                Intent i = new Intent(SplashActivity.this, ScannedBarcodeActivity.class);
                startActivity(i); // invoke the SecondActivity.
                finish();
            }
        }, 3000);
    }



    private boolean checkPermission() {
        int cameraPermission =
                ContextCompat.checkSelfPermission(SplashActivity.this, CAMERA);
        int writeExternalStorage =
                ContextCompat.checkSelfPermission(SplashActivity.this, WRITE_EXTERNAL_STORAGE);
        int ReadExternalStorage =
                ContextCompat.checkSelfPermission(SplashActivity.this, READ_EXTERNAL_STORAGE);
        int AccessCoarseLocation =
                ContextCompat.checkSelfPermission(SplashActivity.this, ACCESS_COARSE_LOCATION);
        int ReadMediaImage =
                ContextCompat.checkSelfPermission(SplashActivity.this, READ_MEDIA_IMAGES);
        int Bluetooth =
                ContextCompat.checkSelfPermission(SplashActivity.this, BLUETOOTH);
        int BluetoothConnect =
                ContextCompat.checkSelfPermission(SplashActivity.this, BLUETOOTH_CONNECT);
        int BluetoothScan =
                ContextCompat.checkSelfPermission(SplashActivity.this, BLUETOOTH_SCAN);



        if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return cameraPermission == PackageManager.PERMISSION_GRANTED
                    && AccessCoarseLocation == PackageManager.PERMISSION_GRANTED  && ReadMediaImage == PackageManager.PERMISSION_GRANTED
                    && Bluetooth == PackageManager.PERMISSION_GRANTED && BluetoothConnect == PackageManager.PERMISSION_GRANTED
                    && BluetoothScan == PackageManager.PERMISSION_GRANTED;
        }else  if (SDK_INT >= Build.VERSION_CODES.S) {
            return cameraPermission == PackageManager.PERMISSION_GRANTED && writeExternalStorage == PackageManager.PERMISSION_GRANTED && AccessCoarseLocation == PackageManager.PERMISSION_GRANTED
                    && Bluetooth == PackageManager.PERMISSION_GRANTED   && BluetoothScan == PackageManager.PERMISSION_GRANTED ;
        } else {
            return cameraPermission == PackageManager.PERMISSION_GRANTED && writeExternalStorage == PackageManager.PERMISSION_GRANTED
                    && ReadExternalStorage == PackageManager.PERMISSION_GRANTED && AccessCoarseLocation == PackageManager.PERMISSION_GRANTED;

        }

    }

    private void requestPermission() {
        if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA,  Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.BLUETOOTH , Manifest.permission.BLUETOOTH_CONNECT
                            , Manifest.permission.BLUETOOTH_SCAN},
                    REQUEST_CODE_PERMISSION);
        } if (SDK_INT >= Build.VERSION_CODES.S) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.BLUETOOTH,
                            Manifest.permission.BLUETOOTH_CONNECT , Manifest.permission.BLUETOOTH_SCAN },
                    REQUEST_CODE_PERMISSION);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,},
                    REQUEST_CODE_PERMISSION);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0) {
                if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    boolean ACCESSCAMERA = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean AccessCoarseLocation = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadMediaImage = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean Bluetooth = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    boolean BluetoothConnect = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                    boolean BluetoothScan = grantResults[5] == PackageManager.PERMISSION_GRANTED;

                    if (ACCESSCAMERA && AccessCoarseLocation && ReadMediaImage && Bluetooth && BluetoothConnect && BluetoothScan) {
                        checkLogin();
                    } else {
                        Toast.makeText(SplashActivity.this, R.string.all_permission, Toast.LENGTH_LONG).show();
                    }
                }
                else  if (SDK_INT >= Build.VERSION_CODES.S) {
                    boolean ACCESSCAMERA = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeExternalStorage =
                            grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean AccessCoarseLocation = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean Bluetooth = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    boolean BluetoothConnect = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                    boolean BluetoothScan = grantResults[5] == PackageManager.PERMISSION_GRANTED;


                    if (ACCESSCAMERA && writeExternalStorage && AccessCoarseLocation && Bluetooth &&  BluetoothConnect && BluetoothScan) {
                        checkLogin();
                    } else {
                        Toast.makeText(SplashActivity.this, R.string.all_permission, Toast.LENGTH_LONG).show();
                    }

                } else {
                    boolean ACCESSCAMERA = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeExternalStorage =
                            grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadExternalStorage =
                            grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean AccessCoarseLocation = grantResults[3] == PackageManager.PERMISSION_GRANTED;


                    if (ACCESSCAMERA && writeExternalStorage && ReadExternalStorage  && AccessCoarseLocation) {
                       checkLogin();
                    } else {
                        Toast.makeText(SplashActivity.this, R.string.all_permission, Toast.LENGTH_LONG).show();
                    }

                }
            }


        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2296) {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    // perform action when allow permission success
                    checkLogin();
                } else {
                    Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}

