package com.dqm.msg;

import com.dqm.annotations.Index;
import com.dqm.msg.common.MsgPack;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by dqm on 2018/9/14.
 */
@Getter
@Setter
public class Headers implements MsgPack {

    @Index(val = 0, specialType = Index.SpecialType.VARINT)
    private int count;
}
