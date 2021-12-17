package com.monadx.othello.network;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Constant {
    public static final InetAddress MULTICAST_ADDRESS = getAddressByNameOrNull("228.5.6.7");
    public static final int MULTICAST_PORT = 34567;

    public static final String KEY_PAIR_ALGORITHM = "EC";
    public static final String KEY_AGREEMENT_ALGORITHM = "ECDH";
    public static final int CRYPTO_KEY_SIZE = 384;

    // reference: `keySize` in sun.security.ec.ECDHKeyAgreement.deriveKeyImpl()
    public static final int SECRET_KEY_LENGTH = (CRYPTO_KEY_SIZE + 7) / 8;

    public static final String CIPHER_ALGORITHM = "AES/CTR/NoPadding";
    public static final String CIPHER_KEY_ALGORITHM = "AES";
    public static final int NONCE_SIZE = 64;
    public static final int NONCE_LENGTH = NONCE_SIZE / 8;
    public static final int TWO_NONCE_LENGTH = NONCE_LENGTH * 2;
    public static final int IV_LENGTH = 128 / 8;
    public static final int AES_KEY_LENGTH = 32;

    public static InetAddress getAddressByNameOrNull(String name) {
        try {
            return InetAddress.getByName(name);
        } catch (UnknownHostException e) {
            return null;
        }
    }
}
