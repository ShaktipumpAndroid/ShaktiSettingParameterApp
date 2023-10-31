package activity.utility;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.vihaan.shaktinewconcept.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import activity.DeviceSettingActivity;


/**
 * Created by Administrator on 1/3/2017.
 */
public class CustomUtility {
    public static Context appContext;
    private static String PREFERENCE = "DealLizard";
     public  static ProgressDialog progressDialog ;


    public static boolean isInternetOn(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
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
    }

    public static void ShowToast(String text, Context context) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
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

    public static void clearSharedPreferences(Context context) {
        SharedPreferences  pref = context.getSharedPreferences(PREFERENCE, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();

    }

    public static boolean doesTableExist(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }
    public static void showProgressDialogue(Context context){
        progressDialog = new ProgressDialog(context);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Sync Data Offline....");
        progressDialog.show();
    }

    public static void hideProgressDialogue(){
        if(progressDialog!=null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
    public static String getDeviceId(Context mContext) {
        String androidId;
        androidId = "" + android.provider.Settings.Secure.getString(mContext.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        return androidId;
    }

}
