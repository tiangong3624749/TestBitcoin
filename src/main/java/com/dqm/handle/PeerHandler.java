package com.dqm.handle;

import com.dqm.msg.*;
import com.dqm.service.BitCoinService;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

/**
 * Created by dqm on 2018/8/23.
 */
public class PeerHandler extends ChannelDuplexHandler {

    private BitCoinService bitCoinService = new BitCoinService();

    /**
     * 进来的请求
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof Version) {
            ctx.write(bitCoinService.doVerAck((Version) msg));
            ctx.flush();
        } else if(msg instanceof VerAck) {
            ctx.write(bitCoinService.doGetbBlocks());
            ctx.flush();
        } else if(msg instanceof Inventory) {
            ctx.write(bitCoinService.doGetDataV2());
            ctx.flush();
        } else if(msg instanceof Inventory) {
            ctx.write(bitCoinService.doGetbBlocks());
            ctx.flush();
        } else if(msg instanceof Ping) {
            ctx.write(bitCoinService.doPong());
            ctx.flush();
        } else if(msg instanceof GetHeaders) {
            ctx.write(bitCoinService.doHeaders());
            ctx.flush();
        }
    }

    /**
     * 发送请求
     * @param ctx
     * @param msg
     * @param promise
     * @throws Exception
     */
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ctx.write(msg);
    }

    // 当连接建立的时候向服务端发送消息 ，channelActive 事件当连接建立的时候会触发
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.write(bitCoinService.doVersion());
        ctx.flush();
    }
}
