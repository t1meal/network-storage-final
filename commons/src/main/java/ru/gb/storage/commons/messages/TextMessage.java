package ru.gb.storage.commons.messages;

import io.netty.channel.ChannelHandlerContext;

public class TextMessage extends Message {
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void handle(ChannelHandlerContext ctx) {
        System.out.println("incoming text message: " + getText());
        ctx.writeAndFlush(this);
    }

    @Override
    public String toString() {
        return "TextMessage{" +
                "text='" + text + '\'' +
                '}';
    }
}
