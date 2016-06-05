package com.example.mrbank.blackboxdesign.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SituationService extends Service {

    private final String logName ="TAG";

   private Sensor mAccelerometer;
    private Sensor mProximity;
    private SensorManager mSensorManager;
    private SensorEventListener mSensorListener;

    private float mLastX;
    private float mLastY;
    private float mLastZ;

    private int curSecs;                // to avaoid writing one more values to list per a second
    private int lastSecs;

    private long currentTime;


   private int SENSORDELAY = 10000;

   private double vector;           // delta accelerometer in three axis
   private boolean yakınlık;

    class sensorDatum{       // sensor values will been sent by this struct
        private double deltaAccelerometer;
        private boolean approxmity ;
        private long miliseconds;

        public sensorDatum(double deltaAccelerometer,boolean approxmity,long miliseconds) {
            this.deltaAccelerometer = deltaAccelerometer;
            this.approxmity = approxmity;
            this.miliseconds = miliseconds;

        }


        public double getDeltaAccelerometer() {
            return deltaAccelerometer;
        }

        public sensorDatum setDeltaAccelerometer(int deltaAccelerometer) {
            this.deltaAccelerometer = deltaAccelerometer;
            return this;
        }

        public boolean isApproxmity() {
            return approxmity;
        }

        public sensorDatum setApproxmity(boolean approxmity) {
            this.approxmity = approxmity;
            return this;
        }

        public long getMiliseconds() {
            return miliseconds;
        }

        public sensorDatum setMiliseconds(long miliseconds) {
            this.miliseconds = miliseconds;
            return this;
        }
    }

    ArrayList<sensorDatum> list;




    public SituationService() {
    }

    public void onCreate(){

        list = new ArrayList<sensorDatum>();  // list will been sent to algorithm

        lastSecs = 100 ;                      // initilazed with out of range number

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);



        mSensorManager = (SensorManager) this
                .getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new SensorEventListener() {
            @Override
            public void onAccuracyChanged(Sensor arg0, int arg1) {
            }

            @Override
            public void onSensorChanged(SensorEvent event) {
                currentTime = System.currentTimeMillis();
                Sensor sensor = event.sensor;
                if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                   vector = deltaAccelerometer(event);
                }
                if (sensor.getType() == Sensor.TYPE_PROXIMITY) {
                     yakınlık = proximty(event);
                }
                curSecs = milisecondsToDate(currentTime);
                //System.out.print("Now:"+curSecs);
                if( curSecs != lastSecs ) {
                    list.add(new sensorDatum(vector, yakınlık, currentTime)); // delta accelerometer and proximity values will write to memory
                    lastSecs = curSecs;
                    //System.out.println("Vector" + vector + "Prox:" + yakınlık + "CT:" + currentTime);
                    Log.i(logName,"Vector" + vector + "Prox:" + yakınlık);
                    //Log.i(logName,""+vector);

                }


            }
        };


        //Details http://stackoverflow.com/questions/30153904/android-how-to-set-sensor-delay
        // yaptım ama yemedi saniyelik veri almak için bu yüzden yukarıdaki yöntem devam edecekg
        mSensorManager.registerListener(mSensorListener,mAccelerometer,mSensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(mSensorListener, mProximity, mSensorManager.SENSOR_DELAY_NORMAL);

        System.out.println("It is started");


    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void onDestroy(){
        mSensorManager.unregisterListener(mSensorListener);
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }


    public double deltaAccelerometer(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        float deltaX = Math.abs(mLastX - x);
        float deltaY = Math.abs(mLastY - y);
        float deltaZ = Math.abs(mLastZ - z);

        double vector = deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ;
        vector = Math.sqrt(vector);                             // calculated deltad

        //System.out.println("Delta Accelerometer:" + vector);

        mLastX = x;
        mLastY = y;
        mLastZ = z;

        return vector;
    }

    public boolean proximty(SensorEvent event){

        if( event.values[0] == 0  )
        {
        yakınlık = false;
        }
        else {
            yakınlık = true;
        }

        //System.out.println("Yakınlık:"+event.values[0]);
        return yakınlık;
    }

    public int milisecondsToDate(long milliseconds) {

        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(milliseconds);
        int year = time.get(Calendar.YEAR);
        int month = time.get(Calendar.MONTH);
        int date = time.get(Calendar.DATE);
        int hour = time.get(Calendar.HOUR);
        int min = time.get(Calendar.MINUTE);
        int sec = time.get(Calendar.SECOND);

        //System.out.println(year+"/"+month+"/"+date+"  "+hour+":"+min+":"+sec);
        return sec;



    }


    // Sinan'ın Methodu
    // sensör değeleri + değerin okunduğu zaman "list" adlı sınıfnn içinde

}
