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
import au.edu.unsw.soacourse.ors.dao.support.JobsDAOImpl;
import au.edu.unsw.soacourse.ors.model.Application;
import au.edu.unsw.soacourse.ors.model.AutoCheck;
import au.edu.unsw.soacourse.ors.model.JobPosting;

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
	@Path("{appId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String autoCheckApplication(@PathParam("appId") String appIdString, AutoCheck newAutoCheck) {
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
			String result = "This should be the bpel";
			//Assuming bpel is done now
			
			autocheck = new AutoCheck();
			autocheck.setResult(result);
			autocheck.setAppId(appId);
			
			int autoCheckId = autoCheckDAO.createAutoCheck(autocheck);
			return "The created job is available at: " 
						+ uriInfo.getBaseUri().toASCIIString()
						+ "AutoCheck/" + autoCheckId;
		}
	}
	
	public String runAutoCheck(Application app) {
		
	    MessageContext msgCtxt = wsCtxt.getMessageContext();
	    HttpServletRequest request =
	        (HttpServletRequest)msgCtxt.get(MessageContext.SERVLET_REQUEST);

	    String hostName = request.getServerName();
	    int port = request.getServerPort();
		String result = null;
		
	    try {
			SOAPConnectionFactory sfc = SOAPConnectionFactory.newInstance();
			SOAPConnection connection = sfc.createConnection();
			
			MessageFactory mf = MessageFactory.newInstance();
			SOAPMessage sm = mf.createMessage();
			
			SOAPBody sb = sm.getSOAPBody();
			
			QName bodyName = new QName(" http://validation.soacourse.unsw.edu.au/","PDVCheck");
			SOAPBodyElement bodyElement = sb.addBodyElement(bodyName);
			QName driversLicence = new QName("driverslicence");
			SOAPElement driversLicenceElem = bodyElement.addChildElement(driversLicence);
			QName name = new QName("name");
			SOAPElement nameElem = bodyElement.addChildElement(name);
			QName postCode = new QName("postcode");
			SOAPElement postCodeElem = bodyElement.addChildElement(postCode);
			
			driversLicenceElem.addTextNode(Integer.toString(app.getDriversLicence()));
			nameElem.addTextNode(app.getFirstName() + " " + app.getLastName());
			postCodeElem.addTextNode(Integer.toString(app.getPostcode()));
			
			System.out.println("\n Soap Request:\n");
		    sm.writeTo(System.out);
		    System.out.println();
		    										
		    URL endpoint = new URL("http://" + hostName + ":" + port + "/ValidationService/ValidationServices");
			SOAPMessage response = connection.call(sm, endpoint);
			
			connection.close();
			response.writeTo(System.out);
			System.out.println();
			
			SOAPBody respBody = response.getSOAPBody();
			
			System.out.println(respBody.toString());
			
			
			result = respBody.getFirstChild().getFirstChild().getTextContent();

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
