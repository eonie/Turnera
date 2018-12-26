package org.turnera.server.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.web.bind.annotation.*;
import org.turnera.core.cron.QuartzCronTrigger;
import org.turnera.server.entity.User;
import org.turnera.server.netty.TurneraServer;
import org.turnera.server.schdule.MessagePrinterTask;
import org.turnera.server.service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;


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
