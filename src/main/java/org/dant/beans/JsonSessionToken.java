package org.dant.beans;

import java.util.UUID;

public class JsonSessionToken {
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
