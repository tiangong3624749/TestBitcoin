package com.dqm.msg.common;

import com.dqm.annotations.Index;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by dqm on 2018/8/31.
 * 区块头
 */
@Getter
@Setter
public class BlockHeader {

    @Index(val = 0, specialType = Index.SpecialType.UINT32)
    private long version;//uint32_t

    @Index(val = 1, size = 32)
    private String prevBlock;

    @Index(val = 2, size = 32)
    private String merkleRoot;

    @Index(val = 3, specialType = Index.SpecialType.UINT32)
    private long timestamp;

    @Index(val = 4, specialType = Index.SpecialType.UINT32)
    private long bits;

    @Index(val = 5, specialType = Index.SpecialType.UINT32)
    private long nonce;

    @Index(val = 6, specialType = Index.SpecialType.UINT8)
    private short txnCount;
}
