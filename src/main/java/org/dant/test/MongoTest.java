package org.dant.test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoTest {

	public static void main(String args[]) {

		try {

			// To connect to mongodb server
			MongoClient mongoClient = new MongoClient("localhost", 27017);

			// Now connect to your databases
			MongoDatabase db = mongoClient.getDatabase("neverlost");
			MongoCollection<Document> collection = db.getCollection("users");
			collection.drop();
			collection.createIndex(new Document("name",1));
			
			ArrayList<Document> shamilFriends=new ArrayList<Document>();
			shamilFriends.add(new Document("email","leo@mail.com").append("confirmed", 1));
			collection.insertOne(new Document("email", "shamil@mail.com").append("username", "shamilevich")
							.append("password", getPassword("shamil")).append("lon", 0.0).append("lat", 0.0)
							.append("friends", shamilFriends).append("date", new Date()).append("active",true));
			
			ArrayList<Document> leoFriends=new ArrayList<Document>();
			leoFriends.add(new Document("email","shamil@mail.com").append("confirmed", 1));
			collection.insertOne(new Document("email", "leo@mail.com").append("username", "machallah")
							.append("password", getPassword("leo")).append("lon", 0.0).append("lat", 0.0)
							.append("friends", leoFriends).append("date", new Date()).append("active",true));
			collection.insertOne(new Document("email", "milan@mail.com").append("username", "bubachvaba")
					.append("password", getPassword("milan")).append("lon", 0.0).append("lat", 0.0)
					.append("friends", new ArrayList<Document>()).append("date", new Date()).append("active",true));
			collection.insertOne(new Document("email", "ibra@mail.com").append("username", "brambaba")
							.append("password", getPassword("ibra")).append("lon", 0.0).append("lat", 0.0)
							.append("friends", new ArrayList<Document>()).append("date", new Date()).append("active",true));
			collection.insertOne(new Document("email", "khaled@mail.com").append("username", "bouboubond")
					.append("password", getPassword("khaled")).append("lon", 0.0).append("lat", 0.0)
					.append("friends", new ArrayList<Document>()).append("date", new Date()).append("active",true));
			mongoClient.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}
	
	public static String getPassword(String password) {
		String salt = "gdhfg798241628003365812Ldtkqsdaz";
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

	
}
