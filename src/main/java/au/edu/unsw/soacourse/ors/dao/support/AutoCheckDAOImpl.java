package au.edu.unsw.soacourse.ors.dao.support;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import au.edu.unsw.soacourse.ors.dao.AutoCheckDAO;
import au.edu.unsw.soacourse.ors.model.AutoCheck;

public class AutoCheckDAOImpl implements AutoCheckDAO {

	public void setUpDatabase() {
		Connection c = null;
	    Statement stmt = null;
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:rest.db");
	      System.out.println("Opened database successfully");

	      stmt = c.createStatement();
	      String sql = "CREATE TABLE IF NOT EXISTS AUTOCHECK " +
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
	
	@Override
	public int createAutoCheck(AutoCheck autocheck) {
		Connection c = null;
		PreparedStatement stmt = null;
	    try {
	    	Class.forName("org.sqlite.JDBC");
	    	c = DriverManager.getConnection("jdbc:sqlite:rest.db");
	    	c.setAutoCommit(false);
		    System.out.println("Opened database successfully");
		    
		    stmt = c.prepareStatement("INSERT INTO AUTOCHECK (APPID, RESULTS) " +
			    	"VALUES (?,?);"); 
		    stmt.setInt(1, autocheck.getAppId());
		    stmt.setString(2, autocheck.getResult());
		    stmt.executeUpdate();
		  
		    c.commit();
		    
		    c.close();
	    } catch ( Exception e ) {
	    	
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    System.out.println("Records created successfully");
	    return lastAutoCheck();
	}
	
	public int lastAutoCheck() {
		Connection c = null;
		Statement stmt = null;
		int lastAutoCheckId = 0;
		
		try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:rest.db");
	      c.setAutoCommit(false);
	      
	      stmt = c.createStatement();
	      ResultSet rs = stmt.executeQuery( "SELECT * FROM AUTOCHECK WHERE ID = (SELECT MAX(ID) FROM AUTOCHECK);" );
	      if(rs.next()) {
	    	  lastAutoCheckId = rs.getInt("ID") ;
	      }
	      rs.close() ;
	      
	      c.commit();
	      c.close();

	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
		
	    return lastAutoCheckId;
	}
	
	public int countAutoChecks() {
		Connection c = null;
		Statement stmt = null;
		int count = 0;
		
		try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:rest.db");
	      c.setAutoCommit(false);
	      
	      stmt = c.createStatement();
	      ResultSet rs = stmt.executeQuery( "SELECT COUNT(*) AS rowcount FROM AUTOCHECK;" );
	      if (rs.next()) {
	    	  count = rs.getInt("rowcount") ;
	      }
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
	public AutoCheck getByAutoCheckID(int autoCheckId) {
		Connection c = null;
		PreparedStatement stmt = null;
		AutoCheck autoCheck = null;
	    try {
	    	Class.forName("org.sqlite.JDBC");
	    	c = DriverManager.getConnection("jdbc:sqlite:rest.db");
	    	c.setAutoCommit(false);
		    System.out.println("Opened database successfully");
		    
		    stmt = c.prepareStatement("SELECT * FROM AUTOCHECK WHERE ID = ? "); 
		    stmt.setInt(1, autoCheckId);
		    ResultSet rs = stmt.executeQuery();
		    autoCheck = new AutoCheck();
		    if(rs.next()) {
		    	
				autoCheck.setAutoCheckId(rs.getInt("ID"));
				autoCheck.setAppId(rs.getInt("APPID"));
				autoCheck.setResult(rs.getString("RESULTS"));
		    }    
		    rs.close();
		    stmt.close();
		    c.close();
	    } catch ( Exception e ) {
	    	
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    return autoCheck;
	}

	@Override
	public AutoCheck getByAppID(int appId) {
		Connection c = null;
		PreparedStatement stmt = null;
		AutoCheck autoCheck = null;
	    try {
	    	Class.forName("org.sqlite.JDBC");
	    	c = DriverManager.getConnection("jdbc:sqlite:rest.db");
	    	c.setAutoCommit(false);
		    System.out.println("Opened database successfully");
		    
		    stmt = c.prepareStatement("SELECT * FROM AUTOCHECK WHERE APPID = ? "); 
		    stmt.setInt(1, appId);
		    ResultSet rs = stmt.executeQuery();
		    
		    if(rs.next()) {
		    	autoCheck = new AutoCheck();
		    	autoCheck.setAutoCheckId(rs.getInt("ID"));
		    	autoCheck.setAppId(rs.getInt("APPID"));
		    	autoCheck.setResult(rs.getString("RESULTS"));
		    }
		    rs.close();
		    stmt.close();
		    c.close();
	    } catch ( Exception e ) {
	    	
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    return autoCheck;
	}

	@Override
	public List<AutoCheck> getAllAutoChecks() {
		Connection c = null;
		Statement stmt = null;
		List<AutoCheck> autoCheckList = new ArrayList<AutoCheck>();
		AutoCheck autoCheck = null;
		try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:rest.db");
	      c.setAutoCommit(false);
	      
	      stmt = c.createStatement();
	      ResultSet rs = stmt.executeQuery( "SELECT * FROM AUTOCHECK;" );
	      while ( rs.next() ) {
	    	  autoCheck = new AutoCheck();
	    	  autoCheck.setAutoCheckId(rs.getInt("ID"));
	    	  autoCheck.setAppId(rs.getInt("APPID"));
	    	  autoCheck.setResult(rs.getString("RESULTS"));
	          
	          autoCheckList.add(autoCheck);
	      }
	      rs.close() ;
	      stmt.close();
	      c.close();

	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
		
	    return autoCheckList;
	}

	@Override
	public void updateAutocheck(AutoCheck autocheck) {
		AutoCheck updatedAutoCheck = getByAppID(autocheck.getAppId());
		if(autocheck.getResult() != null && !autocheck.getResult().equals(updatedAutoCheck.getResult())) {
			updatedAutoCheck.setResult(autocheck.getResult());
		}
		
		Connection c = null;
		PreparedStatement stmt = null;
	    try {
	    	Class.forName("org.sqlite.JDBC");
	    	c = DriverManager.getConnection("jdbc:sqlite:rest.db");
	    	c.setAutoCommit(false);
		    System.out.println("Opened database successfully");
		    
		    stmt = c.prepareStatement("UPDATE AUTOCHECK "
		    		+ "SET RESULTS = ?"
		    		+ "WHERE APPID = ? "); 
		    stmt.setString(1, updatedAutoCheck.getResult());
		    stmt.setInt(2, updatedAutoCheck.getAppId());
		    
		    stmt.executeUpdate();
		    c.commit();
		    
		    stmt.close();
		    c.close();
	    } catch ( Exception e ) {
	    	
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    System.out.println("Records created successfully");

	}

}
