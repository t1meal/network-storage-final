package ru.gb.storage.commons.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import ru.gb.storage.commons.messages.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;


import java.util.List;

public class JSonEncoder extends MessageToMessageEncoder<Message> {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
        byte [] bytes = OBJECT_MAPPER.writeValueAsBytes(msg);
        out.add(ctx.alloc().buffer().writeBytes(bytes));
    }

    public byte[] encodeForClient (Message message) throws JsonProcessingException {

        byte [] bytes = OBJECT_MAPPER.writeValueAsBytes(message);
        return bytes;
    }
}
