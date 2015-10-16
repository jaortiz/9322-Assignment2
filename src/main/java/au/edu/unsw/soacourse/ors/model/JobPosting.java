package au.edu.unsw.soacourse.ors.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class JobPosting {
	
	private int jobId;
	private String jobName;
	private String closeDate;
	private int salaryRate;
	private String positionType;
	private String location;
	private String description;
	private String status;
	
	public int getJobId() {
		return jobId;
	}
	
	public void setJobId(int jobId) {
		this.jobId = jobId;
	}
	
	public String getJobName() {
		return jobName;
	}
	
	public void setJobName (String jobName) {
		this.jobName = jobName;
	}
	
	public String getCloseDate() {
		return closeDate;
	}
	
	public void setCloseDate(String closeDate) {
		this.closeDate = closeDate;
	}
	
	public int getSalaryRate() {
		return salaryRate;
	}
	
	public void setSalaryRate(int salaryRate) {
		this.salaryRate = salaryRate;
	}
	
	public String getPositionType() {
		return positionType;
	}
	
	public void setPositionType(String positionType) {
		this.positionType = positionType;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
	
}
