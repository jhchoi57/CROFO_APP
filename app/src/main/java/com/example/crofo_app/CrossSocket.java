package com.example.crofo_app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class CrossSocket {
    private Socket socket;
    private String url;
    private int crosswalk;
    private int intersection;
    private boolean isConnected;
    private boolean stop;
    private CrossInfo roi;

    public CrossSocket(String url, int intersection_id, int crosswalk_id, CrossInfo roi) {
        this.url = url;
        this.intersection = intersection_id;
        this.crosswalk = crosswalk_id;
        isConnected = false;
        stop = true;
        this.roi = roi;
    }

    public CrossInfo run() {
        if(!isConnected) {
            System.out.println("Android-Node socket is not connected");
        } else {
            stop = false;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!stop) {
                        try {
                            System.out.println("Thread is run now");
                            socket.emit("request", "hi");
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
            socket.on("object", onMessageReceive);
        }
        return this.roi;
    }

    public void stop() {
        stop = true;
    }

    public void setKey(int intersection_id, int crosswalk_id) {
        if (isConnected) {
            System.out.println("Android-Node socket is connected: please disconnect first");
            return;
        }
        this.intersection = intersection_id;
        this.crosswalk = crosswalk_id;
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
            try {
                JSONObject jsonObj = new JSONObject();
                jsonObj.accumulate("in", intersection);
                jsonObj.accumulate("cr", crosswalk);
                socket.emit("where", jsonObj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
            JSONObject jsonObj = (JSONObject)args[0];
            System.out.println(jsonObj);
            try {
                JSONArray jsonArr = jsonObj.getJSONArray("arr");
                int cnt = jsonArr.length();

                for (int i = 0; i < cnt; i++) {
                    JSONObject json = jsonArr.getJSONObject(i);

                    json.getInt("type");
                    json.getInt("x");
                    json.getInt("y");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            isConnected = false;
            System.out.println("Android-Node socket is disconnected");
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            isConnected = false;
            System.out.println("Android-Node socket has Connection-Error");
        }
    };

    private Emitter.Listener onConnectTimeoutError = new Emitter.Listener() {
        public void call(Object... args) {
            isConnected = false;
            System.out.println("Android-Node socket has Connection-Timeout-Error");
        }
    };

    public void disconnect() {
        stop();
        socket.disconnect();
        socket.off(Socket.EVENT_CONNECT, onConnect);
        socket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        socket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        socket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectTimeoutError);
        isConnected = false;
        System.out.println("Android-Node socket is disconnected");
    }
}
