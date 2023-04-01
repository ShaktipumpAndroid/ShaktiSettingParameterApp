package GlobleClass;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.vihaan.shaktinewconcept.R;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * Created by shakti on 11/21/2016.
 */
public class CustomUtility {
    String current_date, current_time;
    static boolean connected;
    Calendar calander = null;
    SimpleDateFormat simpleDateFormat = null;
    GPSTracker gps;

    public String getCurrentDate() {
        simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        current_date = simpleDateFormat.format(new Date());
        return current_date.trim();
    }


    public String getCurrentTime() {
        calander = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        current_time = simpleDateFormat.format(calander.getTime());
        return current_time.trim();
    }


    public static void ShowToast(String text, Context context) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isDateTimeAutoUpdate(Context mContext) {
        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (Settings.Global.getInt(mContext.getContentResolver(), Settings.Global.AUTO_TIME) == 1) {
                    return true;
                }
            } else {
                if (Settings.System.getInt(mContext.getContentResolver(), Settings.Global.AUTO_TIME) == 1) {
                    return true;
                }
            }


        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void showSettingsAlert(final Context mContext) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        // Setting Dialog Title
        alertDialog.setTitle("Date & Time Settings");
        // Setting Dialog Message
        alertDialog.setMessage("Please enable automatic date and time setting");
        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_DATE_SETTINGS);
                mContext.startActivity(intent);
            }
        });
        // on pressing cancel button

//        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });

        // Showing Alert Message
        alertDialog.show();
        //alertDialog.setCancelable(cancellable);
    }


    public static void showTimeSetting(final Context mContext, DialogInterface.OnClickListener pos, DialogInterface.OnClickListener neg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        // Setting Dialog Title
        alertDialog.setTitle("DATE TIME SETTINGS");
        // Setting Dialog Message
        alertDialog.setMessage("Date Time not auto update please check it.");
        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_DATE_SETTINGS);
                mContext.startActivity(intent);
            }
        });
        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
        //alertDialog.setCancelable(cancellable);
    }


    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;

        return capitalize(manufacturer) + "--" + model;
//        if (model.startsWith(manufacturer)) {
//            return capitalize(model);
//        } else {
//            return capitalize(manufacturer) + "--" + model;
//        }


    }

    @SuppressLint("MissingPermission")
    public static String getDeviceId(Context mContext) {
        /*TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return "true";
        }
        return telephonyManager.getDeviceId();*/

        final TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
      //  tmDevice = "" + Objects.requireNonNull(tm).getDeviceId();
      //  tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);

      /*  UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String deviceId = deviceUuid.toString();*/

        return androidId;
    }


    static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }

     //   char first = s.charAt(0);///old verking code

        char first = 1;
        try {

            first = s.charAt(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public static boolean checkNetwork(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
//    TelephonyManager telephonyManager = (TelephonyManager) activity
//            .getSystemService(Context.TELEPHONY_SERVICE);
//    return telephonyManager.getDeviceId();
//}


    public static boolean isOnline(Context mContext) {

        try {

            ConnectivityManager

            connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();

            Log.v("network",   String.valueOf(  connected ) );

//                Process p1 = Runtime.getRuntime().exec("ping -c 1 www.google.com");
//                int returnVal = p1.waitFor();
//
//                Log.v("ping",   String.valueOf(  returnVal ) );
//
//                connected = (returnVal == 0);
//
//                return connected;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }


        } catch (Exception e) {
//            System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.v("connectivity", e.toString());
        }
        return connected;
    }

    public static void isErrorDialog(final Context mContext , final String title, final String message) {



        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {

              AlertDialog.Builder  alertDialog = new AlertDialog.Builder(mContext);

                alertDialog.setTitle(title);
                //alertDialog.setMessage(message);


                // Initialize a new spannable string builder instance
                SpannableStringBuilder ssBuilder = new SpannableStringBuilder(message);
                // Initialize a new relative size span instance
                // Double the text size on this span
                //RelativeSizeSpan largeSizeText = new RelativeSizeSpan(2.0f);
                RelativeSizeSpan largeSizeText = new RelativeSizeSpan(0.9f);


                // Initialize a new  foreground color span
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.RED);

                // Apply the relative size span
                ssBuilder.setSpan(
                        largeSizeText, // Span to add
                        0, // Start of the span
                        message.length(), // End of the span
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE // Do not extent the span when text add later
                );

                // Apply the foreground color span
                ssBuilder.setSpan(
                        foregroundColorSpan, // Span to add
                        0, // Start of the span
                        message.length(), // End of the span
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE // Do not extent the span when text add later
                );

                // Set the alert dialog message using spannable string builder
                alertDialog.setMessage(ssBuilder);





                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });

                alertDialog.show();





//                AlertDialog dialog = alertDialog.create();
//
//                // Finally, display the alert dialog
//                dialog.show();

             //   Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);







                //negativeButton.setTextColor(mContext.getResources().getColor(R.color.white));
               // negativeButton.setTextColor(mContext.getResources().getColor(R.color.shakti_blue));
               // negativeButton.setBackgroundColor(mContext.getResources().getColor(R.color.shakti_blue));



            }
        });

    }

    public static void isSuccessDialog(final Context mContext , final String title, final String message) {



        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {

                AlertDialog.Builder  alertDialog = new AlertDialog.Builder(mContext);

                alertDialog.setTitle(title);
                //alertDialog.setMessage(message);


                // Initialize a new spannable string builder instance
                SpannableStringBuilder ssBuilder = new SpannableStringBuilder(message);
                // Initialize a new relative size span instance
                // Double the text size on this span
                //RelativeSizeSpan largeSizeText = new RelativeSizeSpan(2.0f);
                RelativeSizeSpan largeSizeText = new RelativeSizeSpan(1.0f);


                // Initialize a new  foreground color span
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(mContext .getResources().getColor(R.color.cardview_light_background));

                // Apply the relative size span
                ssBuilder.setSpan(
                        largeSizeText, // Span to add
                        0, // Start of the span
                        message.length(), // End of the span
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE // Do not extent the span when text add later
                );

                // Apply the foreground color span
                ssBuilder.setSpan(
                        foregroundColorSpan, // Span to add
                        0, // Start of the span
                        message.length(), // End of the span
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE // Do not extent the span when text add later
                );

                // Set the alert dialog message using spannable string builder
                alertDialog.setMessage(ssBuilder);





                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();

                    }
                });


                alertDialog.show();





//                AlertDialog dialog = alertDialog.create();
//
//                // Finally, display the alert dialog
//                dialog.show();

                //   Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);







                //negativeButton.setTextColor(mContext.getResources().getColor(R.color.white));
                // negativeButton.setTextColor(mContext.getResources().getColor(R.color.shakti_blue));
                // negativeButton.setBackgroundColor(mContext.getResources().getColor(R.color.shakti_blue));



            }
        });

    }



    public static boolean CheckGPS(Context mContext) {

        GPSTracker  gps = new GPSTracker(mContext);

        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            if (latitude == 0.0) {
               // CustomUtility.ShowToast("Lat Long not captured, Please try again.", mContext);
                CustomUtility.ShowToast("GPS Co-ordinates not received yet. Please Wait for some time", mContext);
                return false;
            }
        }
        else
        {
            gps.showSettingsAlert();
            return false;
        }




        return true;
    }


    /*compress image and convert to  stirng*/
    public static String CompressImage(String mFile)
    {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        //options.inSampleSize = 2;

        Log.d("mFile", "" + mFile );

        final Bitmap bitmap = BitmapFactory.decodeFile(mFile, options);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();


    //    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
         bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);

        byte[] byteArray = stream.toByteArray();


        String encodedImage = Base64.encodeToString(byteArray , Base64.DEFAULT);

        return encodedImage ;
    }



//    public boolean isServiceRunning(Context mContext) {
//        ActivityManager manager = (ActivityManager) mContext.getSystemService( mContext.ACTIVITY_SERVICE );
//        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
//            if("backgroundservice.TimeService".equals(service.service.getClassName())) {
//                return true;
//            }
//        }
//        return false;
//    }


}
