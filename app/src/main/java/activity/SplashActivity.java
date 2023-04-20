package activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;


import androidx.appcompat.app.AppCompatActivity;

import com.vihaan.shaktinewconcept.R;

import activity.utility.CustomUtility;
import webservice.Constants;


public class SplashActivity extends AppCompatActivity {

    // Splash screen timer
    //private static int SPLASH_TIME_OUT = 10000;
    LinearLayout descimage,desctxt;
    Animation uptodown,downtoup;

    private  ImageView mLogo;

    @Override
    /** Called when the activity is first created. */
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mLogo = (ImageView) findViewById(R.id.imageView2);
        descimage = (LinearLayout) findViewById(R.id.titleimage);
        desctxt = (LinearLayout) findViewById(R.id.titletxt);
        uptodown = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        downtoup = AnimationUtils.loadAnimation(this,R.anim.downtoup);
        descimage.setAnimation(downtoup);
        desctxt.setAnimation(uptodown);
        RotateAnimation rotate = new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(3000);
        rotate.setInterpolator(new LinearInterpolator());
        mLogo.startAnimation(rotate);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              if(CustomUtility.getSharedPreferences(getApplicationContext(), Constants.MaterialPumpCode)!=null && !CustomUtility.getSharedPreferences(getApplicationContext(), Constants.MaterialPumpCode).isEmpty()){
                  Intent i = new Intent(SplashActivity.this, DeviceSettingActivity.class);
                  startActivity(i); // invoke the SecondActivity.
                  finish();
              }else {
                  Intent i = new Intent(SplashActivity.this, ScannedBarcodeActivity.class);
                  startActivity(i); // invoke the SecondActivity.
                  finish();
              }
            }
        }, 3000);
    }


}

