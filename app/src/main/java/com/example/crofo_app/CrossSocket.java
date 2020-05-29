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
    private String key;
    private boolean isConnected;
    private boolean stop;

    public CrossSocket(String url, int key) {
        this.url = url;
        this.key = "" + key;
        stop = false;
        isConnected = connect();
    }

    private void threadStop() {
        stop = true;
    }

    public void run() {
        if (!isConnected) {
            System.out.println("Socket is not connected");
            return;
        }
        socket.on(key, onMessageReceive);
        if (stop) {
            System.out.println(key + "thread is terminated");
        }
    }

    private boolean connect() {
        try {
            socket = IO.socket(url);
            socket.connect();
            socket.on(Socket.EVENT_CONNECT, onConnect);
            socket.on(Socket.EVENT_DISCONNECT, onDisconnect);
            socket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
            socket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
            return true;
        } catch (URISyntaxException e) {
            e.printStackTrace();

            return false;
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
                JSONArray jsonArr = jsonObj.getJSONArray("arr");

                System.out.println(jsonObj);
                System.out.println(jsonArr);
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

    private void disconnect() {
        socket.disconnect();
        socket.off(Socket.EVENT_CONNECT, onConnect);
        socket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        socket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        socket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectTimeoutError);
    }
}
