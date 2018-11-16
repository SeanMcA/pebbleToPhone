package com.example.sitting_room.pebbletophone;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.getpebble.android.kit.PebbleKit.PebbleDataReceiver;

public class MainActivity extends AppCompatActivity {

    private static final String WATCHAPP_FILENAME = "android-example.pbw";
    private static final String TAG = "Main Activity: ";
    private static final boolean INFO = true;

    private PebbleDataReceiver appMessageReciever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (INFO) Log.i(TAG, "onCreate started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Customize ActionBar
        //ActionBar actionBar = getActionBar();
        //actionBar.setTitle("PebbleKit Example");
        //actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionbar_orange)));

        // Add Install Button behavior
        Button installButton = findViewById(R.id.button_install);
        installButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Install
                Toast.makeText(getApplicationContext(), "Installing watchApp...", Toast.LENGTH_SHORT).show();
                sideloadInstall(getApplicationContext(), WATCHAPP_FILENAME);
                Intent intent = new Intent(MainActivity.this, recordCoords.class);
                startActivity(intent);
            }
        });

        Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, recordCoords.class);
                startActivity(intent);
            }
        });
    } // onCreate



    @Override
    protected void onPause() {
        super.onPause();

        // Unregister AppMessage reception
        if(appMessageReciever != null) {
            unregisterReceiver(appMessageReciever);
            appMessageReciever = null;
        }
    }

    /**
     * Alternative sideloading method
     * Source: http://forums.getpebble.com/discussion/comment/103733/#Comment_103733
     */
    public static void sideloadInstall(Context ctx, String assetFilename) {
        try {
            // Read .pbw from assets/
            Intent intent = new Intent(Intent.ACTION_VIEW);
            File file = new File(ctx.getExternalFilesDir(null), assetFilename);
            InputStream is = ctx.getResources().getAssets().open(assetFilename);
            OutputStream os = new FileOutputStream(file);
            byte[] pbw = new byte[is.available()];
            is.read(pbw);
            os.write(pbw);
            is.close();
            os.close();

            // Install via Pebble Android app
            intent.setDataAndType(Uri.fromFile(file), "application/pbw");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(intent);
        } catch (IOException e) {
            Toast.makeText(ctx, "App install failed: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

} // class
