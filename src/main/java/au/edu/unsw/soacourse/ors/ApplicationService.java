package au.edu.unsw.soacourse.ors;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import au.edu.unsw.soacourse.ors.dao.support.ApplicationsDAOImpl;
import au.edu.unsw.soacourse.ors.model.Application;


@Path("/applications")
public class ApplicationService {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	@GET
	@Path("setUpDatabase")
	public void setUpDatabase() {
		ApplicationsDAOImpl appDAO = new ApplicationsDAOImpl();
		appDAO.setUpDatabase();
	}
	
	@POST
	@Path("newApplication")
    @Consumes("application/json")
	public void newApplication(Application newApp) throws IOException {
		
		ApplicationsDAOImpl appDAO = new ApplicationsDAOImpl();
		
		int appID = appDAO.createApplication(newApp);
		
		
		//TODO: Return the newly generated job
	}
	
	//ON GET Returns all the applications in the database
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Application> getAllApplications() throws IOException {
		
		ApplicationsDAOImpl appsDAO = new ApplicationsDAOImpl();
		List<Application> appList = appsDAO.getAllApplications();
		return appList;
	}
}
