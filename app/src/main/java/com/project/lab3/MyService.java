package com.project.lab3;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.nfc.Tag;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.Random;

public class MyService extends Service {
    public MyService() {
    }


    private static final String TAG = "MyService";
    private boolean isGenerate;
    private int randomNum;

    public class MyBinder extends Binder {
        public MyService getService() {
            return MyService.this;
        }
    }


    private Binder myBinder = new MyBinder();
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.i(TAG, "onBind");
        return myBinder;

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        Log.i(TAG, "Thread ID:" + Thread.currentThread().getId() + "Start ID" + startId);

        if (isGenerate) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "generate random number running");
                    isGenerate = true;
                    generateRandomNumber();
                }
            };
            Thread thread = new Thread(runnable);
            // run method is not multi-thread, program will
            // thread.run();
            thread.start();
        }
        return START_NOT_STICKY;
    }

    private void generateRandomNumber() {
        while (isGenerate) {
            try {
                Thread.sleep(1000);
            }catch (Exception e) {
                e.printStackTrace();
            }

            randomNum = new Random().nextInt(100);
            Log.i(TAG, "random Number: " + randomNum);
        }
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        isGenerate = false;
        super.onDestroy();
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        Log.i(TAG, "unbindService");
        super.unbindService(conn);
    }

    public int getRD() {
        return randomNum;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "onUnbind");
        return super.onUnbind(intent);
    }
}
