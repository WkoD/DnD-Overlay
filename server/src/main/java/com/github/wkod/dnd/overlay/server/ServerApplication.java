package com.github.wkod.dnd.overlay.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javafx.application.Application;

@SpringBootApplication
public class ServerApplication {

	public static Thread fxThread;
	
	public static void main(String[] args) {
//		SpringApplication.run(ServerApplication.class, args);
//		Overlay.runthis(args);
		Application.launch(Overlay.class, args);
	}

}
