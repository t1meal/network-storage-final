package ru.gb.storage.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import ru.gb.storage.commons.messages.AuthorizationMessage;

public class AuthorizationHandler extends SimpleChannelInboundHandler<AuthorizationMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, AuthorizationMessage msg) throws Exception {
        msg.handle(ctx);
        ctx.writeAndFlush(msg);
    }
}
