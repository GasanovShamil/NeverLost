package org.dant.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.bson.Document;
import org.dant.beans.JsonConnectionBean;
import org.dant.beans.JsonSessionToken;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

@Path("/authentication")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthenticationEndpoint {

	
		
	@POST
	@Path("/login")
	public Response authenticateUser(JsonConnectionBean bean) {
		JsonSessionToken token = authenticate(bean);
		if (token != null) {
			return Response.ok(token).build();
		} else {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}

	@POST
	@Path("/logout")
	public Response logoutUser(JsonSessionToken token) {
			logout(token);
		
			return Response.status(Response.Status.OK).build();
		
	}
	

	private JsonSessionToken authenticate(JsonConnectionBean bean) {
		Document user = null;
		JsonSessionToken token = null;
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		MongoDatabase db = mongoClient.getDatabase("neverlost");

		user = db.getCollection("users").find(new Document("email", bean.getEmail()).append("password", bean.getPassword())).first();
		
		
		if (user != null) {
			token = new JsonSessionToken();
			token.setEmail(user.getString("username"));
			token.genererToken();
			db.getCollection("users").updateOne(user, new Document("$set",new Document("token",token.getToken())));
		}
		mongoClient.close();
		return token;

	}
	
	private void logout(JsonSessionToken token){
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		MongoDatabase db = mongoClient.getDatabase("neverlost");
		db.getCollection("users").updateOne(new Document("email", token.getEmail()).append("token", token.getToken()), new Document("$unset",new Document("token","")));	
		mongoClient.close();
	}

}