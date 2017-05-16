package com.booky.test.service;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.booky.dto.UserDTO;
import com.booky.entity.BookyUser;
import com.booky.exception.UserNotFoundException;
import com.booky.repository.BookyUserRepository;
import com.booky.service.UserService;

@RunWith(SpringRunner.class)
public class UserServiceTest {

	@MockBean
	private BookyUserRepository bookyUserRepository;

	private UserService userService;

	
	private PasswordEncoder passwordEncoder;

	private Optional<BookyUser> bookOptional;
	
	private BookyUser bookyUser=null;

	@Before
	public void setUp() throws Exception {
		passwordEncoder=new BCryptPasswordEncoder();
		userService = spy(new UserService(bookyUserRepository, passwordEncoder));
		bookyUser = new BookyUser();
		bookyUser.setId(1L);
		bookyUser.setEmail("serhadburakan@hotmail.com");
		bookyUser.setFirstname("serhad");
		bookyUser.setLastname("burakan");
		bookyUser.setPassword("serhad");
		bookyUser.setLastPasswordResetDate(new Date());
		bookOptional=Optional.of(bookyUser);
	}

	@Test
	public void getByEmailSuccessful() {
		when(bookyUserRepository.findOneByEmail(any())).thenReturn(bookOptional);
		BookyUser bookyUser = userService.getByEmail("serhadburakan@hotmail.com");
		assert (bookyUser.getFirstname().equals("serhad"));
		assert (bookyUser.getLastname().equals("burakan"));
	}
	
	
	@Test(expected=UsernameNotFoundException.class)
	public void getByEmailThrowsException() {
		when(bookyUserRepository.findOneByEmail(any())).thenReturn(Optional.ofNullable(null));
		userService.getByEmail("serhadburakan@hotmail.com");
		
	}
	
	@Test
	public void createUserSuccessful() {
		when(bookyUserRepository.save(bookyUser)).thenReturn(bookyUser);
		BookyUser user = userService.createUser(new UserDTO(bookyUser));
		String encodedPass=passwordEncoder.encode(bookyUser.getPassword());
		assert (user.getPassword().substring(0, 5).equals(encodedPass.substring(0,5)));
		assert (bookyUser.getLastname().equals("burakan"));
	}
	
	@Test
	public void updateUserSuccessful() {
		when(bookyUserRepository.findOne(1L)).thenReturn(bookyUser);
		when(bookyUserRepository.save(bookyUser)).thenReturn(bookyUser);
		BookyUser user = userService.updateUser(new UserDTO(bookyUser));
		String encodedPass=passwordEncoder.encode(bookyUser.getPassword());
		assert (user.getPassword().substring(0, 5).equals(encodedPass.substring(0,5)));
		assert (bookyUser.getLastname().equals("burakan"));
	}
	
	@Test(expected=UserNotFoundException.class)
	public void updateUserUserNotFound() {
		when(bookyUserRepository.save(bookyUser)).thenReturn(bookyUser);
		BookyUser user = userService.updateUser(new UserDTO(bookyUser));
		String encodedPass=passwordEncoder.encode(bookyUser.getPassword());
		assert (user.getPassword().substring(0, 5).equals(encodedPass.substring(0,5)));
		assert (bookyUser.getLastname().equals("burakan"));
	}
	
	
	@Test
	public void deleteUserUserNotFound() {
		when(bookyUserRepository.findOne(1L)).thenReturn(bookyUser);
		BookyUser user = userService.deleteUser(1L);
		assert (user.getLastname().equals("burakan"));
	}
	
	@Test(expected=UserNotFoundException.class)
	public void deleteUserSuccessful() {
		when(bookyUserRepository.findOne(1L)).thenReturn(null);
		userService.deleteUser(1L);		
	}
	

}
