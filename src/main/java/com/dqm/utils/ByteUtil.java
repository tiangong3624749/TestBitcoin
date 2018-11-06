package com.dqm.utils;

import java.util.Arrays;

/**
 * Created by dqm on 2018/8/24.
 */
public class ByteUtil {

    /**
     * 浮点转换为字节
     *
     * @param f
     * @return
     */
    public static byte[] float2Byte(float f) {

        // 把float转换为byte[]
        int fbit = Float.floatToIntBits(f);

        byte[] src = new byte[4];
        for (int i = 0; i < 4; i++) {
            src[i] = (byte) (fbit >> (24 - i * 8));
        }

        return src;
    }

    /**
     *
     * 双精度转换为字节
     *
     * @param d
     * @return
     */
    public static byte[] double2Byte(double d) {
        // 把float转换为byte[]
        long fbit = Double.doubleToLongBits(d);

        byte[] src = new byte[8];
        for (int i = 0; i < 8; i++) {
            src[i] = (byte) (fbit >> (65 - i * 8));
        }

        return src;
    }

    /**
     * 获取指定的byte
     * @param obj
     * @param index
     * @return
     */
    public static <T extends Number> byte number2Byte(T obj, int index) {
        int right = index * 8;
        if(obj instanceof Short) {
            return (byte)((Short)obj >> right & 0xff);
        }else if(obj instanceof Integer) {
            return (byte)((Integer)obj >> right & 0xff);
        } else {
            return (byte)((Long)obj >> right & 0xff);
        }
    }

    /**
      * 合并byte数组
      * @param first
      * @param second
      * @return
      */
    public static byte[] concat(byte[] first, byte[] second) {
        if(null == second)
            return first;
        byte[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    /**
     * 用于byte数组的大小端转换
     * @param data
     * @return
     */
    public static byte[] invertedByteArrayV2(byte[] data) {
        byte[] inverted = data.clone();
        int ct = inverted.length/2;
        if(ct > 0) {
            for (int i=0;i<ct;i++) {
                byte tmp = inverted[i];
                inverted[i] = inverted[inverted.length - i - 1];
                inverted[inverted.length - i - 1] = tmp;
            }
        }

        return inverted;
    }

    /**
     * 字节码转成Hex字符串
     * @param bytes
     * @return
     */
    public static String bytesToHexString(byte[] bytes) {
        StringBuilder buf = new StringBuilder(bytes.length * 2);
        for(byte b : bytes) { // 使用String的format方法进行转换
            buf.append(String.format("%02x", new Integer(b & 0xff)));
        }

        return buf.toString();
    }

    /**
     * 字节码转成Hex字符串
     * @param chars
     * @return
     */
    public static String charsToHexString(char[] chars) {
        StringBuilder buf = new StringBuilder(chars.length * 2);
        for(char b : chars) { // 使用String的format方法进行转换
            buf.append(String.format("%02x", new Integer(b & 0xff)));
        }

        return buf.toString();
    }

    /**
     * Hex转字节码
     * @param hex
     * @return
     */
    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    /**
     * Hex转字节码
     * @param achar
     * @return
     */
    public static byte[] hexStringToByte(char[] achar) {
        int len = achar.length;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++) {
            result[i] = (byte)achar[i];
        }
        return result;
    }

    private static byte toByte(char c) {
        byte b = (byte) "0123456789abcdef".indexOf(c);
        return b;
    }
}
