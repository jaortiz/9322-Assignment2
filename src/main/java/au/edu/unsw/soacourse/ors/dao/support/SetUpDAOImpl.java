package au.edu.unsw.soacourse.ors.dao.support;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class SetUpDAOImpl {
	public SetUpDAOImpl() {
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
	      
	      sql = "CREATE TABLE IF NOT EXISTS REGISTEREDUSERS " +
                  "(ID INTEGER PRIMARY KEY	AUTOINCREMENT NOT NULL," +
                  " UID					TEXT NOT NULL, " +				
                  " PASSWORD           	TEXT    NOT NULL, " + 
                  " SHORTKEY				TEXT, " + 
                  " LASTNAME				TEXT, " +
                  " FIRSTNAME				TEXT, " + 
                  " ROLE					TEXT, "	+ 
                  " DEPARTMENT				TEXT)"; 
	      stmt.executeUpdate(sql);
	      System.out.println("Registered Users Table created successfully");
	      
	      RegisteredUsersDAOImpl registeredUser = new RegisteredUsersDAOImpl();
	      registeredUser.populateUsersDB();
	      
	      sql = "CREATE TABLE IF NOT EXISTS REVIEWS " +
                  "(ID INTEGER PRIMARY KEY	AUTOINCREMENT NOT NULL," +
                  " APPID           	INTEGER    NOT NULL, " + 
                  " UID				TEXT, " + 
                  " COMMENTS			TEXT, " +
                  " DECISION				TEXT)"; 
	      stmt.executeUpdate(sql);
	      System.out.println("Reviews Table created successfully");
	      
	      sql = "CREATE TABLE IF NOT EXISTS APPLICATIONS " +
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
	      
	      sql = "CREATE TABLE IF NOT EXISTS ASSIGNEDAPPLICATIONS " +
		             "(APPID INTEGER PRIMARY KEY	UNIQUE NOT NULL," +
		             " DEPARTMENT					TEXT		NOT NULL)"; 
	      stmt.executeUpdate(sql);
	      System.out.println("Assigned applications Table created successfully");
	      
	      sql = "CREATE TABLE IF NOT EXISTS AUTOCHECK " +
                  "(ID INTEGER PRIMARY KEY	AUTOINCREMENT NOT NULL," +
                  " APPID					INTEGER UNIQUE NOT NULL," +
                  " RESULTS				TEXT)"; 
	      stmt.executeUpdate(sql);
	      System.out.println("AutoCheck Table created successfully");
	      
	      stmt.close();
	      c.close();
	    
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    
	}
}
