package au.edu.unsw.soacourse.ors;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.annotation.Resource;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import au.edu.unsw.soacourse.ors.dao.support.ApplicationsDAOImpl;
import au.edu.unsw.soacourse.ors.dao.support.AutoCheckDAOImpl;
import au.edu.unsw.soacourse.ors.dao.support.RegisteredUsersDAOImpl;
import au.edu.unsw.soacourse.ors.model.Application;
import au.edu.unsw.soacourse.ors.model.AutoCheck;
import au.edu.unsw.soacourse.ors.model.RegisteredUser;

@Path("/AutoCheck")
public class AutoCheckService {
	// Allows to insert contextual objects into the class, 
	// e.g. ServletContext, Request, Response, UriInfo
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	@Resource    
	private WebServiceContext wsCtxt;
	
	// Runs the autocheck bpel service using the persons details from the application
	@PUT
	@Path("check/{appId}")
	@Produces(MediaType.TEXT_PLAIN)
	public String autoCheckApplication(@HeaderParam("ShortKey") String shortkey, @PathParam("appId") String appIdString, @Context HttpServletRequest httpRequest) {
		
		RegisteredUsersDAOImpl userDAO = new RegisteredUsersDAOImpl();
		RegisteredUser user = userDAO.getUsersbyShortKey(shortkey);
		if (user != null && user.getRole().equals("reviewer")) {
			int appId = Integer.parseInt(appIdString);
	
			AutoCheckDAOImpl autoCheckDAO = new AutoCheckDAOImpl();		
			AutoCheck autocheck = new AutoCheck();
			ApplicationsDAOImpl appsDAO = new ApplicationsDAOImpl();
			Application app = appsDAO.getApplicationByID(appId);
			
			autocheck = autoCheckDAO.getByAppID(appId);
			
			if (autocheck != null) {
				return null;
			} 
			//TODO: Run the bpel service with the details here
			// If app has been checked before return nothing but check  again to update if it has changed
			// if it hasnt, check it and return 
			String result = runAutoCheck(app, httpRequest);
			//Assuming bpel is done now
			System.out.println(result);
			autocheck = new AutoCheck();
			autocheck.setResult(result);
			autocheck.setAppId(appId);
			
			int autoCheckId = autoCheckDAO.createAutoCheck(autocheck);
			return "The created job is available at: " 
						+ uriInfo.getBaseUri().toASCIIString()
						+ "AutoCheck/" + autoCheckId;

		} else {
			return "Must be a manager to initiate autocheck";
		}
	}
	
	// Runs the autocheck bpel service using the persons details from the application
	@PUT
	@Path("{appId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String autoCheckApplication(@PathParam("appId") String appIdString,
			AutoCheck newAutoCheck,
			HttpServletRequest httpRequest) {
		int appId = Integer.parseInt(appIdString);

		AutoCheckDAOImpl autoCheckDAO = new AutoCheckDAOImpl();		
		AutoCheck autocheck = new AutoCheck();
		autocheck = autoCheckDAO.getByAppID(appId);
		
		if (autocheck.getResult() != null) {
			newAutoCheck.setAppId(appId);
			autoCheckDAO.updateAutocheck(newAutoCheck);
			return null;
		} else {
			
			ApplicationsDAOImpl appsDAO = new ApplicationsDAOImpl();
			Application app = appsDAO.getApplicationByID(appId);
			
			//TODO: Run the bpel service with the details here
			// If app has been checked before return nothing but check  again to update if it has changed
			// if it hasnt, check it and return 
			String result = runAutoCheck(app, httpRequest);
			//Assuming bpel is done now
			System.out.println(result);
			autocheck = new AutoCheck();
			autocheck.setResult(result);
			autocheck.setAppId(appId);
			
			int autoCheckId = autoCheckDAO.createAutoCheck(autocheck);
			return "The created job is available at: " 
						+ uriInfo.getBaseUri().toASCIIString()
						+ "AutoCheck/" + autoCheckId;
		}
	}
	
	public String runAutoCheck(Application app, HttpServletRequest request) {

	    String hostName = request.getServerName();
	    int port = request.getServerPort();
		String result = null;
		
	    try {
			SOAPConnectionFactory sfc = SOAPConnectionFactory.newInstance();
			SOAPConnection connection = sfc.createConnection();
			
			MessageFactory mf = MessageFactory.newInstance();
			SOAPMessage sm1 = mf.createMessage();
			SOAPMessage sm2 = mf.createMessage();
			
			SOAPBody sb1 = sm1.getSOAPBody();
			SOAPBody sb2 = sm2.getSOAPBody();
			
			QName bodyName1 = new QName("http://validation.soacourse.unsw.edu.au","PDVCheck", "tns");
			SOAPBodyElement bodyElement1 = sb1.addBodyElement(bodyName1);
			QName driversLicence = new QName("driverslicence");
			SOAPElement driversLicenceElem1 = bodyElement1.addChildElement(driversLicence);
			driversLicenceElem1.removeNamespaceDeclaration("tns");
			
			QName bodyName2 = new QName("http://validation.soacourse.unsw.edu.au","CRVCheck", "tns");
			SOAPBodyElement bodyElement2 = sb2.addBodyElement(bodyName2);
			SOAPElement driversLicenceElem2 = bodyElement2.addChildElement(driversLicence);
			driversLicenceElem2.removeNamespaceDeclaration("tns");
			
			QName name = new QName("name");
			SOAPElement nameElem = bodyElement1.addChildElement(name);
			nameElem.removeNamespaceDeclaration("tns");
			
			QName postCode = new QName("postcode");
			SOAPElement postCodeElem = bodyElement1.addChildElement(postCode);
			postCodeElem.removeNamespaceDeclaration("tns");
			
			driversLicenceElem1.addTextNode(Integer.toString(app.getDriversLicence()));
			nameElem.addTextNode(app.getFirstName() + " " + app.getLastName());
			postCodeElem.addTextNode(Integer.toString(app.getPostcode()));
			
			driversLicenceElem2.addTextNode(Integer.toString(app.getDriversLicence()));
			
			System.out.println("\n Soap Request:\n");
		    sm1.writeTo(System.out);
		    System.out.println();
		    										
		    URL endpoint = new URL("http://" + hostName + ":" + port + "/ValidationService/ValidationServices");
			SOAPMessage response = connection.call(sm1, endpoint);
			SOAPMessage response2 = connection.call(sm2, endpoint);
			
			connection.close();
			response.writeTo(System.out);
			System.out.println();
			
			SOAPBody respBody = response.getSOAPBody();
			SOAPBody respBody2 = response2.getSOAPBody();
			
			System.out.println(respBody.toString());
			
			
			result = respBody.getFirstChild().getFirstChild().getTextContent() + "\n" +
					respBody2.getFirstChild().getFirstChild().getTextContent();

		} catch (Exception e1) {
			e1.printStackTrace();
			System.out.println("Exception in the soap message");
		}
	    
		return result;
	}
	
	@GET
	@Path("{autoCheckId}")
	@Produces(MediaType.APPLICATION_JSON)
	public AutoCheck getByAutoCheckID(@PathParam("autoCheckId") String autoCheckIdString) {
		int autoCheckId = Integer.parseInt(autoCheckIdString);
		
		AutoCheckDAOImpl autoCheckDAO = new AutoCheckDAOImpl();
		AutoCheck autocheck = autoCheckDAO.getByAutoCheckID(autoCheckId);
				
		return autocheck;
	}
	
	@GET
	@Path("byappid/{appId}")
	@Produces(MediaType.APPLICATION_JSON)
	public AutoCheck getByAppID(@PathParam("appId") String appIdString) {
		int appId = Integer.parseInt(appIdString);
		
		AutoCheckDAOImpl autoCheckDAO = new AutoCheckDAOImpl();
		AutoCheck autocheck = autoCheckDAO.getByAutoCheckID(appId);
				
		return autocheck;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<AutoCheck> getAllAutoChecks() {
		AutoCheckDAOImpl autoCheckDAO = new AutoCheckDAOImpl();
		List<AutoCheck> autocheckList = autoCheckDAO.getAllAutoChecks();
				
		return autocheckList;
	}
	
}
