package com.booky.test.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.booky.controller.UserController;
import com.booky.dto.UserDTO;
import com.booky.entity.BookyUser;
import com.booky.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@Autowired
	private ObjectMapper objectMapper;

	private BookyUser user = null;

	@Before
	public void setup() {
		user = new BookyUser();
		user.setId(1L);
		user.setEmail("serhadburakan@hotmail.com");
		user.setEnabled(true);
		user.setFirstname("serhad");
		user.setLastname("burakan");
		user.setPassword("serhad");
	}

	@Test
	@WithMockUser
	public void testCreateBookSuccessfully() throws Exception {

		when(userService.createUser(any(UserDTO.class))).thenReturn(user);

		mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(new UserDTO(user)))).andExpect(status().isOk())
				.andExpect(jsonPath("id", is(1))).andExpect(jsonPath("firstname", is("serhad")));
	}
	
	@Test
	@WithMockUser
	public void testUpdateBookSuccessfully() throws Exception {

		when(userService.updateUser(any(UserDTO.class))).thenReturn(user);

		mockMvc.perform(put("/user").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(new UserDTO(user)))).andExpect(status().isOk())
				.andExpect(jsonPath("id", is(1))).andExpect(jsonPath("firstname", is("serhad")));
	}
	
	@Test
	@WithMockUser
	public void testDeleteBookSuccessfully() throws Exception {

		when(userService.deleteUser(1L)).thenReturn(user);

		mockMvc.perform(delete("/user/1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(new UserDTO(user)))).andExpect(status().isOk())
				.andExpect(jsonPath("id", is(1))).andExpect(jsonPath("firstname", is("serhad")));
	}



}
