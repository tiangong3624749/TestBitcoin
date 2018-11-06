package com.dqm.msg;

import com.dqm.annotations.Index;
import com.dqm.msg.common.MsgPack;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

/**
 * Created by dqm on 2018/9/14.
 */
@Setter
@Getter
public class Ping implements MsgPack {

    @Index(val = 0, specialType = Index.SpecialType.UINT64)
    private BigInteger nonce;
}
