package ru.gb.storage.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import ru.gb.storage.commons.FilesInfo;
import ru.gb.storage.commons.messages.*;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class MainController {
    @FXML
    private AnchorPane AuthorizationPane;
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private AnchorPane WorkingPane;
    @FXML
    private TableView<FilesInfo> tableClient;
    @FXML
    private TableView<FilesInfo> tableCloud;

    private static BooleanProperty isAuthorized;

    private List<FilesInfo> cloudList;
    private final Path CLIENT_PATH = FileSystems.getDefault().getRootDirectories().iterator().next();
    private final Path CLOUD_PATH = Paths.get("C:\\JAVA\\network-storage-final\\server\\src\\main\\java\\resources");


    @FXML
    void initialize() {
        isAuthorized = new SimpleBooleanProperty(false);
        isAuthorized.addListener((observableValue, aBoolean, t1) -> MainController.this.changeScene());
        initTable(tableClient);
        initTable(tableCloud);
        tableClient.getItems().addAll(fillingList(CLIENT_PATH));
        tableCloud.getItems().addAll(fillingList(CLIENT_PATH));
    }

    public void refreshClodTable (List<FilesInfo> storageList){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                tableCloud.getItems().clear();
                tableCloud.getItems().addAll(fillingList(storageList));
            }
        });
    }


    private void initTable(TableView<FilesInfo> table) {
        TableColumn<FilesInfo, String> columnName = new TableColumn<>("Имя");
        columnName.setCellValueFactory(filesInfoStringCellDataFeatures -> new SimpleStringProperty(filesInfoStringCellDataFeatures.getValue().getName()));
        columnName.setPrefWidth(200);

        TableColumn<FilesInfo, Long> columnSize = new TableColumn<>("Размер");
        columnSize.setCellValueFactory(filesInfoLongCellDataFeatures -> new SimpleObjectProperty<>(filesInfoLongCellDataFeatures.getValue().getSize()));
        columnSize.setCellFactory(new Callback<>() {
            @Override
            public TableCell<FilesInfo, Long> call(TableColumn<FilesInfo, Long> filesInfoLongTableColumn) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(Long item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty || item == -1L) {
                            setText(null);
                            setStyle("");
                        } else {

                            setText(String.format("%,d kBts", item/1000));
                        }
                    }
                };
            }
        });
        columnSize.setPrefWidth(80);
        table.getColumns().addAll(columnName, columnSize);
    }

    private ObservableList<FilesInfo> fillingList(Path path) {
        ObservableList<FilesInfo> listFiles = FXCollections.observableArrayList();
        List<File> list = Arrays.asList(path.toFile().listFiles());
        for (File file : list) {
            listFiles.add(new FilesInfo(file.toPath()));
        }
        return listFiles;
    }
    private ObservableList<FilesInfo> fillingList(List<FilesInfo> storageList) {
        ObservableList<FilesInfo> listFiles = FXCollections.observableArrayList();
        listFiles.addAll(storageList);
        return listFiles;
    }

    //      5. метод меняющий статус авторизации
    public static void setIsAuthorized(boolean authorized) {
        MainController.isAuthorized.set(authorized);
    }

    public void changeScene() {
        AuthorizationPane.setVisible(false);
        AuthorizationPane.setManaged(false);
        WorkingPane.setVisible(true);
        WorkingPane.setManaged(true);
    }

    @FXML
    private void tryToAuth() {
        System.out.println("Запрос авторизации к серверу!");
        NetworkController.send(new AuthorizationMessage(loginField.getText(), passwordField.getText()));
//        NetworkController.send(new StorageMessage(this));
        NetworkController.send(new StorageMessage(this));
        loginField.clear();
        passwordField.clear();
    }

    @FXML
    private void sendToCloud() {
        NetworkController.send(new FileRequestMessage(tableClient.getSelectionModel().getSelectedItem().getPath().toString()));
    }

    private void downloadFromCloud() {

    }

    private static void updateUI(Runnable r) {
        if (Platform.isFxApplicationThread()) {
            r.run();
        } else {
            Platform.runLater(r);
        }
    }

}
