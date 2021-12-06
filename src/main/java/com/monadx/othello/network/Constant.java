package com.monadx.othello.network;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Constant {
    public static final InetAddress MULTICAST_ADDRESS = getAddressByNameOrNull("228.5.6.7");
    public static final int MULTICAST_PORT = 34567;

    public static InetAddress getAddressByNameOrNull(String name) {
        try {
            return InetAddress.getByName(name);
        } catch (UnknownHostException e) {
            return null;
        }
    }
}
