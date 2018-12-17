package org.turnera.server.schdule;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.turnera.server.schdule.cron.CronParserUtil;
import org.turnera.server.schdule.cron.QuartzStyleCronSequenceGenerator;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Configuration
public class SchduleConfig {

	@Bean
	public TaskExecutor taskExecutor(){
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(5);
		taskExecutor.setMaxPoolSize(10);
		taskExecutor.setQueueCapacity(25);
		return taskExecutor;
	}
	@Bean
	public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
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
