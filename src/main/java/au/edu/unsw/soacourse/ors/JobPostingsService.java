package au.edu.unsw.soacourse.ors;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import au.edu.unsw.soacourse.ors.dao.support.JobsDAOImpl;
import au.edu.unsw.soacourse.ors.model.JobPosting;
/**
 * NEED TO DO:
 * updating a job posting
 * Searching a job posting
 * Archiving a job posting
 *
 */

@Path("/jobPostings")
public class JobPostingsService {
	// Allows to insert contextual objects into the class, 
	// e.g. ServletContext, Request, Response, UriInfo
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	@POST
	@Path("createJobPosting")
    @Consumes("application/json")
	public void newJob(JobPosting newJob) throws IOException {
		
		JobsDAOImpl jobsDAO = new JobsDAOImpl();
		
		int jobId = jobsDAO.createJob(newJob);
		
		
		//TODO: Return the newly generated job
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<JobPosting> getAllJobs() throws IOException {
		JobsDAOImpl jobsDAO = new JobsDAOImpl();
		
		List<JobPosting> jobsList = jobsDAO.getAllJobs();
		return jobsList;
	}
	
	@GET
	@Path("{jobID}")
	@Produces(MediaType.APPLICATION_JSON)
	public JobPosting getJobById(@PathParam("jobID") String idString) throws IOException {
		int jobID = Integer.parseInt(idString);
		
		JobsDAOImpl jobsDAO = new JobsDAOImpl();
		JobPosting job = jobsDAO.getJobById(jobID);
		
		return job;
		
	}	
	
	
	
}
