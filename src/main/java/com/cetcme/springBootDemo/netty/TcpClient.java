package com.cetcme.springBootDemo.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

public class TcpClient {

    private String host;
    private int port;

    ChannelFuture f = null;
    EventLoopGroup group = new NioEventLoopGroup();

    public void connect(String host, int port) throws Exception {
        // Configure the client.
        this.host = host;
        this.port = port;

        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, new ByteBuf[] { Unpooled.wrappedBuffer(new byte[] { '\r', '\n' }) }));
                            ch.pipeline().addLast(new TcpClientHandler());
                        }
                    });

            // Start the client.
            ChannelFuture f = b.connect(host, port).sync();

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();

        } finally {
            // Shut down the event loop to terminate all threads.
            if (null != f) {
                if (f.channel() != null && f.channel().isOpen()) {
                    f.channel().close();
                }
            }
            System.out.println("准备重连");
            connect(host,port);
            System.out.println("重连成功");

        }
    }

}
