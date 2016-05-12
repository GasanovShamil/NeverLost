package org.dant.db;

import java.io.Closeable;
import java.io.IOException;
import org.bson.Document;
import org.dant.json.JsonConnectionBean;
import org.dant.json.JsonSessionToken;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

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
					.append("password", bean.getPassword()).append("token", token.getToken()));
		}

		return token;
	}

	@Override
	public boolean deleteUser(String email) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addFriend(String email, String friend) {
		Document user = null;
		boolean res = false;

		if (userExists(friend)) {
			user = usersCollection.find(new Document("email", email)).first();

			res = true;
		}

		return res;
	}

	@Override
	public boolean deleteFriend(String email, String friend) {
		// TODO Auto-generated method stub
		return false;
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

}
