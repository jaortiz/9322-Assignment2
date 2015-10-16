package au.edu.unsw.soacourse.ors;

import java.io.IOException;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import au.edu.unsw.soacourse.ors.model.*;
import au.edu.unsw.soacourse.ors.service.*;

@Path("/applications")
public class ApplicationService {
	
	@Autowired
	private ApplicationDAO applicationDAO;

	
	//Need to implement produces and consumes
	@POST
	@Path("/add")
	public void addApplication(
			@FormParam("appId") String appId,
			@FormParam("firstName") String firstName,
			@FormParam("lastName") String lastName,
			@FormParam("email") String email,
			@FormParam("phoneNumber") String phoneNumber,
			@FormParam("coverLetter") String coverLetter,
			@FormParam("resume") String resume
	) throws IOException {
		Application app = new Application();
		
		app.setAppId(appId);
		app.setJobId("1");
		app.setFirstName(firstName);
		app.setLastName(lastName);
		app.setEmail(email);
		app.setPhoneNumber(phoneNumber);
		app.setCoverLetter(coverLetter);
		app.setResume(resume);
		app.setStatus("In Process");
		
		applicationDAO.addApplication(app);
		
		System.out.println("add/POST");
		System.out.println(appId + firstName + lastName + email + phoneNumber + coverLetter + resume);
	}
	
	@GET
	@Produces("application/json")
	@Path("/get/{appID}")
	public Response getApplication(@PathParam("appID") String appId) {
		System.out.println("get");
		Application application = applicationDAO.getApplication(appId);
		return Response.ok().entity(application).build();
	}
}
