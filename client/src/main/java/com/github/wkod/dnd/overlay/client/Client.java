package com.github.wkod.dnd.overlay.client;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Overlay.
 * 
 * @author cpawek
 * @version 1.0
 */
public class Client extends Application {

   public static void runthis(String[] args) {
      Application.launch(args);
   }

   /**
    * start.
    * 
    * @see javafx.application.Application#start(javafx.stage.Stage)
    * @param pPrimaryStage2
    * @throws Exception
    */
   @Override
   public void start(Stage pPrimaryStage) throws Exception {
      new ClientWindow().show();
   }
}
