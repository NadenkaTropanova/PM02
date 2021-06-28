package com.salesregister;

import com.salesregister.controller.AuthenticationController;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Main extends Application {

    private static ConfigurableApplicationContext applicationContext;
    private static Stage stage;

    public static Stage getStage() {
        return stage;
    }

    public static ConfigurableApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        AuthenticationController.load();
    }

    @Override
    public void init() throws Exception {
        applicationContext = SpringApplication.run(Main.class);
    }

    @Override
    public void stop() throws Exception {
        applicationContext.stop();
    }
}