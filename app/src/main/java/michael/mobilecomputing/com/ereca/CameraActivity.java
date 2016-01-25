package michael.mobilecomputing.com.ereca;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends Activity {
    private Camera myCamera = null;
    private Preview preview;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        if (checkCameraHardware(this)) {

            try {
                myCamera = Camera.open();
                myCamera.setDisplayOrientation(90);
                preview = new Preview(this, myCamera);
                FrameLayout cameraPreview = (FrameLayout) findViewById(R.id.preview);
                cameraPreview.addView(preview);
                Button captureButton = (Button) findViewById(R.id.buttonClick);
                captureButton.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // get an image from the camera
                                myCamera.takePicture(null, null, mPicture);
                            }
                        }
                );
                System.out.println("camera opened");
            } catch (Exception e) {
                System.out.println("Error e: "+ e);
            }
        }
    }
    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null){
                Log.d("onPictureTaken", "Error creating media file, check storage permissions: ");
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d("onPictureTaken", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("onPictureTaken", "Error accessing file: " + e.getMessage());
            }

            Intent picture_file = new Intent();
            picture_file.putExtra("FILE_PATH", pictureFile.getPath());
            setResult(RESULT_OK, picture_file);
            finish();
        }
    };
    //    @Override
//    protected void onResume(){
//        super.onResume();
//        try{
//            myCamera.open();
//        }
//        catch (Exception e) {
//            System.out.println("Error e: "+ e);
//            //Here i get the Exception:  Failed to connect to camera service
//        }
//
//
//    }
    @Override
    protected void onPause() {
        super.onPause();
        myCamera.release();
    }
    @Override
    protected void onStop() {
        super.onStop();
        myCamera.release();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("destroying...");
        myCamera.release();
    }


    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            String fileName = mediaStorageDir.getPath() + File.separator +
                    "IMG" + timeStamp + ".jpg";
            mediaFile = new File(fileName);
        } else {
            return null;
        }

        return mediaFile;
    }
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }
}

