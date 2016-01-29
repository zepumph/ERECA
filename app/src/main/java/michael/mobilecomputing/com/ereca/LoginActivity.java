package michael.mobilecomputing.com.ereca;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Arrays;

/**
 * Created by Michael on 1/27/2016.
 */
public class LoginActivity extends AppCompatActivity {

    NfcAdapter myAdapter;
    PendingIntent pendingIntent;
    IntentFilter[] intentFiltersArray;

    String[][] techListsArray;

    EditText et_username;
    Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_username = (EditText) findViewById(R.id.et_username);
        btn_login = (Button) findViewById(R.id.btn_login);


        myAdapter = NfcAdapter.getDefaultAdapter(this);
        pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndef.addDataType("*/*");    /* Handles all MIME based dispatches.
                                       You should specify only the ones that you need. */
        }
        catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }
        IntentFilter td = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        intentFiltersArray = new IntentFilter[] {
                ndef, td
        };
        techListsArray = new String[][] { new String[] { NfcF.class.getName() } };
    }
    public void onPause() {
        super.onPause();
        myAdapter.disableForegroundDispatch(this);
    }
    public void onResume() {
        super.onResume();
        myAdapter.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, techListsArray);
    }

    public void login(View view){
        String username = et_username.getText().toString();
        if (username.equals("") || !username.matches("[a-zA-Z0-9. ]*")){
            Toast t = Toast.makeText(getApplicationContext(), "Please Type in a Valid Username", Toast.LENGTH_LONG);
            t.show();
        }
        else {
            Intent i = new Intent(getBaseContext(), MainActivity.class);
            i.putExtra("USERNAME", username);
            Toast t = Toast.makeText(getApplicationContext(), "Loging in as: " + username, Toast.LENGTH_SHORT);
            t.show();
            startActivity(i);
        }
    }

    public void onNewIntent(Intent intent) {
        String s = new String();

        Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        Parcelable[] data = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (data != null) {
            try {
                for (int i = 0; i < data.length; i++) {
                    NdefRecord[] recs = ((NdefMessage) data[i]).getRecords();
                    for (int j = 0; j < recs.length; j++) {
                        if (recs[j].getTnf() == NdefRecord.TNF_WELL_KNOWN &&
                                Arrays.equals(recs[j].getType(), NdefRecord.RTD_TEXT)) {
                            byte[] payload = recs[j].getPayload();
                            String textEncoding = ((payload[0] & 0200) == 0) ? "UTF-8" : "UTF-16";
                            int langCodeLen = payload[0] & 0077;

                            s += new String(payload, langCodeLen + 1, payload.length - langCodeLen - 1, textEncoding);
                        }
                    }
                }
            } catch (Exception e) {

            }
        }
        // Immediately adds the message gathered from NFC
        et_username.setText(s);
        // Launch the same onClick listener that the login button calls.
        login(btn_login);


         //the idea
         /**
          * Someone is logged in already
          * scanning the tag sets the username to someone else (so you don't have to do a full login)
          * registers their username in the newUser field above
          * sets the Boolean isNewUser to "true"
          * a check happens in the final note creation where:
          * if(isNewUser == true) then <note>.setUser(newUser);
          * ^^^if we do it this way, then we won't have to worry about changing the original person's username back
          * the next note will have the original person's username... At least I believe so... **/
          
          
    }
    
    

}
