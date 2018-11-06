package com.dqm.utils;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by dqm on 2018/8/31.
 */
public class UInt8 {

    public static byte[] fromUnsignedInt(short value) {
        byte[] bytes = new byte[2];
        ByteBuffer.wrap(bytes).putShort(value);

        return Arrays.copyOfRange(bytes, 1, 2);
    }

    public static short toUnsignedInt(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(2).put(new byte[]{0}).put(bytes);
        buffer.position(0);

        return buffer.getShort();
    }

    public static short toUnsignedInt(final ByteBuffer byteBuffer, boolean inverted) {
        byte[] indexBytes = new byte[1];
        byteBuffer.get(indexBytes);
        if(inverted)
            indexBytes = ByteUtil.invertedByteArrayV2(indexBytes);
        return toUnsignedInt(indexBytes);
    }
}
