package org.turnera.core.message;

public class ResultTemplate extends BaseTemplate{
	private String message;
	ResultTemplate(){
		super.setAction(MessageType.RESULT);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
