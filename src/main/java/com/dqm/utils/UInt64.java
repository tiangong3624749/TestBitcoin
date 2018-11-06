package com.dqm.utils;

import java.math.BigInteger;
import java.nio.ByteBuffer;

/**
 * Created by dqm on 2018/8/24.
 * https://technologicaloddity.com/2010/09/22/biginteger-as-unsigned-long-in-java/
 */
public class UInt64 {

    public static final byte[] fromUnsignedLong(BigInteger value) {
        byte[] bytes = value.toByteArray();

        return bytes;
    }

    private static final BigInteger readUnsignedInt64(byte[] readBuffer) {
        if (readBuffer == null || readBuffer.length < 8)
            return new BigInteger("0");
        // 处理成无符号数
        byte[] uint64 = new byte[9];
        uint64[8] = 0;
        System.arraycopy(readBuffer, 0, uint64, 0, 8);
        return new BigInteger(ByteUtil.invertedByteArrayV2(uint64));
    }

    public static final BigInteger toUnsignedLong(byte[] bytes) {
        return new BigInteger(1, bytes);
    }

    public static final BigInteger toUnsignedLong(final ByteBuffer byteBuffer, boolean inverted) {
        byte[] valueBytes = new byte[8];
        byteBuffer.get(valueBytes);
        if(inverted)
            valueBytes = ByteUtil.invertedByteArrayV2(valueBytes);
        return toUnsignedLong(valueBytes);
    }

    public static void main(String[] args) {
        byte[] test = new byte[]{(byte)0xe4, (byte)0xe7, (byte)0x04, (byte)0xfc, (byte)0x41, (byte)0xa3, (byte)0xc4, (byte)0x7f};

        BigInteger a = readUnsignedInt64(test);
        System.out.println(a.longValue());

        byte[] test2 = ByteUtil.invertedByteArrayV2(test);
        BigInteger unsigned = new BigInteger(1, test);
        BigInteger unsigned2 = new BigInteger(1, test2);
        System.out.println(unsigned.longValue());
        System.out.println(unsigned2.longValue());
    }
}
