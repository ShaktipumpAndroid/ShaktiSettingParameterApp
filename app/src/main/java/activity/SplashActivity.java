package activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.shakti.shaktinewconcept.R;


public class SplashActivity extends Activity implements AnimationListener {

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

        Thread myThread = new Thread(){
            @Override
            public void run(){
                try {
                    sleep(4000);
                    Intent intent = new Intent(getApplicationContext(),ScannedBarcodeActivity.class);
                    // Intent intent = new Intent(getApplicationContext(),DeviceSettingActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}

