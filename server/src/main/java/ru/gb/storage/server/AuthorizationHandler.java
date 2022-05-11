package ru.gb.storage.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import ru.gb.storage.commons.messages.AuthorizationMessage;

public class AuthorizationHandler extends SimpleChannelInboundHandler<AuthorizationMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, AuthorizationMessage msg) throws Exception {
        System.out.println("New authorization message!");
        if (msg.getLogin().equals("login1") && msg.getPassword().equals("pass1")){
            msg.setAuthorizationStatus(true);
        } else {
            msg.setAuthorizationStatus(false);
        }
        ctx.writeAndFlush(msg);
    }
}
