package michael.mobilecomputing.com.ereca;

import android.graphics.Bitmap;
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


    private String user;
    private String noteText;
    private double lon;
    private double lat;
    private Bitmap image;
    private int date;


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

    public int getDate() {
        return date;
    }



    public String jsonify(){
        try{
            JSONObject json = new JSONObject();
            json.put("user", user);
            json.put("noteText", noteText);
            json.put("lon", lon);
            json.put("lat", lat);
          //  json.put("image", bitMapToByteArray(image));
            json.put("date", date);

            return json.toString();
        }catch(JSONException e){
            Log.e(ERROR, "JSON exception: " + e.getMessage(), e);
        }
        return "{ error:'jsonify failed in Note.java'}";
    }

    public byte[] bitMapToByteArray(Bitmap bmp){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}
