package org.example;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {
    //Stage
    static Stage primaryStage;
    //Scenes
    static Scene mainScene, newsScene, allNewsScene;


    @Override
    public void start(Stage primaryStage) throws IOException, GeoIp2Exception {
        this.primaryStage = primaryStage;

        //Define main scene - Start screen with three buttons
        SceneCreator mainSceneCreator = new MainSceneCreator(1000,550);
        mainScene = mainSceneCreator.createScene();
        //Define scene where top headlines request is taking place
        SceneCreator newsSceneCreator = new NewsSceneCreator(1000, 550);
        newsScene = newsSceneCreator.createScene();
        // Define scene where all news request is taking place
        SceneCreator allNewsSceneCreator = new allNewsSceneCreator(1000, 550);
        allNewsScene = allNewsSceneCreator.createScene();

        primaryStage.setTitle("NEWS APP");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}