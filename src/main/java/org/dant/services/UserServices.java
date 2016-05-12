package org.dant.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.dant.db.DAOUserImpl;
import org.dant.json.JsonConnectionBean;
import org.dant.json.JsonSessionToken;

import com.google.gson.Gson;

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
	@Path("/createuser")
	public Response deleteuser(JsonSessionToken token) {
		boolean res;
		try (DAOUserImpl userDAO = new DAOUserImpl()) {
			res = userDAO.deleteUser(token);
		} catch (IOException e) {
			res = false;
		}
		if (res) {
			System.out.println("User " + token.getEmail() + ", was deleted");
			return Response.ok(token).entity("User " + token.getEmail() + ", was deleted").build();
		} else {
			return Response.status(Response.Status.CONFLICT).entity("User already exist.").build();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/addfriend/{emailfriend}")
	public Response addFriend(JsonSessionToken token, @PathParam("emailfriend") String emailfriend) {
		boolean res;
		try (DAOUserImpl userDAO = new DAOUserImpl()) {
			res = userDAO.addFriend(token, emailfriend);
		} catch (IOException e) {
			res = false;
		}

		if (res) {
			System.out.println("User " + token.getEmail() + " added " + emailfriend + " to friends");
			return Response.ok().entity("Friend added").build();
		} else {
			return Response.status(Response.Status.CONFLICT).entity("Add friend failed").build();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/deletefriend/{emailfriend}")
	public Response deleteFriend(JsonSessionToken token, @PathParam("emailfriend") String emailfriend) {
		boolean res;
		try (DAOUserImpl userDAO = new DAOUserImpl()) {
			res = userDAO.deleteFriend(token, emailfriend);
		} catch (IOException e) {
			res = false;
		}

		if (res) {
			System.out.println("User " + token.getEmail() + " deleted " + emailfriend + " from friends");
			return Response.ok().build();
		} else {
			return Response.status(Response.Status.CONFLICT).entity("Delete friend failed").build();
		}

	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/getfriendlist")
	public Response getFriendList(JsonSessionToken token) {
		ArrayList<String> friends = null;
		try (DAOUserImpl userDAO = new DAOUserImpl()) {
			friends = userDAO.getFriendList(token);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (friends != null) {
			for(int i = 0; i< friends.size(); i++){
				System.out.println(friends.get(i));
			}
			
			
			//GenericEntity<ArrayList<String>> entity = new GenericEntity<ArrayList<String>>(friends) {};
			return Response.ok(new Gson().toJson(friends)).build();
		} else {
			return Response.status(Response.Status.CONFLICT).entity("No friends LOOOSER").build();
		}
	}
}
