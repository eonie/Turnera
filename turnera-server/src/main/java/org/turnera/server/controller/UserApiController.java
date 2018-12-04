package org.turnera.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.turnera.server.entity.User;
import org.turnera.server.service.UserService;


@RestController
@RequestMapping("/users")
public class UserApiController {

	@Autowired
	private UserService userService;


	@PutMapping("")
	public User save(@RequestBody User user){
		return userService.save(user);
	}
	@GetMapping("{id}")
	public User getUserById(@PathVariable("id") Long id){
		return userService.findOne(id);
	}
	@GetMapping("")
	public Iterable<User> findAll(){
		return userService.findAll();
	}
}
