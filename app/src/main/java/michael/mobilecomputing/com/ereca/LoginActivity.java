package michael.mobilecomputing.com.ereca;

import android.app.Activity;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

/**
 * Created by Michael on 1/27/2016.
 */
public class LoginActivity extends Activity{

    NfcAdapter myAdapter;
    PendingIntent pendingIntent;
    IntentFilter[] intentFiltersArray;

    String[][] techListsArray;

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

        tv = (TextView) findViewById(R.id.texter);
        tv.setText(s);

        Toast t = Toast.makeText(getApplicationContext(),"Message: " + s, Toast.LENGTH_LONG);
        t.show();


    }

}