package org.turnera.server.schdule;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class SchduleConfig {


	@Bean
	public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
	    ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
	    threadPoolTaskScheduler.setPoolSize(100);
	    threadPoolTaskScheduler.setThreadPriority(5);
	    threadPoolTaskScheduler.setRemoveOnCancelPolicy(true);
		return new ThreadPoolTaskScheduler();
	}

//	public static void main(String[] args) {
//		String cron = "0 1/10 * * * * 2019/3";
//		System.out.println(QuartzStyleCronSequenceGenerator.isValidExpression(cron));
//		QuartzStyleCronSequenceGenerator sequenceGenerator = new QuartzStyleCronSequenceGenerator(cron);
//		ZoneId zone = ZoneId.systemDefault();
//		Instant instant = LocalDateTime.parse("2019-01-01T18:02:47", DateTimeFormatter.ISO_LOCAL_DATE_TIME).atZone(zone).toInstant();
//		try {
//			System.out.println(CronParserUtil.getDescription(cron));
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		System.out.println(sequenceGenerator.next(Date.from(instant)));
//	}
}
