package rd22.androidbeamdemo;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import csc495.androidbeamdemo.R;


public class MainActivity extends Activity implements CreateNdefMessageCallback {

    NfcAdapter nfcAdapter;
    TextView textView;

    private static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "in onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);

        //Check for available NFC Adapter
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null) {
            Log.d(TAG, "in onCreate, NFC is not available");
            Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if (!nfcAdapter.isEnabled()) {
            Log.d(TAG, "in onCreate, NFC is not enabled");
            Toast.makeText(this, "NFC is not enabled", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if (!nfcAdapter.isNdefPushEnabled()) {
            Log.d(TAG, "in onCreate, NFC is not push enabled");
            Toast.makeText(this, "NFC is not push enabled", Toast.LENGTH_SHORT).show();

        } else {
            //Register callback
            Log.d(TAG, "in onCreate, register callback");
            nfcAdapter.setNdefPushMessageCallback(this, this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onResume() {

        super.onResume();
        Log.d(TAG, "in onResume");
        //TODO what is this??
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {     //to receive the beam
            processIntent(getIntent());
        }
    }

    public void onNewIntent(Intent intent) {
        setIntent(intent);
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        Log.d(TAG, "in createNdefMessage");
        String text = "Hello World!";
        NdefMessage msg = new NdefMessage(new NdefRecord[]{
                NdefRecord.createMime("application/com.rd22.androidbeamdemo", text.getBytes())
        });
        return msg;
    }

    //to receive the beam
    private void processIntent(Intent intent) {
        Log.d(TAG, "in processIntent");
        //TODO what is this??
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        textView.setText(new String(msg.getRecords()[0].getPayload()));
    }
}
