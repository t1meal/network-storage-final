package ru.gb.storage.commons.messages;

import io.netty.channel.ChannelHandlerContext;
import ru.gb.storage.commons.FilesInfo;

import java.io.IOException;
import java.util.List;

public class DirMessage{
    List<FilesInfo> list;

    public List<FilesInfo> getList() {
        return list;
    }
}
