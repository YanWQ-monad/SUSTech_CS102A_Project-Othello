package com.monadx.othello.network.connection.connection;

import java.io.IOException;
import java.security.*;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;

import com.monadx.othello.network.packet.handshake.ServerboundPasswordVerifyPacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import com.monadx.othello.network.Constant;
import com.monadx.othello.network.connection.handler.ServerHandshakePacketHandler;
import com.monadx.othello.network.packet.PacketStream;
import com.monadx.othello.network.packet.handshake.ClientboundHelloPacket;
import com.monadx.othello.network.packet.handshake.ServerPacketListener;
import com.monadx.othello.network.packet.handshake.ServerboundHelloPacket;
import com.monadx.othello.network.utils.CryptoHelper;

public class ServerHandshakeConnection {
    private final static Logger LOGGER = LogManager.getLogger(ServerHandshakeConnection.class);

    @NotNull private PacketStream stream;
    @NotNull private Stage stage = Stage.START;

    public ServerHandshakeConnection(@NotNull PacketStream stream) {
        this.stream = stream;
    }

    public boolean runUntilComplete(@NotNull String password) throws IOException, NoSuchAlgorithmException {
        KeyAgreement keyAgree = KeyAgreement.getInstance(Constant.KEY_AGREEMENT_ALGORITHM);
        byte[] secret = new byte[Constant.SECRET_KEY_LENGTH];
        byte[] salt = new byte[Constant.PASSWORD_SALT_LENGTH];

        ServerHandshakePacketHandler handler = new ServerHandshakePacketHandler(new ServerPacketListener() {
            @Override
            public void handleHello(@NotNull ServerboundHelloPacket packet) throws IOException {
                assert stage == Stage.START;
                LOGGER.debug("Handling client hello");
                try {
                    byte[] nonce = new byte[Constant.TWO_NONCE_LENGTH];
                    SecureRandom random = new SecureRandom();
                    random.nextBytes(nonce);
                    random.nextBytes(salt);

                    KeyFactory serverKeyFactory = KeyFactory.getInstance(Constant.KEY_PAIR_ALGORITHM);
                    X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(packet.publicKeyEncoded);
                    PublicKey clientPublicKey = serverKeyFactory.generatePublic(x509KeySpec);
                    ECParameterSpec dhParamFromClientPublicKey = ((ECPublicKey)clientPublicKey).getParams();

                    // create server key pair
                    KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance(Constant.KEY_PAIR_ALGORITHM);
                    keyGenerator.initialize(dhParamFromClientPublicKey);
                    KeyPair pair = keyGenerator.generateKeyPair();

                    byte[] publicKeyEncoded = pair.getPublic().getEncoded();

                    keyAgree.init(pair.getPrivate());
                    keyAgree.doPhase(clientPublicKey, true);
                    keyAgree.generateSecret(secret, 0);

                    stage = Stage.AGREED;

                    byte[] serverHashedPassword = CryptoHelper.hashPassword(password, secret, packet.salt);
                    stream.send(new ClientboundHelloPacket(publicKeyEncoded, nonce, salt, serverHashedPassword));

                    for (int i = 0; i < nonce.length; i++)
                        nonce[i] ^= packet.nonce[i];

                    Cipher encryptCipher = CryptoHelper.createCipher(Cipher.ENCRYPT_MODE, secret, nonce, Constant.NONCE_LENGTH);
                    Cipher decryptCipher = CryptoHelper.createCipher(Cipher.DECRYPT_MODE, secret, nonce, 0);
                    stream = CryptoHelper.encryptPacketStream(stream, encryptCipher, decryptCipher);

                    LOGGER.info("The connection is now encrypted");

                    stage = Stage.ENCRYPT;
                } catch (GeneralSecurityException e) {
                    stage = Stage.ERROR;
                    LOGGER.error("Error while handling client hello packet", e);
                }
            }

            @Override
            public void handlePasswordVerify(@NotNull ServerboundPasswordVerifyPacket packet) {
                try {
                    byte[] clientHashedPassword = CryptoHelper.hashPassword(password, secret, salt);
                    if (!Arrays.equals(clientHashedPassword, packet.hashedPassword)) {
                        LOGGER.warn("Client hashed password does not match: got {}, expected {}",
                                CryptoHelper.toHexString(packet.hashedPassword), CryptoHelper.toHexString(clientHashedPassword));
                        stage = Stage.ERROR;
                    } else {
                        stage = Stage.FINISHED;
                    }
                } catch (GeneralSecurityException e) {
                    stage = Stage.ERROR;
                    LOGGER.error("Error while handling password verify packet", e);
                }
            }
        });

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
