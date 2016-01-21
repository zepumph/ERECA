package michael.mobilecomputing.com.ereca;



import android.content.Intent;
import android.graphics.Bitmap;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noteBody = (EditText) findViewById(R.id.et_notepad);
        picTaken = (ImageView) findViewById(R.id.iv_pic_taken);

    }
    public void takePicture(View view){
        Intent i = new Intent();

        i.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, TAKE_PICTURE_ID);
    }
    public void insertSpeech(View view){
        Intent i = new Intent();

        i.setAction(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        startActivityForResult(i, SPEECH_INPUT_ID);
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
            Bitmap bm = (Bitmap) extras.get("data");
            picTaken.setImageBitmap(bm);
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
