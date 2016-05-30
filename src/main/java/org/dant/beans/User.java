package org.dant.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import org.bson.Document;

public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5420434289477335204L;
	private String email, username;
	private String token;
	private ArrayList<Document> friends;
	private double lon,lat;
	private Date date;
	private Integer confirmed;

	
	public User(){}
	
	public User(String email, String username) {
		this.email = email;
		this.username = username;
		this.date = new Date();
		this.friends = new ArrayList<Document>();
		this.lon=0.0;
		this.lat=0.0;
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


	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public Integer getConfirmed() {
		return confirmed;
	}

	public void setConfirmed(Integer confirmed) {
		this.confirmed = confirmed;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	

}
