package com.example.rubmotorsportgroup2.datamanager;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class SentryMode extends Service {

    private final IBinder SentryModeBinder = new MyLocalBinder();

    // Bro benutze threads, und Service is veraltet und kacke
    Runnable r = new Runnable() {
        @Override
        public void run() {
            while (kpbutton):

        }
    };
    @Override
    public IBinder onBind(Intent intent) {

    }


    public class MyLocalBinder extends Binder {
        SentryMode getService(){
            return SentryMode.this;
        }
    }
}