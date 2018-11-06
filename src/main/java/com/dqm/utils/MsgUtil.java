package com.dqm.utils;

import com.dqm.msg.*;
import com.dqm.msg.common.Msg;
import com.dqm.msg.common.MsgPack;
import com.dqm.msg.common.NetAddr;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by dqm on 2018/8/31.
 */
public class MsgUtil {

    public static final byte[] encode(MsgPack msgPack) throws Exception {
        byte[] network = UInt32.fromUnsignedInt(Msg.NETWORK.MAIN.getType());

        //处理command
        byte[] command = new byte[12];
        byte[] tmp = new byte[]{};
        if(msgPack instanceof Version) {//
            tmp = ByteUtil.hexStringToByte("version".toCharArray());
        } else if(msgPack instanceof VerAck) {
            tmp = ByteUtil.hexStringToByte("verack".toCharArray());
        } else if(msgPack instanceof GetBlocks) {
            tmp = ByteUtil.hexStringToByte("getblocks".toCharArray());
        } else if(msgPack instanceof GetData) {
            tmp = ByteUtil.hexStringToByte("getdata".toCharArray());
        } else if(msgPack instanceof Pong) {
            tmp = ByteUtil.hexStringToByte("pong".toCharArray());
        } else if(msgPack instanceof Headers) {
            tmp = ByteUtil.hexStringToByte("headers".toCharArray());
        }

        for(int i=0;i<12;i++) {
            if(i<(12 - tmp.length)) {

            } else {
                command[12 - 1 - i] = tmp[12 - 1 - i];
            }
        }

        network = ByteUtil.concat(network, command);

        byte[] data = CodeUtil.encode(msgPack);

        byte[] length = UInt32.fromUnsignedInt((null == data)?0:data.length);

        network = ByteUtil.concat(network, ByteUtil.invertedByteArrayV2(length));

        if(!(msgPack instanceof VerAck)) {//sha256(sha256(payload)) 的前4个字节(不包含在version 或 verack 中)
            ByteBuffer byteBuffer = ByteBuffer.wrap(data);
            byte[] checksum = SHA256Util.checksum(byteBuffer);
            network = ByteUtil.concat(network, checksum);
        } else {
            ByteBuffer byteBuffer = ByteBuffer.wrap(network);
            byte[] checksum = SHA256Util.checksum(byteBuffer);
            network = ByteUtil.concat(network, checksum);
        }

        return ByteUtil.concat(network, data);
    }

    public static final MsgPack decode(byte[] data) throws Exception {
        ByteBuffer byteBuffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
        return decode(byteBuffer);
    }

    public static final MsgPack decode(ByteBuffer byteBuffer) throws Exception {
        long network = UInt32.toUnsignedInt(byteBuffer, true);

        byte[] command = new byte[12];
        byteBuffer.get(command);

        byte[] tmp = new byte[]{};
        for(int i=0;i<12;i++) {
            if(command[i] != 0x00) {
                tmp = ByteUtil.concat(tmp, new byte[]{command[i]});
            }
        }

        String commandStr = new String(tmp);

//        byte[] length = new byte[4];
        int length = byteBuffer.getInt();

        long checksum = UInt32.toUnsignedInt(byteBuffer, true);
        if(commandStr.equalsIgnoreCase("version")) {
            return CodeUtil.decode(byteBuffer, Version.class);
        } else if(commandStr.equalsIgnoreCase("verack")) {
            return CodeUtil.decode(byteBuffer, VerAck.class);
        } else if(commandStr.equalsIgnoreCase("inv")) {
            return CodeUtil.decode(byteBuffer, Inventory.class);
        } else if(commandStr.equalsIgnoreCase("ping")) {
            return CodeUtil.decode(byteBuffer, Ping.class);
        } else if (commandStr.equalsIgnoreCase("getheaders")) {
            return CodeUtil.decode(byteBuffer, GetHeaders.class);
        }
        return null;
    }

    public static final void main(String[] args) throws Exception {
        /**
        byte[] test = new byte[]{
                (byte)0xF9, (byte)0xBE, (byte)0xB4, (byte)0xD9, (byte)0x76, (byte)0x65, (byte)0x72, (byte)0x73, (byte)0x69, (byte)0x6F, (byte)0x6E, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0x55, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0x9C, (byte)0x7C, (byte)0x00, (byte)0x00, (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0xE6, (byte)0x15, (byte)0x10, (byte)0x4D, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xFF, (byte)0xFF, (byte)0x0A, (byte)0x00, (byte)0x00, (byte)0x01,
                (byte)0xDA, (byte)0xF6, (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xFF, (byte)0xFF, (byte)0x0A, (byte)0x00, (byte)0x00, (byte)0x02, (byte)0x20, (byte)0x8D, (byte)0xDD, (byte)0x9D, (byte)0x20, (byte)0x2C,
                (byte)0x3A, (byte)0xB4, (byte)0x57, (byte)0x13, (byte)0x00, (byte)0x55, (byte)0x81, (byte)0x01, (byte)0x00
        };

        Version Version = (Version) decode(test);
        System.out.print(IpUtil.v6Tov4(ByteUtil.hexStringToByte(Version.getAddrMe().getIp())));
        **/

        Version version = new Version();//85byte
        version.setVersion(31900);
        version.setServices(new BigInteger(1 + ""));
        version.setTimestamp(new BigInteger(319000000 + ""));

        NetAddr me = new NetAddr();
        me.setServices(new BigInteger(1 + ""));
        me.setIp(ByteUtil.bytesToHexString(IpUtil.ipToBytes("10.0.67.86")));
        me.setPort(8333);

        NetAddr you = new NetAddr();
        you.setServices(new BigInteger(1 + ""));
        you.setIp(ByteUtil.bytesToHexString(IpUtil.ipToBytes("198.251.83.19")));
        you.setPort(8333);

        version.setAddrMe(me);
        version.setAddrYou(you);
        version.setNonce(new BigInteger(319798000 + ""));

        VarStr varStr = new VarStr();
        varStr.setLength(0);

        version.setSubVersionNum(varStr);
        version.setStartHeight(98645);

        byte[] a = encode(version);
        System.out.println(ByteUtil.bytesToHexString(a));
        System.out.println("---------->" + a.length);
    }
}
