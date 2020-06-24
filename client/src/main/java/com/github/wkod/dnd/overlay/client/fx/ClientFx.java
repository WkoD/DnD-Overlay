package com.github.wkod.dnd.overlay.client.fx;

import javafx.application.Application;
import javafx.stage.Stage;

public class ClientFx extends Application {

    /**
     * Run.
     * 
     * @param args String[]
     */
    public static void runthis(String[] args) {
        Application.launch(args);
    }

    /**
     * start.
     * 
     * @see javafx.application.Application#start(javafx.stage.Stage)
     * @param primaryStage
     * @throws Exception Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        ClientWindow window = new ClientWindow();
        window.setMinWidth(600d);
        window.setMinHeight(300d);
        window.show();
    }
}
