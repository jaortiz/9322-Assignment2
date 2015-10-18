package au.edu.unsw.soacourse.ors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import au.edu.unsw.soacourse.ors.dao.support.JobsDAOImpl;
import au.edu.unsw.soacourse.ors.dao.support.RegisteredUsersDAOImpl;
import au.edu.unsw.soacourse.ors.model.*;

@Path("/RegisteredUser")
public class RegisteredUserService {
	
	@GET
	@Path("setUpDatabase")
	public Response setUpDatabase() {
		RegisteredUsersDAOImpl regUsersDAO = new RegisteredUsersDAOImpl();
		regUsersDAO.setUpDatabase();
		return Response.ok().build();
	}
	
	
	@GET
	@Path("/users")
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchUsersByTeam(@QueryParam("hireTeam")String hireTeam) {
		RegisteredUsersDAOImpl regUsersDAO = new RegisteredUsersDAOImpl();
		List<RegisteredUser> userList = regUsersDAO.getUsersbyDepartment(hireTeam);
		return Response.ok().entity(userList).build();
	}
	
	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	public Response loginAuthentication(@FormParam("uid")String uid, @FormParam("password") String password) {
		RegisteredUsersDAOImpl regUsersDAO = new RegisteredUsersDAOImpl();
		boolean verified = regUsersDAO.checkLogin(uid, password);
		return Response.ok().entity(verified).build();
	}
	
	//DELETE LATER
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
	public Response getAllUsers() throws IOException {
		RegisteredUsersDAOImpl regUsersDAO = new RegisteredUsersDAOImpl();
		
		List<RegisteredUser> userList = regUsersDAO.getUsers();
		return Response.ok().entity(userList).build();
	}
	
	
	
}
