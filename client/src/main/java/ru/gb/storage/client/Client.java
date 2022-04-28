package ru.gb.storage.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.gb.storage.client.controller.FxController;

import java.io.File;
import java.util.Objects;

public class Client extends Application {
    private final double WIDTH = 600;
    private final double HEIGHT = 600;

    public static void main(String[] args) {
       launch(args);

    }

    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("sample.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("DropBox");
        primaryStage.setWidth(WIDTH);
        primaryStage.setHeight(HEIGHT);

        primaryStage.show();




//        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
//            @Override
//            public void handle(WindowEvent event) {
//                System.out.println("Stage is closing");
//                stage.close();
//                System.exit(1);
//
//            }
        }
    }



