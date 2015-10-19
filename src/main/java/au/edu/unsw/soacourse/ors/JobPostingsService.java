package au.edu.unsw.soacourse.ors;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
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

import au.edu.unsw.soacourse.ors.dao.support.JobsDAOImpl;
import au.edu.unsw.soacourse.ors.dao.support.RegisteredUsersDAOImpl;
import au.edu.unsw.soacourse.ors.model.JobPosting;
import au.edu.unsw.soacourse.ors.model.RegisteredUser;
/**
 * NEED TO DO:
 * IMPROVE SECURITY OF ALL FUNCTIONS
 * Archiving a job posting - restrict to manager
 * ALSO NEED TO TEST
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
	
	@GET
	@Path("setUpDatabase")
	public void setUpDatabase() {
		JobsDAOImpl jobsDAO = new JobsDAOImpl();
		jobsDAO.setUpDatabase();
	}
	
	@POST
	@Path("createJobPosting")
    @Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String newJob(@HeaderParam("ShortKey") String shortkey, JobPosting newJob) throws IOException {
		
		RegisteredUsersDAOImpl userDAO = new RegisteredUsersDAOImpl();
		RegisteredUser user = userDAO.getUsersbyShortKey(shortkey);
		if (user != null && user.getRole().equals("manager")) {
			JobsDAOImpl jobsDAO = new JobsDAOImpl();
			
			int jobId = jobsDAO.createJob(newJob);
			return "The created job is available at: " 
					+ uriInfo.getBaseUri().toASCIIString()
					+ "jobPostings/" + jobId;
		} else {
			return "Must be a manager to create a job";
		}
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
	
	@PUT
	@Path("{jobID}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public JobPosting updateJob(@HeaderParam("ShortKey") String shortkey, @PathParam("jobID") String idString, JobPosting jobToUpdate) {
		RegisteredUsersDAOImpl userDAO = new RegisteredUsersDAOImpl();
		RegisteredUser user = userDAO.getUsersbyShortKey(shortkey);
		JobPosting updatedJob = null;
		if (user != null && user.getRole().equals("manager")) {
			int jobID = Integer.parseInt(idString);
			
			jobToUpdate.setJobId(jobID);
			JobsDAOImpl jobsDAO = new JobsDAOImpl();
			updatedJob = jobsDAO.updateJobById(jobToUpdate);
		}
		return updatedJob;
		
	}
	
	// Can Search using jobName, position, location, status
	@GET
	@Path("jobSearch")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public List<JobPosting> searchJobs(
			@DefaultValue("") @QueryParam("jobName") String name,
			@DefaultValue("") @QueryParam("position") String position,
			@DefaultValue("") @QueryParam("location") String location,
			@DefaultValue("") @QueryParam("description") String description,
			@DefaultValue("") @QueryParam("status") String status) {
		
		JobPosting job = new JobPosting();
		job.setJobName(name);
		job.setPosition(position);
		job.setLocation(location);
		job.setDescription(description);
		job.setStatus(status);
		
		JobsDAOImpl jobsDAO = new JobsDAOImpl();
		List<JobPosting> jobSearchResults = jobsDAO.getJobsBySearch(job);
		
		
		return jobSearchResults;
	}
	
	
	@DELETE
	@Path("deleteJob/{jobid}")
	public void deleteBook(@HeaderParam("ShortKey") String shortkey, @PathParam("jobid") String idString) {
		
		RegisteredUsersDAOImpl userDAO = new RegisteredUsersDAOImpl();
		RegisteredUser user = userDAO.getUsersbyShortKey(shortkey);
		if (user != null && user.getRole().equals("manager")) {
			int jobID = Integer.parseInt(idString);
			
			JobsDAOImpl jobsDAO = new JobsDAOImpl();
			jobsDAO.archiveJob(jobID);
		}		
	}
	
	@GET
	@Path("archivedJobs")
	@Produces(MediaType.APPLICATION_JSON)
	public List<JobPosting> getAllArchivedJobs() throws IOException {
		JobsDAOImpl jobsDAO = new JobsDAOImpl();
		
		List<JobPosting> jobsList = jobsDAO.getAllArchivedJobs();
		return jobsList;
	}
	
	@GET
	@Path("archivedJobs/{jobID}")
	@Produces(MediaType.APPLICATION_JSON)
	public JobPosting getArchivedJobById(@PathParam("jobID") String idString) throws IOException {
		int jobID = Integer.parseInt(idString);
		
		JobsDAOImpl jobsDAO = new JobsDAOImpl();
		JobPosting job = jobsDAO.getArchivedJobById(jobID);
		
		return job;
		
	}
	
	
}
