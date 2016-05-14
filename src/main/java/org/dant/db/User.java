package org.dant.db;

import java.util.ArrayList;

import org.bson.Document;

public class User {
	private String email, username, lastPos;
	private ArrayList<Document> friends;
	
	public User(String email, String username, ArrayList<Document> friends){
		this.email=email;
		this.username=username;
		this.friends=friends;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public ArrayList<Document> getFriends() {
		return friends;
	}

	public void setFriends(ArrayList<Document> friends) {
		this.friends = friends;
	}

	public String getLastPos() {
		return lastPos;
	}

	public void setLastPos(String lastPos) {
		this.lastPos = lastPos;
	}
	
}
