package com.dqm.msg;

import com.dqm.annotations.Index;
import com.dqm.utils.ByteUtil;
import com.dqm.utils.CodeUtil;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by dqm on 2018/8/23.
 */
@Setter
@Getter
public class Transaction {

    @Index(val = 0, specialType = Index.SpecialType.UINT32)
    private long version;//交易版本
    @Index(val = 1, specialType = Index.SpecialType.VARINT)
    private int inputCount;//输入数量
    @Index(val = 2, dependentIndex = 1, dependentType = Index.DependentType.LENGTH)
    private TransactionInput[] inputs;//输入
    @Index(val = 3, specialType = Index.SpecialType.VARINT)
    private int outputCount;//输出数量
    @Index(val = 4, dependentIndex = 3, dependentType = Index.DependentType.LENGTH)
    private TransactionOutput[] outputs;//输出
    @Index(val = 5, specialType = Index.SpecialType.UINT32)
    private long lockTime;

    //输入
    @Setter
    @Getter
    public static class TransactionInput {
        @Index(val = 0)
        private TransactionOutpoint previousOutput;
        @Index(val = 1, specialType = Index.SpecialType.VARINT)
        private int signScriptLen;//签名脚本长度
        @Index(val = 2, dependentIndex = 1, dependentType = Index.DependentType.LENGTH)
        private String signScript;//签名脚本
        @Index(val = 3, specialType = Index.SpecialType.UINT32)
        private long sequence;
    }

    @Setter
    @Getter
    public static class TransactionOutpoint {
        @Index(val = 0, size = 32)
        private String hash;//交易用的散列
        @Index(val = 1, specialType = Index.SpecialType.UINT32)
        private long index;//输出索引
    }

    //输出
    @Setter
    @Getter
    public static class TransactionOutput {
        @Index(val = 0, specialType = Index.SpecialType.UINT64)
        private BigInteger value;//数量
        @Index(val = 1, specialType = Index.SpecialType.VARINT)
        private int pkScriptLen;//公钥脚本长度
        @Index(val = 2, dependentIndex = 1, dependentType = Index.DependentType.LENGTH)
        private String pkScript;//公钥脚本
    }

    public static final void main(String[] args) throws Exception {
        byte[] test = {//https://en.bitcoin.it/wiki/Protocol_documentation#tx
        //(byte)0xF9, (byte)0xBE, (byte)0xB4, (byte)0xD9, (byte)0x74, (byte)0x78, (byte)0x00, (byte)0x00 , (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00
       //, (byte)0x02, (byte)0x01, (byte)0x00, (byte)0x00, (byte)0xE2, (byte)0x93, (byte)0xCD,
         //(byte)0xBE ,
                (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x01, (byte)0x6D, (byte)0xBD, (byte)0xDB
       , (byte)0x08, (byte)0x5B, (byte)0x1D, (byte)0x8A, (byte)0xF7, (byte)0x51, (byte)0x84, (byte)0xF0 , (byte)0xBC, (byte)0x01, (byte)0xFA, (byte)0xD5, (byte)0x8D, (byte)0x12, (byte)0x66, (byte)0xE9
       , (byte)0xB6, (byte)0x3B, (byte)0x50, (byte)0x88, (byte)0x19, (byte)0x90, (byte)0xE4, (byte)0xB4 , (byte)0x0D, (byte)0x6A, (byte)0xEE, (byte)0x36, (byte)0x29, (byte)0x00, (byte)0x00, (byte)0x00
       , (byte)0x00, (byte)0x8B, (byte)0x48, (byte)0x30, (byte)0x45, (byte)0x02, (byte)0x21, (byte)0x00 , (byte)0xF3, (byte)0x58, (byte)0x1E, (byte)0x19, (byte)0x72, (byte)0xAE, (byte)0x8A, (byte)0xC7
       , (byte)0xC7, (byte)0x36, (byte)0x7A, (byte)0x7A, (byte)0x25, (byte)0x3B, (byte)0xC1, (byte)0x13 , (byte)0x52, (byte)0x23, (byte)0xAD, (byte)0xB9, (byte)0xA4, (byte)0x68, (byte)0xBB, (byte)0x3A
       , (byte)0x59, (byte)0x23, (byte)0x3F, (byte)0x45, (byte)0xBC, (byte)0x57, (byte)0x83, (byte)0x80 , (byte)0x02, (byte)0x20, (byte)0x59, (byte)0xAF, (byte)0x01, (byte)0xCA, (byte)0x17, (byte)0xD0
       , (byte)0x0E, (byte)0x41, (byte)0x83, (byte)0x7A, (byte)0x1D, (byte)0x58, (byte)0xE9, (byte)0x7A , (byte)0xA3, (byte)0x1B, (byte)0xAE, (byte)0x58, (byte)0x4E, (byte)0xDE, (byte)0xC2, (byte)0x8D
       , (byte)0x35, (byte)0xBD, (byte)0x96, (byte)0x92, (byte)0x36, (byte)0x90, (byte)0x91, (byte)0x3B , (byte)0xAE, (byte)0x9A, (byte)0x01, (byte)0x41, (byte)0x04, (byte)0x9C, (byte)0x02, (byte)0xBF
       , (byte)0xC9, (byte)0x7E, (byte)0xF2, (byte)0x36, (byte)0xCE, (byte)0x6D, (byte)0x8F, (byte)0xE5 , (byte)0xD9, (byte)0x40, (byte)0x13, (byte)0xC7, (byte)0x21, (byte)0xE9, (byte)0x15, (byte)0x98
       , (byte)0x2A, (byte)0xCD, (byte)0x2B, (byte)0x12, (byte)0xB6, (byte)0x5D, (byte)0x9B, (byte)0x7D , (byte)0x59, (byte)0xE2, (byte)0x0A, (byte)0x84, (byte)0x20, (byte)0x05, (byte)0xF8, (byte)0xFC
       , (byte)0x4E, (byte)0x02, (byte)0x53, (byte)0x2E, (byte)0x87, (byte)0x3D, (byte)0x37, (byte)0xB9 , (byte)0x6F, (byte)0x09, (byte)0xD6, (byte)0xD4, (byte)0x51, (byte)0x1A, (byte)0xDA, (byte)0x8F
       , (byte)0x14, (byte)0x04, (byte)0x2F, (byte)0x46, (byte)0x61, (byte)0x4A, (byte)0x4C, (byte)0x70 , (byte)0xC0, (byte)0xF1, (byte)0x4B, (byte)0xEF, (byte)0xF5, (byte)0xFF, (byte)0xFF, (byte)0xFF
       , (byte)0xFF, (byte)0x02, (byte)0x40, (byte)0x4B, (byte)0x4C, (byte)0x00, (byte)0x00, (byte)0x00 , (byte)0x00, (byte)0x00, (byte)0x19, (byte)0x76, (byte)0xA9, (byte)0x14, (byte)0x1A, (byte)0xA0
       , (byte)0xCD, (byte)0x1C, (byte)0xBE, (byte)0xA6, (byte)0xE7, (byte)0x45, (byte)0x8A, (byte)0x7A , (byte)0xBA, (byte)0xD5, (byte)0x12, (byte)0xA9, (byte)0xD9, (byte)0xEA, (byte)0x1A, (byte)0xFB
       , (byte)0x22, (byte)0x5E, (byte)0x88, (byte)0xAC, (byte)0x80, (byte)0xFA, (byte)0xE9, (byte)0xC7 , (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x19, (byte)0x76, (byte)0xA9, (byte)0x14
       , (byte)0x0E, (byte)0xAB, (byte)0x5B, (byte)0xEA, (byte)0x43, (byte)0x6A, (byte)0x04, (byte)0x84 , (byte)0xCF, (byte)0xAB, (byte)0x12, (byte)0x48, (byte)0x5E, (byte)0xFD, (byte)0xA0, (byte)0xB7
       , (byte)0x8B, (byte)0x4E, (byte)0xCC, (byte)0x52, (byte)0x88, (byte)0xAC, (byte)0x00, (byte)0x00 , (byte)0x00, (byte)0x00};
        
        ByteBuffer byteBuffer = ByteBuffer.wrap(test).order(ByteOrder.BIG_ENDIAN);
        Transaction transaction = CodeUtil.decode(byteBuffer, Transaction.class);
        System.out.println(transaction.getVersion());
        System.out.println(transaction.getOutputs()[0].getValue());
        System.out.println(transaction.getInputs()[0].getPreviousOutput().getHash());
        byte[] a = ByteUtil.hexStringToByte(transaction.getInputs()[0].getPreviousOutput().getHash());
        System.out.println(a.length);
    }
}
