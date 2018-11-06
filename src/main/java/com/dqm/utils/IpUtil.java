package com.dqm.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by dqm on 2018/8/31.
 */
public class IpUtil {

    /**
     * 将ipv6转成v4
     * @param ipv6
     * @return int可能为负数
     * @throws UnknownHostException
     */
    public static String v6Tov4(byte[] ipv6) throws UnknownHostException {
        byte[] src = Arrays.copyOfRange(ipv6, 12,16);
        return (src[0] & 0xff) + "." + (src[1] & 0xff) + "." + (src[2] & 0xff) + "." + (src[3] & 0xff);
    }

    /**
     * 将ip字符串转为byte数组,注意:ip不可以是域名,否则会进行域名解析
     * @param ip
     * @return byte[]
     * @throws UnknownHostException
     */
    public static byte[] ipToBytes(String ip) throws UnknownHostException {
        byte[] ipv6 = new byte[16];
        byte [] tmp = InetAddress.getByName(ip).getAddress();
        ByteBuffer byteBuffer = ByteBuffer.allocate(16).put(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}).put(tmp);
        byteBuffer.position(0);
        byteBuffer.get(ipv6);
        return ipv6;
    }

    public static final void main(String[] args) throws UnknownHostException {
        byte[] t = new byte[] {
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0x00, (byte)0x00, (byte)0xFF, (byte)0xFF, (byte)0x0A, (byte)0x00, (byte)0x00, (byte)0x02
        };
        System.out.println(v6Tov4(t));

        System.out.println(ByteUtil.bytesToHexString(ipToBytes("183.193.213.50")));
    }
}
