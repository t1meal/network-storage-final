package ru.gb.storage.commons.messages;


import ru.gb.storage.client.MainController;
import ru.gb.storage.commons.FilesInfo;

import java.util.List;

public class StorageMessage extends Message {

    private List<FilesInfo> list;

    MainController controller;

    public StorageMessage(MainController controller) {
        this.controller = controller;
    }

    public List<FilesInfo> getList() {
        return list;
    }

    public void setList(List<FilesInfo> list) {
        this.list = list;
    }

    public MainController getController() {
        return controller;
    }
}
