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
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        geocoder = new Geocoder(getApplicationContext());


        noteBody = (EditText) findViewById(R.id.et_notepad);
        picTaken = (ImageView) findViewById(R.id.iv_pic_taken);

    }
    public void takePicture(View view){
        Intent i = new Intent(this, CameraActivity.class);

        startActivityForResult(i, TAKE_PICTURE_ID);
    }
    public void insertSpeech(View view){
        Intent i = new Intent();

        i.setAction(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        startActivityForResult(i, SPEECH_INPUT_ID);
    }

    private void getLocality(Location location){
        if(location != null){
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            try{
                address = (Address) geocoder.getFromLocation(latitude, longitude, 1).toArray()[0];
                String locality = address.getSubAdminArea();
                if (locality == null) {
                    locality = address.getLocality();
                }

                if (locality == null){
                    locality = address.getSubAdminArea();
                }
                if (locality == null) {
                    locality = address.getAddressLine(0);
                }
                Toast t = Toast.makeText(getApplicationContext(), locality, Toast.LENGTH_LONG);
            t.show();
            }
            catch (Exception e) {
                System.out.println(e);
//            }
//            longitude = location.getLongitude();
//            latitude = location.getLatitude();
//            Toast t = Toast.makeText(getApplicationContext(),
//                    String.format("long: " + longitude + "\nlat: " + latitude), Toast.LENGTH_LONG);
//            t.show();
            }
        }
        else{
            Toast t = Toast.makeText(getApplicationContext(), "NO LAST LOCATION", Toast.LENGTH_SHORT);
            t.show();
            
        }

    }

    public void getLocation(View view) {
        locationRequested = true;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try{
            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            getLocality(lastLocation);
        }
        catch (SecurityException e){
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

        try{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
        }
        catch (SecurityException e){
            System.out.print(e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast t = Toast.makeText(getApplicationContext(), "HJelkfd", Toast.LENGTH_LONG);
        t.show();
        if( requestCode == SPEECH_INPUT_ID && resultCode == RESULT_OK){
            ArrayList<String> outputList = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            noteBody.append(" " + outputList.get(0));
        }
        else if( requestCode == TAKE_PICTURE_ID && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            String fileName = extras.getString("FILE_PATH");
            System.out.println(fileName);
            Bitmap bitmapToDisplayFromSDCard = BitmapFactory.decodeFile(fileName);
            picTaken.setImageBitmap(bitmapToDisplayFromSDCard);
        }
    }


    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
