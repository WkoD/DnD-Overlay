package com.github.wkod.dnd.overlay.server;

import java.io.ByteArrayInputStream;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import com.sun.glass.ui.Screen;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Overlay.
 * 
 * @author cpawek
 * @version 1.0
 */
public class Overlay extends Application {
	
	private ConfigurableApplicationContext applicationContext;

	private static OverlayPane mImagePane;

	private static BackgroundStage mBackgroundStage;

	@Override
	public void init() {
        String[] args = getParameters().getRaw().toArray(new String[0]);

        this.applicationContext = new SpringApplicationBuilder()
                .sources(ServerApplication.class)
                .run(args);
	}
	
	@Override
	public void stop() {
	    this.applicationContext.close();
	    Platform.exit();
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
		Screen screen = Screen.getScreens().get(1);

		// Image Overlay
		pPrimaryStage.setAlwaysOnTop(true);
		pPrimaryStage.initStyle(StageStyle.TRANSPARENT);
		pPrimaryStage.setX(screen.getX());
		pPrimaryStage.setY(screen.getY());
		pPrimaryStage.setWidth(screen.getWidth());
		pPrimaryStage.setHeight(screen.getHeight());

		mImagePane = new OverlayPane(screen);
		mImagePane.setBackground(null);
		Scene scene = new Scene(mImagePane);
		scene.setFill(Color.TRANSPARENT);

		pPrimaryStage.setScene(scene);
		pPrimaryStage.show();

		// Background Overlay
		mBackgroundStage = new BackgroundStage(screen);
		mBackgroundStage.hide();
	}

	public static void setBackground(String name, byte[] image) {
		Runnable runnable = () -> {
			Platform.runLater(() -> {
				if (image != null) {
//					mBackgroundStage.reset();

					if (!mBackgroundStage.isShowing()) {
						mBackgroundStage.show();
						mImagePane.toFront();
					}
					
					mBackgroundStage.setImage(new Image(new ByteArrayInputStream(image)));
					
				} else {
					mBackgroundStage.hide();
				}
			});
		};

		runnable.run();
	}

	public static void addImage(String name, byte[] image) {

		Runnable runnable = () -> {
			Platform.runLater(() -> {
				mImagePane.addImage(new OverlayImage(mImagePane, name, new Image(new ByteArrayInputStream(image))));
			});
		};
		
		runnable.run();
	}

}
