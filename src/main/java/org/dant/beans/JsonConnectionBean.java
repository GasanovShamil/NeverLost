package org.dant.beans;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class JsonConnectionBean implements Serializable {

	private static final long serialVersionUID = 798241628003365812L;
	private final String salt = "gdhfg798241628003365812Ldtkqsdaz";

	public String email;
	public String password;
	public String username;

	public JsonConnectionBean() {
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		MessageDigest md = null;
		String fortpass = password + salt;
		StringBuffer sb;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		md.update(fortpass.getBytes());

		byte[] digest = md.digest();
		sb = new StringBuffer();
		for (byte b : digest) {
			sb.append(String.format("%02x", b & 0xff));
		}
		System.out.println("Digest(in hex format):: " + sb.toString());
		return sb.toString();
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

	public String toString() {
		return "" + email + " : " + password;
	}

}