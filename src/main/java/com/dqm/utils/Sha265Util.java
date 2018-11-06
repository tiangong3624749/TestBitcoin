package com.dqm.utils;

import com.dqm.signature.AddrType;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by dqm on 2018/9/18.
 * http://blog.51cto.com/favccxx/1738655
 * http://denebola.win/2017/05/15/BitCoin-transaction-address/
 */
public class Sha265Util {

    /**
     * HASH计算：SHA-256
     * @return
     */
    public static byte[] sha256(final byte[] data) {
        // 是否是有效字符串
        if (data != null && data.length > 0) {
            try {
                // SHA 加密开始
                // 创建加密对象 并傳入加密類型
                MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                // 传入要加密的字符串
                messageDigest.update(data);
                // 得到 byte 類型结果
                return messageDigest.digest();

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        return null;
    }


    /**
     * CSPRNG随机产生
     * @return
     */
    private static byte[] random() {
        SecureRandom sr;
        byte[] salt = new byte[16];
        try {
            sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
            sr.nextBytes(salt);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return salt;
    }

    /**
     * 私钥（CSPRNG随机产生,256bit二进制数，64位16进制表示）
     * @return
     */
    public static String newPrivateKey() {
        return ByteUtil.bytesToHexString(sha256(random()));
    }

    /**
     * 公钥（私钥 通过椭圆曲线相乘RIPEMD160产生，20字节160bit）
     * @param privateKey
     * @return
     */
    public static byte[] newPublicKey(String privateKey) {
        byte[] tmp = privateKey.getBytes();
        RIPEMD160Digest digest = new RIPEMD160Digest();
        digest.update(tmp, 0, tmp.length);
        byte[] ripemd160Bytes = new byte[digest.getDigestSize()];
        digest.doFinal(ripemd160Bytes, 0);
        return ripemd160Bytes;
    }

    /**
     * 1. 公钥哈希（公钥通过HSA256和RIPEMD160处理得到，20字节160bit）
     * 2. 比特币地址（公钥哈希通过Base58check编码得到）
      * @param rawAddr
     * @return
     */
    public static String newBtcAddr(AddrType addrType, byte[] rawAddr){
        String pubHash = ByteUtil.bytesToHexString(rawAddr);
        StringBuffer raw = new StringBuffer(addrType.getType());
        raw.append(pubHash);
        byte[] append = sha256(sha256(ByteUtil.hexStringToByte(raw.toString())));
        raw.append(ByteUtil.bytesToHexString(append).substring(0,8));
        return Base58.encode(ByteUtil.hexStringToByte(raw.toString().toLowerCase()));
    }

    public static final void main(String[] args) {
        String t1 = Sha265Util.newPrivateKey();
        byte[] tmp = Sha265Util.newPublicKey(t1);
        String t2 = Base58.encode(tmp);
        String t3 = Sha265Util.newBtcAddr(AddrType.BitcoinPubkey, tmp);
        System.out.println(t1);
        System.out.println(t2);
        System.out.println(t3);
    }
}
