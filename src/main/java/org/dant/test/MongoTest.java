package org.dant.test;

import java.util.ArrayList;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class MongoTest {

	public static void main(String args[]) {

		try {

			// To connect to mongodb server
			MongoClient mongoClient = new MongoClient("localhost", 27017);

			// Now connect to your databases
			MongoDatabase db = mongoClient.getDatabase("neverlost");

			db.getCollection("users").drop();
			db.getCollection("users")
					.insertOne(new Document("email", "shamil@mail.com").append("username", "shamilevich")
							.append("password", "shamil").append("lon", 0.0).append("lat", 0.0)
							.append("friends", new ArrayList<Document>()));
			db.getCollection("users")
					.insertOne(new Document("email", "leo@mail.com").append("username", "machallah")
							.append("password", "leo").append("lon", 0.0).append("lat", 0.0)
							.append("friends", new ArrayList<Document>()));
			db.getCollection("users")
					.insertOne(new Document("email", "milan@mail.com").append("username", "bubachvaba")
					.append("password", "milan").append("lon", 0.0).append("lat", 0.0)
					.append("friends", new ArrayList<Document>()));
			db.getCollection("users")
					.insertOne(new Document("email", "ibra@mail.com").append("username", "brambaba")
							.append("password", "ibra").append("lon", 0.0).append("lat", 0.0)
							.append("friends", new ArrayList<Document>()));
			db.getCollection("users")
			.insertOne(new Document("email", "khaled@mail.com").append("username", "bouboubond")
					.append("password", "khaled").append("lon", 0.0).append("lat", 0.0)
					.append("friends", new ArrayList<Document>()));
			mongoClient.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}
}
