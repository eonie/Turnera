package org.turnera.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.turnera.server.netty.TurneraServer;


@RestController
@RequestMapping("/server")
public class TurneraServerApiController {

	@Autowired
	private TurneraServer turneraServer;

	@GetMapping("start")
	public void getUserById() throws Exception {
		turneraServer.start();
	}
}
