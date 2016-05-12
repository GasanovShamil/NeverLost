package org.dant.filters;

import java.io.IOException;
import java.io.InputStream;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import org.apache.commons.io.IOUtils;
import org.dant.db.DAOUserImpl;
import org.dant.json.JsonSessionToken;

import com.google.gson.Gson;

@Provider
public class AuthFilter implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext context) throws IOException {
		String url = context.getUriInfo().getPath();

		if (!url.contains("login") && !url.contains("checkout") && !url.contains("createuser")) {
			String token = IOUtils.toString(context.getEntityStream(), "UTF-8");
			System.out.println("FILTER CHECK : " + new Gson().fromJson(token, JsonSessionToken.class).token);
			boolean auth;
			try (DAOUserImpl userDAO = new DAOUserImpl()) {
				auth = userDAO.checkout(new Gson().fromJson(token, JsonSessionToken.class));
			}
			if (!auth) {
				context.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("User cannot access the resource.").build());
				return;
			} else {
				InputStream in = IOUtils.toInputStream(token, "UTF-8");

				context.setEntityStream(in);
			}

		}
	}

}