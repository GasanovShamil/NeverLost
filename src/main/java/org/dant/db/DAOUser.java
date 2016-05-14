package org.dant.db;

import java.util.ArrayList;

import org.bson.Document;
import org.dant.json.JsonConnectionBean;
import org.dant.json.JsonSessionToken;

public interface DAOUser {
	public JsonSessionToken login(JsonConnectionBean bean);
	public boolean checkout(JsonSessionToken token);
	public void logout(JsonSessionToken token);
	public JsonSessionToken createUser(JsonConnectionBean bean);
	public boolean deleteUser(JsonSessionToken token);
	public boolean addFriend(JsonSessionToken token, String friend);
	public boolean deleteFriend(JsonSessionToken token, String friend);
	public boolean userExists(String email);
	public ArrayList<Document> getFriendList(JsonSessionToken token);
	public User getUser(String email);
}
