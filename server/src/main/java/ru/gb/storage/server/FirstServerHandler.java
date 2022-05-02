package ru.gb.storage.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import ru.gb.storage.commons.messages.Message;
import java.io.IOException;

    public class FirstServerHandler extends SimpleChannelInboundHandler<Message> {

        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            System.out.println("New active channel");
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
            System.out.println("New message " + msg.getClass().toString());
            msg.handle(ctx);
        }


        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            ctx.close();
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws IOException {
            System.out.println("client disconnect");
        }
    }

