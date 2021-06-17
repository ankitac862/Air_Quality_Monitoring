package com.anki.airquality.socket;

import com.anki.airquality.utils.DebugUtils;

import androidx.annotation.Nullable;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class SocketListner extends WebSocketListener {

    private Mediator mediator;

    public static final int CLOSURE_STATUS = 1000;
    private WebSocket webSocket = null;

    public SocketListner(Mediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        this.webSocket = webSocket;
        mediator.onConnected(webSocket, response);
    }

    public void sendMsg(String msg) {
        if (webSocket != null)
            webSocket.send(msg);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
        mediator.onFailure(webSocket, t.getLocalizedMessage());
        int code = response != null ? response.code() : t != null ? 400 : 0;
        @Nullable String message = response != null ? response.message() : t != null ? t.getMessage() : "null";
        DebugUtils.debug(SocketListner.class, String.format("On Failure. Code: %s, message: %s", code, message));
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        mediator.closingOrClosed(true, webSocket, reason, code);
        DebugUtils.debug(SocketListner.class,"Closed :" + reason);
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        mediator.closingOrClosed(false, webSocket, reason, code);
        DebugUtils.debug(SocketListner.class,"Closing :"+reason);
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        mediator.getMessage(text);
    }

    public interface Mediator {
        void onConnected(WebSocket webSocket, Response response);

        void getMessage(String msg);

        void onFailure(WebSocket webSocket, String reason);

        void closingOrClosed(boolean isClosed, WebSocket webSocket, String reason, int code);

    }

    public WebSocket getWebSocket() {
        return webSocket;
    }

}
