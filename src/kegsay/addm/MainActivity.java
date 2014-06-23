package kegsay.addm;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

    private EditText mUpdateRate;
    private EditText mUrl;
    private Config mConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUrl = (EditText) findViewById(R.id.editText_url);
        mUpdateRate = (EditText) findViewById(R.id.editText_update_rate);
        mConfig = new Config(getApplicationContext());
        
        // populate from config
        mUrl.setText(mConfig.getUrl());
        mUpdateRate.setText(String.valueOf(mConfig.getUpdateRate()));
    }

    /**
     * Linked via XML. Called when the update button is clicked.
     * 
     * @param v The view clicked
     */
    public void onUpdateClick(View v) {
        // TODO
    }

    /**
     * Linked via XML. Called when the save button is clicked.
     * 
     * @param v The view clicked
     */
    public void onSaveClick(View v) {
        long updateFreqMins = -1L;
        // sanity check
        try {
            updateFreqMins = Long.parseLong(mUpdateRate.getText().toString());
            if (updateFreqMins <= 0) {
                Toast.makeText(getApplicationContext(),
                        "Update rate must be a positive number and not 0.",
                        Toast.LENGTH_SHORT).show();
                return;
            }
        } 
        catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(),
                    "Update rate must be a number.", Toast.LENGTH_SHORT).show();
            return;
        }
        
        String url = mUrl.getText().toString();
        if (TextUtils.isEmpty(url)) {
            Toast.makeText(getApplicationContext(), "URL must not be empty.", Toast.LENGTH_SHORT).show();
            return;
        }
        
        Uri uri = Uri.parse(url);
        if (!"http".equals(uri.getScheme()) && !"https".equals(uri.getScheme())) {
            Toast.makeText(getApplicationContext(), "URL must be http/https.", Toast.LENGTH_SHORT).show();
            return;
        }
        
        mConfig.setUrl(url);
        mConfig.setUpdateRate(updateFreqMins);
        Toast.makeText(getApplicationContext(), "Settings saved.", Toast.LENGTH_SHORT).show();
    }

}
