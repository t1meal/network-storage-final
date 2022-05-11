package ru.gb.storage.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import ru.gb.storage.commons.messages.Message;
import ru.gb.storage.commons.messages.StorageMessage;

public class ClientHandler extends SimpleChannelInboundHandler<Message> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        System.out.println("New message " + msg.getClass().toString());
        if (msg instanceof StorageMessage){
            StorageMessage storageMessage = (StorageMessage) msg;
            storageMessage.getController().refreshClodTable(storageMessage.getList());
        }
    }

}
