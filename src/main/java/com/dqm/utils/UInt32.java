package com.dqm.utils;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by dqm on 2018/8/24.
 */
public class UInt32 {

    public static byte[] fromUnsignedInt(long value) {
        byte[] bytes = new byte[8];
        ByteBuffer.wrap(bytes).putLong(value);

        return Arrays.copyOfRange(bytes, 4, 8);
    }

    public static long toUnsignedInt(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(8).put(new byte[]{0, 0, 0, 0}).put(bytes);
        buffer.position(0);

        return buffer.getLong();
    }

    public static long toUnsignedInt(final ByteBuffer byteBuffer, boolean inverted) {
        byte[] indexBytes = new byte[4];
        byteBuffer.get(indexBytes);
        if(inverted)
            indexBytes = ByteUtil.invertedByteArrayV2(indexBytes);
        return toUnsignedInt(indexBytes);
    }
}
