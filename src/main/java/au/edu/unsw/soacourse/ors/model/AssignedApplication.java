package au.edu.unsw.soacourse.ors.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AssignedApplication {
	private int appId;
	private String department;
	
	public int getAppId() {
		return appId;
	}
	public void setAppId(int appId) {
		this.appId = appId;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	
}
