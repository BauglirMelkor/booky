package com.booky.login;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public class UserDetail extends org.springframework.security.core.userdetails.User {

	public UserDetail(String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		// TODO Auto-generated constructor stub
	}

	private String username;
	private String password;

	private boolean accountNonExpired;
	private boolean accountNonLocked;
	private boolean credentialsNonExpired;
	private boolean enabled;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
	
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		
		return accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
	
		return accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
	
		return credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
	
		return enabled;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

}