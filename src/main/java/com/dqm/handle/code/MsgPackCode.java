package com.dqm.handle.code;

import com.dqm.msg.common.MsgPack;
import com.dqm.utils.MsgUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.util.List;

/**
 * Created by dqm on 2018/8/30.
 * 能处理in/out的两种请求
 */
public class MsgPackCode extends ByteToMessageCodec<MsgPack> {

    public void encode(ChannelHandlerContext context, MsgPack payload, ByteBuf out) throws Exception {
        try {
            out.writeBytes(MsgUtil.encode(payload));
            context.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void decode(ChannelHandlerContext context, ByteBuf byteBuf, List<Object> in) throws Exception {
        byte[] data = ByteBufUtil.getBytes(byteBuf);
        MsgPack msgPack = MsgUtil.decode(data);
        if(null != msgPack)
            in.add(msgPack);
        byteBuf.skipBytes(byteBuf.readableBytes());
    }
}
