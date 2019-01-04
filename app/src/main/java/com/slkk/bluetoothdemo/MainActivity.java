package com.slkk.bluetoothdemo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.Set;

import static android.bluetooth.BluetoothAdapter.EXTRA_STATE;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter mDefaultAdapter;
    private BroadcastReceiver mBluetoothReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initBluetooth();
        registerBluetoothReceiver();
    }

    private void registerBluetoothReceiver() {
        mBluetoothReceiver = new BluetoothReceiver();
        IntentFilter intentFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mBluetoothReceiver, intentFilter);
    }

    private void initBluetooth() {
        mDefaultAdapter = BluetoothAdapter.getDefaultAdapter();
//        mDefaultAdapter.startDiscovery();
        if (mDefaultAdapter != null) {
            Log.d(TAG, "initBluetooth: defaultAdapter != null");
            if (!mDefaultAdapter.isEnabled()) {
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, REQUEST_ENABLE_BT);
            } else {
                queryBondDevices();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
//                Bundle extras = data.getExtras();
//                Set<String> strings = extras.keySet();
//                while (strings.iterator().hasNext()) {
//                    String string = extras.getString(strings.iterator().next());
//                    Log.d(TAG, "onActivityResult: resultCode" + string);
//                }
                if (resultCode == -1) {
                    Log.d(TAG, "onActivityResult: bluetooth enable success");
                    queryBondDevices();
                }
                break;
        }
    }

    private void queryBondDevices() {
        Set<BluetoothDevice> bondedDevices = mDefaultAdapter.getBondedDevices();
        if (bondedDevices.size() > 0) {
            for (BluetoothDevice device : bondedDevices) {
                String name = device.getName();
                String address = device.getAddress();
                Log.d(TAG, "queryBondDevices: name: " + name + " address: " + address);
            }
        }
    }

    class BluetoothReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                int intExtra = intent.getIntExtra(EXTRA_STATE, 0);
                Log.d(TAG, "onReceive: " + intExtra);
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String name = device.getName();
                String address = device.getAddress();
                Log.d(TAG, "onReceive: ACTION_FOUND name: " + name + " address: " + address);
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBluetoothReceiver);
    }
}
