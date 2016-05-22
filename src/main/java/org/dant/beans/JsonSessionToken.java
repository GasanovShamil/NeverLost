package org.dant.beans;

import java.io.Serializable;
import java.util.UUID;

import javax.xml.bind.annotation.XmlRootElement;


public class JsonSessionToken implements Serializable {
	
	private static final long serialVersionUID = 5196803681861554203L;
	
	public String email;
	public String token;
	public String test;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email=email;
	}
	
	
	public void setToken(String token){
		this.token=token;
	}
	
	public String getToken() {
		return token;
	}

	public void generateToken(){
		this.token = UUID.randomUUID().toString();
	}
	
	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test=test;
	}
}
