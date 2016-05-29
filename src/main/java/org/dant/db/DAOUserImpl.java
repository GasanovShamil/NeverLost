package org.dant.db;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.bson.Document;
import org.dant.beans.JsonConnectionBean;
import org.dant.beans.JsonSessionToken;
import org.dant.beans.User;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class DAOUserImpl implements DAOUser, Closeable {

	MongoClient mongoClient = null;
	MongoDatabase db = null;
	MongoCollection<Document> usersCollection = null;

	public DAOUserImpl() {
		mongoClient = new MongoClient("localhost", 27017);
		db = mongoClient.getDatabase("neverlost");
		usersCollection = db.getCollection("users");
	}

	@Override
	public JsonSessionToken login(JsonConnectionBean bean) {
		Document user = null;
		JsonSessionToken token = null;
		user = usersCollection.find(new Document("email", bean.getEmail()).append("password", bean.getPassword()))
				.first();

		if (user != null) {
			token = new JsonSessionToken();
			token.setEmail(user.getString("email"));
			token.generateToken();
			usersCollection.updateOne(user, new Document("$set", new Document("token", token.getToken())));
		}
		return token;
	}

	@Override
	public void logout(JsonSessionToken token) {
		usersCollection.updateOne(new Document("email", token.getEmail()).append("token", token.getToken()),
				new Document("$unset", new Document("token", "")));
	}

	@Override
	public void close() throws IOException {
		mongoClient.close();

	}

	@Override
	public JsonSessionToken createUser(JsonConnectionBean bean) {
		Document user = null;
		JsonSessionToken token = null;
		user = usersCollection.find(new Document("email", bean.getEmail())).first();
		if (user == null) {
			token = new JsonSessionToken();
			token.setEmail(bean.getEmail());
			token.generateToken();
			usersCollection.insertOne(new Document("email", bean.getEmail()).append("username", bean.getUsername())
					.append("password", bean.getPassword()).append("token", token.getToken()).append("lon", 0.0)
					.append("lat", 0.0).append("friends", new ArrayList<Document>()));
		}
		return token;
	}

	@Override
	public boolean deleteUser(JsonSessionToken token) {
		ArrayList<Document> friends = this.getUser(token.getEmail()).getFriends();
		if (friends != null) {
			for (Document doc : friends) {
				deleteFriend(token.getEmail(), doc.getString("email"));
			}
		}
		DeleteResult deleteResult = usersCollection
				.deleteOne(new Document("email", token.getEmail()).append("token", token.getToken()));
		return deleteResult.wasAcknowledged();
	}

	@Override
	public boolean addFriend(String me, String friend) {

		boolean res = false;
		User user = getUser(friend);
		if (user != null) {
			UpdateResult updateResult = usersCollection.updateOne(new Document("email", me),
					new Document("$addToSet", new Document("friends",
							new Document("email", user.getEmail()).append("username", user.getUsername()))));
			// res = updateResult.getModifiedCount()>1;
			res = updateResult.wasAcknowledged();
		}
		return res;
	}

	@Override
	public boolean deleteFriend(String me, String friend) {
		UpdateResult updateResult1 = usersCollection.updateOne(new Document("email", me),
				new Document("$pull", new Document("friends", new Document("email", friend))));
		UpdateResult updateResult2 = usersCollection.updateOne(new Document("email", friend),
				new Document("$pull", new Document("friends", new Document("email", me))));

		return updateResult1.wasAcknowledged() && updateResult2.wasAcknowledged();
	}

	@Override
	public boolean checkout(JsonSessionToken token) {
		Document user = null;
		boolean res = false;
		user = usersCollection.find(new Document("email", token.getEmail()).append("token", token.getToken())).first();

		if (user != null) {
			res = true;
		}
		return res;
	}

	@Override
	public boolean userExists(String email) {
		Document user = null;
		boolean res = false;
		user = usersCollection.find(new Document("email", email)).first();

		if (user != null) {
			res = true;
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	@Override
	public User getUser(String email) {
		Document result = null;
		User userClass = null;
		result = usersCollection.find(new Document("email", email)).first();

		if (result != null) {
			userClass = new User(result.getString("email"), result.getString("username"));
			userClass.setFriends(result.get("friends", ArrayList.class));
			userClass.setDate(result.getDate("date"));
			userClass.setLon(result.getDouble("lon"));
			userClass.setLat(result.getDouble("lat"));
//			userClass = new Gson().fromJson(result.toJson(), User.class);
		}
		return userClass;
	}

//	@Override
//	public ArrayList<Document> getFriendList(String email) {
//		Document user = null;
//		user = usersCollection.find(new Document("email", email)).first();
//		@SuppressWarnings("unchecked")
//		ArrayList<Document> friends = user.get("friends", ArrayList.class);
//		return friends;
//	}

	@Override
	public ArrayList<User> getFriends(String email) {
		Document user = null;
		user = usersCollection.find(new Document("email", email)).first();
		@SuppressWarnings("unchecked")
		ArrayList<Document> friendList = user.get("friends", ArrayList.class);
		ArrayList<User> friends = new ArrayList<User>();
		for (Document doc : friendList) {
			User tmp = getUser(doc.getString("email"));
			tmp.setConfirmed(doc.getInteger("confirmed"));
			friends.add(tmp);
		}
		return friends;
	}

	@Override
	public boolean requestFriend(String me, String friend) {

		boolean res = false;
		User friendUser = getUser(friend);
		User meUser = getUser(me);
		if (friendUser != null) {
			UpdateResult updateResult1 = usersCollection.updateOne(new Document("email", me), new Document("$addToSet",
					new Document("friends", new Document("email", friend).append("confirmed", 0))));
			UpdateResult updateResult2 = usersCollection.updateOne(new Document("email", friend), new Document(
					"$addToSet", new Document("friends", new Document("email", me).append("confirmed", -1))));
			// res = updateResult.getModifiedCount()>1;
			res = updateResult1.wasAcknowledged() && updateResult2.wasAcknowledged();
		}
		return res;
	}

	@Override
	public boolean confirmFriend(String me, String friend) {
		boolean res = false;
		User user = getUser(friend);
		UpdateResult updateResult1 = null;
		UpdateResult updateResult2 = null;
		UpdateResult updateResult3 = null;
		UpdateResult updateResult4 = null;
		if (user != null) {
			updateResult1 = usersCollection.updateOne(new Document("email", me), new Document("$pull",
					new Document("friends", new Document("email", friend).append("confirmed", -1))));
			updateResult2 = usersCollection.updateOne(new Document("email", me), new Document("$addToSet",
						new Document("friends", new Document("email", friend).append("confirmed", 1))));
			updateResult3 = usersCollection.updateOne(new Document("email", friend),
					new Document("$pull", new Document("friends", new Document("email", me).append("confirmed", 0))));
			updateResult4 = usersCollection.updateOne(new Document("email", friend), new Document("$addToSet",
						new Document("friends", new Document("email", me).append("confirmed", 1))));
			// res = updateResult.getModifiedCount()>1;
			res = updateResult1.wasAcknowledged() && updateResult2.wasAcknowledged() && updateResult3.wasAcknowledged()
					&& updateResult4.wasAcknowledged();
		}
		return res;
	}

	@Override
	public boolean refuseFriend(String me, String friend) {
		return deleteFriend(me, friend);
	}

	@Override
	public boolean setUserPos(String email, Date date, double lon, double lat) {
		UpdateResult updateResult = usersCollection.updateOne(new Document("email", email),
				new Document("$set", new Document("lon", lon).append("lat", lat).append("date", date)));

		return updateResult.wasAcknowledged();
	}

}
