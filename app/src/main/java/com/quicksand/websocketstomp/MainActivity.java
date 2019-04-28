package com.quicksand.websocketstomp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.quicksand.websocketstomp.Utils.StompClientUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.LifecycleEvent;
import ua.naiksoftware.stomp.client.StompClient;
import ua.naiksoftware.stomp.client.StompMessage;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView bt_connect;
    private StompClient stompClient;
    public static final String TAG = MainActivity.class.getSimpleName();
    private TextView tv_context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initWebSocketStomp();
        initRectiver();
    }

    //接收后台推送消息
    private void initRectiver() {

        StompClientUtils.getInstance().topic("注意这里请填写推送url名称")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<StompMessage>() {
                    @Override
                    public void accept(StompMessage stompMessage) throws Exception {
                        String payload = stompMessage.getPayload();
                        tv_context.setText("后台推送内容"+payload);
                    }
                });
    }

    /**
     * 初始化WebSocketStomp
     */
    private void initWebSocketStomp() {

        stompClient = StompClientUtils.getInstance();

    }

    private void initView() {
        bt_connect = (TextView) findViewById(R.id.bt_connect);
        tv_context = (TextView) findViewById(R.id.tv_context);
        tv_context.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_connect:
                initConnect();
                break;
        }
    }

    /**
     * 向后台的WebSocket发出连接请求
     */
    private void initConnect() {
        stompClient.lifecycle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LifecycleEvent>() {
                    @Override
                    public void accept(LifecycleEvent lifecycleEvent) throws Exception {
                        //监听WebSocket连接情况
                        switch (lifecycleEvent.getType()) {
                            case OPENED:
                                //连接成功
                                Log.w(TAG, "Stomp connection open");
                                break;
                            case ERROR:
                                //连接错误
                                Log.w(TAG, "Stomp connection error");
                                break;
                            case CLOSED:
                                //连接失败
                                Log.w(TAG, "Stomp connection closed");
                                break;
                        }
                    }
                });
        //设置心跳包   以毫秒为单位
        stompClient.setHeartbeat(1000);
        //连接后台
        stompClient.connect();
    }
}
