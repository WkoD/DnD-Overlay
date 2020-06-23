package dnd.overlay.client;

import dnd.overlay.client.appconfig.Configuration;

public class ClientApplication {

	public static void main(String[] args) {
	   Configuration.readArgs(args);
		Client.runthis(args);
	}

}
