package org.dant.beans;

import java.io.Serializable;
import java.util.UUID;

public class JsonSessionToken implements Serializable {
	
	private static final long serialVersionUID = 5196803681861554203L;
	
	private String username;
	private String token;

	public JsonSessionToken(String username) {
		this.username = username;
		token = UUID.randomUUID().toString();
	}

	public String getUsername() {
		return username;
	}

	public String getToken() {
		return token;
	}

}
