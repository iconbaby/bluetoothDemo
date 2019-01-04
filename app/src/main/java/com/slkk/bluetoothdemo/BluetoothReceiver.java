package com.slkk.bluetoothdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import static android.bluetooth.BluetoothAdapter.EXTRA_STATE;

public class BluetoothReceiver extends BroadcastReceiver {

    private static final String TAG = "BluetoothReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        int intExtra = intent.getIntExtra(EXTRA_STATE,0);
        Log.d(TAG, "onReceive: " + intExtra);
    }
}
