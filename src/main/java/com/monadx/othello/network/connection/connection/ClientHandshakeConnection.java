package com.monadx.othello.network.connection.connection;

import java.io.IOException;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import javax.crypto.*;

import com.monadx.othello.network.packet.handshake.ServerboundPasswordVerifyPacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import com.monadx.othello.network.Constant;
import com.monadx.othello.network.connection.handler.ClientHandshakePacketHandler;
import com.monadx.othello.network.packet.PacketStream;
import com.monadx.othello.network.packet.handshake.ClientboundHelloPacket;
import com.monadx.othello.network.packet.handshake.ClientPacketListener;
import com.monadx.othello.network.packet.handshake.ServerboundHelloPacket;
import com.monadx.othello.network.utils.CryptoHelper;

public class ClientHandshakeConnection {
    private final static Logger LOGGER = LogManager.getLogger(ClientHandshakeConnection.class);

    @NotNull private PacketStream stream;
    @NotNull private Stage stage = Stage.START;

    public ClientHandshakeConnection(@NotNull PacketStream stream) {
        this.stream = stream;
    }

    public boolean runUntilComplete(@NotNull String password) throws IOException, GeneralSecurityException {
        KeyAgreement keyAgree = KeyAgreement.getInstance(Constant.KEY_AGREEMENT_ALGORITHM);
        byte[] nonce = new byte[Constant.TWO_NONCE_LENGTH];
        byte[] secret = new byte[Constant.SECRET_KEY_LENGTH];
        byte[] salt = new byte[Constant.PASSWORD_SALT_LENGTH];

        ClientHandshakePacketHandler handler = new ClientHandshakePacketHandler(new ClientPacketListener() {
            @Override
            public void handleHello(@NotNull ClientboundHelloPacket packet) throws IOException {
                assert stage == Stage.START;
                LOGGER.debug("Handling server hello");
                try {
                    KeyFactory clientKeyFactory = KeyFactory.getInstance(Constant.KEY_PAIR_ALGORITHM);
                    X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(packet.publicKeyEncoded);
                    PublicKey serverPublicKey = clientKeyFactory.generatePublic(x509KeySpec);

                    keyAgree.doPhase(serverPublicKey, true);
                    keyAgree.generateSecret(secret, 0);

                    stage = Stage.AGREED;

                    byte[] serverHashedPassword = CryptoHelper.hashPassword(password, secret, salt);
                    if (!Arrays.equals(serverHashedPassword, packet.hashedPassword)) {
                        LOGGER.warn("Server hashed password does not match: got {}, expected {}",
                                CryptoHelper.toHexString(packet.hashedPassword), CryptoHelper.toHexString(serverHashedPassword));
                        stage = Stage.ERROR;
                        return;
                    }

                    for (int i = 0; i < nonce.length; i++)
                        nonce[i] ^= packet.nonce[i];

                    Cipher encryptCipher = CryptoHelper.createCipher(Cipher.ENCRYPT_MODE, secret, nonce, 0);
                    Cipher decryptCipher = CryptoHelper.createCipher(Cipher.DECRYPT_MODE, secret, nonce, Constant.NONCE_LENGTH);
                    stream = CryptoHelper.encryptPacketStream(stream, encryptCipher, decryptCipher);

                    stage = Stage.ENCRYPT;
                    LOGGER.info("The connection is now encrypted");

                    byte[] clientHashedPassword = CryptoHelper.hashPassword(password, secret, packet.salt);
                    stream.send(new ServerboundPasswordVerifyPacket(clientHashedPassword));

                    stage = Stage.FINISHED;
                } catch (GeneralSecurityException e) {
                    stage = Stage.ERROR;
                    LOGGER.error("Error while handling server hello packet", e);
                }
            }
        });

        SecureRandom random = new SecureRandom();
        random.nextBytes(nonce);
        random.nextBytes(salt);

        KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance(Constant.KEY_PAIR_ALGORITHM);
        keyGenerator.initialize(Constant.CRYPTO_KEY_SIZE);
        KeyPair pair = keyGenerator.generateKeyPair();

        keyAgree.init(pair.getPrivate());
        byte[] publicKeyEncoded = pair.getPublic().getEncoded();

        stream.send(new ServerboundHelloPacket(publicKeyEncoded, nonce, salt));
        while (stage != Stage.FINISHED && stage != Stage.ERROR) {
            handler.handle(stream.read());
        }
        return stage == Stage.FINISHED;
    }

    @NotNull
    public PacketStream getStream() {
        return stream;
    }

    @NotNull
    Stage getStage() {
        return stage;
    }

    enum Stage {
        START,
        AGREED,
        ENCRYPT,
        FINISHED,
        ERROR,
    }
}
