package au.edu.unsw.soacourse.ors;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
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
		return Response.created(new URI("http://localhost8080/ORSRestfulService/applications/view?appID=" + appID)).build();
	}
	
	//ON GET Returns all the applications in the database
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllApplications() throws IOException {
		ApplicationsDAOImpl appsDAO = new ApplicationsDAOImpl();
		List<Application> appList = appsDAO.getAllApplications();
		return Response.ok().entity(appList).build();
	}
	
	@GET
	@Path("view")
	@Produces(MediaType.APPLICATION_JSON) 
	public Response getApplicationbyID(@QueryParam("appID") int appID) {
		ApplicationsDAOImpl appsDAO = new ApplicationsDAOImpl();
		Application app = appsDAO.getApplicationByID(appID);
		return Response.ok().entity(app).build();
		
	}
}
