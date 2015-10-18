package au.edu.unsw.soacourse.ors;

import java.io.IOException;
import java.util.List;

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
import au.edu.unsw.soacourse.ors.dao.support.AutoCheckDAOImpl;
import au.edu.unsw.soacourse.ors.dao.support.JobsDAOImpl;
import au.edu.unsw.soacourse.ors.model.Application;
import au.edu.unsw.soacourse.ors.model.AutoCheck;
import au.edu.unsw.soacourse.ors.model.JobPosting;

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
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String autoCheckApplication(@PathParam("appId") String appIdString, AutoCheck newAutoCheck) {
		int appId = Integer.parseInt(appIdString);

		AutoCheckDAOImpl autoCheckDAO = new AutoCheckDAOImpl();		
		AutoCheck autocheck = new AutoCheck();
		autocheck = autoCheckDAO.getByAppID(appId);
		
		if (autocheck.getResult() != null) {
			newAutoCheck.setAppId(appId);
			autoCheckDAO.updateAutocheck(newAutoCheck);
			return null;
		} else {
			
			ApplicationsDAOImpl appsDAO = new ApplicationsDAOImpl();
			Application app = appsDAO.getApplicationByID(appId);
			
			//TODO: Run the bpel service with the details here
			// If app has been checked before return nothing but check  again to update if it has changed
			// if it hasnt, check it and return 
			String result = "This should be the bpel";
			//Assuming bpel is done now
			
			autocheck = new AutoCheck();
			autocheck.setResult(result);
			autocheck.setAppId(appId);
			
			int autoCheckId = autoCheckDAO.createAutoCheck(autocheck);
			return "The created job is available at: " 
						+ uriInfo.getBaseUri().toASCIIString()
						+ "AutoCheck/" + autoCheckId;
		}
	}

	@GET
	@Path("{autoCheckId}")
	@Produces(MediaType.APPLICATION_JSON)
	public AutoCheck getByAutoCheckID(@PathParam("autoCheckId") String autoCheckIdString) {
		int autoCheckId = Integer.parseInt(autoCheckIdString);
		
		AutoCheckDAOImpl autoCheckDAO = new AutoCheckDAOImpl();
		AutoCheck autocheck = autoCheckDAO.getByAutoCheckID(autoCheckId);
				
		return autocheck;
	}
	
	@GET
	@Path("byappid/{appId}")
	@Produces(MediaType.APPLICATION_JSON)
	public AutoCheck getByAppID(@PathParam("appId") String appIdString) {
		int appId = Integer.parseInt(appIdString);
		
		AutoCheckDAOImpl autoCheckDAO = new AutoCheckDAOImpl();
		AutoCheck autocheck = autoCheckDAO.getByAutoCheckID(appId);
				
		return autocheck;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<AutoCheck> getAllAutoChecks() {
		AutoCheckDAOImpl autoCheckDAO = new AutoCheckDAOImpl();
		List<AutoCheck> autocheckList = autoCheckDAO.getAllAutoChecks();
				
		return autocheckList;
	}
	
}
