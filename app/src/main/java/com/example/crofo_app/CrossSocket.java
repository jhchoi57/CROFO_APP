package com.example.crofo_app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class CrossSocket extends Thread {
    private Socket socket;
    private String url;
    private String event;
    private boolean isConnected;

    public CrossSocket(String url, String key) {
        this.url = url;
        this.event = key;
        isConnected = connect();
    }

    public void run() {
        if (!isConnected) {
            System.out.println("Socket is not connected");
            return;
        }
        socket.on(event, onMessageReceive);
    }

    private boolean connect() {
        try {
            socket = IO.socket(url);
            socket.connect();
            socket.on(Socket.EVENT_CONNECT, onConnect);

            return true;
        } catch (URISyntaxException e) {
            e.printStackTrace();

            return false;
        }
    }
    private Emitter.Listener onConnect = new Emitter.Listener() {

        @Override
        public void call(Object... args) {

        }
    };

    private Emitter.Listener onMessageReceive = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                JSONObject jsonObj = (JSONObject)args[0];
                JSONArray jsonArr = jsonObj.getJSONArray("arr");

                System.out.println(jsonObj);
                System.out.println(jsonArr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}
