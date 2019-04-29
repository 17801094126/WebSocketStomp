package com.quicksand.websocketstomp.Utils;


import com.quicksand.websocketstomp.BuildConfig;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;

public class StompClientUtils {
    private static StompClient client;
    private StompClientUtils(){}
    public static StompClient getInstance(){
        if (client==null){
            synchronized (StompClient.class){
                if (client==null){
                    //这里可供俩种模式
                    /**
                     * Stomp.ConnectionProvider.JWS(Java携带的WebSocket)
                     * Stomp.ConnectionProvider.OKHTTP(OkHttp中自带的WebSocket)
                     * WebSocketUrl是后台的推送地址
                     */
                    client= Stomp.over(Stomp.ConnectionProvider.JWS, BuildConfig.WebSocketUrl);
                }
            }
        }
        return client;
    }

}
