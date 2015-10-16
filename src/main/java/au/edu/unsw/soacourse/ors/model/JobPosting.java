package au.edu.unsw.soacourse.ors.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class JobPosting {
	
	private int jobId;
	private String jobName;
	private String closingDate;
	private int salary;
	private String position;
	private String location;
	private String description;
	private String status;
	
	public JobPosting() {
	}
	
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
	
	public String getClosingDate() {
		return closingDate;
	}
	
	public void setClosingDate(String closingDate) {
		this.closingDate = closingDate;
	}
	
	public int getSalary() {
		return salary;
	}
	
	public void setSalary(int salary) {
		this.salary = salary;
	}
	
	public String getPosition() {
		return position;
	}
	
	public void setPosition(String positionType) {
		this.position = positionType;
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
