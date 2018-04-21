package com.bibip.sensordatasender;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.net.InetAddress;

public class MainActivity extends Activity implements OnClickListener {

    Button changeActivityButton;
    Intent sensorActivity;
    CheckBox checkBoxAudio;
    EditText editTextAudioIP;
    EditText editTextAudioPORT;
    GenericAudioStream audioStream;
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorActivity = new Intent(this, SensorsActivity.class);

        changeActivityButton = (Button) findViewById(R.id.buttonStart);
        checkBoxAudio = (CheckBox) findViewById(R.id.checkBoxSendAudio);
        editTextAudioIP = (EditText) findViewById(R.id.EditTextAudioIP);
        editTextAudioPORT = (EditText) findViewById(R.id.EditTextAudioPORT);

        //startActivity(sensorActivity);

        // Example of a call to a native method
        //TextView tv = (TextView) findViewById(R.id.sample_text);
        //tv.setText(stringFromJNI());

        changeActivityButton.setOnClickListener(this);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonStart:
                startActivity(sensorActivity);
                break;
            case R.id.checkBoxSendAudio:
                sendingAudioCheck();
                break;
        }
    }

    private void sendingAudioCheck() {
        try {
            audioStream = new GenericAudioStream();
            audioStream.setDestination(InetAddress.getByName(editTextAudioIP.toString()), Integer.parseInt(editTextAudioPORT.toString()));
            if (checkBoxAudio.isChecked()) {
                audioStream.start();
            }
            else {
                audioStream.stop();
            }
        }
        catch (Exception e){
            Log.d("AudioSending",e.getMessage());
        }
    }
}
