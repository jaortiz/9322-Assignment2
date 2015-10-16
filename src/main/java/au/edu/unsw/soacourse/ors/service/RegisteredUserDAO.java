package au.edu.unsw.soacourse.ors.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import au.edu.unsw.soacourse.ors.model.*;

public class RegisteredUserDAO {
	
private List<RegisteredUser> registeredUsers = new ArrayList<RegisteredUser>();
	
	public RegisteredUserDAO() {
		System.out.println("initialisng");
		registeredUsers = createRegisteredUsers();
	}
	
	private List<RegisteredUser> createRegisteredUsers() {
		
		NodeList nodes = readRegisteredUsers();	//get the nodes from the xml file i.e. all the entry nodes
		
		System.out.println("No Nodes:" + nodes.getLength());
		
		for(int i = 0;i < nodes.getLength();i++) {
			Node node = nodes.item(i);
			
			if(node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				try {
					RegisteredUser user = new RegisteredUser();
					
					Element loginElement = (Element) element.getElementsByTagName("Login").item(0);
					user.setuId(loginElement.getElementsByTagName("_uid").item(0).getTextContent());
					user.setPassword(loginElement.getElementsByTagName("_pwd").item(0).getTextContent());
					user.setShortKey(loginElement.getElementsByTagName("ShortKey").item(0).getTextContent());
					
					Element detailElement = (Element) element.getElementsByTagName("Details").item(0);
					user.setLastName(detailElement.getElementsByTagName("LastName").item(0).getTextContent());
					user.setFirstName(detailElement.getElementsByTagName("FirstName").item(0).getTextContent());
					user.setRole(detailElement.getElementsByTagName("Role").item(0).getTextContent());
					user.setDepartment(detailElement.getElementsByTagName("Department").item(0).getTextContent());
					
					registeredUsers.add(user);
					
				} catch(Exception e) {
					
					e.printStackTrace();
					
				}
			}
		}
		
		return registeredUsers;
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
	
	public List<RegisteredUser> getUsers() {
		return this.registeredUsers;
	}
}
