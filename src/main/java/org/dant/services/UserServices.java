package org.dant.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bson.Document;
import org.dant.db.DAOUserImpl;
import org.dant.db.User;
import org.dant.json.JsonConnectionBean;
import org.dant.json.JsonSessionToken;

import com.google.gson.Gson;

@RequestScoped
@Path("/services")
public class UserServices {

	@Inject
	Sender sender;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/createuser")
	public Response createUser(JsonConnectionBean bean) {

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
	@Path("/deleteuser")
	public Response deleteUser(JsonSessionToken token) {
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
	@Path("/requestfriend/{emailfriend}")
	public Response requestFriend(JsonSessionToken token, @PathParam("emailfriend") String emailfriend) {
		boolean res;
		User me = null;
		try (DAOUserImpl userDAO = new DAOUserImpl()) {
			me = userDAO.getUser(token.getEmail());
			res = userDAO.requestFriend(token.getEmail(), emailfriend);
		} catch (IOException e) {
			res = false;
		}
		if (res && me != null) {
			HashMap<String, String> data = new HashMap<String, String>();
			data.put("email", me.getEmail());
			data.put("username", me.getUsername());
			ArrayList<String> channels = new ArrayList<String>();
			channels.add(emailfriend);
			sender.send(channels, "friendRequest", data);
			System.out.println("User " + token.getEmail() + " send to " + emailfriend + " friend request");
			return Response.ok().entity("Request sended").build();
		} else {
			return Response.status(Response.Status.CONFLICT).entity("Request friend failed").build();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/confirmfriend/{emailfriend}")
	public Response confirmFriend(JsonSessionToken token, @PathParam("emailfriend") String emailfriend) {
		boolean res;
		User me = null;
		try (DAOUserImpl userDAO = new DAOUserImpl()) {
			me = userDAO.getUser(token.getEmail());
			res = userDAO.confirmFriend(token.getEmail(), emailfriend);
		} catch (IOException e) {
			res = false;
		}

		if (res && me != null) {
			HashMap<String, String> data = new HashMap<String, String>();
			data.put("email", me.getEmail());
			data.put("username", me.getUsername());
			ArrayList<String> channels = new ArrayList<String>();
			channels.add(emailfriend);
			sender.send(channels, "friendConfirm", data);
			System.out.println("User " + token.getEmail() + " send to " + emailfriend + " friend request");
			return Response.ok().entity("Request sended").build();
		} else {
			return Response.status(Response.Status.CONFLICT).entity("Request friend failed").build();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/deletefriend/{emailfriend}")
	public Response deleteFriend(JsonSessionToken token, @PathParam("emailfriend") String emailfriend) {
		boolean res;
		try (DAOUserImpl userDAO = new DAOUserImpl()) {
			res = userDAO.deleteFriend(token.getEmail(), emailfriend);
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
		ArrayList<User> friends = null;
		try (DAOUserImpl userDAO = new DAOUserImpl()) {
			friends = userDAO.getFriends(token);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (friends != null) {
			for (int i = 0; i < friends.size(); i++) {
				System.out.println(friends.get(i));
			}
			// GenericEntity<ArrayList<String>> entity = new
			// GenericEntity<ArrayList<String>>(friends) {};
			return Response.ok(new Gson().toJson(friends)).build();
		} else {
			return Response.status(Response.Status.CONFLICT).entity("No friends LOOOSER").build();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/sendmypos/{lon}/{lat}")
	public Response sendMyPos(JsonSessionToken token, @PathParam("lon") String lon, @PathParam("lat") String lat) {

		ArrayList<Document> friends = null;
		ArrayList<String> channels = new ArrayList<String>();

		try (DAOUserImpl userDAO = new DAOUserImpl()) {
			friends = userDAO.getFriendList(token);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (friends != null) {

			for (Document doc : friends) {
				if (doc.getInteger("confirmed") >= 1) {
					channels.add(doc.getString("email"));
				}
			}
			HashMap<String, String> data = new HashMap<String, String>();
			data.put("email", token.getEmail());
			data.put("lon", lon);
			data.put("lat", lat);
			sender.send(channels, "updatePos", data);
			return Response.ok().build();
		}
		return Response.status(Response.Status.CONFLICT).entity("No friends LOOOSER").build();
	}

}
