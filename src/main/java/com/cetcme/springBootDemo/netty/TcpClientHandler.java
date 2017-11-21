package com.cetcme.springBootDemo.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;

public class TcpClientHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = Logger.getLogger(TcpClientHandler.class.getName());
    public static ChannelHandlerContext ctx;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // TODO Auto-generated method stub
        System.out.println("chanel active");
        TcpClientHandler.ctx = ctx;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // TODO Auto-generated method stub
        ByteBuf in = (ByteBuf) msg;
        byte[] buff = new byte[in.readableBytes()];
        StringBuilder sb = new StringBuilder();
        in.readBytes(buff);
        for (byte b : buff) {
            sb.append(b);
            sb.append(",");
        }
        String message;
        try {
            message = new String(buff, "UTF-8");
            logger.info("Receive tcp: " + message);
            System.out.println(message);
        } catch (UnsupportedEncodingException e) {
            logger.error("TCP接收数据出错:" + e.getMessage());
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // TODO Auto-generated method stub
//        if(thread != null) {
//            thread.interrupt();
//        }
        logger.warn("TCP is closed:");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // TODO Auto-generated method stub
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // TODO Auto-generated method stub
        super.exceptionCaught(ctx, cause);
    }

}
