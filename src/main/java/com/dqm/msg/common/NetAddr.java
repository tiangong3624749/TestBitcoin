package com.dqm.msg.common;

import com.dqm.annotations.Index;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

/**
 * Created by dqm on 2018/8/25.
 */
@Setter
@Getter
public class NetAddr {

    @Index(val = 0, specialType = Index.SpecialType.UINT64)
    private BigInteger services;//uint64_t,常量

    @Index(val = 1, size = 16)
    private String ip;

    @Index(val = 2, specialType = Index.SpecialType.UINT16, inverted = false)
    private int port;//uint16_t

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("services:【 ").append(services.longValue());
        sb.append(" 】").append('\n').append("【 ");
        sb.append("ip:").append(ip);
        sb.append(" 】").append('\n').append("【 ");
        sb.append("port:").append(port).append(" 】");
        return sb.toString();
    }
}