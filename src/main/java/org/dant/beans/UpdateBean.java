package org.dant.beans;

import java.io.Serializable;

public class UpdateBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1335223341230167258L;
	public String email;
	public String username;
	public String password;
	
	
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	

	
}
