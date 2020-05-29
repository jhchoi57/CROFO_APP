package com.example.crofo_app;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class CrossSocket {
    private Socket socket;
    private String url;
    private String key;
    private boolean isConnected;

    public CrossSocket(String url, String key) {
        this.url = url;
        this.key = key;
    }

    public void run() {
        if(!isConnected) {
            System.out.println("Android-Node socket is not connected");
        } else {
            socket.on(key, onMessageReceive);
        }
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void connect() {
        try {
            socket = IO.socket(url);
            socket.connect();
            socket.on(Socket.EVENT_CONNECT, onConnect);
            socket.on(Socket.EVENT_DISCONNECT, onDisconnect);
            socket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
            socket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
            isConnected = true;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    private Emitter.Listener onConnect = new Emitter.Listener() {

        @Override
        public void call(Object... args) {
            System.out.println("Android-Node socket is connected");
        }
    };

    private Emitter.Listener onMessageReceive = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                JSONObject jsonObj = (JSONObject)args[0];
                String result = jsonObj.getString("result");
                System.out.println(jsonObj);
                System.out.println(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            System.out.println("Android-Node socket is disconnected");
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            System.out.println("Android-Node socket has Connection-Error");
        }
    };

    private Emitter.Listener onConnectTimeoutError = new Emitter.Listener() {
        public void call(Object... args) {
            System.out.println("Android-Node socket has Connection-Timeout-Error");
        }
    };

    public void disconnect() {
        socket.disconnect();
        socket.off(Socket.EVENT_CONNECT, onConnect);
        socket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        socket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        socket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectTimeoutError);
        isConnected = false;
        System.out.println("Android-Node socket is disconnected");
    }
}
