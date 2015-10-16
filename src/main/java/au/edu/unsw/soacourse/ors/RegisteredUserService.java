package au.edu.unsw.soacourse.ors;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import au.edu.unsw.soacourse.ors.model.*;
import au.edu.unsw.soacourse.ors.service.*;

@Path("/RegisteredUser")
public class RegisteredUserService {

	@Autowired
	private RegisteredUserDAO registeredUserDAO;
	
	@GET
	@Path("/size")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCount() {
		int size = registeredUserDAO.getUsers().size();
		return String.valueOf(size);
	}
	
	/*
	public List<RegisteredUser> getUsers() {
		List<RegisteredUser> users = new ArrayList<RegisteredUser>();
		users.addAll(RegisteredUserDAO.instance.getUsers().values());
		return users;
	}
	*/
	
	
	
}
