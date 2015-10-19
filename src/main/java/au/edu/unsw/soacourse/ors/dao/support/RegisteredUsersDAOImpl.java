package au.edu.unsw.soacourse.ors.dao.support;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import au.edu.unsw.soacourse.ors.dao.RegisteredUsersDAO;
import au.edu.unsw.soacourse.ors.model.Application;
import au.edu.unsw.soacourse.ors.model.RegisteredUser;

public class RegisteredUsersDAOImpl implements RegisteredUsersDAO {


	public void setUpDatabase () {
		Connection c = null;
	    Statement stmt = null;
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:rest.db");
	      System.out.println("Opened database successfully");

	      stmt = c.createStatement();
	      String sql = "CREATE TABLE IF NOT EXISTS REGISTEREDUSERS " +
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
	      
	      stmt.close();
	      c.close();
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    
	    populateUsersDB();
	    
	}
	
	
	
	public void populateUsersDB () {
		Connection c = null;
		PreparedStatement stmt = null;
		NodeList nodes = readRegisteredUsers();	//get the nodes from the xml file i.e. all the entry nodes
		
		System.out.println("No Nodes:" + nodes.getLength());
		try {
			
			Class.forName("org.sqlite.JDBC");
	    	c = DriverManager.getConnection("jdbc:sqlite:rest.db");
	    	c.setAutoCommit(false);
		    System.out.println("Opened database successfully");
		    
		    for(int i = 0;i < nodes.getLength();i++) {
				Node node = nodes.item(i);
				
				if(node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					
					try {
						
					    stmt = c.prepareStatement("INSERT INTO REGISTEREDUSERS (UID, PASSWORD, " +
						    	"SHORTKEY, LASTNAME, FIRSTNAME, ROLE, DEPARTMENT) " +
						    	"VALUES (?,?,?,?,?,?,?);"); 
					    
					    Element loginElement = (Element) element.getElementsByTagName("Login").item(0);
					    Element detailElement = (Element) element.getElementsByTagName("Details").item(0);
					    
					    stmt.setString(1, loginElement.getElementsByTagName("_uid").item(0).getTextContent());
						stmt.setString(2, loginElement.getElementsByTagName("_pwd").item(0).getTextContent());
						stmt.setString(3, loginElement.getElementsByTagName("ShortKey").item(0).getTextContent());
						
						stmt.setString(4, detailElement.getElementsByTagName("LastName").item(0).getTextContent());
						stmt.setString(5, detailElement.getElementsByTagName("FirstName").item(0).getTextContent());
						stmt.setString(6, detailElement.getElementsByTagName("Role").item(0).getTextContent());
						stmt.setString(7, detailElement.getElementsByTagName("Department").item(0).getTextContent());
						
						stmt.executeUpdate();
					    c.commit();
					} catch(Exception e) {
						
						e.printStackTrace();
						
					}
				} 
		
			}
		    c.close();
		} catch(Exception e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		
	}

	@Override
	public List<RegisteredUser> getUsers() {
		Connection c = null;
		Statement stmt = null;
		List<RegisteredUser> userList = new ArrayList<RegisteredUser>();
		RegisteredUser user = null;
		try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:rest.db");
	      c.setAutoCommit(false);
	      
	      stmt = c.createStatement();
	      ResultSet rs = stmt.executeQuery( "SELECT * FROM REGISTEREDUSERS;" );
	      while ( rs.next() ) {
	    	  user = new RegisteredUser();
	    	  user.setuId(rs.getString("UID"));
	    	  user.setPassword(rs.getString("PASSWORD"));
	    	  user.setShortKey(rs.getString("SHORTKEY"));
	    	  user.setLastName(rs.getString("LASTNAME"));
	    	  user.setFirstName(rs.getString("FIRSTNAME"));
	    	  user.setRole(rs.getString("ROLE"));
	    	  user.setDepartment(rs.getString("DEPARTMENT"));
	    	  
	    	  userList.add(user);
	      }
	      rs.close() ;
	      stmt.close();
	      c.close();

	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
		
	    return userList;
	}

	//Read the xml and return the entry nodes of the xml
	private NodeList readRegisteredUsers() {

		//InputSource xmlFile = new InputSource(System.getProperty("catalina.home") + File.separator + 
			//	"webapps" + File.separator + "ROOT" + File.separator + "resources" + File.separator + "RegisteredUsers.xml");
		ClassLoader cl = this.getClass().getClassLoader();
		NodeList nodes = null;
		InputStream xmlFile = cl.getResourceAsStream("RegisteredUsers.xml");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(xmlFile);
			nodes = doc.getElementsByTagName("Entry");
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return nodes;
		
	}



	@Override
	public List<RegisteredUser> getUsersbyDepartment(String department) {
		Connection c = null;
		PreparedStatement stmt = null;
		List<RegisteredUser> userList = new ArrayList<RegisteredUser>();
		RegisteredUser user = null;
		try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:rest.db");
	      c.setAutoCommit(false);
	     
	      
	      stmt = c.prepareStatement("SELECT * FROM REGISTEREDUSERS WHERE DEPARTMENT LIKE ?"); 
	      stmt.setString(1, "%" + department + "%");	//Have % wildcard here since prepated statement didnt like it above...
		  ResultSet rs = stmt.executeQuery();
	      
	      while ( rs.next() ) {
	    	  user = new RegisteredUser();
	    	  user.setuId(rs.getString("UID"));
	    	  user.setPassword(rs.getString("PASSWORD"));
	    	  user.setShortKey(rs.getString("SHORTKEY"));
	    	  user.setLastName(rs.getString("LASTNAME"));
	    	  user.setFirstName(rs.getString("FIRSTNAME"));
	    	  user.setRole(rs.getString("ROLE"));
	    	  user.setDepartment(rs.getString("DEPARTMENT"));
	    	  
	    	  userList.add(user);
	      }
	      rs.close() ;
	      stmt.close();
	      c.close();

	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
		
	    return userList;
	}

	@Override
	public RegisteredUser getUsersbyShortKey(String shortKey) {
		Connection c = null;
		PreparedStatement stmt = null;
		RegisteredUser user = null;
		try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:rest.db");
	      c.setAutoCommit(false);
	     
	      
	      stmt = c.prepareStatement("SELECT * FROM REGISTEREDUSERS WHERE SHORTKEY = ?"); 
	      stmt.setString(1, shortKey );	
		  ResultSet rs = stmt.executeQuery();
	      
	      if(rs.next() ){
	    	  user = new RegisteredUser();
	    	  user.setuId(rs.getString("UID"));
	    	  user.setPassword(rs.getString("PASSWORD"));
	    	  user.setShortKey(rs.getString("SHORTKEY"));
	    	  user.setLastName(rs.getString("LASTNAME"));
	    	  user.setFirstName(rs.getString("FIRSTNAME"));
	    	  user.setRole(rs.getString("ROLE"));
	    	  user.setDepartment(rs.getString("DEPARTMENT"));
	    	  
	      }
	      rs.close() ;
	      stmt.close();
	      c.close();

	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
		
	    return user;
	}


	@Override
	public boolean checkLogin(String uid, String password) {
		Connection c = null;
		PreparedStatement stmt = null;
		boolean verified = false;
	    try {
	    	Class.forName("org.sqlite.JDBC");
	    	c = DriverManager.getConnection("jdbc:sqlite:rest.db");
	    	c.setAutoCommit(false);
		    System.out.println("Opened database successfully");
		    
		    stmt = c.prepareStatement("SELECT * FROM REGISTEREDUSERS WHERE UID = ? "); 
		    stmt.setString(1, uid);
		    ResultSet rs = stmt.executeQuery();
		    
		    if(rs.next()) {	//if result set is not empty
		    	String pass = rs.getString("PASSWORD");
		    	if(pass.equals(password)) {
		    		verified = true;
		    	}
		    } 
		    
		    rs.close();
		    stmt.close();
		    c.close();
	    } catch ( Exception e ) {
	    	
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    return verified;
	}
}
