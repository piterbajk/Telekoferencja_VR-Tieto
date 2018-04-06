package com.bibip.sensordatasender;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    private final float[] mAccelerometerReading = new float[3];
    private final float[] mMagnetometerReading = new float[3];

    private final float[] mRotationMatrix = new float[9];
    private final float[] mOrientationAngles = new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);

        roll = (TextView) findViewById(R.id.rollText);
        pitch = (TextView) findViewById(R.id.pitchText);
        azimuth = (TextView) findViewById(R.id.azimuthText);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);


        sendOrientation();
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
        }
        else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, mMagnetometerReading,
                    0, mMagnetometerReading.length);
        }
        updateOrientationAngles();
        //updateText();
    }

    public void  updateOrientationAngles() {
        SensorManager.getRotationMatrix(mRotationMatrix, null, mAccelerometerReading, mMagnetometerReading);
        SensorManager.getOrientation(mRotationMatrix, mOrientationAngles);
        updateText();
    }

    public void updateText() {
        roll.setText("Roll: " + (int) (mOrientationAngles[2] * RADIANTODEGREES));
        pitch.setText("Pitch: " + (int) (mOrientationAngles[1] * RADIANTODEGREES));
        azimuth.setText("Azymut: " + (int) (mOrientationAngles[0] * RADIANTODEGREES));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

    private void sendOrientation() {

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

}
