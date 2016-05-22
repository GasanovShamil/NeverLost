package org.dant.services;

import java.io.IOException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.dant.beans.JsonConnectionBean;
import org.dant.beans.JsonSessionToken;
import org.dant.db.DAOUserImpl;

@Path("/authentication")
public class AuthenticationEndpoint {

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/login")
	public Response authenticateUser(JsonConnectionBean bean) {
		JsonSessionToken token;
		try (DAOUserImpl userDAO = new DAOUserImpl()) {
			token = userDAO.login(bean);
		} catch (IOException e) {
			token = null;
		}

		if (token != null) {
			System.out.println("login : " + bean.getEmail() + ", password : " + bean.getPassword());
			return Response.ok(token).build();
		} else {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/logout")
	public Response logoutUser(JsonSessionToken token) {
		try (DAOUserImpl userDAO = new DAOUserImpl()) {
			userDAO.logout(token);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Response.status(Response.Status.OK).build();

	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/checkout")
	public Response checkoutUser(JsonSessionToken token) {
		boolean res=false;
		try (DAOUserImpl userDAO = new DAOUserImpl()) {
			res=userDAO.checkout(token);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(res){
		return Response.status(Response.Status.OK).build();
		}else{
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}

	}
}