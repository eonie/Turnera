package org.turnera.core.message;

public class ExecuteTemplate extends BaseTemplate{
	private String className;
	ExecuteTemplate(){
		super.setAction(MessageType.EXECUTE);
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
}
