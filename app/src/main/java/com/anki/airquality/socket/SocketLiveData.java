package com.anki.airquality.socket;

import com.anki.airquality.AppConstant;
import com.anki.airquality.AppController;
import com.anki.airquality.pojo.AQIModel;
import com.anki.airquality.utils.DebugUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class SocketLiveData extends LiveData<List<AQIModel>> {
    private static SocketLiveData instance = new SocketLiveData();
    private static WebSocket webSocket;
    private static AtomicBoolean disconnected = new AtomicBoolean(true);

    private SocketLiveData() {

    }

    public static SocketLiveData get() {
        return instance;
    }

    @Override
    protected synchronized void onActive() {
        super.onActive();
        connect();
    }

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super List<AQIModel>> observer) {
        super.observe(owner, observer);
        DebugUtils.debug(SocketLiveData.class, "Observing");
        connect();
    }

    public synchronized void connect() {
        try {
            DebugUtils.debug(SocketLiveData.class, "Attempting to connect " +  disconnected);
            if (disconnected.compareAndSet(true, false)) {
                DebugUtils.debug(SocketLiveData.class, "Connecting...");
                DebugUtils.debug(SocketLiveData.class, "Socket url: " + AppConstant.WS_URL);
                Request request = new Request.Builder().url(AppConstant.WS_URL).build();
                webSocket = AppController.getOkHttpClient().newWebSocket(request, webSocketListener);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private final WebSocketListener webSocketListener = new SocketListner(new SocketListner.Mediator() {

        @Override
        public void onConnected(WebSocket webSocket, Response response) {
            disconnected.set(false);
        }

        @Override
        public void getMessage(String text) {
            handleEvent(text);
        }

        @Override
        public void onFailure(WebSocket webSocket, String reason) {
            disconnected.set(true);
        }

        @Override
        public void closingOrClosed(boolean isClosed, WebSocket webSocket, String reason, int code) {
            disconnected.set(true);
        }
    });


    private synchronized void handleEvent(String jsonOutput) {
        try {
            DebugUtils.debug(SocketLiveData.class, "Processing event before: " + jsonOutput);
            Gson gson = AppController.getGson();
            Type listType = new TypeToken<List<AQIModel>>(){}.getType();
            List<AQIModel> aqiModels = gson.fromJson(jsonOutput, listType);
            processEvent(aqiModels);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private synchronized void processEvent(List<AQIModel> eventModel) throws Exception {
        postValue(eventModel);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        disconnect();
        DebugUtils.debug(SocketLiveData.class, "Inactive. Has observers observers? " + hasActiveObservers());
    }

    public boolean isDisconnected() {
        return disconnected.get();
    }

    public void disconnect() {
        if (!hasActiveObservers())
            if (webSocket != null){
                disconnected.set(true);
                webSocket.close(1000, "Done using");
            }
    }

}
