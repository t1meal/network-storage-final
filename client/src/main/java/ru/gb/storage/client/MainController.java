package ru.gb.storage.client;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import ru.gb.storage.commons.FilesInfo;
import ru.gb.storage.commons.messages.AuthorizationMessage;
import ru.gb.storage.commons.messages.DirMessage;
import ru.gb.storage.commons.messages.FileRequestMessage;

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
    private final Path CLIENT_PATH = FileSystems.getDefault().getRootDirectories().iterator().next();
    private final Path CLOUD_PATH = Paths.get("C:\\JAVA\\network-storage-final\\server\\src\\main\\java\\resources");

    @FXML
    void initialize() {
        isAuthorized = new SimpleBooleanProperty(false);
        isAuthorized.addListener((observableValue, aBoolean, t1) -> MainController.this.changeScene());
        initTable(tableClient, CLIENT_PATH);
        initTable(tableCloud, CLOUD_PATH);
        tableClient.getItems().addAll(fillingList(CLIENT_PATH));
        tableClient.getItems().addAll(fillingList(CLOUD_PATH));
    }

    private void initTable(TableView<FilesInfo> table, Path root) {
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
                            setText(String.format("%,d bytes", item));
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
    private ObservableList<FilesInfo> fillingList(DirMessage message) {
        ObservableList<FilesInfo> listFiles = FXCollections.observableArrayList();
        listFiles.addAll(message.getList());
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
        AuthorizationMessage message = new AuthorizationMessage(loginField.getText(), passwordField.getText());
        NetworkController.send(message);
        loginField.clear();
        passwordField.clear();
    }

    @FXML
    private void sendToCloud() {
        NetworkController.send(new FileRequestMessage(tableClient.getSelectionModel().getSelectedItem().getPath().toString()));
    }

    private void downloadFromCloud() {

    }

}
