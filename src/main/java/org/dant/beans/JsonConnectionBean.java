package org.dant.beans;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;


public class JsonConnectionBean implements Serializable {

	private static final long serialVersionUID = 798241628003365812L;

	public String email;
	public String password;
	public String username;
	
	public JsonConnectionBean(){}
	
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String toString(){
		return ""+email+" : "+password;
	}
	
}