package au.edu.unsw.soacourse.ors.dao.support;

import java.sql.*;
import java.util.List;

import au.edu.unsw.soacourse.ors.dao.JobsDAO;
import au.edu.unsw.soacourse.ors.model.JobPosting;

public class JobsDAOImpl implements JobsDAO {
	
	public JobsDAOImpl() {
	}
	
	@Override
	public void createJob(JobPosting job) {
		Connection c = null;
		PreparedStatement stmt = null;
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:rest.db");
	      c.setAutoCommit(false);
	      System.out.println("Opened database successfully");

	      stmt = c.prepareStatement("INSERT INTO JOBPOSTINGS (JOBNAME, CLOSEDATE, " +
	    		  	"SALARY, POSITIONTYPE, LOCATION, DESCRIPTION, STATUS) " +
	    		  	"VALUES (?,?,?,?,?,?,?);"); 
	      stmt.setString(1, job.getJobName());
	      stmt.setString(2, job.getCloseDate());
	      stmt.setInt(3, job.getSalaryRate());
	      stmt.setString(4, job.getPositionType());
	      stmt.setString(5, job.getLocation());
	      stmt.setString(6, job.getDescription());
	      stmt.setString(7, "created");
	      stmt.executeUpdate();
	      
	      c.commit();
	      c.close();

	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    System.out.println("Records created successfully");

	}
	
	public int countJobs() {
		Connection c = null;
		Statement stmt = null;
		int count = 0;
		
		try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:rest.db");
	      c.setAutoCommit(false);
	      System.out.println("Opened database successfully");
	      
	      stmt = c.createStatement();
	      ResultSet rs = stmt.executeQuery( "SELECT COUNT(*) AS rowcount FROM JOBPOSTINGS;" );
	      rs.next();
	      count = rs.getInt("rowcount") ;
	      rs.close() ;
	      
	      c.commit();
	      c.close();

	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    System.out.println("Records created successfully");
		
	    return count;
	}

	@Override
	public JobPosting getJobById(int JobID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<JobPosting> getAllJobs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateJobById(JobPosting job) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<JobPosting> getJobsBySearch(JobPosting job) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void archiveJob(int JobID) {
		// TODO Auto-generated method stub

	}

}
