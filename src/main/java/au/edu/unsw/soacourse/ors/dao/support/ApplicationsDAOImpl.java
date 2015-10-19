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
	                   " POSTCODE				INTEGER, " +			
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
                  " POSTCODE				INTEGER, " +	
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
			    	"LASTNAME, DRIVERSLICENSE, EMAIL, PHONENUMBER, POSTCODE, COVERLETTER, RESUME, STATUS) " +
			    	"VALUES (?,?,?,?,?,?,?,?,?,?);"); 
		    stmt.setInt(1, application.getJobId());
		    stmt.setString(2, application.getFirstName());
		    stmt.setString(3, application.getLastName());
		    stmt.setInt(4, application.getDriversLicence());
		    stmt.setString(5, application.getEmail());
		    stmt.setString(6, application.getPhoneNumber());
		    stmt.setInt(7, application.getPostcode());
		    stmt.setString(8, application.getCoverLetter());
		    stmt.setString(9, application.getResume());
		    stmt.setString(10, "created");
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
	      ResultSet rs = stmt.executeQuery( "SELECT * FROM APPLICATIONS WHERE ID = (SELECT MAX(ID) FROM APPLICATIONS);" );
	      if (rs.next()) {
	    	  lastAppId = rs.getInt("ID") ;
	      }
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
		    
		    if(rs.next()) {
			    
		    	application = new Application();
		    	application.setAppId(rs.getInt("ID"));
		    	application.setJobId(rs.getInt("JOBID"));
		    	application.setFirstName(rs.getString("FIRSTNAME"));
		    	application.setLastName(rs.getString("LASTNAME"));
		    	application.setDriversLicence(rs.getInt("DRIVERSLICENSE"));
		    	application.setEmail(rs.getString("EMAIL"));
		    	application.setPhoneNumber(rs.getString("PHONENUMBER"));
		    	application.setPostcode(rs.getInt("POSTCODE"));
		    	application.setCoverLetter(rs.getString("COVERLETTER"));
		    	application.setResume(rs.getString("RESUME"));
		    	application.setStatus(rs.getString("STATUS"));
		    }
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
	public Application updateApplicationByID(Application updatedApp) {
		Application application = getApplicationByID(updatedApp.getAppId());
		
		if(updatedApp.getFirstName() != null && !updatedApp.getFirstName().equals(application.getFirstName())) {
			application.setFirstName(updatedApp.getFirstName());
		}
		
		if(updatedApp.getLastName() != null && !updatedApp.getLastName().equals(application.getLastName())) {
			application.setLastName(updatedApp.getLastName());
		}
		
		if(updatedApp.getDriversLicence() != 0 && updatedApp.getDriversLicence() != application.getDriversLicence()) {
			application.setDriversLicence(updatedApp.getDriversLicence());
		}
		
		if(updatedApp.getEmail() != null && !updatedApp.getEmail().equals(application.getEmail())) {
			application.setEmail(updatedApp.getEmail());
		}
		
		if(updatedApp.getPhoneNumber() != null && !updatedApp.getPhoneNumber().equals(application.getPhoneNumber())) {
			application.setPhoneNumber(updatedApp.getPhoneNumber());
		}
		
		if(updatedApp.getPostcode() != 0 && updatedApp.getPostcode() != application.getPostcode()) {
			application.setPostcode(updatedApp.getPostcode());
		}
		
		if(updatedApp.getCoverLetter() != null && !updatedApp.getCoverLetter().equals(application.getCoverLetter())) {
			application.setCoverLetter(updatedApp.getCoverLetter());
		}
		
		if(updatedApp.getResume() != null && !updatedApp.getResume().equals(application.getResume())) {
			application.setResume(updatedApp.getResume());
		}
		
		
		if(updatedApp.getStatus() != null && !updatedApp.getStatus().equals(application.getStatus())) {
			application.setStatus(updatedApp.getStatus());
		}
		
		Connection c = null;
		PreparedStatement stmt = null;
	    try {
	    	Class.forName("org.sqlite.JDBC");
	    	c = DriverManager.getConnection("jdbc:sqlite:rest.db");
	    	c.setAutoCommit(false);
		    System.out.println("Opened database successfully");
		    
		    stmt = c.prepareStatement("UPDATE APPLICATIONS "
		    		+ "SET JOBID = ?, FIRSTNAME = ?, LASTNAME = ?, DRIVERSLICENSE = ?, EMAIL = ?, "
		    		+ "PHONENUMBER = ?, POSTCODE = ?, COVERLETTER = ?, RESUME = ?, STATUS = ?"
		    		+ "WHERE ID = ? "); 
		    
		    stmt.setInt(1, application.getJobId());
		    stmt.setString(2, application.getFirstName());
		    stmt.setString(3, application.getLastName());
		    stmt.setInt(4, application.getDriversLicence());
		    stmt.setString(5, application.getEmail());
		    stmt.setString(6, application.getPhoneNumber());
		    stmt.setInt(7, application.getPostcode());
		    stmt.setString(8, application.getCoverLetter());
		    stmt.setString(9, application.getResume());
		    stmt.setString(10, application.getStatus());
		    stmt.setInt(11, application.getAppId());
		     
		    stmt.executeUpdate();
		    c.commit();
		    
		    stmt = c.prepareStatement("SELECT * FROM APPLICATIONS WHERE ID = ? "); 
		    stmt.setInt(1, application.getAppId());
		    ResultSet rs = stmt.executeQuery();
		    
		    if (rs.next()){
			    application = new Application();
				application.setAppId(rs.getInt("ID"));
		    	application.setJobId(rs.getInt("JOBID"));
		    	application.setFirstName(rs.getString("FIRSTNAME"));
		    	application.setLastName(rs.getString("LASTNAME"));
		    	application.setDriversLicence(rs.getInt("DRIVERSLICENSE"));
		    	application.setEmail(rs.getString("EMAIL"));
		    	application.setPhoneNumber(rs.getString("PHONENUMBER"));
		    	application.setPostcode(rs.getInt("POSTCODE"));
		    	application.setCoverLetter(rs.getString("COVERLETTER"));
		    	application.setResume(rs.getString("RESUME"));
		    	application.setStatus(rs.getString("STATUS"));
		    }
		    
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
			application.setPostcode(rs.getInt("POSTCODE"));
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
		    
		    if(rs.next()) {
			    application = new Application();
				application.setAppId(rs.getInt("ID"));
				application.setJobId(rs.getInt("JOBID"));
				application.setFirstName(rs.getString("FIRSNAME"));
				application.setLastName(rs.getString("LASTNAME"));
				application.setDriversLicence(rs.getInt("DRIVERSLICENSE"));
				application.setEmail(rs.getString("EMAIL"));
				application.setPhoneNumber(rs.getString("PHONENUMBER"));
				application.setPostcode(rs.getInt("POSTCODE"));
				application.setCoverLetter(rs.getString("COVERLETTER"));
				application.setResume(rs.getString("RESUME"));
				application.setStatus(rs.getString("STATUS"));
		    }
			rs.close();
		    
			stmt = c.prepareStatement("INSERT INTO ARCHIVEDAPPLICATIONS (JOBID, FIRSTNAME, " +
			    	"LASTNAME,DRIVERSLICENSE EMAIL, PHONENUMBER, POSTCODE, COVERLETTER, RESUME, STATUS) " +
			    	"VALUES (?,?,?,?,?,?,?,?,?,?);"); 
			stmt.setInt(1, application.getJobId());
		    stmt.setString(2, application.getFirstName());
		    stmt.setString(3, application.getLastName());
		    stmt.setInt(4, application.getDriversLicence());
		    stmt.setString(5, application.getEmail());
		    stmt.setString(6, application.getPhoneNumber());
		    stmt.setInt(7, application.getPostcode());
		    stmt.setString(8, application.getCoverLetter());
		    stmt.setString(9, application.getResume());
		    stmt.setString(10, application.getStatus());
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
			application.setPostcode(rs.getInt("POSTCODE"));
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
			application.setPostcode(rs.getInt("POSTCODE"));
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
