package com.cetcme.springBootDemo.task;

import com.cetcme.springBootDemo.utils.ConvertUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by qiuhong on 09/11/2017.
 */
public class TcpSendTask implements Runnable {

    private ChannelHandlerContext ctx;
    private String sendMsg;

    public void setCtx(ChannelHandlerContext ctx){
        this.ctx = ctx;
    }

    public void setSendMsg(String sendMsg){
        this.sendMsg = sendMsg;
    }

    @Override
    public void run() {
        try {
            byte[] tempdata = ConvertUtil.stringToBytes(sendMsg);
            byte[] senddata = new byte[tempdata.length + 2];

            System.arraycopy(tempdata, 0, senddata, 0, senddata.length - 2);
            senddata[senddata.length - 2] = 0x0d;
            senddata[senddata.length - 1] = 0x0a;

            ByteBuf responseMsgBuf = Unpooled.copiedBuffer(senddata);
            ctx.channel().writeAndFlush(responseMsgBuf);
            Thread.sleep(50);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
