package com.tomo.tomolive.globalSoket;

import android.util.Log;

import com.tomo.tomolive.BuildConfig;

import java.net.URI;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.transports.Polling;
import io.socket.engineio.client.transports.WebSocket;

public class GlobalSoket {
    private static final String TAG = "globalsoket";
    OnGlobalSoketChangeListner onGlobalSoketChangeListner;


    public GlobalSoket() {
        Log.d(TAG, "GlobalSoket: init");
        IO.Options options = IO.Options.builder()
                // IO factory options
                .setForceNew(false)
                .setMultiplex(true)

                // low-level engine options
                .setTransports(new String[]{Polling.NAME, WebSocket.NAME})
                .setUpgrade(true)
                .setRememberUpgrade(false)
                .setPath("/socket.io/")
                .setQuery("globalRoom=12021")
                .setExtraHeaders(null)

                // Manager options
                .setReconnection(true)
                .setReconnectionAttempts(Integer.MAX_VALUE)
                .setReconnectionDelay(1_000)
                .setReconnectionDelayMax(5_000)
                .setRandomizationFactor(0.5)
                .setTimeout(20_000)

                // Socket options
                .setAuth(null)
                .build();

        URI uri = URI.create(BuildConfig.BASE_URL);
        Socket socket = IO.socket(uri, options);
        socket.connect();

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d(TAG, "connected: globelSoket");

                onGlobalSoketChangeListner.onSoketConnected(socket);


            }
        });

        socket.on("inform", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d(TAG, "call: inforemed");

                onGlobalSoketChangeListner.onEventFaced(socket);

            }
        });


    }

    public OnGlobalSoketChangeListner getOnGlobalSoketChangeListner() {
        return onGlobalSoketChangeListner;
    }

    public void setOnGlobalSoketChangeListner(OnGlobalSoketChangeListner onGlobalSoketChangeListner) {
        this.onGlobalSoketChangeListner = onGlobalSoketChangeListner;
    }

    public void sendNewEvent(Socket finelSoket) {
        Log.d(TAG, "sendNewEvent: finelsoket==" + finelSoket);
        if (finelSoket != null) {
            finelSoket.emit("inform", "new");
        } else {
            new GlobalSoket();
            sendNewEvent(finelSoket);
        }
    }

    public interface OnGlobalSoketChangeListner {
        void onEventFaced(Socket finelSoket);

        void onSoketConnected(Socket socket);
    }
}
