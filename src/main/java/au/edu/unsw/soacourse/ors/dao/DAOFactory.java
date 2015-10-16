package au.edu.unsw.soacourse.ors.dao;

import java.util.HashMap;
import java.util.Map;

import au.edu.unsw.soacourse.ors.dao.support.*;

public class DAOFactory {
	
	private static final String JOBS_DAO = "jobsDAO";
	
	private Map daos;
	
	private static DAOFactory instance = new DAOFactory();
	
	private DAOFactory() {
		daos = new HashMap();
		daos.put(JOBS_DAO, new JobsDAOImpl());
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