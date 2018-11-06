package com.dqm.utils;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Created by dqm on 2018/8/31.
 */
public class SHA256Util {

    public static MessageDigest newDigest() {
        try {
            return MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e); // Can't happen.
        }
    }

    public static byte[] hash(ByteBuffer input) {
        MessageDigest digest = newDigest();
        digest.update(input);
        return digest.digest();
    }


    public static byte[] checksum(ByteBuffer input) {
        return Arrays.copyOfRange(hash(input), 0, 4);
    }
}
