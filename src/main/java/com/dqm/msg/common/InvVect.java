package com.dqm.msg.common;

import com.dqm.annotations.Index;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by dqm on 2018/8/31.
 */
@Getter
@Setter
public class InvVect {

    @Index(val = 0, specialType = Index.SpecialType.UINT32)
    private long type;//uint32_t【包含的值：0表示ERROR，数据可忽略；1表示MSG_TX，hash是关于交易的；2表示MSG_BLOCK，hash是关于区块的】

    @Index(val = 1, size = 32)
    private String hash;
}
