package com.github.wkod.dnd.overlay.client.fx;

import static com.github.wkod.dnd.overlay.client.fx.localization.Label.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.github.wkod.dnd.overlay.api.OlScreen;
import com.github.wkod.dnd.overlay.client.fx.configuration.ConfigurationWindow;
import com.github.wkod.dnd.overlay.client.rest.RestClient;
import com.github.wkod.dnd.overlay.configuration.ClientConfiguration;
import com.github.wkod.dnd.overlay.configuration.ServerConfiguration;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class ClientWindow extends Stage {

    private Menu viewmenu;
    private int screensmenudefaultitems;

    private final List<OlScreenBox> screenBoxList = new ArrayList<>();
    private final HBox screenBoxPane;

    private final TextArea console;

    public ClientWindow() {
        // root pane
        BorderPane root = new BorderPane();

        // set menu
        root.setTop(createMenu());

        // set screen pane
        screenBoxPane = new HBox();
        root.setCenter(screenBoxPane);

        // set console
        console = createConsole();
        root.setBottom(console);

        // set scene
        Scene scene = new Scene(root);

        setTitle("Control Window");
        setScene(scene);

        // update screens if enabled
        if (ClientConfiguration.UPDATE_SCREEN_STARTUP.get()) {
            updateScreenListPane();
        }

        setMinWidth(600d);
        setMinHeight(400d);
    }

    private MenuBar createMenu() {
        // create menu elements
        Menu menu = new Menu(CLIENT_MENU.localize());
        MenuItem readscreens = new MenuItem(CLIENT_MENU_READ_SCREENS.localize());
        MenuItem exit = new MenuItem(CLIENT_MENU_EXIT.localize());

        menu.getItems().add(readscreens);
        menu.getItems().add(exit);

        Menu configuration = new Menu(CLIENT_CONFIGURATION.localize());
        MenuItem client = new MenuItem(CLIENT_CONFIGURATION_CLIENT.localize());
        MenuItem server = new MenuItem(CLIENT_CONFIGURATION_SERVER.localize());

        configuration.getItems().add(client);
        configuration.getItems().add(server);

        viewmenu = new Menu(CLIENT_VIEW.localize());
        CheckMenuItem toggleconsole = new CheckMenuItem(CLIENT_VIEW_CONSOLE.localize());
        toggleconsole.setSelected(true);

        viewmenu.getItems().add(toggleconsole);
        viewmenu.getItems().add(new SeparatorMenuItem());
        screensmenudefaultitems = viewmenu.getItems().size();

        // create menu bar
        MenuBar menubar = new MenuBar();
        menubar.getMenus().add(menu);
        menubar.getMenus().add(configuration);
        menubar.getMenus().add(viewmenu);

        // set events
        readscreens.setOnAction(e -> {
            updateScreenListPane();
            e.consume();
        });

        exit.setOnAction(e -> {
            Platform.exit();
            e.consume();
        });

        setOnCloseRequest(e -> {
            Platform.exit();
        });

        client.setOnAction(e -> {
            ConfigurationWindow configwindow = new ConfigurationWindow(ClientConfiguration.class, screenBoxList.size());
            configwindow.show();
            e.consume();
        });

        server.setOnAction(e -> {
            ConfigurationWindow configwindow = new ConfigurationWindow(ServerConfiguration.class, screenBoxList.size());
            configwindow.show();
            e.consume();
        });
        
        toggleconsole.setOnAction(e -> {
            boolean togglestate = toggleconsole.isSelected();
            console.setManaged(togglestate);
            console.setVisible(togglestate);
            e.consume();
        });

        return menubar;
    }

    private TextArea createConsole() {
        TextArea console = new TextArea();
        console.setEditable(false);
        console.setStyle("-fx-font-family: 'monospaced';");

        StaticOutputStreamAppender.setStaticOutputStream(new TextAreaOutputStream(console));

        return console;
    }

    private void updateScreenListPane() {
        screenBoxList.clear();
        screenBoxPane.getChildren().clear();

        // remove screens from menu
        if (viewmenu.getItems().size() > screensmenudefaultitems) {
            viewmenu.getItems().remove(screensmenudefaultitems + 1, viewmenu.getItems().size());
        }

        List<OlScreen> list = RestClient.getScreens();

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

            // add screen to menu
            viewmenu.getItems().add(new ScreenMenuItem(pane));
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
