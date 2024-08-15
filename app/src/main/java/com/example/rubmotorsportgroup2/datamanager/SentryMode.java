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

    public  SentryMode () {
        SentryModeThread s = new SentryModeThread();
        s.start();
    }

    public class SentryModeThread extends Thread {

        @Override
        public void run() {
            while (true) {
                running = sm.isSentryMode();
                int timer = 1000;

                while (running) {
                    try {
                        running = sm.isSentryMode();
                        if (timer >= 1000) {
                            timer = 0;
                            if (dm.hasError() > 0) {
                                ArrayList<Errorcode> allErrors = dm.getErrorsAsList();
                                ArrayList<String> dicKeys = Collections.list(errorDic.keys());
                                for (int i = 0; i < allErrors.size(); i++) {
                                    String currError = allErrors.get(i).toString();
                                    if (dicKeys.contains(currError)) {
                                        errorDic.put(currError, (errorDic.get(currError) + 1));
                                    } else {
                                        errorDic.put(currError, 1);
                                    }

                                }

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
                            ArrayList<String> finalDicKeys = Collections.list(errorDic.keys());
                            for (int i = 0; i < finalDicKeys.size(); i++){
                                String currKey = finalDicKeys.get(i);
                                if ((errorDic.get(currKey)) == 2 ) {
                                    EmailSender SendAt2Secs = new EmailSender();
                                    SendAt2Secs.SendErrorMail(finalDicKeys);
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