package michael.mobilecomputing.com.ereca.gridviewer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import michael.mobilecomputing.com.ereca.DetailAcvitity;
import michael.mobilecomputing.com.ereca.MainActivity;
import michael.mobilecomputing.com.ereca.Note;
import michael.mobilecomputing.com.ereca.R;
import pl.polidea.view.ZoomView;



public class GridViewActivity extends Activity {

    GridView gridview;
    ZoomView zoomView;
    LinearLayout main_container;
    ImageAdapter imageAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_view);


        /* from xander's code */
        /* Zoom View */
        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.zoomable_view, null, false);
        //v.setLayoutParams(new LinearLayout.LayoutParams(GridView.LayoutParams.FILL_PARENT, GridView.FILL_PARENT));

        zoomView = new ZoomView(this);
        zoomView.addView(v);

        main_container = (LinearLayout) findViewById(R.id.main_container);
        main_container.addView(zoomView);

//        /* nav bar */
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(GridViewActivity.this);


        /* Grid View */
        gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(imageAdapter = new ImageAdapter(this));
        final Intent detailIntent = new Intent(this, DetailAcvitity.class);
        Bitmap icon = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.sample_2);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        icon.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        detailIntent.putExtra("image", byteArray);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                startActivity(detailIntent);
                Toast.makeText(GridViewActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();
                //gridview.setColumnWidth(gridview.getColumnWidth()+5);
            }
        });

        //getNote(null);
        testAddNote();
    }

    // Set up as a an onclick listener to a 'save' button or something like that.
    // Currently not initiated with anything
    public void getNote(View view) {
        final String urlText = "http://cs.coloradocollege.edu/~cp341mobile/cgi-bin/notes.cgi";
        final String reqMeth = "GET";
        final String action = "getNote&user=testUser";


        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        String[] params = {reqMeth, "", action, urlText};
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
            //EditText et_messageText = (EditText) findViewById(R.id.et_notepad);
            //et_messageText.setText(result);
            //note = new Note();
            gridview.invalidateViews();

        }

        /** Copied by Xander from MainActivity and modified to get a note rather than recieve */
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

                Log.d(MainActivity.DEBUG, "Connection object: " + conn.getContent());

                // Starts the query
                conn.connect();

                int response = conn.getResponseCode();
                Log.d(MainActivity.DEBUG, "The response is: " + response);
                is = conn.getInputStream();

                // Convert the InputStream into a string
                int len = 4048 * 4;
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

            /* add the note to the imageAdapter */
            Note note = Note.createFromJson(s);
            imageAdapter.addNote(note);

            return s;


        }
    }

    private void testAddNote(){
        getNote(null);
        //gridview.invalidateViews();
    }




    @Override
    public void onBackPressed() {
        GridViewActivity.this.finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){
        gridview.invalidateViews();
       return false;
    }




}
