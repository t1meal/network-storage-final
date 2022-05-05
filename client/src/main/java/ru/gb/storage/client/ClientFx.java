package ru.gb.storage.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientFx extends Application {
    private final double WIDTH = 600;
    private final double HEIGHT = 600;


    public static void main(String[] args) {
       launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("DropBox");
        primaryStage.setWidth(WIDTH);
        primaryStage.setHeight(HEIGHT);
        //        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
//            @Override
//            public void handle(WindowEvent event) {
//                System.out.println("Stage is closing");
//                stage.close();
//                System.exit(1);
//
//            }
        primaryStage.show();
        new NetworkController().connect();

    }
}



