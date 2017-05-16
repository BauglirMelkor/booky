package com.booky.dto;

import java.io.Serializable;
import java.util.Date;

import com.booky.entity.BookyUser;

public class UserDTO implements Serializable {

	private Long id;

	private String password;

	private String firstname;

	private String lastname;

	private String email;

	private Boolean enabled;

	private Date lastPasswordResetDate;

	public UserDTO() {

	}

	public UserDTO(BookyUser user) {
		this.id = user.getId();
		this.email = user.getEmail();
		this.password = user.getPassword();
		this.firstname = user.getFirstname();
		this.lastname = user.getLastname();
		this.lastPasswordResetDate = user.getLastPasswordResetDate();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Date getLastPasswordResetDate() {
		return lastPasswordResetDate;
	}

	public void setLastPasswordResetDate(Date lastPasswordResetDate) {
		this.lastPasswordResetDate = lastPasswordResetDate;
	}

}
