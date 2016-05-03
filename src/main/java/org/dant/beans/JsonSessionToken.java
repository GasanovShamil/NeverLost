package org.dant.beans;

import java.io.Serializable;
import java.util.UUID;

import javax.xml.bind.annotation.XmlRootElement;


public class JsonSessionToken implements Serializable {
	
	private static final long serialVersionUID = 5196803681861554203L;
	
	public String email;
	public String token;
	

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

	public void genererToken(){
		this.token = UUID.randomUUID().toString();
	}
}
