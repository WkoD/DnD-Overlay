package com.github.wkod.dnd.overlay.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(ServerController.REQUEST_URL)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ServerController {
	
	private static final Logger logger = LoggerFactory.getLogger(ServerController.class);

	public static final String REQUEST_URL = "/overlay";
	
	private final ServerService service;
	
	@PostMapping("/background")
	public String enableBackground(@RequestBody(required = true)Img img) throws Exception {
		return service.setBackground(img.getName(), img.getData());
	}
	
	@PostMapping("/image")
	public String addImage(@RequestBody(required = true)Img img) throws Exception {
		logger.error("name: " + img.getName());
		return service.addImage(img.getName(), img.getData());
	}
}
