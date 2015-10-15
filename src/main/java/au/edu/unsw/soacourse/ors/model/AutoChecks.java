package au.edu.unsw.soacourse.ors.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AutoChecks {
	private String autoCheckId;
	private String appId;
	
	private String result;
	private String details;
	
	public String getAutoCheckId() {
		return autoCheckId;
	}
	public void setAutoCheckId(String autoCheckId) {
		this.autoCheckId = autoCheckId;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	
	
}
