package com.monadx.othello.network.request;

import com.monadx.othello.network.connection.connection.GameConnection;
import com.monadx.othello.network.packet.Packet;
import com.monadx.othello.network.packet.game.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RequestManager {
    private final static Logger LOGGER = LogManager.getLogger(RequestManager.class);

    public final int UNDO_REQUEST_TYPE_ID = 0x01;
    public final int RESTART_REQUEST_TYPE_ID = 0x02;

    Random random = new Random();
    GameConnection connection;
    RequestListener listener;

    Map<ItemKey, RequestResultConsumer> map = new HashMap<>();

    public RequestManager(GameConnection connection, RequestListener listener) {
        this.connection = connection;
        this.listener = listener;

        connection.registerRequestListener(new GameRequestPacketListener() {
            @Override
            public void onUndoRequest(UndoRequestPacket packet) {
                listener.onUndoRequest((response) -> sendWithHandlingException(new UndoResponsePacket(packet.requestId, response)));
            }

            @Override
            public void onUndoResponse(UndoResponsePacket packet) {
                handleResponse(packet.requestId, UNDO_REQUEST_TYPE_ID, packet.response);
            }

            @Override
            public void onRestartRequest(RestartRequestPacket packet) {
                listener.onRestartRequest((response) -> sendWithHandlingException(new RestartResponsePacket(packet.requestId, response)));
            }

            @Override
            public void onRestartResponse(RestartResponsePacket packet) {
                handleResponse(packet.requestId, RESTART_REQUEST_TYPE_ID, packet.response);
            }
        });
    }

    public void sendWithHandlingException(Packet<GameRequestPacketListener> packet) {
        try {
            connection.send(packet);
        } catch (IOException e) {
            LOGGER.error("Error sending request packet", e);
        }
    }

    public void sendUndoRequest(RequestResultConsumer onResponse) throws IOException {
        int requestId = random.nextInt();
        map.put(new ItemKey(requestId, UNDO_REQUEST_TYPE_ID), onResponse);
        connection.send(new UndoRequestPacket(requestId));
    }

    public void sendRestartRequest(RequestResultConsumer onResponse) throws IOException {
        int requestId = random.nextInt();
        map.put(new ItemKey(requestId, RESTART_REQUEST_TYPE_ID), onResponse);
        connection.send(new RestartRequestPacket(requestId));
    }

    private void handleResponse(int requestId, int requestTypeId, boolean response) {
        ItemKey key = new ItemKey(requestId, requestTypeId);
        RequestResultConsumer consumer = map.get(key);
        if (consumer != null) {
            map.remove(key);
            consumer.accept(response);
        } else {
            LOGGER.warn("Unknown request (requestTypeId={}, requestId={})", requestId, requestTypeId);
        }
    }

    public record ItemKey(int requestTypeId, int requestId) {}

    @FunctionalInterface
    public interface RequestResultConsumer {
        void accept(boolean result);
    }
}
