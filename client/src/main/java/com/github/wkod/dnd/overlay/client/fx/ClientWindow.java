package com.github.wkod.dnd.overlay.client.fx;

import static com.github.wkod.dnd.overlay.api.localization.Label.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.github.wkod.dnd.overlay.api.OlScreen;
import com.github.wkod.dnd.overlay.client.config.Configuration;
import com.github.wkod.dnd.overlay.client.rest.Sender;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

/**
 * ControlWindow.
 * 
 * @author cpawek
 * @version 1.0
 */
public class ClientWindow extends Stage {

    private final List<OlScreenBox> screenBoxList = new ArrayList<>();
    private final HBox screenBoxPane;

    private final Scene scene;

    public ClientWindow() {
        // root pane
        BorderPane root = new BorderPane();

        // set menu
        root.setTop(createMenu());

        // set screen pane
        screenBoxPane = new HBox();
        screenBoxPane.setMinHeight(200d);
        root.setCenter(screenBoxPane);

        // set console
        root.setBottom(createConsole());

        // set scene
        scene = new Scene(root);

        setTitle("Control Window");
        setScene(scene);

        // update screens if enabled
        if (Configuration.UPDATE_SCREEN_STARTUP.get()) {
            updateScreenListPane();
        }
    }

    private MenuBar createMenu() {
        // create menu elements
        Menu menu = new Menu(CLIENT_MENU.localize());
        MenuItem screens = new MenuItem(CLIENT_MENU_READ_SCREENS.localize());
        MenuItem console = new MenuItem(CLIENT_MENU_TOGGLE_CONSOLE.localize());
        MenuItem exit = new MenuItem(CLIENT_MENU_EXIT.localize());

        menu.getItems().add(screens);
        menu.getItems().add(console);
        menu.getItems().add(exit);

        // create menu bar
        MenuBar menubar = new MenuBar();
        menubar.getMenus().add(menu);

        // set events
        screens.setOnAction(e -> {
            updateScreenListPane();
            e.consume();
        });

        console.setOnAction(e -> {
            Node node = ((BorderPane) (menubar.getParent())).getBottom();

            boolean togglestate = !node.isVisible();
            node.setManaged(togglestate);
            node.setVisible(togglestate);
            e.consume();
        });

        exit.setOnAction(e -> {
            Platform.exit();
            e.consume();
        });

        setOnCloseRequest(e -> {
            Platform.exit();
        });

        return menubar;
    }

    private TextArea createConsole() {
        TextArea console = new TextArea();
        console.setEditable(false);
        console.setStyle("-fx-font-family: 'monospaced';");
        console.setMinHeight(100d);
        console.setMaxHeight(100d);

        StaticOutputStreamAppender.setStaticOutputStream(new TextAreaOutputStream(console));

        return console;
    }

    private void updateScreenListPane() {
        screenBoxList.clear();
        screenBoxPane.getChildren().clear();

        List<OlScreen> list = Sender.getScreens();

        if (list == null) {
            return;
        }

        for (OlScreen screen : list) {
            // set main pane
            OlScreenBox pane = new OlScreenBox(screen);

            // add to screen pane
            HBox.setHgrow(pane, Priority.ALWAYS);
            screenBoxPane.getChildren().add(pane);
            screenBoxList.add(pane);
        }
    }

    private static class TextAreaOutputStream extends OutputStream {

        private final TextArea textArea;

        public TextAreaOutputStream(TextArea textArea) {
            this.textArea = textArea;
        }

        @Override
        public void write(int b) throws IOException {
            textArea.appendText(String.valueOf((char) b));
        }
    }
}
