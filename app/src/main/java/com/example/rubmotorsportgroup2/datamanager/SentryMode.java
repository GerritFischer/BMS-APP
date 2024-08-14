package com.example.rubmotorsportgroup2.datamanager;

import static com.example.rubmotorsportgroup2.MainActivity.dm;
import static com.example.rubmotorsportgroup2.MainActivity.sm;

import android.util.Log;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;


public class SentryMode {

    private boolean running = sm.isSentryMode();
    private final int tickTime = 10;
    Dictionary<String, Integer> errorDic = new Hashtable<>();

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
                        if (timer >= 1000) {
                            timer = 0;
                            if (dm.hasError() > 0) {
                                Log.i("sentrymode", "found errors");
                            ArrayList<Errorcode> allErrors = dm.getErrorsAsList();
                            ArrayList<String> dicKeys = (ArrayList<String>) errorDic.keys();
                            for (int i = 0; i < allErrors.size(); i++){
                                String currError = allErrors.get(i).toString();
                                if (dicKeys.contains(currError)){
                                    errorDic.put(currError, (errorDic.get(currError) + 1));
                                }

                                else {
                                    Log.i("sentrymode", "added error");
                                    errorDic.put(currError, 1);
                                }

                            }
                            ArrayList<String> dicKeysUpdated = (ArrayList<String>) errorDic.keys();
                                for (int i = 0; i < dicKeysUpdated.size(); i++){
                                    String currKey = dicKeysUpdated.get(i);
                                    for (int j = 0; i < allErrors.size(); i++) {
                                        String currError2 =  allErrors.get(j).toString();
                                        if (!dicKeysUpdated.contains(currError2)){
                                            errorDic.remove(currKey);
                                        }
                                    }
                                }



                            //schau ob was mehr als 1 ist dann email versenden bei 2 sec, 5 min , 30 min
                        }

                            ArrayList<String> finalDicKeys = (ArrayList<String>) errorDic.keys();
                            for (int i = 0; i < finalDicKeys.size(); i++){
                                String currKey = finalDicKeys.get(i);
                                if ((errorDic.get(currKey)) == 2 ) {
                                    EmailSender SendAt2Secs = new EmailSender();
                                    SendAt2Secs.SendErrorMail(finalDicKeys);
                                    Log.i("sentrymode", "send mail");
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
                            Thread.sleep(tickTime);
                            timer += tickTime;
                        }
                    } catch (Exception e) {
                        Log.i("sentrymode", "Exception");
                    }
                }

            }
        }
    }

}