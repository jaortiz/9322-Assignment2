package au.edu.unsw.soacourse.ors.dao;

import java.util.List;

import au.edu.unsw.soacourse.ors.model.*;

public interface ApplicationsDAO {

	/**
	 * Creates the Application and puts it in the database
	 * 
	 * @param application
	 * @return URI of the new resource
	 */
	int createApplication(Application application);
	
	
	/**
	 * Gets an application by the appID
	 * 
	 * @param appID
	 * @return
	 */
	Application getApplicationByID(int appID);
	
	
	/**
	 * Updates and application by its appID
	 * 
	 * @param appID
	 */
	Application updateApplicationByID(Application updatedApp);
	
	
	/**
	 * Gets a list of all the current applications
	 * 
	 * @return
	 */
	List<Application> getAllApplications();
	
	
	/**
	 * Archives an application by ID
	 * 
	 * @param appID
	 */
	void archiveApplication(int appID);
	
	
	/**
	 * Gets an archived application by ID
	 * 
	 * @param appId
	 * @return
	 */
	Application getArchivedApplicationById(int appId);
	
	
	/**
	 * Gets a list of all the archived Applications
	 * 
	 * @return
	 */
	List<Application> getAllArchivedApplications();
	
	/**
	 * Gets a list of all applications for a job posting
	 * 
	 * @param jobID
	 * @return
	 */
	List<Application> getAllApplicationsByJobID(int jobID);


	void assignApplication(int appId, String department);


	AssignedApplication getAssignedAppByID(int appID);


	List<AssignedApplication> getAppIdByAssignedTeam(String team);


	void updateAssignedApplication(int appId, String department);
}
