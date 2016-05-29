package org.dant.filters;

import java.io.IOException;
import java.io.InputStream;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import org.apache.commons.io.IOUtils;
import org.dant.beans.JsonSessionToken;
import org.dant.db.DAOUserImpl;

import com.google.gson.Gson;

@Provider
public class AuthFilter implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext context) throws IOException {
		String url = context.getUriInfo().getPath();

		if (!url.contains("login") && !url.contains("checkout") && !url.contains("createuser")) {
			String data = IOUtils.toString(context.getEntityStream(), "UTF-8");
			System.out.println("FILTER CHECK : " + new Gson().fromJson(data, JsonSessionToken.class).token);
			boolean auth;
			try (DAOUserImpl userDAO = new DAOUserImpl()) {
				auth = userDAO.checkout(new Gson().fromJson(data, JsonSessionToken.class));
			}
			if (!auth) {
				context.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("User cannot access the resource.").build());
				return;
			} else {
				InputStream in = IOUtils.toInputStream(data, "UTF-8");

				context.setEntityStream(in);
			}

		}
	}

}