package com.booky.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.booky.dto.UserDTO;
import com.booky.entity.BookyUser;
import com.booky.repository.BookyUserRepository;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

	private final Logger log = LoggerFactory.getLogger(UserService.class);

	private final BookyUserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	public UserService(BookyUserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	

	public BookyUser createUser(UserDTO userDTO) {
		BookyUser user = new BookyUser();
		user.setEmail(userDTO.getEmail());
		user.setFirstname(userDTO.getFirstname());
		user.setLastname(userDTO.getLastname());
		String encryptedPassword = passwordEncoder.encode(userDTO.getPassword());
		user.setPassword(encryptedPassword);
		userRepository.save(user);
		log.debug("Created Information for User: {}", user);
		return user;
	}

	/**
	 * Update all information for a specific user, and return the modified user.
	 *
	 * @param userDTO
	 *            user to update
	 * @return updated user
	 */
	public BookyUser updateUser(UserDTO userDTO) {
		BookyUser user = userRepository.findOne(userDTO.getId());
		user.setEmail(userDTO.getEmail());
		user.setFirstname(userDTO.getFirstname());
		user.setLastname(userDTO.getLastname());
		log.debug("Updated User: {}", user);
		return userRepository.save(user);

	}

	public BookyUser deleteUser(Long id) {
		BookyUser user = userRepository.findOne(id);
		userRepository.delete(user);
		log.debug("Deleted User: {}", user);
		return user;

	}

	public BookyUser getByEmail(String email) {
		Optional<BookyUser> user = userRepository.findOneByEmail(email);
		return user.get();
	}

}
