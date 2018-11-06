package com.dqm;

import com.dqm.handle.PeerHandler;
import com.dqm.handle.code.MsgPackCode;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by dqm on 2018/8/23.
 */
public class PeerServer {

    public static final void main(String[] args) {
        try {
            EventLoopGroup clientGroup = new NioEventLoopGroup();

            Bootstrap client = new Bootstrap();

            client.group(clientGroup);

            client.channel(NioSocketChannel.class);
            client.handler(new ChannelInitializer<Channel>() {
                //NioServerSocketChannel虽然继承了ChannelInboundHandlerAdapter，
                //但是初始化知乎，会从通道的管道中移除初始化器ChannelInitializer上下文，
                //并从通道初始化器的上下文Map中移除通道初始化器ChannelInitializer上下文

                @Override
                public void initChannel(Channel channel) throws Exception {
                    channel.pipeline().addLast(new MsgPackCode());
                    channel.pipeline().addLast(new PeerHandler());
                }
            });
//            Channel channel = client.connect("198.251.83.19", 18333).channel();
            Channel channel = client.connect("119.23.63.114", 8333).channel();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
