package com.cursosdedesarrollo.sensoresandroid;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor sensorBrujula;
    private Sensor sensorAcelerometro;
    private Sensor sensorGiroscopio;
    private TextView textView;
    private TextView xaxis;
    private TextView yaxis;
    private TextView zaxis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textView=(TextView)findViewById(R.id.textView);
        xaxis=(TextView)findViewById(R.id.xaxis);
        yaxis=(TextView)findViewById(R.id.yaxis);
        zaxis=(TextView)findViewById(R.id.zaxis);
        prepareSensorManager();
        //preparar el sensor de la brujula
        prepareMagnetic();
        //preparar el sensor del acelerómetro
        prepareAccelerometer();
        //preparar el sensor del giroscopio
        prepareGyroscopy();
        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
    }

    private void prepareSensorManager() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
    }

    private void prepareGyroscopy() {
        sensorGiroscopio = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    private void prepareAccelerometer() {
        sensorAcelerometro = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    private void prepareMagnetic() {
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null){
            /*
            // Success! There's a magnetometer.
            Log.d("app","Hay en magnetómetro");
            List<Sensor> gravSensors = mSensorManager.getSensorList(Sensor.TYPE_GRAVITY);
            for(int i=0; i<gravSensors.size(); i++) {
                sensorBrujula =gravSensors.get(i);
                Log.d("app","Sensor:"+ sensorBrujula);

            }
            */
            sensorBrujula =mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        }
        else {
            // Failure! No magnetometer.
            Log.d("app","No hay en magnetómetro");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //registrar sensores
        mSensorManager.registerListener(this, sensorBrujula, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, sensorAcelerometro, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, sensorGiroscopio, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //desregistrar sensores
        mSensorManager.unregisterListener(this);
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

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // The light sensor returns a single value.
        // Many sensors return 3 values, one for each axis.
        //Log.d("app","onSensorChanged");
        //Log.d("app","sensor:"+sensorEvent.sensor.getType());
        if(sensorEvent.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD){
            //Log.d("app","sensor:"+sensorEvent.sensor.getType());
            float valor = sensorEvent.values[0];
            //Log.d("app","valor:"+valor);
            BigDecimal result;
            result=round(valor,2);
            textView.setText(""+result);
            // Do something with this sensor value.
        }
        if(sensorEvent.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            float valorx = sensorEvent.values[0];
            float valory = sensorEvent.values[1];
            float valorz = sensorEvent.values[2];
            BigDecimal result;
            result=round(valorx,2);
            textView.setText(""+result);
            xaxis.setText(""+result);
            result=round(valory,2);
            yaxis.setText(""+result);
            result=round(valorz,2);
            zaxis.setText(""+result);
        }

    }
    public static BigDecimal round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
