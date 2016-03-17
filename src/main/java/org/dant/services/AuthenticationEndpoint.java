package org.dant.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.bson.Document;
import org.dant.beans.JsonConnectionBean;
import org.dant.beans.JsonSessionToken;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

@Path("/authentication")
public class AuthenticationEndpoint {

//    @POST
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response authenticateUser(JsonConnectionBean connect) {
//
//       
//            JsonSessionToken token=authenticate(connect.getEmail(), connect.getPassword());
//
//            // Issue a token for the user
//            
//
//            // Return the token on the response
//            if(token!= null){
//            	return Response.ok(new Gson().toJson(token)).build();
//            }else{
//            	return Response.status(Response.Status.UNAUTHORIZED).build();
//            }
//            
//            
//           
//            
//    }

    
//    @POST
//    @Produces(MediaType.TEXT_HTML)
//    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//    public String authenticateUser1(@FormParam("email") String email, 
//            							@FormParam("password") String password) {
//
//       
//            JsonSessionToken token=authenticate(email, password);
//
//            // Issue a token for the user
//            // String token = issueToken(username);
//            // Return the token on the response
//            //return Response.ok(token).build();
//            //return Response.status(Response.Status.UNAUTHORIZED).build();
//            String res=(token==null)?"UNAUTHORIZED":""+token.getUsername()+" : "+token.getToken();
//            return "<html> " + "<title>" + "Test Jersey" + "</title>"
//                    + "<body><h1>" + res + "</h1></body>" + "</html> ";
//            
//    }

    
    
    private JsonSessionToken authenticate(String email, String password){
    	Document user=null;
    	JsonSessionToken token=null;
    	MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
			
         // Now connect to your databases
         MongoDatabase db = mongoClient.getDatabase("neverlost");
         
         user=db.getCollection("users").find(new Document("email",email).append("password", password)).first();
		
         if(user != null){
        	 token=new JsonSessionToken(user.getString("username"));
         }
         mongoClient.close();
         return token;
         
    }


}