package org.turnera.server.schdule;

public class MessagePrinterTask implements Runnable {
	private String taskName ;
	public MessagePrinterTask(String taskName) {
		this.taskName = taskName;
	}

	@Override
	public void run() {
		System.out.println(this.taskName);
	}
}
