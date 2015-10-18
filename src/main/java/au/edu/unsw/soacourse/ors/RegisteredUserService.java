package au.edu.unsw.soacourse.ors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import au.edu.unsw.soacourse.ors.dao.support.JobsDAOImpl;
import au.edu.unsw.soacourse.ors.dao.support.RegisteredUsersDAOImpl;
import au.edu.unsw.soacourse.ors.model.*;

@Path("/RegisteredUser")
public class RegisteredUserService {
	
	@GET
	@Path("setUpDatabase")
	public void setUpDatabase() {
		RegisteredUsersDAOImpl regUsersDAO = new RegisteredUsersDAOImpl();
		regUsersDAO.setUpDatabase();
	}
	
	/*
	@GET
	@Path("/users")
	@produces(MediaType.APPLICATION_JSON)
	public List<RegisteredUser> searchUsersByTeam
	*/
	
	@GET
	@Path("size")
	@Produces(MediaType.TEXT_PLAIN)
	public int getCount() {
		RegisteredUsersDAOImpl regUsersDAO = new RegisteredUsersDAOImpl();
		int size = regUsersDAO.getUsers().size();
		return size;
	}
	
	//helper method to get a list of all the registered users SHOULD PROBABLY DELETE THIS LATER
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<RegisteredUser> getAllUsers() throws IOException {
		RegisteredUsersDAOImpl regUsersDAO = new RegisteredUsersDAOImpl();
		
		List<RegisteredUser> userList = regUsersDAO.getUsers();
		return userList;
	}
	
	
	
}
