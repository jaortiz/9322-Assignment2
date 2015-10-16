package au.edu.unsw.soacourse.ors.dao;

import java.util.List;

import au.edu.unsw.soacourse.ors.model.JobPosting;

/**
 * The Data Access Object for jobs
 *
 */
public interface JobsDAO {
	
	/**
	 * Creates the job in the database
	 * @param job the jobModel to insert
	 */
	int createJob (JobPosting job); 
	
	/**
	 * Retrieves a job posting from the database
	 * @param JobID the id of the job to recieve
	 * @return the JobPosting
	 */
	JobPosting getJobById (int jobID);
	
	/**
	 * Retrieves all the jobs from the database that arent archived
	 * @return all unarchived jobs
	 */
	List<JobPosting> getAllJobs ();
	
	/**
	 * updates the job with new details
	 * @param job the model containing the updated elements
	 */
	void updateJobById (JobPosting job);	
	
	/**
	 * Retrieves the jobs according to the search
	 * @param job the model containing the search terms
	 * @return list of jobs matching the search
	 */
	List<JobPosting> getJobsBySearch (JobPosting job);
	
	/**
	 * Archives the job
	 * @param JobID the job to archive
	 */
	void archiveJob (int JobID);
}
