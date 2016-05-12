package org.dant.services;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.dant.db.DAOUserImpl;
import org.dant.json.JsonConnectionBean;
import org.dant.json.JsonSessionToken;

@Path("/services")
public class UserServices {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/createuser")
	public Response createuser(JsonConnectionBean bean) {
		
		JsonSessionToken token;
		try (DAOUserImpl userDAO = new DAOUserImpl()) {
			token = userDAO.createUser(bean);
		} catch (IOException e) {
			token = null;
		}

		if (token != null) {
			System.out.println("User created : " + bean.getEmail() + ", password : " + bean.getPassword());
			return Response.ok(token).build();
		} else {
			return Response.status(Response.Status.CONFLICT).entity("User already exist.").build();
		}
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/addfriend/{emailfriend}")
	public Response addFriend(JsonSessionToken token, @PathParam("emailfriend") String emailfriend) {
		
//		try (DAOUserImpl userDAO = new DAOUserImpl()) {
//			token = userDAO.login(bean);
//		} catch (IOException e) {
//			token = null;
//		}
//
//		if (token != null) {
//			System.out.println("login : " + bean.getEmail() + ", password : " + bean.getPassword());
//			return Response.ok(token).build();
//		} else {
//			return Response.status(Response.Status.UNAUTHORIZED).build();
//		}
		System.out.println(token.getEmail()+" : "+token.getToken()+";");
		System.out.println("Friend : "+emailfriend);
		return Response.status(Response.Status.OK).build();
	}
}
