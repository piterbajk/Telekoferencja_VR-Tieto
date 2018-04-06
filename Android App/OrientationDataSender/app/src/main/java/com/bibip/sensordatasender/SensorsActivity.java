package com.bibip.sensordatasender;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class SensorsActivity extends Activity implements SensorEventListener {

    private static final double RADIANTODEGREES = 56.962;

    private SensorManager sensorManager;
    Sensor accelerometer;
    Sensor magnetometer;

    TextView roll;
    TextView pitch;
    TextView azimuth;
    EditText edIP;
    EditText edPORT;
    CheckBox cbSEND;

    private final float[] mAccelerometerReading = new float[3];
    private final float[] mMagnetometerReading = new float[3];

    private final float[] mRotationMatrix = new float[9];
    private final float[] mOrientationAngles = new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);

        edIP = (EditText) findViewById(R.id.editTextIP);
        edPORT =(EditText) findViewById(R.id.editTextPORT);
        cbSEND=(CheckBox) findViewById(R.id.checkBoxSend);

        roll = (TextView) findViewById(R.id.rollText);
        pitch = (TextView) findViewById(R.id.pitchText);
        azimuth = (TextView) findViewById(R.id.azimuthText);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);


    }

    @Override
    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, magnetometer, sensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, mAccelerometerReading,
                    0, mAccelerometerReading.length);
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, mMagnetometerReading,
                    0, mMagnetometerReading.length);
        }
        updateOrientationAngles();
        //updateText();
    }

    public void updateOrientationAngles() {
        SensorManager.getRotationMatrix(mRotationMatrix, null, mAccelerometerReading, mMagnetometerReading);
        SensorManager.getOrientation(mRotationMatrix, mOrientationAngles);
        updateText();
    }

    public void updateText() {
        roll.setText("Roll: " + (mOrientationAngles[2] + 3.14));
        pitch.setText("Pitch: " +  (mOrientationAngles[1] + 3.14));
        azimuth.setText("Azymut: " + (mOrientationAngles[0] + 3.14));
        if(cbSEND.isChecked())
            sendMessage("Azymut: " + mOrientationAngles[0] + 3.14 + " Roll: " + (mOrientationAngles[2] + 3.14)+ " Pitch: " +  (mOrientationAngles[1] + 3.14));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void sendMessage(final String message) {
        //final Handler handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            //String stringData;
            @Override
            public void run() {
                DatagramSocket ds = null;
                try {
                    ds = new DatagramSocket();
                    // IP Address below is the IP address of that Device where server socket is opened.
                    InetAddress serverAddr = InetAddress.getByName(edIP.getText().toString());
                    DatagramPacket dp;
                    dp = new DatagramPacket(message.getBytes(), message.length(), serverAddr, Integer.parseInt(edPORT.getText().toString()));
                    ds.send(dp);

                    byte[] lMsg = new byte[1000];
                    dp = new DatagramPacket(lMsg, lMsg.length);
                    ds.receive(dp);
                    //stringData = new String(lMsg, 0, dp.getLength());

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (ds != null) {
                        ds.close();
                    }
                }
            }
        });
        thread.start();

    }
}

     /* private void sendOrientation() {

      Thread thread = new Thread(new Runnable() {

            private String stringData = null;

            @Override
            public void run() {

                byte[] msg = new byte[1024];
                DatagramPacket dp = new DatagramPacket(msg, msg.length);
                DatagramSocket ds = null;
                try {
                    ds = new DatagramSocket(801);
                    //ds.setSoTimeout(50000);

                    stringData = new String(msg, 0, dp.getLength());

                    String msgToSender = ((int) (mOrientationAngles[0] * RADIANTODEGREES)) + ":" + ((int) (mOrientationAngles[1] * RADIANTODEGREES)) + ":" + ((int) (mOrientationAngles[2] * RADIANTODEGREES));
                    dp = new DatagramPacket(msgToSender.getBytes(), msgToSender.length(), dp.getAddress(), dp.getPort());
                    ds.send(dp);

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (ds != null) {
                        ds.close();
                    }
                }
            }

        });
        thread.start();
    }
}*/

