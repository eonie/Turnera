package org.turnera.client.annotations;

@TurneraJob(cron = "0 * * * * ?")
public class DemoJob {
	public void execute(String name){
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>" + name);
	}
}
