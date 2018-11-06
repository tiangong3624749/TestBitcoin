package com.dqm.utils;

import com.dqm.annotations.Index;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by dqm on 2018/8/31.
 */
@Setter
@Getter
public class VarStr {
    @Index(val = 0, specialType = Index.SpecialType.VARINT)
    private int length;//varint

    @Index(val = 1, dependentType = Index.DependentType.LENGTH, dependentIndex = 0, hex = false)
    private String str;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("length:【 ").append(length);
        sb.append(" 】").append('\n').append("【 ");
        sb.append("str:").append(str).append(" 】");
        return sb.toString();
    }
}
