package com.star.broadcasttest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String ACTION_CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";
    public static final String ACTION_MY_BROADCAST = "com.star.broadcasttest.MY_BROADCAST";
    public static final String ACTION_LOCAL_BROADCAST = "com.star.broadcasttest.LOCAL_BROADCAST";

    private Button mBroadcastButton;
    private Button mLocalBroadcastButton;

    private IntentFilter mBroadcastIntentFilter;
    private NetworkChangeReceiver mNetworkChangeReceiver;

    private LocalBroadcastManager mLocalBroadcastManager;

    private IntentFilter mLocalBroadcastIntentFilter;
    private LocalBroadcastReceiver mLocalBroadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);

        mBroadcastButton = (Button) findViewById(R.id.broadcast_button);
        mBroadcastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ACTION_MY_BROADCAST);
                sendOrderedBroadcast(intent, null);
            }
        });

        mLocalBroadcastButton = (Button) findViewById(R.id.local_broadcast_button);
        mLocalBroadcastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ACTION_LOCAL_BROADCAST);
                mLocalBroadcastManager.sendBroadcast(intent);
            }
        });

        mBroadcastIntentFilter = new IntentFilter();
        mBroadcastIntentFilter.addAction(ACTION_CONNECTIVITY_CHANGE);

        mNetworkChangeReceiver = new NetworkChangeReceiver();

        registerReceiver(mNetworkChangeReceiver, mBroadcastIntentFilter);

        mLocalBroadcastIntentFilter = new IntentFilter();
        mLocalBroadcastIntentFilter.addAction(ACTION_LOCAL_BROADCAST);

        mLocalBroadcastReceiver = new LocalBroadcastReceiver();
        mLocalBroadcastManager.registerReceiver(mLocalBroadcastReceiver,
                mLocalBroadcastIntentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(mNetworkChangeReceiver);
        mLocalBroadcastManager.unregisterReceiver(mLocalBroadcastReceiver);
    }

    private class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            ConnectivityManager connectivityManager = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isAvailable()) {
                Toast.makeText(context, "Network is available", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Network is unavailable", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class LocalBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Toast.makeText(context, "Received in LocalBroadcastReceiver", Toast.LENGTH_LONG).show();
        }
    }
}
