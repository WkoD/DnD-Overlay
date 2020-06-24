package com.github.wkod.dnd.overlay.server.fx;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import com.github.wkod.dnd.overlay.api.OlScreen;
import com.github.wkod.dnd.overlay.server.Server;
import com.sun.glass.ui.Screen;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ServerFx extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerFx.class);

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

        this.applicationContext = new SpringApplicationBuilder().sources(Server.class).run(args);
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

        for (Screen screen : Screen.getScreens()) {
            OlStage stage = new OlStage(screen.getX(), screen.getY(), screen.getWidth(), screen.getHeight());
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
     * @param screenid Integer
     * @param name String
     * @param image byte[]
     */
    public static void setBackground(Integer screenid, String name, byte[] image) {
        OlStage stage;

        // invalid screen
        if (screenid == null || (stage = screenMap.get(screenid)) == null) {
            return;
        }

        Runnable runnable = () -> {
            Platform.runLater(() -> {
                if (image != null) {
                    try (InputStream is = new ByteArrayInputStream(image)) {
                        stage.setBackground(name, new Image(is));
                    } catch (IOException e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                }
            });
        };

        runnable.run();
    }

    /**
     * Add an image to a given screen.
     * 
     * @param screenid Integer
     * @param name String
     * @param image byte[]
     */
    public static void setImage(Integer screenid, String name, byte[] image) {
        OlStage stage;

        // invalid screen
        if (screenid == null || (stage = screenMap.get(screenid)) == null) {
            return;
        }

        Runnable runnable = () -> {
            Platform.runLater(() -> {
                stage.setImage(name, new Image(new ByteArrayInputStream(image)));
            });
        };

        runnable.run();
    }
    
    /**
     * Toggles all images or the background image between shown and hidden.
     * 
     * @param screenid Integer
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
                    if (stage.isShowing()) {
                        stage.hide();
                    } else {
                        stage.show();
                    }
                }
            });
        };

        runnable.run();
    }

    /**
     * Remove all images or the background image for a given screen.
     * 
     * @param screenid Integer
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