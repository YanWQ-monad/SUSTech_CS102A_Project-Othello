package com.monadx.othello.network.connection.connection;

import com.monadx.othello.testutils.PairedStream;
import com.monadx.othello.testutils.WrapThread;
import com.monadx.othello.network.connection.Channel;
import com.monadx.othello.network.packet.PacketStream;
import com.monadx.othello.network.packet.RawPacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class HandshakeConnectionTest {
    private static final Logger LOGGER = LogManager.getLogger(HandshakeConnectionTest.class);

    PairedStream serverToClientStream;
    PairedStream clientToServerStream;

    ClientHandshakeConnection client;
    ServerHandshakeConnection server;

    @BeforeEach
    void setUp(TestInfo testInfo) {
        LOGGER.warn("Starting test: {}", testInfo.getDisplayName());

        serverToClientStream = new PairedStream();
        clientToServerStream = new PairedStream();

        client = new ClientHandshakeConnection(new PacketStream(
                new Channel(serverToClientStream.getInputStream(), clientToServerStream.getOutputStream(), clientToServerStream)));
        server = new ServerHandshakeConnection(new PacketStream(
                new Channel(clientToServerStream.getInputStream(), serverToClientStream.getOutputStream(), serverToClientStream)));
    }

    @AfterEach
    void tearDown() throws IOException {
        serverToClientStream.close();
        clientToServerStream.close();
    }

    void connect() throws Exception {
        WrapThread<?> clientThread = new WrapThread<>(() -> client.runUntilComplete(""));
        WrapThread<?> serverThread = new WrapThread<>(() -> server.runUntilComplete(""));

        clientThread.start();
        serverThread.start();

        clientThread.join();
        serverThread.join();

        serverToClientStream.maintainThread();
        clientToServerStream.maintainThread();
    }

    @Test
    @Timeout(2)
    void testConnect() throws Exception {
        connect();

        assertEquals(client.getStage(), ClientHandshakeConnection.Stage.FINISHED);
        assertEquals(server.getStage(), ServerHandshakeConnection.Stage.FINISHED);

        RawPacket packet = RawPacket.createFrom(0x13, new byte[]{0x01, 0x02, 0x03, 0x04});
        client.getStream().send(packet);
        RawPacket receivedPacket = server.getStream().read();
        assertEquals(packet, receivedPacket);
    }

    @Test
    @Timeout(2)
    void testIncorrectPassword() throws Exception {
        WrapThread<Boolean> clientThread = new WrapThread<>(() -> client.runUntilComplete("123"));
        WrapThread<?> serverThread = new WrapThread<>(() -> server.runUntilComplete("456"));

        clientThread.start();
        serverThread.start();

        assertFalse(clientThread.join());
        assertEquals(client.getStage(), ClientHandshakeConnection.Stage.ERROR);

        clientToServerStream.close();
        assertThrows(IOException.class, serverThread::join);
    }
}
