package au.edu.unsw.soacourse.ors.dao;

import java.util.List;

import au.edu.unsw.soacourse.ors.model.RegisteredUser;

public interface RegisteredUsersDAO {
	
	List<RegisteredUser> getUsers();
	
	List<RegisteredUser> getUsersbyDepartment(String department);
	
	boolean checkLogin(String uid, String password);

	RegisteredUser getUsersbyShortKey(String shortKey);
}
