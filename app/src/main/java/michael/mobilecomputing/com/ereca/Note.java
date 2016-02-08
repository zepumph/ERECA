package michael.mobilecomputing.com.ereca;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Iterator;


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

    /* global key values added by xander so there's no confusion */
    private static final String KEY_USER = "user";
    private static final String KEY_NOTE_TEXT = "noteText";
    private static final String KEY_LAT = "lat";
    private static final String KEY_LON = "lon";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_DATE = "date";



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
            json = putValue(json, KEY_USER, user);
            json = putValue(json, KEY_NOTE_TEXT, noteText);
            json = putValue(json, KEY_LAT, lat);
            json = putValue(json, KEY_LON, lon);
            json = putValue(json, KEY_DATE, date);
            json = putValue(json, KEY_IMAGE, image);


            Log.d(DEBUG, "JSONED Item (1st 500 chars): " + json.toString().substring(0, 500) );
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
        printByteArray();
        String encodedImage = Base64.encodeToString(tempArray, Base64.DEFAULT);
        return encodedImage;
    }

    private void printByteArray() {

    }


//

    /* added by xander */
    /*       reverse of jsonify, that is, take in json data and make a note
     */
    public static Note createFromJson(String jsontext){
        JSONObject json = null;
        Bitmap image = null;
        HashMap<String,String> content = null;

        /* get the json object and decode the image */
        try {
            //json = new JSONObject(jsontext);
            content = jsonToMap(jsontext);
            image = base64ToBitMap( content.get("image") );
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("failed to de-jsonify");
        }

        //public Note(String user, String noteText, double lon, double lat, Bitmap image, int date) {

        /* create an empty note */
        Note output = new Note();

        output.setImage(image);

        /* try to get info from note */
        try {
            output.setNoteText(content.get(KEY_NOTE_TEXT));
            output.setUser(content.get(KEY_USER));
            output.setDate(Long.parseLong( content.get(KEY_DATE) ));
            output.setLat(Double.parseDouble( content.get(KEY_LAT) ));
            output.setLon(Double.parseDouble( content.get(KEY_LON) ));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
        }

        return output;

    }
    /* reverse of bitMapToBase64(Bitmap bmp) */
    public static Bitmap base64ToBitMap( String base64 ){
        Bitmap bmp = null;

        /* try to decode the data, if  not return stock bitmap */
        try {
            byte[] data = Base64.decode(base64, Base64.DEFAULT);
            bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
        } catch (IllegalArgumentException e ){
            return BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.sample_0 );
        }


        return bmp;
    }

    /* Added by Xander. Much of this function is from
        http://stackoverflow.com/questions/22011200/creating-hashmap-from-a-json-string
        converts a json string to a hashmap if possible
     */
    private static HashMap<String,String> jsonToMap(String t) throws JSONException {

        HashMap<String, String> map = new HashMap<String, String>();
        JSONObject jObject = new JSONObject(t);
        Iterator<?> keys = jObject.keys();

        while( keys.hasNext() ){
            String key = (String)keys.next();
            String value = jObject.getString(key);
            map.put(key, value);

        }

        return map;
    }
}
