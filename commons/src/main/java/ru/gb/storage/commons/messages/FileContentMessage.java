package ru.gb.storage.commons.messages;

import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.io.RandomAccessFile;

public class FileContentMessage extends Message{
    private byte [] content;
    private long startPosition;
    private RandomAccessFile accessFile;
    private boolean last;

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public long getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(long startPosition) {
        this.startPosition = startPosition;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }


    public void handle(ChannelHandlerContext ctx) throws IOException  {
        accessFile = new RandomAccessFile("F:\\9.pdf", "rw");
        System.out.println(getStartPosition());
        accessFile.seek(getStartPosition());
        accessFile.write(getContent());
        if (isLast()){
            ctx.close();
        }
    }
}

