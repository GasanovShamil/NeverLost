package org.dant.services;

import java.io.IOException;
import java.text.DateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bson.Document;
import org.dant.beans.JsonConnectionBean;
import org.dant.beans.JsonSessionToken;
import org.dant.beans.User;
import org.dant.db.DAOUserImpl;

import com.google.gson.Gson;

@RequestScoped
@Path("/services")
public class UserServices {

	@Inject
	PusherSender sender;

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

	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("/confirmemail")
	public String confirmEmail(@QueryParam("email") String email, @QueryParam("token") String token) {
		boolean res = false;
		try (DAOUserImpl userDAO = new DAOUserImpl()) {
			res = userDAO.confirmUser(email, token);
		} catch (IOException e) {
			return "<html> " + "<title>" + "Confirm" + "</title>" + "<body><h1>" + "Something went wrong!"
					+ "</body></h1>" + "</html> ";
		}
		if (res) {
			return "<html> " + "<title>" + "Confirm" + "</title>" + "<body><h1>Hello " + email + "!</body></h1>"+"<br><P>Email confirmed!</p>"+"</html> ";
		} else {
			return "<html> " + "<title>" + "NNNNNNNOOOOOOOONONONONONONONONONONONON" + "</title>"
					+ "<body><h1>NNNNNNNOOOOOOOONONONONONONONONONONONON</body></h1>" + "</html> ";
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updateuser")
	public Response updateUser(User bean) {

		boolean res;
		try (DAOUserImpl userDAO = new DAOUserImpl()) {
			res = userDAO.updateUser(bean);
		} catch (IOException e) {
			res = false;
		}

		if (res) {
			System.out.println("User : " + bean.getEmail() + " - updated");
			return Response.ok().build();
		} else {
			return Response.status(Response.Status.CONFLICT).build();
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
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/findfriend/{emailfriend}")
	public Response findFriend(JsonSessionToken token, @PathParam("emailfriend") String emailfriend) {

		User friend = null;
		try (DAOUserImpl userDAO = new DAOUserImpl()) {
			friend = userDAO.getUser(emailfriend);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (friend != null) {
			return Response.ok(new Gson().toJson(friend)).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
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
			Date date = me.getDate();
			DateFormat shortDate = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT);	
			HashMap<String, String> data = new HashMap<String, String>();
			data.put("email", me.getEmail());
			data.put("username", me.getUsername());
			data.put("date", shortDate.format(date));
			data.put("lat", "" + me.getLat());
			data.put("lon", "" + me.getLon());
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
			Date date = me.getDate();
			DateFormat shortDate = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT);
			HashMap<String, String> data = new HashMap<String, String>();
			data.put("email", me.getEmail());
			data.put("username", me.getUsername());
			data.put("date", shortDate.format(date));
			data.put("lat", "" + me.getLat());
			data.put("lon", "" + me.getLon());
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
			friends = userDAO.getFriends(token.getEmail());
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
			return Response.status(Response.Status.NOT_FOUND).entity("No friends LOOOSER").build();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/sendmypos/{lat}/{lon}")
	public Response sendMyPos(JsonSessionToken token, @PathParam("lat") double lat, @PathParam("lon") double lon) {
		User user = null;
		ArrayList<Document> friends = null;
		ArrayList<String> channels = new ArrayList<String>();
		Date date = new Date();
		DateFormat shortDate = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
		shortDate.setTimeZone(TimeZone.getTimeZone("GMT+2"));
		try (DAOUserImpl userDAO = new DAOUserImpl()) {
			user = userDAO.getUser(token.getEmail());
			friends = user.getFriends();
			userDAO.setUserPos(token.getEmail(), date, lat, lon);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (!friends.isEmpty()) {

			for (Document tmp : friends) {
				if (tmp.getInteger("confirmed") >= 1) {
					channels.add(tmp.getString("email"));
				}
			}
			HashMap<String, String> data = new HashMap<String, String>();
			data.put("email", token.getEmail());
			data.put("date", shortDate.format(date));
			data.put("lat", "" + lat);
			data.put("lon", "" + lon);
			sender.send(channels, "updatePos", data);
			return Response.ok().build();
		}
		return Response.status(Response.Status.NOT_FOUND).entity("No friends LOOOSER").build();
	}

}
