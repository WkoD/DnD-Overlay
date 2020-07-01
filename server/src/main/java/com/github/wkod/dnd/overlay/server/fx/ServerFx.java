package com.github.wkod.dnd.overlay.server.fx;

import static com.github.wkod.dnd.overlay.api.localization.Messages.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.cal10n.LocLogger;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import com.github.wkod.dnd.overlay.api.OlScreen;
import com.github.wkod.dnd.overlay.server.Server;
import com.github.wkod.dnd.overlay.server.config.Configuration;
import com.github.wkod.dnd.overlay.util.Utils;
import com.sun.glass.ui.Screen;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ServerFx extends Application {

    private static final LocLogger LOGGER = Utils.getLogger(ServerFx.class);

    /**
     * Spring context.
     */
    private ConfigurableApplicationContext applicationContext;

    /**
     * Screen map.
     */
    private static Map<Integer, OlStage> screenMap = new LinkedHashMap<>();

    /**
     * Init for Spring context.
     */
    @Override
    public void init() {
        String[] args = getParameters().getRaw().toArray(new String[0]);

        this.applicationContext = new SpringApplicationBuilder().sources(Server.class)
                .properties(Configuration.getCopy()).run(args);
    }

    /**
     * stop.
     * 
     * @see javafx.application.Application#stop()
     */
    @Override
    public void stop() {
        this.applicationContext.close();
        Platform.exit();
    }

    /**
     * start.
     * 
     * @see javafx.application.Application#start(javafx.stage.Stage)
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        screenMap.clear();
        int index = 0;

        // set primary stage without taskbar entry and no actual visible window
        primaryStage.initStyle(StageStyle.UTILITY);
        primaryStage.setOpacity(0);
        primaryStage.setHeight(0);
        primaryStage.setWidth(0);
        primaryStage.show();
        
        for (Screen screen : Screen.getScreens()) {
            OlStage stage = new OlStage(screen.getX(), screen.getY(), screen.getWidth(), screen.getHeight());
            
            // set stage as sub stage of primary to get rid of taskbar entries
            stage.initOwner(primaryStage);
            
            stage.show();
            screenMap.put(index++, stage);
        }
    }

    /**
     * Returns a list of all currently available server screens.
     * 
     * @return List<OlScreen>
     */
    public static List<OlScreen> getScreens() {
        List<OlScreen> list = new ArrayList<>();
        int index = 0;

        for (Screen screen : Screen.getScreens()) {
            OlScreen ols = new OlScreen();
            ols.setId(index++);
            ols.setX(screen.getX());
            ols.setY(screen.getY());
            ols.setWidth(screen.getWidth());
            ols.setHeight(screen.getHeight());
            ols.setMain(Screen.getMainScreen().equals(screen));

            list.add(ols);
        }

        return list;
    }

    /**
     * Set background image for a given screen.
     * 
     * @param screenid   Integer
     * @param name       String
     * @param image      byte[]
     * @param background boolean
     */
    public static void setImage(Integer screenid, String name, byte[] image, boolean background) {
        OlStage stage;

        // invalid screen
        if (screenid == null || (stage = screenMap.get(screenid)) == null) {
            return;
        }

        Runnable runnable = () -> {
            Platform.runLater(() -> {
                if (image != null) {
                    try (InputStream is = new ByteArrayInputStream(image)) {
                        if (background) {
                            stage.setBackground(name, new Image(is));
                        } else {
                            stage.setImage(name, new Image(is));
                        }
                    } catch (IOException e) {
                        LOGGER.error(SERVER_DATA_TRANSFER_ERROR, e);
                    }
                }
            });
        };

        runnable.run();
    }

    /**
     * Toggles all images or the background image between shown and hidden.
     * 
     * @param screenid   Integer
     * @param background boolean
     */
    public static void toggleScreen(Integer screenid, boolean background) {
        OlStage stage;

        // invalid screen
        if (screenid == null || (stage = screenMap.get(screenid)) == null) {
            return;
        }

        Runnable runnable = () -> {
            Platform.runLater(() -> {
                if (background) {
                    stage.toggleBackground();
                } else {
                    stage.toogleImage();
                }
            });
        };

        runnable.run();
    }

    /**
     * Remove all images or the background image for a given screen.
     * 
     * @param screenid   Integer
     * @param background boolean
     */
    public static void clearScreen(Integer screenid, boolean background) {
        OlStage stage;

        // invalid screen
        if (screenid == null || (stage = screenMap.get(screenid)) == null) {
            return;
        }

        Runnable runnable = () -> {
            Platform.runLater(() -> {
                if (background) {
                    stage.clearBackground();
                } else {
                    stage.clearImage();
                }
            });
        };

        runnable.run();
    }
}
