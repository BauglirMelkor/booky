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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.booky.entity.BookyUser;
import com.booky.repository.BookyUserRepository;
import com.booky.service.UserService;

@RunWith(SpringRunner.class)
public class UserServiceTest {

	@MockBean
	private BookyUserRepository bookyUserRepository;

	private UserService userService;

	@MockBean
	private PasswordEncoder passwordEncoder;

	private Optional<BookyUser> bookOptional;

	@Before
	public void setUp() throws Exception {

		userService = spy(new UserService(bookyUserRepository, passwordEncoder));
		BookyUser bookyUser = new BookyUser();
		bookyUser.setEmail("serhadburakan@hotmail.com");
		bookyUser.setFirstname("serhad");
		bookyUser.setLastname("burakan");
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

}
