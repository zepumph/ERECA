package michael.mobilecomputing.com.ereca.http;

import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import michael.mobilecomputing.com.ereca.MainActivity;

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
public class HttpHelper extends AsyncTask<String, Void, String> {

    // This will be executed onPostCreate
    // declared from class outside
    public AsyncResponse delegate = null;



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

        //this is not null because its declared from calling class ie gridview or mainactivity
        delegate.processFinish( result );

    }

    /** Copied by Xander from MainActivity and modified to get a note rather than recieve */
    private String sendHTTP(String[] params) throws IOException {
        String method = params[0];
        String noteJSON = params[1];
        String action = params[2];
        String urlText = params[3];
        //String endGoal = params[4];

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

            Log.d(MainActivity.DEBUG, "Connection object: " + conn.getContent());

            // Starts the query
            conn.connect();

            int response = conn.getResponseCode();
            Log.d(MainActivity.DEBUG, "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            int len = 100000;
            conn.getContentLength();
            return readResultIS(is, len);

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        }
        catch(IOException e){
            Log.d(MainActivity.DEBUG, "CONNECTION PROBLEMS" + e.getMessage(), e);
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
        String s = "";
        char[] buffer = new char[len];

            /* read content of unknown length */
        String toAdd = "";
        while( reader.read(buffer) != -1 ){
            toAdd = new String ( buffer );
            s += toAdd;
            buffer = new char[len];
        }


        Log.d(MainActivity.DEBUG, "RESPONSE STRING: " + s);

        return s;


    }
}