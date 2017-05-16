package com.booky.service;

import java.util.Date;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.booky.dto.UserDTO;
import com.booky.entity.BookyUser;
import com.booky.exception.UserNotFoundException;
import com.booky.repository.BookyUserRepository;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

	private final Logger log = LoggerFactory.getLogger(UserService.class);

	private final BookyUserRepository bookyUserRepository;

	private final PasswordEncoder passwordEncoder;

	public UserService(BookyUserRepository bookyUserRepository, PasswordEncoder passwordEncoder) {
		this.bookyUserRepository = bookyUserRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	

	public BookyUser createUser(UserDTO userDTO) {
		BookyUser user = new BookyUser();
		user.setEmail(userDTO.getEmail());
		user.setFirstname(userDTO.getFirstname());
		user.setLastname(userDTO.getLastname());
		String encryptedPassword = passwordEncoder.encode(userDTO.getPassword());
		user.setPassword(encryptedPassword);
		user.setEnabled(true);
		user.setLastPasswordResetDate(new Date());
		log.debug("Created Information for User: {}", user);
		try{
			bookyUserRepository.save(user);
		}catch(Exception e){
			log.error(e.getMessage());
		}
		return user;
		
	}

	
	public BookyUser updateUser(UserDTO userDTO) {
		BookyUser user = bookyUserRepository.findOne(userDTO.getId());
		if(user==null){
			throw new UserNotFoundException("user not found for id "+userDTO.getId());			
		}
		user.setEmail(userDTO.getEmail());
		user.setFirstname(userDTO.getFirstname());
		user.setLastname(userDTO.getLastname());
		user.setEnabled(true);
		user.setLastPasswordResetDate(new Date());
		String encryptedPassword = passwordEncoder.encode(userDTO.getPassword());
		user.setPassword(encryptedPassword);
		log.debug("Updated User: {}", user);
		try{
			bookyUserRepository.save(user);
		}catch(Exception e){
			log.error(e.getMessage());
		}
		return user;

	}

	public BookyUser deleteUser(Long id) {
		BookyUser user = bookyUserRepository.findOne(id);
		if(user==null){
			throw new UserNotFoundException("user not found for id "+id);			
		}
		bookyUserRepository.delete(user);
		log.debug("Deleted User: {}", user);
		return user;

	}

	public BookyUser getByEmail(String email) {
		Optional<BookyUser> user = bookyUserRepository.findOneByEmail(email);
		if(!user.isPresent()){
			throw new UsernameNotFoundException("User with email "+ email+" cannot be found!");
		}
		return user.get();
	}

}
