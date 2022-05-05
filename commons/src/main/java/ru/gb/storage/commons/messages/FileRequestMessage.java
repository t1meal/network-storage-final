package ru.gb.storage.commons.messages;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;


public class FileRequestMessage extends Message {
    private String path;
    private int counter = 0;
    private RandomAccessFile accessFile;

    public FileRequestMessage(String path) {
        this.path = path;
    }

    @Override
    public void handle(ChannelHandlerContext ctx) throws IOException {
            final File file = new File(getPath());
            if (accessFile == null){
                accessFile = new RandomAccessFile(file, "r");
                sendFile(ctx);
            }
    }

    private void sendFile(ChannelHandlerContext ctx) throws IOException {

        if (accessFile != null) {
            byte[] partOfFile;
            final long available = accessFile.length() - accessFile.getFilePointer();
            if (available > 64 * 1024) {
                partOfFile = new byte[64 * 1024];
            } else {
                partOfFile = new byte[(int) available];
            }
            FileContentMessage contentMessage = new FileContentMessage();
            contentMessage.setStartPosition(accessFile.getFilePointer());
            accessFile.read(partOfFile);
            contentMessage.setContent(partOfFile);
            final boolean last = accessFile.getFilePointer() == accessFile.length();
            contentMessage.setLast(last);
            System.out.println("message send:" + ++counter);

            ctx.channel().writeAndFlush(contentMessage).addListener((ChannelFutureListener) future -> {
                if (!last) {
                    sendFile(ctx);
                }
            });
            if (last) {
                accessFile.close();
                accessFile = null;
            }
        }
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath () {return path;}

}


