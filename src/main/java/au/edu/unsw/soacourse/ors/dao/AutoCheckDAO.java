package au.edu.unsw.soacourse.ors.dao;

import java.util.List;

import au.edu.unsw.soacourse.ors.model.AutoCheck;

public interface AutoCheckDAO {
	
	/**
	 * Creates a autocheck and return a URI to the new resource
	 * 
	 * @return
	 */
	int createAutoCheck(AutoCheck autocheck);
	
	
	/**
	 * Gets an autocheck by its autocheckid
	 * 
	 * @param autoCheckId
	 * @return
	 */
	AutoCheck getByAutoCheckID(int autoCheckId);
	
	/**
	 * Gets an autocheck by the appId
	 * @param appId
	 * @return
	 */
	AutoCheck getByAppID(int appId);
	
	/**
	 * Gets a list of all autoCheck
	 * 
	 * @return
	 */
	List<AutoCheck> getAllAutoChecks();
	
	void updateAutocheck (AutoCheck autocheck);

}
