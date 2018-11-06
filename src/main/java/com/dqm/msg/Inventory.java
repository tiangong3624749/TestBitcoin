package com.dqm.msg;

import com.dqm.annotations.Index;
import com.dqm.msg.common.InvVect;
import com.dqm.msg.common.MsgPack;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by dqm on 2018/9/13.
 */
@Getter
@Setter
public class Inventory implements MsgPack {

    @Index(val = 0, specialType = Index.SpecialType.VARINT)
    private int count;

    @Index(val = 1, dependentIndex = 0, dependentType = Index.DependentType.LENGTH)
    private InvVect[] inventory;
}
