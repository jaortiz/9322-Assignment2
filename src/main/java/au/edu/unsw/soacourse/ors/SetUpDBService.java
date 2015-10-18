package au.edu.unsw.soacourse.ors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import au.edu.unsw.soacourse.ors.dao.support.SetUpDAOImpl;

@Path("/setupdb")
public class SetUpDBService {
	// Allows to insert contextual objects into the class, 
	// e.g. ServletContext, Request, Response, UriInfo
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	@GET
	public void setUpDatabase() {
		SetUpDAOImpl setUpDAO = new SetUpDAOImpl();
		setUpDAO.setUpDatabase();
	}

}
