package com.example.rubmotorsportgroup2.datamanager;

import androidx.annotation.NonNull;
import com.example.rubmotorsportgroup2.BuildConfig;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class DataRetriever {
    private final DataManager dm;


    public DataRetriever(DataManager dm) {
        this.dm = dm; //set data manager to send data back
    }
    public void start(){
        OkHttpClient client = new OkHttpClient(); //init websocket client
        String url = "wss://tsac.rubmotorsport.de/live";
        String credential = Credentials.basic(BuildConfig.USERNAME, BuildConfig.PASSWORD); //get cred from local.prop, not on github
        Request request = new Request.Builder().url(url).addHeader("Authorization", credential).build(); //build request
        RUBWebSocketListener listener = new RUBWebSocketListener(); //init SocketListener
        WebSocket ws = client.newWebSocket(request, listener); //init websocket
        client.dispatcher().executorService().shutdown(); //shutdown dispatcher
    }

    private final class RUBWebSocketListener extends WebSocketListener{
        @Override
        public void onOpen(WebSocket webSocket, @NonNull Response response){
            webSocket.send("setId/Group2");
            webSocket.send("start");
            //sending starting parameters to init data stream
        }

        public void onMessage(@NonNull WebSocket webSocket, String string){
            if(!string.equals("pong") && !string.equals("ok Group2") && !string.equals("ok") ){
                System.out.println("RECEIVED FRAME");
                dm.updateDataSet(string); //check if message is real data, call update with new data
            }
        }
        @Override
        public void onClosing(WebSocket webSocket, int code, @NonNull String reason){
            webSocket.send("stop");
            webSocket.close(1000, null);
        }
        @Override
        public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, Response response){
            //TODO: pray it doesn't fail
        }
    }
}
