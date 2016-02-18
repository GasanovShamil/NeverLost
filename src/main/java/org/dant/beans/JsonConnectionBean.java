package org.dant.beans;

import java.io.Serializable;

public class JsonConnectionBean implements Serializable {

	private static final long serialVersionUID = 798241628003365812L;

	private String email;
	private String password;

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

}