package cn.edu.buaa.cngrid;

import java.util.List;

public class HPC {
	private String nodeName;
	private int activeUser;
	private int runJob;
	private int pendJob;
	private int runCore;
	private int pendCore;
	private List<String> userlist;
	
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public int getActiveUser() {
		return activeUser;
	}
	public void setActiveUser(int activeUser) {
		this.activeUser = activeUser;
	}
	public int getRunJob() {
		return runJob;
	}
	public void setRunJob(int runJob) {
		this.runJob = runJob;
	}
	public int getPendJob() {
		return pendJob;
	}
	public void setPendJob(int pendJob) {
		this.pendJob = pendJob;
	}
	public int getRunCore() {
		return runCore;
	}
	public void setRunCore(int runCore) {
		this.runCore = runCore;
	}
	public int getPendCore() {
		return pendCore;
	}
	public void setPendCore(int pendCore) {
		this.pendCore = pendCore;
	}
	public List<String> getUserlist() {
		return userlist;
	}
	public void setUserlist(List<String> userlist) {
		this.userlist = userlist;
	} 
}
