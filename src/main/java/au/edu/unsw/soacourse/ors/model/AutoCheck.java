package au.edu.unsw.soacourse.ors.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AutoCheck {
	private int autoCheckId;
	private int appId;
	
	private String result;
	
	public int getAutoCheckId() {
		return autoCheckId;
	}
	public void setAutoCheckId(int autoCheckId) {
		this.autoCheckId = autoCheckId;
	}
	public int getAppId() {
		return appId;
	}
	public void setAppId(int appId) {
		this.appId = appId;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
}
