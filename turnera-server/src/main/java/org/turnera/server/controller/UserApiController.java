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
import org.turnera.server.schedule.MessagePrinterTask;
import org.turnera.server.service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;


@RestController
@RequestMapping("/users")
public class UserApiController {

	@Autowired
	private UserService userService;
	@Autowired
	TaskScheduler taskScheduler ;
	private static Map<String,ScheduledFuture<?>> futures = new HashMap<>();
	@PutMapping("")
	public User save(@RequestBody User user){
		return userService.save(user);
	}
	@GetMapping("{id}")
	public User getUserById(@PathVariable("id") Long id){
		return userService.findOne(id);
	}
	@GetMapping("")
	public Page<User> findAll(@PageableDefault Pageable pageable){
		return userService.findAll(pageable);
	}
	@GetMapping("views")
	@JsonView(User.ListView.class)
	public Page<User> findAllWithJsonView(@PageableDefault Pageable pageable){
		return userService.findAll(pageable);
	}
	@GetMapping("schedule")
	public void schedule(@RequestParam("id") Long id){
		ScheduledFuture future = taskScheduler.schedule(new MessagePrinterTask(id.toString()), new QuartzCronTrigger("0/5 */1 * * * ?"));
		futures.put(id.toString(), future);
	}
	@GetMapping("schedules")
	public Map<String,ScheduledFuture<?>> schdules(){
		return futures;
	}
	@GetMapping("schedules/{id}/stop")
	public Map<String,ScheduledFuture<?>> schdules(@PathVariable("id") String key){
		ScheduledFuture future = futures.get(key);
		future.cancel(false);
		return futures;
	}
}
