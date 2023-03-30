package activity.utility;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.shakti.shaktinewconcept.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;



/**
 * Created by Administrator on 1/3/2017.
 */
public class CustomUtility {
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static final String PERMISSIONS_FILE_PICKER = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static Context appContext;
    static boolean connected;
    private static String PREFERENCE = "DealLizard";
    String current_date, current_time;
    Calendar calander = null;
    SimpleDateFormat simpleDateFormat = null;



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
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext, R.style.MyDialogTheme);
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

//        alertDialog.setNegativeButton("Cancel", newworkorder DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });

        // Showing Alert Message
        alertDialog.show();
        //alertDialog.setCancelable(cancellable);
    }

    public static void showTimeSetting(final Context mContext, DialogInterface.OnClickListener pos, DialogInterface.OnClickListener neg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext,R.style.MyDialogTheme);
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

    static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

  /*  public static boolean isInternetOn() {


        ConnectivityManager connectivity = (ConnectivityManager) BaseActivity.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    Log.e("INTERNET:", String.valueOf(i));
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        Log.e("INTERNET123:", "connected!");
                        return true;
                    }
                }
            }
        }
        return false;

    }*/

    public static String formateDate(String date) {
        String formatedDate = "";
        try {
            SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd");
            Date mDate = formate.parse(date);
//            SimpleDateFormat appFormate = newworkorder SimpleDateFormat("dd MMM, yyyy");
            SimpleDateFormat appFormate = new SimpleDateFormat("dd.MM.yyyy");

            formatedDate = appFormate.format(mDate);
            Log.i("Result", "mDate " + formatedDate);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return formatedDate;
    }

    public static String formateDate1(String date) {
        String formatedDate = "";
        try {
            SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd");
            Date mDate = formate.parse(date);
//            SimpleDateFormat appFormate = newworkorder SimpleDateFormat("dd MMM, yyyy");
            SimpleDateFormat appFormate = new SimpleDateFormat("yyyyMMdd");
            formatedDate = appFormate.format(mDate);
            Log.i("Result", "mDate " + formatedDate);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return formatedDate;
    }

    public static boolean checkPermission(final Context context) {


        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion > Build.VERSION_CODES.LOLLIPOP) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context,R.style.MyDialogTheme);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    // for username string preferences
    public static void setSharedPreference(Context context, String name,
                                           String value) {
        appContext = context;
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
        SharedPreferences.Editor editor = settings.edit();
        // editor.clear();
        editor.putString(name, value);
        editor.commit();
    }

    public static String getSharedPreferences(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
        return settings.getString(name, "");
    }

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


}
