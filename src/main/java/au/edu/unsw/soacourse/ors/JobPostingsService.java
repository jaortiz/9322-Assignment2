package au.edu.unsw.soacourse.ors;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

@Path("/jobPostings")
public class JobPostingsService {
	// Allows to insert contextual objects into the class, 
	// e.g. ServletContext, Request, Response, UriInfo
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
    // POST to create a book - FIX THIS
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void newJob(
			@FormParam("jobName") String jobName,
			@FormParam("closingDate") String closingDate,
			@FormParam("salary") String salary,
			@FormParam("position") String position,
			@FormParam("location") String location,
			@FormParam("description") String description
	) throws IOException {
		
	}
}
