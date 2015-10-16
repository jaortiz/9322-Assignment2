package au.edu.unsw.soacourse.ors.dao.support;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import au.edu.unsw.soacourse.ors.dao.JobsDAO;
import au.edu.unsw.soacourse.ors.model.JobPosting;

public class JobsDAOImpl implements JobsDAO {
	
	public JobsDAOImpl() {
	}
	
	@Override
	public int createJob(JobPosting job) {
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
		    stmt.setString(2, job.getClosingDate());
		    stmt.setInt(3, job.getSalary());
		    stmt.setString(4, job.getPosition());
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
	    return countJobs();
	}
	
	public int countJobs() {
		Connection c = null;
		Statement stmt = null;
		int count = 0;
		
		try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:rest.db");
	      c.setAutoCommit(false);
	      
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
		
	    return count;
	}

	@Override
	public JobPosting getJobById(int jobID) {
		Connection c = null;
		PreparedStatement stmt = null;
		JobPosting job = null;
	    try {
	    	Class.forName("org.sqlite.JDBC");
	    	c = DriverManager.getConnection("jdbc:sqlite:rest.db");
	    	c.setAutoCommit(false);
		    System.out.println("Opened database successfully");
		    
		    stmt = c.prepareStatement("SELECT * FROM JOBPOSTINGS WHERE ID = ? "); 
		    stmt.setInt(1, jobID);
		    ResultSet rs = stmt.executeQuery();
		    
		    rs.next();
		    job = new JobPosting();
			job.setJobId(rs.getInt("ID"));
			job.setJobName(rs.getString("JOBNAME"));
			job.setClosingDate(rs.getString("CLOSEDATE"));
			job.setSalary(rs.getInt("SALARY"));
			job.setPosition(rs.getString("POSITIONTYPE"));
			job.setLocation(rs.getString("LOCATION"));
			job.setDescription(rs.getString("DESCRIPTION"));
			job.setStatus(rs.getString("STATUS"));
			
			
		    
		    rs.close();
		    stmt.close();
		    c.close();
	    } catch ( Exception e ) {
	    	
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    System.out.println("Records created successfully");
	    return job;
	}

	@Override
	public List<JobPosting> getAllJobs() {
		Connection c = null;
		Statement stmt = null;
		List<JobPosting> jobList = new ArrayList<JobPosting>();
		JobPosting job = null;
		try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:rest.db");
	      c.setAutoCommit(false);
	      
	      stmt = c.createStatement();
	      ResultSet rs = stmt.executeQuery( "SELECT * FROM JOBPOSTINGS;" );
	      while ( rs.next() ) {
	    	  job = new JobPosting();
	          job.setJobId(rs.getInt("ID"));
	          job.setJobName(rs.getString("JOBNAME"));
	          job.setClosingDate(rs.getString("CLOSEDATE"));
	          job.setSalary(rs.getInt("SALARY"));
	          job.setPosition(rs.getString("POSITIONTYPE"));
	          job.setLocation(rs.getString("LOCATION"));
	          job.setDescription(rs.getString("DESCRIPTION"));
	          job.setStatus(rs.getString("STATUS"));
	          
	          jobList.add(job);
	      }
	      rs.close() ;
	      stmt.close();
	      c.close();

	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
		
	    return jobList;
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
