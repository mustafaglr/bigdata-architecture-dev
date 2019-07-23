package com.example.sendmessagewithsocket;

import android.content.Context;
import android.content.Intent;
import android.hardware.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.*;

import java.net.*;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private Socket socket;
    private SensorManager sm;
    private Sensor sensor;

    private TriggerEventListener triggerEventListener;

    static final float NS2S = 1.0f / 1000000000.0f;
    float[] gravity = new float[3];
    float[] linear_acceleration = new float[3];
    float[] velocityOld = null;
    float[] position = null;
    float[] acceleration = null;
    long last_timestamp = 0;


    EditText xAxis,yAxis,zAxis;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText ipAdress=(EditText)findViewById(R.id.ipAdress);
        final EditText port=(EditText)findViewById(R.id.port);
        final EditText x=(EditText)findViewById(R.id.x);
        final EditText y=(EditText)findViewById(R.id.y);
        final EditText z=(EditText)findViewById(R.id.z);

        xAxis=x; yAxis=y; zAxis=z;

        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if(sm.getSensorList(Sensor.TYPE_ACCELEROMETER).size()!=0){
            sensor = sm.getSensorList(Sensor.TYPE_LINEAR_ACCELERATION).get(0);
            sm.registerListener(this,sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }






        findViewById(R.id.myButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            socket = new Socket(ipAdress.getText().toString(), Integer.parseInt(port.getText().toString()));
                            PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
                            printWriter.write(x.getText().toString()+","+y.getText().toString()+","+z.getText().toString()+"\n");
                            printWriter.flush();
                            printWriter.close();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();


            }
        });
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        final float alpha = 0.8f;



        // Isolate the force of gravity with the low-pass filter.
        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

        // Remove the gravity contribution with the high-pass filter.
        linear_acceleration[0] = event.values[0] - gravity[0];
        linear_acceleration[1] = event.values[1] - gravity[1];
        linear_acceleration[2] = event.values[2] - gravity[2];


        xAxis.setText(String.valueOf(linear_acceleration[0]));
        yAxis.setText(String.valueOf(linear_acceleration[1]));
        zAxis.setText(String.valueOf(linear_acceleration[2]));

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


}
