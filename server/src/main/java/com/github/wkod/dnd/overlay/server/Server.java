package com.github.wkod.dnd.overlay.server;

import java.io.File;
import java.io.IOException;

import javax.annotation.PreDestroy;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.github.wkod.dnd.overlay.server.config.Configuration;
import com.github.wkod.dnd.overlay.server.fx.ServerFx;

import javafx.application.Application;
import javafx.application.Platform;

@SpringBootApplication
public class Server {

	public static void main(String[] args) throws IOException {
	    Configuration.load(new File("configuration.properties"));
		Application.launch(ServerFx.class, args);
	}

	@PreDestroy
	public void onExit() {
	    // close javafx
	    Platform.exit();
	}
}
