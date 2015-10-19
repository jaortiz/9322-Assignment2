package au.edu.unsw.soacourse.ors.dao.support;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import au.edu.unsw.soacourse.ors.dao.ReviewDAO;
import au.edu.unsw.soacourse.ors.model.Application;
import au.edu.unsw.soacourse.ors.model.JobPosting;
import au.edu.unsw.soacourse.ors.model.Review;

public class ReviewsDAOImpl implements ReviewDAO{

	public void setUpDatabase() {
		Connection c = null;
	    Statement stmt = null;
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:rest.db");
	      System.out.println("Opened database successfully");

	      stmt = c.createStatement();
	      String sql = "CREATE TABLE IF NOT EXISTS REVIEWS " +
	                   "(ID INTEGER PRIMARY KEY	AUTOINCREMENT NOT NULL," +
	                   " APPID           	INTEGER    NOT NULL, " + 
	                   " UID				TEXT, " + 
	                   " COMMENTS			TEXT, " +
	                   " DECISION				TEXT)"; 
	      stmt.executeUpdate(sql);
	      System.out.println("Reviews Table created successfully");
	      
	      stmt.close();
	      c.close();
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	}
	
	@Override
	public int createReview(Review review) {
		Connection c = null;
		PreparedStatement stmt = null;
	    try {
	    	Class.forName("org.sqlite.JDBC");
	    	c = DriverManager.getConnection("jdbc:sqlite:rest.db");
	    	c.setAutoCommit(false);
		    System.out.println("Opened database successfully");
		    
		    stmt = c.prepareStatement("INSERT INTO REVIEWS (APPID, UID, " +
			    	"COMMENTS, DECISION) " +
			    	"VALUES (?,?,?,?);");
		    
		    stmt.setInt(1, review.getAppId());
		    stmt.setString(2, review.getuId());
		    stmt.setString(3, review.getComments());
		    stmt.setString(4, review.getDecision());
		    stmt.executeUpdate();
		    c.commit();
		    
		    c.close();
	    } catch ( Exception e ) {
	    	
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    System.out.println("Records created successfully");
	    return 0;
	}

	
	@Override
	public Review getReviewByID(int rID) {
		Connection c = null;
		PreparedStatement stmt = null;
		Review review = null;
	    try {
	    	Class.forName("org.sqlite.JDBC");
	    	c = DriverManager.getConnection("jdbc:sqlite:rest.db");
	    	c.setAutoCommit(false);
		    System.out.println("Opened database successfully");
		    
		    stmt = c.prepareStatement("SELECT * FROM REVIEWS WHERE ID = ? "); 
		    stmt.setInt(1, rID);
		    ResultSet rs = stmt.executeQuery();
		    
		    if (rs.next()) {
			    review = new Review();
			    review.setReviewId(rs.getInt("ID"));
			    review.setAppId(rs.getInt("APPID"));
			    review.setuId(rs.getString("UID"));
			    review.setComments(rs.getString("COMMENTS"));;
			    review.setDecision(rs.getString("DECISION"));
		    }
		    
		    rs.close();
		    stmt.close();
		    c.close();
	    } catch ( Exception e ) {
	    	
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    System.out.println("Records created successfully");
	    return review;
	}

	@Override
	public List<Review> getAllReviews() {
		Connection c = null;
		Statement stmt = null;
		List<Review> reviewList = new ArrayList<Review>();
		Review review = null;
		try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:rest.db");
	      c.setAutoCommit(false);
	      
	      stmt = c.createStatement();
	      ResultSet rs = stmt.executeQuery( "SELECT * FROM REVIEWS;" );
	      while ( rs.next() ) {
	    	  
	    	  review = new Review();
	    	  review.setReviewId(rs.getInt("ID"));
	    	  review.setAppId(rs.getInt("APPID"));
	    	  review.setuId(rs.getString("UID"));
	    	  review.setComments(rs.getString("COMMENTS"));
	    	  review.setDecision(rs.getString("DECISION"));
	    	  reviewList.add(review);
	      }
	      rs.close() ;
	      stmt.close();
	      c.close();

	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
		
	    return reviewList;
	}
	

}
