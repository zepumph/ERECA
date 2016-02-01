package michael.mobilecomputing.com.ereca;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import michael.mobilecomputing.com.ereca.gridviewer.GridViewActivity;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Constants
    private static final int SPEECH_INPUT_ID = 3500;
    private static final int TAKE_PICTURE_ID = 3501;
    private static final String DEBUG = "DEBUG";
    private static final String ERROR = "ERROR";
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int GO_TO_GALLERY = 344;

    /**
     * note holds all data fields needed
     * These are added to one instance variable as they are created dynamically.
     * The note object is the reset to null values once a note is sent.
     */
    Note note;
    // Will need to be changes to something better eventually.
    String user = "testUser";

    // View variables
    ImageView picTaken;
    EditText noteBody;

    // Camera instance variables
    File pictureFile;
    Preview preview;
    private Camera myCamera = null;



    // Location instance variables
    LocationManager locationManager;
    boolean locationRequested = false;


    // Used for the locality functionality
    //Address address;
//    Geocoder geocoder;
    //    String locality;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        /* set up nav bar */
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /* init global vars + constants */
        note = new Note();

        noteBody = (EditText) findViewById(R.id.et_notepad);
        picTaken = (ImageView) findViewById(R.id.iv_pic_taken);

        locationManager = null;
//        geocoder = new Geocoder(getApplicationContext());

        getLocation();


        //initCamera();
        //camera is initialized on in onStart()



    }

    /**
     * Access the camera and assign in to the variable myCamera
     *      This should be called in onCreate and onResume
     */
    private void initCamera(){
        //Camera
        //checks if there is a cam
        if (checkCameraHardware(this)) {

            try {
                myCamera = Camera.open();
                Camera.Parameters params = myCamera.getParameters();
                myCamera.setParameters(params);
                myCamera.setDisplayOrientation(90);
                preview = new Preview(this, myCamera);
                FrameLayout cameraPreview = (FrameLayout) findViewById(R.id.preview);

                /* added by Xander in to remove camera release error
                * empties the layout so the old preview doesn't raise hell
                * */
                cameraPreview.removeAllViews();
                cameraPreview.addView(preview);
                System.out.println("camera opened");
            } catch (Exception e) {
                System.out.println("Error e: " + e);
            }
        }
    }

    private void hideKeyboard(){
//        InputMethodManager inputMethodManager = (InputMethodManager)  this.getSystemService(this.INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }

    /* called upon key presses while in this activity */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {
                    //TODO
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    takePicture();
                    insertSpeech();
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            /* go the the grid view */
            Intent goToGallery = new Intent(this, GridViewActivity.class);
            startActivityForResult(goToGallery, GO_TO_GALLERY );
        } else if (id == R.id.nav_notes) {

        } else if (id == R.id.nav_manage) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
    Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.d("onPictureTaken", "entering picture callback");

            pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null){
                Log.d("onPictureTaken", "Error creating media file, check storage permissions: ");
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);

                fos.write(data);
                fos.close();
                Log.d("onPictureTaken", "successfully created file ");

            } catch (FileNotFoundException e) {
                Log.d("onPictureTaken", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("onPictureTaken", "Error accessing file: " + e.getMessage());
            }
            //picTaken.refreshDrawableState();
            Bitmap picBitmap = BitmapFactory.decodeFile(pictureFile.getPath());
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            Bitmap image = Bitmap.createBitmap(picBitmap , 0, 0, picBitmap.getWidth(), picBitmap.getHeight(), matrix, true);

            //myCamera.stopPreview();
            note.setImage(image);
            picTaken.setImageBitmap(image);

            myCamera.startPreview();

        }
    };

    private static File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");


        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("ERECA", "failed to create directory");
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
    public void takePicture(){
        myCamera.takePicture(null, null, mPicture);

    }
    public void insertSpeech() {
        Intent i = new Intent();

        i.setAction(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        startActivityForResult(i, SPEECH_INPUT_ID);
    }

    private void getLocality(Location location) {
        if (location != null) {
            note.setLon(location.getLongitude());
            note.setLat(location.getLatitude());




            /**
             * Michael 1/27:
             * Not needed in the current design of the app, but great functionality for geocoding if
             * decided to use it. Keep it for now.
             */
            //            try {
            //                address = (Address) geocoder.getFromLocation(latitude, longitude, 1).toArray()[0];
            //                locality = address.getSubAdminArea();
            //                if (locality == null) {
            //                    locality = address.getLocality();
            //                }
            //
            //                if (locality == null) {
            //                    locality = address.getSubAdminArea();
            //                }
            //                if (locality == null) {
            //                    locality = address.getAddressLine(0);
            //                }
            //                Toast t = Toast.makeText(getApplicationContext(), locality, Toast.LENGTH_LONG);
            //                t.show();
            //            } catch (Exception e) {
            //                System.out.println(e);
            //
            //            }
        } else {
            Toast t = Toast.makeText(getApplicationContext(), "NO LAST LOCATION", Toast.LENGTH_SHORT);
            t.show();

        }

    }

    public void getLocation() {//took out View view as input, seemed unnecessary
        locationRequested = true;
        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }
        try {
            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            getLocality(lastLocation);
        } catch (SecurityException e) {
            System.out.print(e);
        }
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                getLocality(location);
            }
            @Override
            public void onProviderDisabled(String provider) {
            }
            @Override
            public void onProviderEnabled(String provider) {
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
        };

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
        } catch (SecurityException e) {
            System.out.print(e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SPEECH_INPUT_ID && resultCode == RESULT_OK) {
            ArrayList<String> outputList = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            noteBody.append(" " + outputList.get(0));
        } else if (requestCode == TAKE_PICTURE_ID && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            String fileName = extras.getString("FILE_PATH");
            System.out.println(fileName);
            Bitmap bitmapToDisplayFromSDCard = BitmapFactory.decodeFile(fileName);
            picTaken.setImageBitmap(bitmapToDisplayFromSDCard);
        } else if (requestCode == GO_TO_GALLERY){

        }
    }

    public void setNote(){

        // Set the note text
        note.setNoteText(noteBody.getText().toString());

        // Set the date before sending it
        Calendar c = Calendar.getInstance();
        note.setDate( c.getTime().getTime());

        // Set the user.
        note.setUser(user);


        // Set the location
        getLocation();

    }

    // Set up as a an onclick listener to a 'save' button or something like that.
    // Currently not initiated with anything
    public void sendNote(View view) {
        final String urlText = "http://cs.coloradocollege.edu/~cp341mobile/cgi-bin/notes_test_for_michael.cgi";
        final String reqMeth = "POST";
        final String action = "addNote";

        // set most of the note fields at once
        setNote();

        // This statement implies that all notes will have text in them. . . or else.
        if (note.getNoteText()== null){
            Log.e(ERROR, "Note not created before saved!");
            Toast t = Toast.makeText(getApplicationContext(), "Please Make a Note Before Saving",Toast.LENGTH_LONG);
            t.show();
            return;
        }

        // Make a json representation of the Note object
        final String noteJSON = note.jsonify();


        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        String[] params = {reqMeth, noteJSON, action, urlText};
        if (networkInfo != null && networkInfo.isConnected()) {
            new HTTPHelper().execute(params);
        } else {
            Toast t = Toast.makeText(getApplicationContext(), "No network connection", Toast.LENGTH_LONG);
            t.show();
        }
    }


    /**
     * Created by Michael on 1/26/2016.
     * Much of this code was gathered from Android tutorials and adapted for the full project.
     * See http://developer.android.com/training/basics/network-ops/connecting.html
     * <p/>
     * This class in embedded inside of the Main Activity class because it relies on the context of
     * the application.
     * <p/>
     * the params object holds all data needing to be sent to the server:
     * <p/>
     * 0  request method,
     * 1  jsonified note object
     * 2  query value of 'action', options: addNote, addUser, getUser (retrieves all the notes),
     * 3  the URL of the cgi script, no query params needed
     */
    class HTTPHelper extends AsyncTask<String, Void, String> {
        //private static final String DEBUG = "DEBUG";

        @Override
        protected String doInBackground(String... params) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return sendHTTP(params);
            } catch (IOException e) {
                return "Unable to find web page. URL may be invalid.";
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            EditText et_messageText = (EditText) findViewById(R.id.et_notepad);
            et_messageText.setText(result);
            note = new Note();
        }


        private String sendHTTP(String[] params) throws IOException {
            String method = params[0];
            String noteJSON = params[1];
            String action = params[2];
            String urlText = params[3];

            InputStream is = null;

            try {
                URL url = new URL(urlText + "?action=" + action);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestProperty("Content-type", "application/json");
                conn.setRequestMethod(method);
                conn.setDoInput(true);
                conn.setDoOutput(true);

                //set the body of the http request
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(URLEncoder.encode(noteJSON, "UTF-8"));
                dos.flush();
                dos.close();

                Log.d(DEBUG, "Connection object: " + conn.getContent());

                // Starts the query
                conn.connect();

                int response = conn.getResponseCode();
                Log.d(DEBUG, "The response is: " + response);
                is = conn.getInputStream();

                // Convert the InputStream into a string
                return readResultIS(is, 500);

                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            }
            catch(IOException e){
                Log.d(DEBUG, "CONNECTION PROBLEMS" + e.getMessage(), e);
                return "Exception Caught";
            }
            finally {
                if (is != null) {
                    is.close();
                }
            }
        }


        public String readResultIS(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
            Reader reader = null;
            reader = new InputStreamReader(stream, "UTF-8");
            char[] buffer = new char[len];
            reader.read(buffer);
            String s =  new String(buffer);
            Log.d(DEBUG, "RESPONSE STRING: " + s);
            return s;
        }
    }

    /* release camera when navigating away from the activity
     */
    @Override
    protected void onStop() {
        super.onStop();
        myCamera.release();
        myCamera = null;
        preview = null;
    }

    /* re-initialize camera
     */
    @Override
    public void onStart() {
        super.onStart();
        initCamera();

    }





    }
