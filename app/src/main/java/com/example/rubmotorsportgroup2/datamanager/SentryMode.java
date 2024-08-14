package com.example.rubmotorsportgroup2.datamanager;

import static com.example.rubmotorsportgroup2.MainActivity.dm;
import static com.example.rubmotorsportgroup2.MainActivity.sm;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;


public class SentryMode {

    private boolean running = sm.isSentryMode();
    private final int tickTime = 10;
    Dictionary<String, Integer> errorDic = new Hashtable<>();
    int x = 1;

    public  SentryMode () {
        SentryModeThread s = new SentryModeThread();
        s.start();
    }

    public class SentryModeThread extends Thread {

        @Override
        public void run() {
            while (true) {
                Log.i("sentrymode", "Sentry mode not running");
                running = sm.isSentryMode();
                int timer = 1000;

                while (running) {
                    Log.i("sentrymode", "Sentry mode running");
                    try {
                        running = sm.isSentryMode();
                        Log.i("sentrymodeError", "warum hänge ich hier");
                        if (timer >= 1000) {
                            timer = 0;
                            if (dm.hasError() > 0) {
                                Log.i("sentrymodeError", "found errors");
                                ArrayList<Errorcode> allErrors = dm.getErrorsAsList();
                                ArrayList<String> dicKeys = Collections.list(errorDic.keys());
                                x = 4;
                                Log.i("sentrymodeError", " " + allErrors.size());
                                for (int i = 0; i < allErrors.size(); i++) {
                                    String currError = allErrors.get(i).toString();
                                    if (dicKeys.contains(currError)) {
                                        x = 40;
                                        Log.i("sentrymode1", "increase error " + i);
                                        errorDic.put(currError, (errorDic.get(currError) + 1));
                                    } else {
                                        Log.i("sentrymode1", "added error " + i);
                                        errorDic.put(currError, 1);
                                    }

                                }
                                x = 5;
                                Log.i("sentrymode1", "arbeitet weiter danach " );

                                ArrayList<String> dicKeysUpdated = Collections.list(errorDic.keys());
                                for (int i = 0; i < dicKeysUpdated.size(); i++){
                                    String currKey = dicKeysUpdated.get(i);
                                    Boolean contained = false;
                                    for (int j= 0; j < allErrors.size(); j++){
                                     String currError = allErrors.get(j).toString();
                                     if (currError.equals(currKey)){
                                         contained = true;
                                     }
                                    }
                                    if (!contained){
                                        errorDic.remove(currKey);
                                    }
                                }


                            }
                            Log.i("sentrymode2", "nach dem found error");
                            ArrayList<String> finalDicKeys = Collections.list(errorDic.keys());
                            Log.i("sentrymode2", "nach dem found error und Collections");
                            for (int i = 0; i < finalDicKeys.size(); i++){
                                Log.i("sentrymode2", "bin in der for schleife");
                                String currKey = finalDicKeys.get(i);
                                if ((errorDic.get(currKey)) == 2 ) {
                                    sm.addMail("maxiwojak2407@gmail.com");
                                    EmailSender SendAt2Secs = new EmailSender();
                                    SendAt2Secs.SendErrorMail(finalDicKeys);
                                    Log.i("sentrymodeMail", "send mail   " + String.join(", ", sm.getMails()));
                                    break;
                                }
                            }
                            for (int i = 0; i < finalDicKeys.size(); i++){
                                String currKey = finalDicKeys.get(i);
                                if ((errorDic.get(currKey)) == 300 ) {
                                    EmailSender SendAt2Secs = new EmailSender();
                                    SendAt2Secs.SendErrorMail(finalDicKeys);
                                    break;
                                }
                            }
                            for (int i = 0; i < finalDicKeys.size(); i++){
                                String currKey = finalDicKeys.get(i);
                                if ((errorDic.get(currKey)) == 1800 ) {
                                    EmailSender SendAt2Secs = new EmailSender();
                                    SendAt2Secs.SendErrorMail(finalDicKeys);
                                    break;
                                }
                            }
                        }
                        else{
                            Log.i("sentrymode3", "increase of time");
                            Thread.sleep(tickTime);
                            timer += tickTime;
                        }
                    } catch (Exception e) {
                        Log.i("sentrymodeError2", "Exception von Maxi" + x);
                    }
                }

            }
        }
    }

}