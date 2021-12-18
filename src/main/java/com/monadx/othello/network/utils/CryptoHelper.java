package com.monadx.othello.network.utils;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.jetbrains.annotations.NotNull;

import com.monadx.othello.network.Constant;
import com.monadx.othello.network.connection.Channel;
import com.monadx.othello.network.packet.PacketStream;

public class CryptoHelper {
    @NotNull
    public static Cipher createCipher(int mode, @NotNull byte[] key, @NotNull byte[] nonce, int nonceOffset) throws GeneralSecurityException {
        byte[] iv = new byte[Constant.IV_LENGTH];
        System.arraycopy(nonce, nonceOffset, iv, 0, Constant.NONCE_LENGTH);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        SecretKeySpec aesKey = new SecretKeySpec(key, 0, Constant.AES_KEY_LENGTH, Constant.CIPHER_KEY_ALGORITHM);
        Cipher cipher = Cipher.getInstance(Constant.CIPHER_ALGORITHM);
        cipher.init(mode, aesKey, ivSpec);

        return cipher;
    }

    @NotNull
    public static PacketStream encryptPacketStream(@NotNull PacketStream stream, @NotNull Cipher encryptCipher, @NotNull Cipher decryptCipher) {
        CipherInputStream input = new CipherInputStream(stream.getChannel().input(), decryptCipher);
        CipherOutputStream output = new CipherOutputStream(stream.getChannel().output(), encryptCipher);
        return new PacketStream(new Channel(input, output, stream.getChannel().socket()));
    }

    @NotNull
    public static byte[] hashPassword(@NotNull String password, @NotNull byte[] key, @NotNull byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(Constant.DIGEST_ALGORITHM);
        byte[] passwordBytes = password.getBytes();
        md.update(passwordBytes);
        md.update(key, Constant.AES_KEY_LENGTH, Constant.REMAINING_SECRET_LENGTH);
        md.update(salt);
        return md.digest();
    }

    private static void byteToHex(byte b, @NotNull StringBuffer buf) {
        char[] hexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
                '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        int high = ((b & 0xf0) >> 4);
        int low = (b & 0x0f);
        buf.append(hexChars[high]);
        buf.append(hexChars[low]);
    }

    @NotNull
    public static String toHexString(@NotNull byte[] block) {
        StringBuffer buf = new StringBuffer();
        for (byte b : block) {
            byteToHex(b, buf);
        }
        return buf.toString();
    }
}
