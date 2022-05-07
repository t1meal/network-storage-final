package ru.gb.storage.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import ru.gb.storage.commons.FilesInfo;
import ru.gb.storage.commons.messages.Message;
import ru.gb.storage.commons.messages.StorageMessage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FirstServerHandler extends SimpleChannelInboundHandler<Message> {

        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            System.out.println("New active channel");
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
            System.out.println("New message " + msg.getClass().toString());
            if (msg instanceof StorageMessage){
                StorageMessage storageMessage = (StorageMessage) msg;
                Path path = Paths.get("server/src/main/java/resources");
                File[] storageFiles = path.toFile().listFiles();
                List<FilesInfo> listFiles = new ArrayList<>();
                for (File file : storageFiles) {
                    listFiles.add(new FilesInfo(file.toPath()));
                }
                storageMessage.setList(listFiles);
                ctx.writeAndFlush(storageMessage);
            }

        }


        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            ctx.close();
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws IOException {
            System.out.println("Client disconnect!");
        }
    }

