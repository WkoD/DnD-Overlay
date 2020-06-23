package dnd.overlay.server;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

@Service
public class ServerService {
	
	public String setBackground(String name, String data) {
		Overlay.setBackground(name, data != null ? Base64.decodeBase64(data) : null);
		return "OK";
	}

	public String addImage(String name, String data) {
		Overlay.addImage(name, data != null ? Base64.decodeBase64(data) : null);
		return "OK";
	}

}
