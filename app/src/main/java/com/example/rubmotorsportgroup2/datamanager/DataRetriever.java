package com.example.rubmotorsportgroup2.datamanager;

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
        this.dm = dm;
    }
    public void start(){
        OkHttpClient client = new OkHttpClient();
        String url = "wss://tsac.rubmotorsport.de/live";
        String credential = Credentials.basic(BuildConfig.USERNAME, BuildConfig.PASSWORD);
        Request request = new Request.Builder().url(url).addHeader("Authorization", credential).build();
        RUBWebSocketListener listener = new RUBWebSocketListener();
        WebSocket ws = client.newWebSocket(request, listener);
        client.dispatcher().executorService().shutdown();
    }

    private final class RUBWebSocketListener extends WebSocketListener{
        @Override
        public void onOpen(WebSocket webSocket, Response response){
            webSocket.send("setId/Group2");
            webSocket.send("start");
        }

        public void onMessage(WebSocket webSocket, String string){
            if(string == "blaa"){
                //TODO: check for start/end messages recieved by the server
            }
            else {
                dm.updateDataSet(string);
            }
        }
        @Override
        public void onClosing(WebSocket webSocket, int code, String reason){
            webSocket.close(1000, null);
        }
        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response){
            //TODO: pray it doesnt fail
        }
    }



}
