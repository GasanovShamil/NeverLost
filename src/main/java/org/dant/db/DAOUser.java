package org.dant.db;

import org.dant.json.JsonConnectionBean;
import org.dant.json.JsonSessionToken;

public interface DAOUser {
	public JsonSessionToken login(JsonConnectionBean bean);
	public boolean checkout(JsonSessionToken token);
	public void logout(JsonSessionToken token);
	public JsonSessionToken createUser(JsonConnectionBean bean);
	public boolean deleteUser(String email);
	public boolean addFriend(String email, String friend);
	public boolean deleteFriend(String email, String friend);
	public boolean userExists(String email);
}
