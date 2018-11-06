package com.dqm.msg;

import com.dqm.annotations.Index;
import com.dqm.msg.common.MsgPack;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by dqm on 2018/9/13.
 */
@Getter
@Setter
public class GetBlocks implements MsgPack {

    @Index(val=0, specialType = Index.SpecialType.VARINT)
    private int startCount;

    @Index(val=1, size = 32, hex = true)
    private String hashStart;

    @Index(val=2, size = 32, hex = true)
    private String hashStop;
}
