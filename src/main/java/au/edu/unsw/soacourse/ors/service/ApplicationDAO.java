package au.edu.unsw.soacourse.ors.service;

import java.util.ArrayList;
import java.util.List;

import au.edu.unsw.soacourse.ors.model.*;

public class ApplicationDAO {
	private List<Application> applications = new ArrayList<Application>();
	
	public void addApplication(Application application) {
		applications.add(application);
		System.out.println("application Added");
		System.out.println("Application size: " + applications.size());
	}
	
	public Application getApplication(String appID) {
		System.out.println("looking for appID:" + appID);
		for(int i = 0; i < applications.size();i++) {
			if(applications.get(i).getAppId().equalsIgnoreCase(appID)) {
				return applications.get(i);
			}
		}
		
		return null;
	}
	
}
