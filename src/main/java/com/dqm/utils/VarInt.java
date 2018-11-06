package com.dqm.utils;

import java.nio.ByteBuffer;

public class VarInt {

    public static int byte2Int(ByteBuffer bf, boolean inverted) {
        byte[] tmp = new byte[1];
        bf.get(tmp);
        int first = 0xFF & tmp[0];
        if (first < 253) {//1字节【只需要tmp的1字节】
            return first;
        } else if (first == 253) {//2字节【除去了tmp的1字节】
            byte[] tmp1 = new byte[2];
            bf.get(tmp1);
            if(inverted)
                return (tmp1[1] & 0xff) | ((tmp1[0] & 0xff) << 8);//小端
            else
                return (tmp1[0] & 0xff) | ((tmp1[1] & 0xff) << 8);//大端
        } else if (first == 254) {//4字节【除去了tmp的1字节】
            byte[] tmp1 = new byte[4];
            //return (tmp1[0] & 0xff) | ((tmp1[1] & 0xff) << 8) | ((tmp1[2] & 0xff) << 16) | ((tmp1[3] & 0xff) << 24);//大端
            return (tmp1[3] & 0xff) | ((tmp1[2] & 0xff) << 8) | ((tmp1[1] & 0xff) << 16) | ((tmp1[0] & 0xff) << 24);//小端
        } else {//8字节【除去了tmp的1字节】
            byte[] tmp1 = new byte[8];
            //TOOD：暂时不做，因为java的数组不支持long的下标
            return -1;
        }
    }


    private static int sizeOf(int value) {
        // if negative, it's actually a very large unsigned long value
        if (value < 0) return 9; // 1 marker + 8 data bytes
        if (value < 253) return 1; // 1 data byte
        if (value <= 0xFFFFL) return 3; // 1 marker + 2 data bytes
        if (value <= 0xFFFFFFFFL) return 5; // 1 marker + 4 data bytes
        return 9; // 1 marker + 8 data bytes
    }

    public static byte[] int2Byte(int value) {
        byte[] bytes;
        switch (sizeOf(value)) {
            case 1:
                return new byte[]{(byte) value};
            case 3:
                bytes = new byte[3];
                bytes[0] = (byte) 253;
                bytes[1] = (byte) (0xFF & value);
                bytes[2] = (byte) (0xFF & (value >> 8));
                return bytes;
            case 5:
                bytes = new byte[5];
                bytes[0] = (byte) 254;
                bytes[1] = (byte) (0xFF & value);
                bytes[2] = (byte) (0xFF & (value >> 8));
                bytes[3] = (byte) (0xFF & (value >> 16));
                bytes[4] = (byte) (0xFF & (value >> 24));
                return bytes;
            default:
                bytes = new byte[9];
                bytes[0] = (byte) 255;
                return bytes;
        }
    }

}
