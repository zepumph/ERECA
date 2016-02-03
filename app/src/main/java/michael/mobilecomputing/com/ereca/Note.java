package michael.mobilecomputing.com.ereca;

import android.graphics.Bitmap;
import android.os.Debug;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

/**
 * Created by Michael on 1/25/2016.
 */


/**
 *
 *  Waiting for a rework of the ui organization
 *  Jack and Rebecca: Currently working on one button note taking.
 *  This POJO is to be used for holding a new note once collection occurs in one step.
 */

public class Note {
    private static final String ERROR = "ERROR";
    private static final String DEBUG = "DEBUG";


    private String user;
    private String noteText;
    private double lon;
    private double lat;
    private Bitmap image;
    private long date;


    public Note(){
        this.user = null;
        this.noteText = null;
        this.lon = 0;
        this.lat = 0;
        this.image = null;
        this.date = 0;
    }
    public Note(String user, String noteText, double lon, double lat, Bitmap image, int date) {
        this.user = user;
        this.noteText = noteText;
        this.lon = lon;
        this.lat = lat;
        this.image = image;
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public String getNoteText() {
        return noteText;
    }

    public double getLongitude() {
        return lon;
    }

    public double getLatitude() {
        return lat;
    }

    public Bitmap getImage() {
        return image;
    }

    public long getDate() {
        return date;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setDate(long date) {
        this.date = date;
    }

    //Creates a json of the file
    public String jsonify(){
        try{
            Log.d(DEBUG, "Bitmap Image: " + image );
            JSONObject json = new JSONObject();
            json = putValue(json, "user", user);
            json = putValue(json, "noteText", noteText);
            json = putValue(json, "lat", lat);
            json = putValue(json, "lon", lon);
            json = putValue(json, "image", image);
            json = putValue(json, "date", date);

            Log.d(DEBUG, "JSONED Item: " + json.toString() );
            return json.toString();
        }catch(JSONException e){
            Log.e(ERROR, "JSON exception: " + e.getMessage(), e);
        }
        return "{ error:'jsonify failed in Note.java'}";
    }

    public JSONObject putValue(JSONObject json, String field, Object value) throws JSONException{
        if (value == null){
            return json.put(field, false   );
        }
        else {
            // Load the json with bytes of the img to be stored.
            if( value instanceof Bitmap){
                String encodedImage = bitMapToBase64((Bitmap) value);
                return json.put(field, encodedImage);

            }
            return json.put(field, value);
        }
    }

    public String bitMapToBase64(Bitmap bmp){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] tempArray = stream.toByteArray();
        String encodedImage = Base64.encodeToString(tempArray, Base64.DEFAULT);
        return encodedImage;
    }
}
