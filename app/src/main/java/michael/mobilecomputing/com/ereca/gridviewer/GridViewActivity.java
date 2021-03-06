package michael.mobilecomputing.com.ereca.gridviewer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import michael.mobilecomputing.com.ereca.DetailActivity;
import michael.mobilecomputing.com.ereca.http.AsyncResponse;
import michael.mobilecomputing.com.ereca.Note;
import michael.mobilecomputing.com.ereca.R;
import michael.mobilecomputing.com.ereca.http.HttpHelper;
import pl.polidea.view.ZoomView;



public class GridViewActivity extends Activity implements AsyncResponse {

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

        /* Grid View */
        gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(imageAdapter = new ImageAdapter(this));
        final Intent detailIntent = new Intent(this, DetailActivity.class);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                /* start an activity to view the note up close */
                Note noteToDisplay = (Note) imageAdapter.getItem(position);
                String filePath = "/sdcard/" + "detailImage" + ".png";
                saveBitmap(noteToDisplay.getImage(),filePath);
                String noteString = noteToDisplay.getNoteText()+"|"+noteToDisplay.getLatitude()+"|"+noteToDisplay.getLongitude();

                detailIntent.putExtra("noteString", noteString);
                startActivity(detailIntent);
//                Toast.makeText(GridViewActivity.this, "" + position,
//                        Toast.LENGTH_SHORT).show();
                //gridview.setColumnWidth(gridview.getColumnWidth()+5);
            }
        });

        //getNote(null);
        getNotesFromServer();
    }


    // Set up as a an onclick listener to a 'save' button or something like that.
    // Currently not initiated with anything
    public void getNote(View view, String noteID) {
        final String urlText = "http://cs.coloradocollege.edu/~cp341mobile/cgi-bin/notes.cgi";
        final String reqMeth = "GET";
        final String action = "getNote&user=testUser&noteId=" + noteID;


        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        String[] params = {reqMeth, "", action, urlText};
        if (networkInfo != null && networkInfo.isConnected()) {
            HttpHelper httpHelper = new HttpHelper();
            httpHelper.delegate = this;
            httpHelper.execute(params);

        } else {
            Toast t = Toast.makeText(getApplicationContext(), "No network connection", Toast.LENGTH_LONG);
            t.show();
        }
    }

    /* get the list of notesids and then use another async task to get them from the server */
    public void getNotesFromServer(){
        final String urlText = "http://cs.coloradocollege.edu/~cp341mobile/cgi-bin/notes.cgi";
        final String reqMeth = "GET";
        final String action = "getNoteIDs&user=testUser";


        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        String[] params = {reqMeth, "", action, urlText};
        if (networkInfo != null && networkInfo.isConnected()) {
            HttpHelper httpHelper = new HttpHelper();
            httpHelper.delegate = new GetListResponse( this );
            httpHelper.execute(params);

        } else {
            Toast t = Toast.makeText(getApplicationContext(), "No network connection", Toast.LENGTH_LONG);
            t.show();
        }

    }

    private boolean saveBitmap(Bitmap bitmapInput, String filePath) {
        try {
            FileOutputStream stream = new FileOutputStream(filePath);
            bitmapInput.compress(Bitmap.CompressFormat.PNG, 40, stream);
            //Don't we want to do all saving at one time
            //dbHelper.updateRow("image", filePath, uuid);
            return true;

        } catch (FileNotFoundException e) {
            System.out.print(e);
            return false;
        }
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

    /*
    This is the callback for when the http get request finishes adding a note to the grid view.
     */
    @Override
    public void processFinish(String result) {
        /* add the note to the imageAdapter */
        Note note = Note.createFromJson(result);
        imageAdapter.addNote(note);
        gridview.invalidateViews();

        Toast addedNote = Toast.makeText(this, "Added a note to the grid!", Toast.LENGTH_SHORT);
        addedNote.show();

    }
}


