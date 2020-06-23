package com.github.wkod.dnd.overlay.client;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.client.ClientConfig;

import com.github.wkod.dnd.overlay.client.appconfig.Configuration;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * ControlWindow.
 * 
 * @author cpawek
 * @version 1.0
 */
public class ClientWindow extends Stage {

	private final GridPane mBgControl;

	private final Label mDropped;

	public ClientWindow() {

		// background control
		CheckBox bgSetter = new CheckBox();
		bgSetter.setText("Set as background");
		bgSetter.setIndeterminate(false);
		bgSetter.setSelected(false);

		Button bgReset = new Button();
		bgReset.setText("Reset background");

		bgReset.setOnAction(e -> {
			sendImage(null, null, true);
		});

		mBgControl = new GridPane();
		mBgControl.add(bgSetter, 0, 0);
		mBgControl.add(bgReset, 1, 0);

		// drag and drop box
		mDropped = new Label("");
		VBox dragTarget = new VBox();
		dragTarget.getChildren().addAll(mDropped);

		GridPane root = new GridPane();
		root.add(createMenu(), 0, 0);
		root.add(mBgControl, 0, 1);
		root.add(dragTarget, 0, 2);

		Scene scene = new Scene(root, 500, 250);

		setTitle("Control Window");
		setScene(scene);

		// set events
		// Drag & Drop
		scene.setOnDragOver(e -> {
			if (e.getGestureSource() != dragTarget && e.getDragboard().hasFiles()) {
				/* allow for both copying and moving, whatever user chooses */
				e.acceptTransferModes(TransferMode.COPY_OR_MOVE);
			}
			e.consume();
		});

		scene.setOnDragDropped(e -> {
			Dragboard db = e.getDragboard();
			boolean success = false;
			if (db.hasFiles()) {
				mDropped.setText(db.getFiles().toString());
				success = true;
			}
			/*
			 * let the source know whether the string was successfully transferred and used
			 */
			e.setDropCompleted(success);
			for (final File file : db.getFiles()) {
				if (bgSetter.isSelected()) {
					try (FileInputStream fis = new FileInputStream(file)) {
						sendImage("Background", new Image(fis), true);
					} catch (IOException e1) {
					}
					bgSetter.setSelected(false);
				} else {
					try (FileInputStream fis = new FileInputStream(file)) {
						sendImage(file.getName(), new Image(fis), false);
					} catch (IOException e1) {
					}
				}
			}

			e.consume();
		});

		scene.setOnKeyPressed(e -> {
			if (e.isControlDown() && KeyCode.V.equals(e.getCode())) {
				Clipboard cb = Clipboard.getSystemClipboard();

				if (cb.hasContent(DataFormat.IMAGE)) {
					if (bgSetter.isSelected()) {
						sendImage("Background", cb.getImage(), true);
						bgSetter.setSelected(false);
					} else {
						sendImage("Image", cb.getImage(), false);
					}
				}
			}
		});

		setOnCloseRequest(e -> {
			Platform.exit();
		});
	}

	private MenuBar createMenu() {
		// create elements
		Menu menu = new Menu("Background");
		MenuItem bgToggle = new MenuItem("Toggle");

		menu.getItems().add(bgToggle);

		MenuBar menubar = new MenuBar();
		menubar.getMenus().add(menu);

		// set events
		EventHandler<ActionEvent> bgToggleEvent = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent pEvent) {

				sendImage(null, null, true);
			}
		};

		bgToggle.setOnAction(bgToggleEvent);

		return menubar;
	}

	private void sendImage(String name, Image image, boolean background) {

		try {
			Img img = new Img();

			if (name != null) {
				img.setName(name);
			}

			if (image != null) {
				BufferedImage bimage = SwingFXUtils.fromFXImage(image, null);
				
				try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
					ImageIO.write(bimage, "png", stream);
					byte[] result = stream.toByteArray();
					img.setData(Base64.getEncoder().encodeToString(result));
				}
			}

			ClientConfig config = new ClientConfig();
			Client client = ClientBuilder.newClient(config);

			String targeturl = Configuration.getUrl();

			if (background) {
				targeturl += "/background";
			} else {
				targeturl += "/image";
			}

			WebTarget target = client.target(targeturl);
			String response = target.request(MediaType.APPLICATION_JSON)
					.post(Entity.json(img), Img.class).toString();

			mDropped.setText(response);

		} catch (Exception e) {
			mDropped.setText(e.getMessage());
		}

	}
}
