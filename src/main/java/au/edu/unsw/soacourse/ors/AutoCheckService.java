package au.edu.unsw.soacourse.ors;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import au.edu.unsw.soacourse.ors.dao.support.ApplicationsDAOImpl;
import au.edu.unsw.soacourse.ors.model.Application;

@Path("/AutoCheck")
public class AutoCheckService {
	// Allows to insert contextual objects into the class, 
	// e.g. ServletContext, Request, Response, UriInfo
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	// Runs the autocheck bpel service using the persons details from the application
	@PUT
	@Path("{appId}")
	@Produces(MediaType.TEXT_PLAIN)
	public String autoCheckApplication(@PathParam("appId") String appIdString) {
		int appId = Integer.parseInt(appIdString);
		
		ApplicationsDAOImpl appsDAO = new ApplicationsDAOImpl();
		Application app = appsDAO.getApplicationByID(appId);

		//TODO: Run the bpel service with the details here
		// If app has been checked before return nothing but check  again to update if it has changed
		// if it hasnt, check it and return 
		
		//Assuming bpel is done now
		
		
		int autoCheckId = 0;
		return "The created job is available at: " 
					+ uriInfo.getBaseUri().toASCIIString()
					+ "AutoCheck/" + autoCheckId;
		
	}
}
