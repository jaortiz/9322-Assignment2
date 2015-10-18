package au.edu.unsw.soacourse.ors.dao;

import java.util.HashMap;
import java.util.Map;

import au.edu.unsw.soacourse.ors.dao.support.*;

public class DAOFactory {
	
	private static final String JOBS_DAO = "jobsDAO";
	private static final String APPLICATIONS_DAO = "applicationsDAO";
	private static final String REVIEWS_DAO = "reviewsDAO";
	private static final String REGISTEREDUSERS_DAO = "registeredUsersDAO";
	private Map daos;
	
	private static DAOFactory instance = new DAOFactory();
	
	private DAOFactory() {
		daos = new HashMap();
		daos.put(JOBS_DAO, new JobsDAOImpl());
		daos.put(APPLICATIONS_DAO, new ApplicationsDAOImpl());
		daos.put(REVIEWS_DAO, new ReviewsDAOImpl());
		daos.put(REGISTEREDUSERS_DAO, new RegisteredUsersDAOImpl());
	}
	
	/**
	 * Finds the jobs dao
	 * @return
	 */
	public JobsDAO getJobsDAO() {
		return (JobsDAO) daos.get(JOBS_DAO);
	}
	
	public static DAOFactory getInstance() {
		return instance;
	}
}