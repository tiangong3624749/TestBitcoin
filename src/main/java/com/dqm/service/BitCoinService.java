package com.dqm.service;

import com.dqm.msg.*;
import com.dqm.msg.common.InvVect;
import com.dqm.msg.common.NetAddr;
import com.dqm.utils.*;

import java.math.BigInteger;

/**
 * Created by dqm on 2018/9/2.
 */
public class BitCoinService {

    //发送Version消息
    public Version doVersion() {
        try {
            Version version = new Version();//85byte
            version.setVersion(60001);
            version.setServices(new BigInteger(1 + ""));
            version.setTimestamp(new BigInteger(319000000 + ""));

            NetAddr me = new NetAddr();
            me.setServices(new BigInteger(1 + ""));
//            me.setIp(ByteUtil.bytesToHexString(IpUtil.ipToBytes("10.0.67.86")));
            me.setIp(ByteUtil.bytesToHexString(IpUtil.ipToBytes("192.168.1.4")));
            me.setPort(18333);

            NetAddr you = new NetAddr();
            you.setServices(new BigInteger(1 + ""));
            you.setIp(ByteUtil.bytesToHexString(IpUtil.ipToBytes("198.251.83.19")));
            you.setPort(18333);

            version.setAddrMe(me);
            version.setAddrYou(you);
            version.setNonce(new BigInteger(319798000 + ""));

            VarStr varStr = new VarStr();
            varStr.setLength(0);

            version.setSubVersionNum(varStr);
            version.setStartHeight(98645);

            return version;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 响应version
     * @param version
     */
    public VerAck doVerAck(Version version) {
        try {
            System.out.println(IpUtil.v6Tov4(ByteUtil.hexStringToByte(version.getAddrYou().getIp())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new VerAck();
    }

    /**
     * getblocks消息
     */
    public GetBlocks doGetbBlocks() {
        GetBlocks getBlocks = new GetBlocks();
        getBlocks.setStartCount(1);
        //getBlocks.setHashStart("d39f608a7775b537729884d4e6633bb2105e55a16a14d31b0000000000000000");
        getBlocks.setHashStart("0000000000000000000000000000000000000000000000000000000000000000");
        getBlocks.setHashStop("0000000000000000000000000000000000000000000000000000000000000000");
        return getBlocks;
    }

    /**
     * getblocks消息
     */
    public Pong doPong() {
        Pong pong = new Pong();
        pong.setNonce(new BigInteger(System.currentTimeMillis() + ""));
        return pong;
    }

    /**
     * getheaders消息
     */
    public Headers doHeaders() {
        Headers headers = new Headers();
        headers.setCount(0);
        return headers;
    }

    /**
     * getdata消息
     * @param invVect
     * @return
     */
    public GetData doGetData(InvVect invVect) {
        GetData getData = new GetData();
        getData.setCount(1);

        InvVect[]  invVects = new InvVect[1];

        invVect.setHash("000000000000000000063cb4bcf8e036747ec8afb59877ab8d80405a44501c57");
        invVects[0] = invVect;
        getData.setInventory(invVects);
        return getData;
    }

    /**
     * getdata消息
     * @param invVect
     * @return
     */
    public GetData doGetDataV2() {
        GetData getData = new GetData();
        getData.setCount(1);

        InvVect[]  invVects = new InvVect[1];
        InvVect invVect = new InvVect();
        invVect.setType(2);
        invVect.setHash("000000000000000000063cb4bcf8e036747ec8afb59877ab8d80405a44501c57");
        invVects[0] = invVect;
        getData.setInventory(invVects);
        return getData;
    }

    /**
     * getdata消息
     * @param invVect
     * @return
     */
    public GetData doGetDatas(Inventory inventory) {

        GetData getData = new GetData();
        getData.setCount(inventory.getCount());

        getData.setInventory(inventory.getInventory());
        return getData;
    }
}
