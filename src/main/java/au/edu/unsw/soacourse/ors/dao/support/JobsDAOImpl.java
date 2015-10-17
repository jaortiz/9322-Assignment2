package au.edu.unsw.soacourse.ors.dao.support;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import au.edu.unsw.soacourse.ors.dao.JobsDAO;
import au.edu.unsw.soacourse.ors.model.JobPosting;

public class JobsDAOImpl implements JobsDAO {
	
	public JobsDAOImpl() {
	}
	
	public void setUpDatabase () {
		Connection c = null;
	    Statement stmt = null;
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:rest.db");
	      System.out.println("Opened database successfully");

	      stmt = c.createStatement();
	      String sql = "CREATE TABLE IF NOT EXISTS JOBPOSTINGS " +
	                   "(ID INTEGER PRIMARY KEY	AUTOINCREMENT NOT NULL," +
	                   " JOBNAME           	TEXT    NOT NULL, " + 
	                   " CLOSEDATE			TEXT, " + 
	                   " SALARY				REAL, " + 
	                   " POSITIONTYPE		TEXT, " +
	                   " LOCATION			TEXT, " + 
	                   " DESCRIPTION		TEXT, "	+ 
	                   " STATUS				TEXT)"; 
	      stmt.executeUpdate(sql);
	      System.out.println("Job Postings Table created successfully");
	      
	      sql = "CREATE TABLE IF NOT EXISTS ARCHIVEDJOBS " +
                  "(ID INTEGER PRIMARY KEY	AUTOINCREMENT NOT NULL," +
                  " JOBNAME           	TEXT    NOT NULL, " + 
                  " CLOSEDATE			TEXT, " + 
                  " SALARY				REAL, " + 
                  " POSITIONTYPE		TEXT, " +
                  " LOCATION			TEXT, " + 
                  " DESCRIPTION			TEXT, "	+ 
                  " STATUS				TEXT)";
	      stmt.executeUpdate(sql);
	      System.out.println("Archived jobs Table created successfully");
	      stmt.close();
	      c.close();
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    
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
	    return lastJob();
	}
	
	public int lastJob() {
		Connection c = null;
		Statement stmt = null;
		int lastJobId = 0;
		
		try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:rest.db");
	      c.setAutoCommit(false);
	      
	      stmt = c.createStatement();
	      ResultSet rs = stmt.executeQuery( "SELECT * FROM JOBPOSTINGS WHERE ID = (SELECT MAX(ID) FROM JOBPOSTINGS);" );
	      rs.next();
	      lastJobId = rs.getInt("ID") ;
	      rs.close() ;
	      
	      c.commit();
	      c.close();

	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
		
	    return lastJobId;
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

;
	@Override
	public List<JobPosting> getJobsBySearch(JobPosting job) {
		Connection c = null;
		PreparedStatement stmt = null;
		List<JobPosting> jobList = new ArrayList<JobPosting>();
		JobPosting jobRecord = null;
		try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:rest.db");
	      c.setAutoCommit(false);
	      
	      String wildCard = "%";
	      String name = wildCard + job.getJobName() + wildCard;
	      String position = wildCard + job.getPosition() + wildCard;
	      String location = wildCard + job.getLocation() + wildCard;
	      String description = wildCard + job.getDescription() + wildCard;
	      String status = wildCard + job.getStatus() + wildCard;
	      
	      stmt = c.prepareStatement("SELECT * FROM JOBPOSTINGS WHERE LOWER(JOBNAME) LIKE LOWER(?)" +
	    		  	"AND LOWER(POSITIONTYPE) LIKE LOWER(?)" + 
	    		  	"AND LOWER(LOCATION) LIKE LOWER(?)" +
	    		  	"AND LOWER(DESCRIPTION) LIKE LOWER(?)" + 
	    		  	"AND LOWER(STATUS) LIKE LOWER(?)");
	      
	      stmt.setString(1, name);
	      stmt.setString(2, position);
	      stmt.setString(3, location);
	      stmt.setString(4, description);
	      stmt.setString(5, status);
	      
	      ResultSet rs = stmt.executeQuery();
	      while ( rs.next() ) {
	    	  jobRecord = new JobPosting();
	          jobRecord.setJobId(rs.getInt("ID"));
	          jobRecord.setJobName(rs.getString("JOBNAME"));
	          jobRecord.setClosingDate(rs.getString("CLOSEDATE"));
	          jobRecord.setSalary(rs.getInt("SALARY"));
	          jobRecord.setPosition(rs.getString("POSITIONTYPE"));
	          jobRecord.setLocation(rs.getString("LOCATION"));
	          jobRecord.setDescription(rs.getString("DESCRIPTION"));
	          jobRecord.setStatus(rs.getString("STATUS"));
	          
	          jobList.add(jobRecord);
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
	public void archiveJob(int jobID) {
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
		    
			stmt = c.prepareStatement("INSERT INTO ARCHIVEDJOBS (JOBNAME, CLOSEDATE, " +
			    	"SALARY, POSITIONTYPE, LOCATION, DESCRIPTION, STATUS) " +
			    	"VALUES (?,?,?,?,?,?,?);"); 
		    stmt.setString(1, job.getJobName());
		    stmt.setString(2, job.getClosingDate());
		    stmt.setInt(3, job.getSalary());
		    stmt.setString(4, job.getPosition());
		    stmt.setString(5, job.getLocation());
		    stmt.setString(6, job.getDescription());
		    stmt.setString(7, job.getStatus());
			stmt.executeUpdate();
			
			c.commit();
			
			stmt = c.prepareStatement("DELETE FROM JOBPOSTINGS WHERE ID = ? "); 
		    stmt.setInt(1, job.getJobId());
		    stmt.executeUpdate();
			c.commit();
			
			stmt.close();
		    c.close();
	    } catch ( Exception e ) {
	    	
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }

	}
	
	@Override
	public JobPosting getArchivedJobById (int jobID) {
		Connection c = null;
		PreparedStatement stmt = null;
		JobPosting job = null;
	    try {
	    	Class.forName("org.sqlite.JDBC");
	    	c = DriverManager.getConnection("jdbc:sqlite:rest.db");
	    	c.setAutoCommit(false);
		    System.out.println("Opened database successfully");
		    
		    stmt = c.prepareStatement("SELECT * FROM ARCHIVEDJOBS WHERE ID = ? "); 
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
	public List<JobPosting> getAllArchivedJobs() {
		Connection c = null;
		Statement stmt = null;
		List<JobPosting> jobList = new ArrayList<JobPosting>();
		JobPosting job = null;
		try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:rest.db");
	      c.setAutoCommit(false);
	      
	      stmt = c.createStatement();
	      ResultSet rs = stmt.executeQuery( "SELECT * FROM ARCHIVEDJOBS;" );
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

}
