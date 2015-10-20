package au.edu.unsw.soacourse.ors;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import au.edu.unsw.soacourse.ors.dao.support.ApplicationsDAOImpl;
import au.edu.unsw.soacourse.ors.model.Application;
import au.edu.unsw.soacourse.ors.model.AssignedApplication;


@Path("/applications")
public class ApplicationService {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	@GET
	@Path("setUpDatabase")
	public Response setUpDatabase() {
		ApplicationsDAOImpl appDAO = new ApplicationsDAOImpl();
		appDAO.setUpDatabase();
		return Response.ok().build();
	}
	
	@POST
	@Path("newApplication")
    @Consumes("application/json")
	public Response newApplication(Application newApp) throws IOException, URISyntaxException {
		
		ApplicationsDAOImpl appDAO = new ApplicationsDAOImpl();
		int appID = appDAO.createApplication(newApp);
		return Response.created(new URI(uriInfo.getBaseUri() + "applications/viewByID?appID=" + appID)).build();
	}
	
	
	@GET
	@Path("viewAll")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllApplications() throws IOException {
		ApplicationsDAOImpl appsDAO = new ApplicationsDAOImpl();
		List<Application> appList = appsDAO.getAllApplications();
		return Response.ok().entity(appList).build();
	}
	
	@GET
	@Path("viewByID")
	@Produces(MediaType.APPLICATION_JSON) 
	public Application getApplicationbyID(@QueryParam("appID") int appID) {
		ApplicationsDAOImpl appsDAO = new ApplicationsDAOImpl();
		Application app = appsDAO.getApplicationByID(appID);
		return app;
		
	}
	
	@GET
	@Path("viewByJobID")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllApplicationsByJobID(@QueryParam("jobID") int jobID) {
		ApplicationsDAOImpl appsDAO = new ApplicationsDAOImpl();
		List<Application> appList = appsDAO.getAllApplicationsByJobID(jobID);
		return Response.ok().entity(appList).build();
	}
	
	@DELETE
	@Path("archive")
	public Response archiveApplicationByID(@QueryParam("appID") int appID) {
		ApplicationsDAOImpl appsDAO = new ApplicationsDAOImpl();
		appsDAO.archiveApplication(appID);
		return Response.ok().build();
	}
	
	@PUT
	@Path("{appID}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Application updateApplicationByID(@PathParam("appID") int appID, Application appToUpdate) {
		ApplicationsDAOImpl appsDAO = new ApplicationsDAOImpl();
		Application updatedApp = null;
		
		appToUpdate.setAppId(appID);
		updatedApp = appsDAO.updateApplicationByID(appToUpdate);
		
		return updatedApp;
	}
	
	@PUT
	@Path("assignApplication/{appID}")
	@Consumes(MediaType.TEXT_PLAIN)
	public void assignApplication(@PathParam("appID") int appID, String team) {
		ApplicationsDAOImpl appsDAO = new ApplicationsDAOImpl();
		
		if (appsDAO.getApplicationByID(appID) != null) {
			AssignedApplication app = appsDAO.getAssignedAppByID(appID);
			if(app == null) {
				appsDAO.assignApplication(appID, team);
			} else {
				appsDAO.updateAssignedApplication(appID, team);
			}
		}
	}
	
	@GET
	@Path("assignedApplicationByTeam/{team}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<AssignedApplication> getAssignedApplicationsByTeam(@PathParam("team") String team) {
		ApplicationsDAOImpl appsDAO = new ApplicationsDAOImpl();
		
		return appsDAO.getAppIdByAssignedTeam(team);
	}
	
	@GET
	@Path("assignedApplication/{appId}")
	@Produces(MediaType.APPLICATION_JSON)
	public AssignedApplication getAssignedApplicationsById(@PathParam("appId") int appId) {
		ApplicationsDAOImpl appsDAO = new ApplicationsDAOImpl();
		
		return appsDAO.getAssignedAppByID(appId);
	}
}
