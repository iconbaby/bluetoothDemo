package com.slkk.bluetoothdemo;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BluetoothFragment extends Fragment {
    private static final String TAG = "BluetoothFragment";
    private BluetoothAdapter mDefaultAdapter;
    private static final int REQUST_ENABLE_BT = 1;
    private ArrayAdapter mConversationAdapter;
    private ListView mListviewChat;
    private Button mBtnSend;
    private EditText mEtInput;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private TextView.OnEditorActionListener mWriteListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
                String message = v.getText().toString();
                sendMessage(message);
            }
            return true;
        }
    };
    private BluetoothService mBluetoothService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mDefaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mDefaultAdapter == null) {
            FragmentActivity activity = getActivity();
            Toast.makeText(activity, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            activity.finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!mDefaultAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQUST_ENABLE_BT);
        } else {
            setupChat();
        }
    }


    private void setupChat() {
        mConversationAdapter = new ArrayAdapter<String>(getActivity(), R.layout.message);
        mListviewChat.setAdapter(mConversationAdapter);

        mEtInput.setOnEditorActionListener(mWriteListener);

        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputText = mEtInput.getText().toString().trim();
                sendMessage(inputText);
            }
        });

        mBluetoothService = new BluetoothService(getActivity(), mHandler);
    }

    private void sendMessage(String inputText) {
        
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bluetooth, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mListviewChat = view.findViewById(R.id.fragment_bluetooth_lv_chat);
        mBtnSend = view.findViewById(R.id.fragment_bluetooth_bt_send);
        mEtInput = view.findViewById(R.id.fragment_bluetooth_et_input);
    }
}
