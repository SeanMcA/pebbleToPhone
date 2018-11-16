package com.example.sitting_room.pebbletophone;
/*
    0 = General location
    1 = location of putt
    2 = location of flag
 */

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class recordCoords extends Service implements Observer{
    private static final UUID WATCHAPP_UUID = UUID.fromString("6092637b-8f58-4199-94d8-c606b1e45040");
    boolean collectingData = false;

    double lat ;
    double lng;
    double acc;
    int puttCounter = 0;
    int holeNumber = 1;
    Button startButton;
    Button stopButton;
    TextView accuracyTextview;
    TextFile tf;
    Boolean enableButton = true;
    private static Context cxt;
    private static final int KEY_BUTTON = 0;
    private static final int BUTTON_UP = 0;
    private static final int BUTTON_SELECT = 1;
    private static final int BUTTON_DOWN = 2;
    private Handler handler = new Handler();
    private PebbleKit.PebbleDataReceiver appMessageReciever;
    private static final String TAG = "Pebble";
    private static final boolean logging = true;


    @Override
    public void onCreate() {
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        Log.i(TAG, "recordCoords Service started");
        cxt = this;
        tf = new TextFile();
        GPS gps = new GPS(this);
        Coordinates coord = new Coordinates();
        coord.registerObserver(this);//todo - check if registered already

        // Define AppMessage behavior
        if(appMessageReciever == null) {
            appMessageReciever = new PebbleKit.PebbleDataReceiver(WATCHAPP_UUID) {

                @Override
                public void receiveData(Context context, int transactionId, PebbleDictionary data) {
                    // Always ACK
                    PebbleKit.sendAckToPebble(context, transactionId);

                    // What message was received?
                    if(data.getInteger(KEY_BUTTON) != null) {
                        // KEY_BUTTON was received, determine which button
                        final int button = data.getInteger(KEY_BUTTON).intValue();

                        // Update UI on correct thread
                        handler.post(new Runnable() {

                            @Override
                            public void run() {
                                switch(button) {
                                    case BUTTON_UP: // putt
                                        recordPutt();
                                        //whichButtonView.setText("Putt");
                                        break;
                                    case BUTTON_SELECT:
                                        //recordHole();
                                        if(!collectingData){
                                            startCollectingData();
                                        }else{
                                            stopCollectingData();
                                        }
                                        //whichButtonView.setText("SELECT");
                                        break;
                                    case BUTTON_DOWN: // flag
                                        recordFlagLocation();
                                        //whichButtonView.setText("Flag");
                                        break;
                                    default:
                                        Toast.makeText(getApplicationContext(), "Unknown button: " + button, Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }

                        });
                    }
                }
            };

            // Add AppMessage capabilities
            PebbleKit.registerReceivedDataHandler(this, appMessageReciever);
        }

    }//onCreate




    public void startCollectingData(){
        String data = "Hole number: " + holeNumber + "\r\n";//1 means this is a putt location.
        tf.writeData(data, true);
        holeNumber ++;
        if (logging) Log.i(TAG, "recording Putt");
        if(logging) Log.i(TAG, "startCollectingData started");
        Toast toast = Toast.makeText(this, "Starting data collection.", Toast.LENGTH_SHORT);
        toast.show();
        collectingData = true;
        Log.i(TAG, "collecting data");
        final Handler hdlr = new Handler();
        final int delay = 1000; // milliseconds OR 1 second

        hdlr.postDelayed(new Runnable() {
            public void run() {
                if(collectingData) { // stops auto gps data collection
                    String data = lat + ", " + lng + ", 0" + "\r\n";//0 means this is a general location record.
                    tf.writeData(data, true);
                }
                    hdlr.postDelayed(this, delay);
            }
        }, delay);

    }

    public void stopCollectingData(){
                if(logging) Log.i(TAG, "stopCollectingData started");
                collectingData = false;
                Toast toast = Toast.makeText(cxt, "Stopping data collection.", Toast.LENGTH_SHORT);
                toast.show();
    }



    public void recordPutt(){
        String data = lat + ", " + lng + ", 1" +"\r\n";//1 means this is a putt location.
        tf.writeData(data, true);
        puttCounter++;
        if (logging) Log.i(TAG, "recording Putt");
    }

    public void recordFlagLocation(){
        String data = lat + ", " + lng + ", 2" +"\r\n";//2 means this is a flag location.
        tf.writeData(data, true);
        if (logging) Log.i(TAG, "recording flag location");
    }



    @Override
    public void update(double currentLatitude, double currentLongitude, double accuracy) {
        this.lat = currentLatitude;
        this.lng = currentLongitude;
        this.acc = accuracy;

        accuracyTextview.setText("" + accuracy);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_LONG).show();
        unregisterReceiver(appMessageReciever);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}//class
