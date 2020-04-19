package com.example.accelerometerexample;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private TextView x_value,y_value,z_value;
    SensorManager manager;
    Sensor accelerometer;
    float currentX,currentY,currentZ;
    float lastX,lastY,lastZ;

    float diffX,diffY,diffZ;
    boolean isAvailable;
    boolean isNotFirst=false;

    Vibrator vibrator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        x_value=(TextView) findViewById(R.id.x_value);
        y_value=(TextView) findViewById(R.id.y_value);
        z_value=(TextView) findViewById(R.id.z_value);

        manager= (SensorManager) this.getSystemService(SENSOR_SERVICE);


        if(manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!=null)
        {
            accelerometer=manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            isAvailable=true;
        }
        else
        {
            Toast.makeText(this, "Senor not available", Toast.LENGTH_SHORT).show();
            isAvailable=false;
        }




    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        x_value.setText(event.values[0]+"m/s2");
        y_value.setText(event.values[1]+"m/s");
        z_value.setText(event.values[2]+"m/s2");

        currentX=event.values[0];
        currentY=event.values[1];
        currentZ=event.values[2];

        if(isNotFirst)
        {
            diffX=Math.abs(lastX-currentX);
            diffY=Math.abs(lastY-currentY);
            diffZ=Math.abs(lastZ-currentZ);
        }

        lastX=currentX;
        lastY=currentY;
        lastZ=currentZ;

        if((diffX>5f && diffY>5f) || (diffX>5f && diffZ>5f) ||(diffY>5f && diffZ>5f))
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE));
            }
            else
            {
                vibrator.vibrate(500);
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isAvailable)
        manager.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.unregisterListener(this);
    }
}
