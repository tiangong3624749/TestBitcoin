package com.dqm.utils;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by dqm on 2018/8/30.
 */
public class UInt16 {

    public static byte[] fromUnsignedInt(int value) {
        byte[] bytes = new byte[4];
        ByteBuffer.wrap(bytes).putInt(value);

        return Arrays.copyOfRange(bytes, 2, 4);
    }

    public static int toUnsignedInt(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(4).put(new byte[]{0, 0}).put(bytes);
        buffer.position(0);

        return buffer.getInt();
    }

    public static int toUnsignedInt(final ByteBuffer byteBuffer, boolean inverted) {
        byte[] indexBytes = new byte[2];
        byteBuffer.get(indexBytes);
        if(inverted)
            indexBytes = ByteUtil.invertedByteArrayV2(indexBytes);
        return toUnsignedInt(indexBytes);
    }
}
