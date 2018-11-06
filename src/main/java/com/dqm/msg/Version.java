package com.dqm.msg;

import com.dqm.annotations.Index;
import com.dqm.msg.common.MsgPack;
import com.dqm.msg.common.NetAddr;
import com.dqm.utils.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

/**
 * Created by dqm on 2018/8/25.
 */
@Getter
@Setter
public class Version implements MsgPack {

    @Index(val = 0, specialType = Index.SpecialType.UINT32)
    private long version;//uint32

    @Index(val = 1, specialType = Index.SpecialType.UINT64)
    private BigInteger services;//uint64

    @Index(val = 2, specialType = Index.SpecialType.UINT64)
    private BigInteger timestamp;//uint64

    @Index(val = 3)
    private NetAddr addrMe;//NetAddr

    @Index(val = 4, dependentIndex = 0, dependentType = Index.DependentType.EXIST, dependentVal = 106)
    private NetAddr addrYou;

    @Index(val = 5, specialType = Index.SpecialType.UINT64, dependentIndex = 0, dependentType = Index.DependentType.EXIST, dependentVal = 106)
    private BigInteger nonce;//uint64

    @Index(val = 6, dependentIndex = 0, dependentType = Index.DependentType.EXIST, dependentVal = 106)
    private VarStr subVersionNum;

    @Index(val = 7, specialType = Index.SpecialType.UINT32, dependentIndex = 0, dependentType = Index.DependentType.EXIST, dependentVal = 209)
    private long startHeight;//uint32

    @Index(val = 8)
    private byte relayFlag;//uint32

    public static final byte[] test() {
        byte[] test = new byte[]{
                (byte)0xF9, (byte)0xBE, (byte)0xB4, (byte)0xD9, (byte)0x76, (byte)0x65, (byte)0x72, (byte)0x73, (byte)0x69, (byte)0x6F, (byte)0x6E, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0x55, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0x9C, (byte)0x7C, (byte)0x00, (byte)0x00, (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0xE6, (byte)0x15, (byte)0x10, (byte)0x4D, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xFF, (byte)0xFF, (byte)0x0A, (byte)0x00, (byte)0x00, (byte)0x01,
                //(byte)0xDA, (byte)0xF6,
                (byte)0x20, (byte)0x8D,
                (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, //(byte)0xFF, (byte)0xFF, (byte)0x0A, (byte)0x00, (byte)0x00, (byte)0x02,
                (byte)0x00, (byte)0x00, (byte)0xb7, (byte)0xc1, (byte)0xd5, (byte)0x32,
                (byte)0x20, (byte)0x8D, (byte)0xDD, (byte)0x9D, (byte)0x20, (byte)0x2C,
                (byte)0x3A, (byte)0xB4, (byte)0x57, (byte)0x13, (byte)0x00, (byte)0x55, (byte)0x81, (byte)0x01, (byte)0x00
        };
        return test;
    }

    public static final byte[] test2() {
        byte[] a = {
                (byte)0x7F, (byte)0x11, (byte)0x01, (byte)0x00, (byte)0x0D, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x06, (byte)0x09, (byte)0x3D, (byte)0x5A,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x09, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xFF, (byte)0xFF,
                (byte)0x59, (byte)0x4B, (byte)0x49, (byte)0x0B, (byte)0x20, (byte)0x8D, (byte)0x0D, (byte)0x00,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
//                (byte)0x7F, (byte)0xC4, (byte)0xA3, (byte)0x41, (byte)0xFC, (byte)0x04, (byte)0xE7, (byte)0xE4,
                (byte)0x0F, (byte)0x04, (byte)0x03, (byte)0x01, (byte)0x0C, (byte)0x04, (byte)0x07, (byte)0x04,
                (byte)0x10, (byte)0x2F, (byte)0x53, (byte)0x61, (byte)0x74, (byte)0x6F, (byte)0x73, (byte)0x68,
                (byte)0x69, (byte)0x3A, (byte)0x30, (byte)0x2E, (byte)0x31, (byte)0x35, (byte)0x2E, (byte)0x31,
                (byte)0x2F, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x01
        };

        return a;
    }

    public static final Version test1() {
        try {
            Version version = new Version();//85byte
            version.setVersion(70002);
            version.setServices(new BigInteger(1 + ""));
            version.setTimestamp(new BigInteger(System.currentTimeMillis() + ""));

            NetAddr me = new NetAddr();
            me.setServices(new BigInteger(1 + ""));
            me.setIp(ByteUtil.bytesToHexString(IpUtil.ipToBytes("10.0.67.86")));
            me.setPort(8333);

            NetAddr you = new NetAddr();
            you.setServices(new BigInteger(1 + ""));
            you.setIp(ByteUtil.bytesToHexString(IpUtil.ipToBytes("119.23.55.31")));
            you.setPort(8333);

            version.setAddrMe(me);
            version.setAddrYou(you);
            version.setNonce(new BigInteger(111221 + ""));

            VarStr varStr = new VarStr();
            varStr.setLength(16);
            varStr.setStr("/Satoshi:0.16.0/");

            version.setSubVersionNum(varStr);
            version.setStartHeight(539780);
            version.setRelayFlag((byte)0x00);

            return version;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("version:【 ").append(version);
        sb.append(" 】").append('\n').append("【 ");
        sb.append("services:").append(services.longValue());
        sb.append(" 】").append('\n').append("【 ");
        sb.append("timestamp:").append(timestamp);
        sb.append(" 】").append('\n').append("【 ");
        sb.append("addrMe:").append(addrMe.toString());
        sb.append(" 】").append('\n').append("【 ");
        sb.append("addrYou:").append(addrYou.toString());
        sb.append(" 】").append('\n').append("【 ");
        sb.append("nonce:").append(nonce);
        sb.append(" 】").append('\n').append("【 ");
        sb.append("subVersionNum:").append(subVersionNum.toString());
        sb.append(" 】").append('\n').append("【 ");
        sb.append("startHeight:").append(startHeight);
        sb.append(" 】").append('\n').append("【 ");
        sb.append("rlayFlag:").append(ByteUtil.bytesToHexString(new byte[] {relayFlag})).append(" 】");
        return sb.toString();
    }

    public static final void main(String[] args) throws Exception {
        byte[] a = {
                (byte)0xF9, (byte)0xBe, (byte)0xB4, (byte)0xD9, (byte)0x76, (byte)0x65, (byte)0x72, (byte)0x73,
                (byte)0x69, (byte)0x6F, (byte)0x6E, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0x66, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x47, (byte)0x48, (byte)0xC3, (byte)0x13,
                (byte)0x7F, (byte)0x11, (byte)0x01, (byte)0x00, (byte)0x0D, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x06, (byte)0x09, (byte)0x3D, (byte)0x5A,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x09, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xFF, (byte)0xFF,
                (byte)0x59, (byte)0x4B, (byte)0x49, (byte)0x0B, (byte)0x20, (byte)0x8D, (byte)0x0D, (byte)0x00,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0x7F, (byte)0xC4, (byte)0xA3, (byte)0x41, (byte)0xFC, (byte)0x04, (byte)0xE7, (byte)0xE4,
                (byte)0x10, (byte)0x2F, (byte)0x53, (byte)0x61, (byte)0x74, (byte)0x6F, (byte)0x73, (byte)0x68,
                (byte)0x69, (byte)0x3A, (byte)0x30, (byte)0x2E, (byte)0x31, (byte)0x35, (byte)0x2E, (byte)0x31,
                (byte)0x2F, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x01
        };

        Version v = (Version)MsgUtil.decode(a);
        System.out.println(v.toString());
    }
}
