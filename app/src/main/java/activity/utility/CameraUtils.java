package activity.utility;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.multidex.BuildConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;



/**
 * Created by Administrator on 10/27/2018.
 */
public class CameraUtils {

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final String IMAGE_EXTENSION = "jpg";
    public static final String VIDEO_EXTENSION = "mp4";
    public static final String GALLERY_DIRECTORY_NAME = "Solar Photo";
    protected static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;


    /**
     * Refreshes gallery on adding newworkorder image/video. Gallery won't be refreshed
     * on older devices until device is rebooted
     */
    public static void refreshGallery(Context context, String filePath) {
        // ScanFile so it will be appeared on Gallery
        MediaScannerConnection.scanFile(context,
                new String[]{filePath}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String s, Uri uri) {
                    }

                });
    }

    public static boolean checkPermissions(Context context) {


        int permissionCamera = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        int permissionStorage = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionStorageRead = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }


        if (permissionStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (permissionStorageRead != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions((Activity) context, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;


    }

    /**
     * Downsizing the bitmap to avoid OutOfMemory exceptions
     */
    public static Bitmap optimizeBitmap(int sampleSize, String filePath) {

        // bitmap factory
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = sampleSize;
        options.inJustDecodeBounds = false;
        BitmapFactory.decodeFile(filePath, options);


        return BitmapFactory.decodeFile(filePath,options);
    }

    /**
     * Checks whether device has camera or not. This method not necessary if
     * android:required="true" is used in manifest file
     */
    public static boolean isDeviceSupportCamera(Context context) {
        if (context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    private static int calculateInSampleSize(BitmapFactory.Options bmOptions)
    {
        // Raw height and width of image
        final int photoWidth = bmOptions.outWidth;
        final int photoHeight = bmOptions.outHeight;
        int scaleFactor = 1;
        if (photoWidth > 500
                || photoHeight > 500)
        {
            final int halfPhotoWidth = photoWidth / 2;
            final int halfPhotoHeight = photoHeight / 2;

            // Calculate the largest inSampleSize value that is a power of 2
            //and keeps both height and width larger than the requested height and width.
            while ((halfPhotoWidth / scaleFactor) >= 500
                    && (halfPhotoHeight / scaleFactor) >= 500)
            {
                scaleFactor *= 2;
            }
        }

        return scaleFactor;
    }

    /**
     * Open device app settings to allow user to enable permissions
     */
    public static void openSettings(Context context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", BuildConfig.APPLICATION_ID, null));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static Uri getOutputMediaFileUri(Context context, File file) {
        return FileProvider.getUriForFile(Objects.requireNonNull(context), context.getPackageName() + ".provider", file);
    }

    /**
     * Creates and returns the image or video file before opening the camera
     */
    public static File getOutputMediaFile1(int type) {

        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),GALLERY_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e(GALLERY_DIRECTORY_NAME, "Oops! Failed create "
                        + GALLERY_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Preparing media file naming convention
        // adds timestamp
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());

        File mediaFile;

        if (type == MEDIA_TYPE_IMAGE)
        {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + "." + IMAGE_EXTENSION);
        }
        else if (type == MEDIA_TYPE_VIDEO)
        {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + "." + VIDEO_EXTENSION);
        }
        else
        {
            return null;
        }

        return mediaFile;
    }


    /**
     * Creates and returns the image or video file before opening the camera
     */
    public static File getOutputMediaFile(int type, String enq_docno, String keyimage, String from) {

        File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),GALLERY_DIRECTORY_NAME);

        File dir = new File(root.getAbsolutePath() + "/SKAPP/" + from); //it is my root directory

        File billno = new File(root.getAbsolutePath() + "/SKAPP/" + from + enq_docno); // it is my sub folder directory .. it can vary..

        try {
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (!billno.exists()) {
                billno.mkdirs();
                if (!billno.mkdirs()) {
                    Log.e(enq_docno, "Oops! Failed create "
                            + enq_docno + " directory");
                    return null;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        // adds timestamp
        //String timeStamp = newworkorder SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(newworkorder Date());

        File mediaFile;
        Bitmap bitmap = null;

        if (type == MEDIA_TYPE_IMAGE) {



            mediaFile = new File(billno.getPath() + File.separator + "IMG_" /*+ timeStamp + "_"*/ + keyimage + "." + IMAGE_EXTENSION);
            if (mediaFile.exists()) {
                mediaFile.delete();
            }

            try {

                FileOutputStream out = new FileOutputStream(mediaFile);


                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }


        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(billno.getPath() + File.separator
                    + "VID_" /*+ timeStamp + "_"*/ + keyimage + "." + VIDEO_EXTENSION);
        } else {
            return null;
        }


        return mediaFile;
    }




    public static File getOutputMediaFile1(int type, String keyimage, String from) {

        File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),GALLERY_DIRECTORY_NAME);

        File dir = new File(root.getAbsolutePath() + "/SKAPP/SERVICE/" + from); //it is my root directory
      //  File dir = new File(root.getAbsolutePath() + "/SKAPP/" + from); //it is my root directory


        try {
            if (!dir.exists()) {
                dir.mkdirs();
            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        // adds timestamp
        //String timeStamp = newworkorder SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(newworkorder Date());

        File mediaFile;

        if (type == MEDIA_TYPE_IMAGE) {

            mediaFile = new File(dir.getPath() + File.separator + "IMG_" /*+ timeStamp + "_"*/ + keyimage + "." + IMAGE_EXTENSION);
            if (mediaFile.exists()) {
                mediaFile.delete();
                try {

                    FileOutputStream out = new FileOutputStream(mediaFile);

                    out.flush();
                    out.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else {
            return null;
        }


        return mediaFile;
    }


    public static File getOutputMediaFile(int type, String enq_docno, String keyimage) {

        File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),GALLERY_DIRECTORY_NAME);

        File dir = new File(root.getAbsolutePath() + "/SKAPP/SERVICE/" + enq_docno); //it is my root directory

        File billno = new File(root.getAbsolutePath() + "/SKAPP/SERVICE/" + enq_docno); // it is my sub folder directory .. it can vary..

        try {
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (!billno.exists()) {
                billno.mkdirs();
                if (!billno.mkdirs()) {
                    Log.e(enq_docno, "Oops! Failed create "
                            + enq_docno + " directory");
                    return null;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        // adds timestamp
        //String timeStamp = newworkorder SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(newworkorder Date());

        File mediaFile;
        Bitmap bitmap = null;

        if (type == MEDIA_TYPE_IMAGE) {



            mediaFile = new File(billno.getPath() + File.separator + "IMG_" /*+ timeStamp + "_"*/ + keyimage + "." + IMAGE_EXTENSION);
            if (mediaFile.exists()) {
                mediaFile.delete();
            }

            try {

                FileOutputStream out = new FileOutputStream(mediaFile);


                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }


        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(billno.getPath() + File.separator
                    + "VID_" /*+ timeStamp + "_"*/ + keyimage + "." + VIDEO_EXTENSION);
        } else {
            return null;
        }


        return mediaFile;
    }

}
