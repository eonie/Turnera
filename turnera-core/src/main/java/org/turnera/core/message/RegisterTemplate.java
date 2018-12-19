package org.turnera.core.message;

import java.util.List;

public class RegisterTemplate extends BaseTemplate{
	private String action = MessageType.REGISTER;
	private String host;
	private int port;
	private List<JobInfo> jobInfos;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public List<JobInfo> getJobInfos() {
		return jobInfos;
	}

	public void setJobInfos(List<JobInfo> jobInfos) {
		this.jobInfos = jobInfos;
	}
}
