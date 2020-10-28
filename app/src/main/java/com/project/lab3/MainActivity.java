package com.project.lab3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";

    Button buttonStart, buttonStop;
    Button buttonBind, buttonUnBind;
    Button buttonGetRandomNumber;
    TextView tvRandomNumber;


    Intent sIntent;
    ServiceConnection myServiceConnection;
    MyService myService;
    boolean isBound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set layout
        setContentView(R.layout.activity_main);

        //initialize UI elements
        buttonStart = findViewById(R.id.btn_start);
        buttonStop = findViewById(R.id.btn_stop);
        buttonBind = findViewById(R.id.btn_bind);
        buttonUnBind = findViewById(R.id.btn_unbind);
        buttonGetRandomNumber = findViewById(R.id.btn_getNumber);
        tvRandomNumber = findViewById(R.id.tv_random_number);

        //set on click listener
        buttonStart.setOnClickListener(this);
        buttonStop.setOnClickListener(this);
        buttonBind.setOnClickListener(this);
        buttonUnBind.setOnClickListener(this);
        buttonGetRandomNumber.setOnClickListener(this);

        sIntent = new Intent(this, MyService.class);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                //1: START A SERVICE
                startMyService();
                break;
            case R.id.btn_stop:
                //2: STOP A SERVICE
                stopMyService();
                break;
            case R.id.btn_bind:
                //3: BIND A SERVICE
                bindMyService();
                break;
            case R.id.btn_unbind:
                //4: UNBIND A SERVICE
                unbindMyService();
                break;
            case R.id.btn_getNumber:
                //TODO 5: GET AND RANDOM NUMBER FROM SERVICE AND DISPLAY IT
                showRandomNum();
                break;
        }
    }

    private void showRandomNum() {
        if (isBound) {
            tvRandomNumber.setText("Random Number is : " + myService.getRD());
        } else {
            tvRandomNumber.setText("not bound yet");
        }
    }

    private void unbindMyService() {
        Log.i(TAG, "unbindMyService");
        isBound = false;
        unbindService(myServiceConnection);
        myServiceConnection = null;
    }

    private void bindMyService() {
        Log.i(TAG, "bindMyService");
        if (myServiceConnection == null) {
            myServiceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    Log.i(TAG, "onServiceConnected");
                    MyService.MyBinder binder = (MyService.MyBinder) iBinder;
                    myService = ((MyService.MyBinder) iBinder).getService();
                    isBound = true;

                }

                @Override
                public void onServiceDisconnected(ComponentName componentName) {
                    Log.i(TAG, "onServiceDisconnected");
                    isBound = false;
                }
            };
        }

        bindService(sIntent, myServiceConnection, BIND_AUTO_CREATE);
    }

    private void stopMyService() {
        Log.i(TAG, "stopMyService");
        stopService(sIntent);
    }

    private void startMyService() {
        Log.i(TAG, "startMyService");
        startService(sIntent);
    }


}