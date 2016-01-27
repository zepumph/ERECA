package michael.mobilecomputing.com.ereca;



import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int SPEECH_INPUT_ID = 3500;
    private static final int TAKE_PICTURE_ID = 3501;

    EditText noteBody;
    ImageView picTaken;
    LocationManager locationManager;
    double longitude;
    double latitude;
    boolean locationRequested = false;
    Address address;
    Geocoder geocoder;

    Note note;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        geocoder = new Geocoder(getApplicationContext());


        noteBody = (EditText) findViewById(R.id.et_notepad);
        picTaken = (ImageView) findViewById(R.id.iv_pic_taken);

    }

    public void takePicture(View view) {
        Intent i = new Intent(this, CameraActivity.class);

        startActivityForResult(i, TAKE_PICTURE_ID);
    }

    public void insertSpeech(View view) {
        Intent i = new Intent();

        i.setAction(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        startActivityForResult(i, SPEECH_INPUT_ID);
    }

    private void getLocality(Location location) {
        if (location != null) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            try {
                address = (Address) geocoder.getFromLocation(latitude, longitude, 1).toArray()[0];
                String locality = address.getSubAdminArea();
                if (locality == null) {
                    locality = address.getLocality();
                }

                if (locality == null) {
                    locality = address.getSubAdminArea();
                }
                if (locality == null) {
                    locality = address.getAddressLine(0);
                }
                Toast t = Toast.makeText(getApplicationContext(), locality, Toast.LENGTH_LONG);
                t.show();
            } catch (Exception e) {
                System.out.println(e);
//            }
//            longitude = location.getLongitude();
//            latitude = location.getLatitude();
//            Toast t = Toast.makeText(getApplicationContext(),
//                    String.format("long: " + longitude + "\nlat: " + latitude), Toast.LENGTH_LONG);
//            t.show();
            }
        } else {
            Toast t = Toast.makeText(getApplicationContext(), "NO LAST LOCATION", Toast.LENGTH_SHORT);
            t.show();

        }

    }

    public void getLocation(View view) {
        locationRequested = true;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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
        Toast t = Toast.makeText(getApplicationContext(), "HJelkfd", Toast.LENGTH_LONG);
        t.show();
        if (requestCode == SPEECH_INPUT_ID && resultCode == RESULT_OK) {
            ArrayList<String> outputList = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            noteBody.append(" " + outputList.get(0));
        } else if (requestCode == TAKE_PICTURE_ID && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            String fileName = extras.getString("FILE_PATH");
            System.out.println(fileName);
            Bitmap bitmapToDisplayFromSDCard = BitmapFactory.decodeFile(fileName);
            picTaken.setImageBitmap(bitmapToDisplayFromSDCard);
        }
    }


    public Note getNote() {
        return note;
    }

    public void updateNote(){
        Calendar c = Calendar.getInstance();
        int d = c.get(Calendar.SECOND);
        note = new Note("michael",noteBody.getText().toString(),
                longitude, latitude, picTaken.getDrawingCache(),
                d);
    }

    public void sendNote(View view) {

        //Temporality needed to load the note object from other members
        updateNote();

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        String[] params = {"POST", note.jsonify(), "addNote"};
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
     *
     * This class in embedded inside of the Main Activity class because it relies on the context of
     * the application.
     *
     * the params object holds all data needing to be sent to the server:
     *
     *  request method,
     *  query value of 'action', options: addNote, addUser, getUser (retrieves all the notes),
     *  jsonified note object
     *
     *
     */
    class HTTPHelper extends AsyncTask<String, Void, String> {
        private static final String DEBUG = "DEBUG";
        private static final String urlText = "http://cs.coloradocollege.edu/~cp341mobile?action=";

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
//            Toast t = Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG);
//            t.show();
        }


        private String sendHTTP(  String[] params ) throws IOException {
            String method = params[0];
            String noteJSON = params[1];
            String action = params[2];

            InputStream is = null;

            try {
                URL url = new URL(urlText + action);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestProperty("Content-type", "application/json");
                conn.setRequestMethod(method);
                conn.setDoInput(true);

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
            } finally {
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
            return new String(buffer);
        }
    }
}