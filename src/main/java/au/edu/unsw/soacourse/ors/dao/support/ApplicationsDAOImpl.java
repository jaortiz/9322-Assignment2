package au.edu.unsw.soacourse.ors.dao.support;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import au.edu.unsw.soacourse.ors.dao.*;
import au.edu.unsw.soacourse.ors.model.Application;
import au.edu.unsw.soacourse.ors.model.JobPosting;

public class ApplicationsDAOImpl implements ApplicationsDAO {

	public void setUpDatabase() {
		Connection c = null;
	    Statement stmt = null;
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:rest.db");
	      System.out.println("Opened database successfully");

	      stmt = c.createStatement();
	      String sql = "CREATE TABLE IF NOT EXISTS APPLICATIONS " +
	                   "(ID INTEGER PRIMARY KEY	AUTOINCREMENT NOT NULL," +
	                   " JOBID					INTEGER		NOT NULL," +
	                   " FIRSTNAME  			TEXT, " + 
	                   " LASTNAME				TEXT, " + 
	                   " DRIVERSLICENSE			INTEGER, " +
	                   " EMAIL					TEXT, " + 
	                   " PHONENUMBER			TEXT, " +
	                   " COVERLETTER			TEXT, " + 
	                   " RESUME					TEXT, "	+ 
	                   " STATUS					TEXT)"; 
	      stmt.executeUpdate(sql);
	      System.out.println("Applications Table created successfully");
	      
	      sql = "CREATE TABLE IF NOT EXISTS ARCHIVEDAPPLICATIONS " +
                  "(ID INTEGER PRIMARY KEY	AUTOINCREMENT NOT NULL," +
                  " JOBID					INTEGER		NOT NULL," +
                  " FIRSTNAME  			TEXT, " + 
                  " LASTNAME				TEXT, " + 
                  " DRIVERSLICENSE			INTEGER, " +			
                  " EMAIL					TEXT, " + 
                  " PHONENUMBER			TEXT, " +
                  " COVERLETTER			TEXT, " + 
                  " RESUME					TEXT, "	+ 
                  " STATUS					TEXT)"; 
	      stmt.executeUpdate(sql);
	      System.out.println("Archived applications Table created successfully");
	      stmt.close();
	      c.close();
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    
	}
	
	@Override
	public int createApplication(Application application) {
		Connection c = null;
		PreparedStatement stmt = null;
	    try {
	    	Class.forName("org.sqlite.JDBC");
	    	c = DriverManager.getConnection("jdbc:sqlite:rest.db");
	    	c.setAutoCommit(false);
		    System.out.println("Opened database successfully");
		    
		    stmt = c.prepareStatement("INSERT INTO APPLICATIONS (JOBID, FIRSTNAME, " +
			    	"LASTNAME, DRIVERSLICENSE, EMAIL, PHONENUMBER, COVERLETTER, RESUME, STATUS) " +
			    	"VALUES (?,?,?,?,?,?,?,?,?);"); 
		    stmt.setInt(1, application.getJobId());
		    stmt.setString(2, application.getFirstName());
		    stmt.setString(3, application.getLastName());
		    stmt.setInt(4, application.getDriversLicence());
		    stmt.setString(5, application.getEmail());
		    stmt.setString(6, application.getPhoneNumber());
		    stmt.setString(7, application.getCoverLetter());
		    stmt.setString(8, application.getResume());
		    stmt.setString(9, "created");
		    stmt.executeUpdate();
		  
		    c.commit();
		    
		    c.close();
	    } catch ( Exception e ) {
	    	
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    System.out.println("Records created successfully");
	    return lastApplication();
	}

	public int lastApplication() {
		Connection c = null;
		Statement stmt = null;
		int lastAppId = 0;
		
		try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:rest.db");
	      c.setAutoCommit(false);
	      
	      stmt = c.createStatement();
	      ResultSet rs = stmt.executeQuery( "SELECT * FROM APPLICATIONSS WHERE ID = (SELECT MAX(ID) FROM APPLICATIONS);" );
	      rs.next();
	      lastAppId = rs.getInt("ID") ;
	      rs.close() ;
	      
	      c.commit();
	      c.close();

	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
		
	    return lastAppId;
	}
	
	@Override
	public Application getApplicationByID(int appID) {
		Connection c = null;
		PreparedStatement stmt = null;
		Application application = null;
	    try {
	    	Class.forName("org.sqlite.JDBC");
	    	c = DriverManager.getConnection("jdbc:sqlite:rest.db");
	    	c.setAutoCommit(false);
		    System.out.println("Opened database successfully");
		    
		    stmt = c.prepareStatement("SELECT * FROM APPLICATIONS WHERE ID = ? "); 
		    stmt.setInt(1, appID);
		    ResultSet rs = stmt.executeQuery();
		    
		    rs.next();
		    
	    	application = new Application();
	    	application.setAppId(rs.getInt("ID"));
	    	application.setJobId(rs.getInt("JOBID"));
	    	application.setFirstName(rs.getString("FIRSTNAME"));
	    	application.setLastName(rs.getString("LASTNAME"));
	    	application.setDriversLicence(rs.getInt("DRIVERSLICENSE"));
	    	application.setEmail(rs.getString("EMAIL"));
	    	application.setPhoneNumber(rs.getString("PHONENUMBER"));
	    	application.setCoverLetter(rs.getString("COVERLETTER"));
	    	application.setResume(rs.getString("RESUME"));
	    	application.setStatus(rs.getString("STATUS"));
		    
		    rs.close();
		    stmt.close();
		    c.close();
	    } catch ( Exception e ) {
	    	
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    System.out.println("Records created successfully");
	    return application;
	}

	@Override
	public void updateApplicationByID(int appID, Application application) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Application> getAllApplications() {
		Connection c = null;
		Statement stmt = null;
		List<Application> appList = new ArrayList<Application>();
		Application application = null;
		try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:rest.db");
	      c.setAutoCommit(false);
	      
	      stmt = c.createStatement();
	      ResultSet rs = stmt.executeQuery( "SELECT * FROM APPLICATIONS;" );
	      while ( rs.next() ) {
    	  	application = new Application();
			application.setAppId(rs.getInt("ID"));
			application.setJobId(rs.getInt("JOBID"));
			application.setFirstName(rs.getString("FIRSTNAME"));
			application.setLastName(rs.getString("LASTNAME"));
			application.setDriversLicence(rs.getInt("DRIVERSLICENSE"));
			application.setEmail(rs.getString("EMAIL"));
			application.setPhoneNumber(rs.getString("PHONENUMBER"));
			application.setCoverLetter(rs.getString("COVERLETTER"));
			application.setResume(rs.getString("RESUME"));
			application.setStatus(rs.getString("STATUS"));
	          
	          appList.add(application);
	      }
	      rs.close() ;
	      stmt.close();
	      c.close();

	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
		
	    return appList;
	}

	@Override
	public void archiveApplication(int appID) {
		Connection c = null;
		PreparedStatement stmt = null;
		Application application = null;
	    try {
	    	Class.forName("org.sqlite.JDBC");
	    	c = DriverManager.getConnection("jdbc:sqlite:rest.db");
	    	c.setAutoCommit(false);
		    System.out.println("Opened database successfully");
		    
		    stmt = c.prepareStatement("SELECT * FROM APPLICATIONS WHERE ID = ? "); 
		    stmt.setInt(1, appID);
		    ResultSet rs = stmt.executeQuery();
		    
		    rs.next();
		    application = new Application();
			application.setAppId(rs.getInt("ID"));
			application.setJobId(rs.getInt("JOBID"));
			application.setFirstName(rs.getString("FIRSNAME"));
			application.setLastName(rs.getString("LASTNAME"));
			application.setDriversLicence(rs.getInt("DRIVERSLICENSE"));
			application.setEmail(rs.getString("EMAIL"));
			application.setPhoneNumber(rs.getString("PHONENUMBER"));
			application.setCoverLetter(rs.getString("COVERLETTER"));
			application.setResume(rs.getString("RESUME"));
			application.setStatus(rs.getString("STATUS"));
			rs.close();
		    
			stmt = c.prepareStatement("INSERT INTO ARCHIVEDAPPLICATIONS (JOBID, FIRSTNAME, " +
			    	"LASTNAME,DRIVERSLICENSE EMAIL, PHONENUMBER, COVERLETTER, RESUME, STATUS) " +
			    	"VALUES (?,?,?,?,?,?,?,?,?);"); 
			stmt.setInt(1, application.getJobId());
		    stmt.setString(2, application.getFirstName());
		    stmt.setString(3, application.getLastName());
		    stmt.setInt(4, application.getDriversLicence());
		    stmt.setString(5, application.getEmail());
		    stmt.setString(6, application.getPhoneNumber());
		    stmt.setString(7, application.getCoverLetter());
		    stmt.setString(8, application.getResume());
		    stmt.setString(9, application.getStatus());
		    stmt.executeUpdate();
			
			c.commit();
			
			stmt = c.prepareStatement("DELETE FROM APPLICATIONS WHERE ID = ? "); 
		    stmt.setInt(1, application.getAppId());
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
	public Application getArchivedApplicationById(int appId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Application> getAllArchivedApplications() {
		Connection c = null;
		Statement stmt = null;
		List<Application> appList = new ArrayList<Application>();
		Application application = null;
		try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:rest.db");
	      c.setAutoCommit(false);
	      
	      stmt = c.createStatement();
	      ResultSet rs = stmt.executeQuery( "SELECT * FROM ARCHIVEDAPPLICATIONS;" );
	      while ( rs.next() ) {
    	  	application = new Application();
			application.setAppId(rs.getInt("ID"));
			application.setJobId(rs.getInt("JOBID"));
			application.setFirstName(rs.getString("FIRSNAME"));
			application.setLastName(rs.getString("LASTNAME"));
			application.setDriversLicence(rs.getInt("DRIVERSLICENSE"));
			application.setEmail(rs.getString("EMAIL"));
			application.setPhoneNumber(rs.getString("PHONENUMBER"));
			application.setCoverLetter(rs.getString("COVERLETTER"));
			application.setResume(rs.getString("RESUME"));
			application.setStatus(rs.getString("STATUS"));
	          
			appList.add(application);
	      }
	      rs.close() ;
	      stmt.close();
	      c.close();

	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
		
	    return appList;
	}

	@Override
	public List<Application> getAllApplicationsByJobID(int jobID) {
		Connection c = null;
		PreparedStatement stmt = null;
		List<Application> appList = new ArrayList<Application>();
		Application application = null;
		try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:rest.db");
	      c.setAutoCommit(false);
	      
	      stmt = c.prepareStatement("SELECT * FROM APPLICATIONS WHERE JOBID = ?");
	      stmt.setInt(1, jobID);
	      ResultSet rs = stmt.executeQuery();
	      
	      while ( rs.next() ) {
    	  	application = new Application();
			application.setAppId(rs.getInt("ID"));
			application.setJobId(rs.getInt("JOBID"));
			application.setFirstName(rs.getString("FIRSTNAME"));
			application.setLastName(rs.getString("LASTNAME"));
			application.setDriversLicence(rs.getInt("DRIVERSLICENSE"));
			application.setEmail(rs.getString("EMAIL"));
			application.setPhoneNumber(rs.getString("PHONENUMBER"));
			application.setCoverLetter(rs.getString("COVERLETTER"));
			application.setResume(rs.getString("RESUME"));
			application.setStatus(rs.getString("STATUS"));
	          
	          appList.add(application);
	      }
	      rs.close() ;
	      stmt.close();
	      c.close();

	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
		
	    return appList;
	}

}
