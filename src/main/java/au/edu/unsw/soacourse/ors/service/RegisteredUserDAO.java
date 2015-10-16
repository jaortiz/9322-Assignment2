package au.edu.unsw.soacourse.ors.service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import au.edu.unsw.soacourse.ors.model.*;

public enum RegisteredUserDAO {
	instance;
	
	//Map key pair <uid, registeredUserObject>
	private Map<String,RegisteredUser> registeredUsersMap = new HashMap<String,RegisteredUser>();
	
	//Initialisation
	private RegisteredUserDAO() {
		createRegisteredUsers();
	}
	
	
	//Populates the registered users map with the xml contents
	private void createRegisteredUsers() {
		
		NodeList nodes = readRegisteredUsers();	//get the nodes from the xml file i.e. all the entry nodes
		
		for(int i = 0;i < nodes.getLength();i++) {
			Node node = nodes.item(i);
			
			if(node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				RegisteredUser user = new RegisteredUser();
				
				user.setuId(element.getElementsByTagName("uid").item(0).getTextContent());
				user.setPassword(element.getElementsByTagName("pwd").item(0).getTextContent());
				user.setShortKey(element.getElementsByTagName("ShortKey").item(0).getTextContent());
				
				user.setLastName(element.getElementsByTagName("LastName").item(0).getTextContent());
				user.setFirstName(element.getElementsByTagName("FirstName").item(0).getTextContent());
				user.setRole(element.getElementsByTagName("Role").item(0).getTextContent());
				user.setDepartment(element.getElementsByTagName("Department").item(0).getTextContent());
				
				registeredUsersMap.put(element.getElementsByTagName("uid").item(0).getTextContent(), user);
				
			}
		}
		
		//return registeredUsersMap;
	}
	
	
	//Read the xml and return the entry nodes of the xml
	private NodeList readRegisteredUsers() {
		
		ClassLoader cl = this.getClass().getClassLoader();
		NodeList nodes = null;
		InputStream registeredUsersXML = cl.getResourceAsStream("RegisteredUsers.xml");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(registeredUsersXML);
			nodes = doc.getElementsByTagName("Entry");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return nodes;
		
	}
	
	
	//Getter 
	public Map<String,RegisteredUser> getUsers() {
		return registeredUsersMap;
	}
}
