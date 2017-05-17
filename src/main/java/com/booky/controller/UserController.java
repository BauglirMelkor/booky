package com.booky.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.booky.dto.UserDTO;
import com.booky.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	private final Logger log = LoggerFactory.getLogger(CategoryController.class);

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping
	public UserDTO create(@RequestBody UserDTO userDTO) {
		log.debug("creating user named " + userDTO.getFirstname());
		return new UserDTO(userService.createUser(userDTO));
	}
	
	@PutMapping
	public UserDTO update(@RequestBody UserDTO userDTO) {
		log.debug("updating user named " + userDTO.getFirstname());
		return new UserDTO(userService.updateUser(userDTO));
	}
	
	@DeleteMapping("/{id}")
	public UserDTO delete(@RequestBody UserDTO userDTO) {
		log.debug("deleting user named " + userDTO.getFirstname());
		return new UserDTO(userService.deleteUser(userDTO.getId()));
	}

}