package org.turnera.core.message;

import java.io.Serializable;

public class BaseTemplate implements Serializable {

	private static final long serialVersionUID = -1168570023074673954L;
	private String action;
	private int statusCode;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
}
