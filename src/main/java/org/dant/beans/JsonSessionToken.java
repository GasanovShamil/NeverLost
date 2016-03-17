package org.dant.beans;

import java.io.Serializable;
import java.util.UUID;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class JsonSessionToken implements Serializable {
	
	private static final long serialVersionUID = 5196803681861554203L;
	
	public String username;
	public String token;
	
	public JsonSessionToken(){}
	
	public JsonSessionToken(String username) {
		this.username = username;
		this.token = UUID.randomUUID().toString();
	}

	public String getUsername() {
		return username;
	}

	public String getToken() {
		return token;
	}

}
