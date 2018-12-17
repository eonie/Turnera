package org.turnera.server.schdule.cron;

import org.springframework.scheduling.support.CronSequenceGenerator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Test {
	private static void testCronAlg(Map<String, String> map) throws Exception {
		int count = 0;
		for (Map.Entry<String, String> entry : map.entrySet()) {
			System.out.println(++count);
			System.out.println("cron = "+entry.getKey());
			System.out.println("date = "+entry.getValue());
			CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator(entry.getKey());
			CustomCronSequenceGenerator customCronSequenceGenerator = new CustomCronSequenceGenerator(entry.getKey());
			QuartzCronExpression quartzCronExpression = new QuartzCronExpression(entry.getKey());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = sdf.parse(entry.getValue());

			long nanoTime1 = System.nanoTime();
			Date date1 = null;
			try {
				date1 = cronSequenceGenerator.next(date);
			} catch (Exception e) {
			}
			long nanoTime2 = System.nanoTime();
			String str1 = null;
			if (date1 != null) {
				str1 = sdf.format(date1);
			}
			System.out.println("old method : result date = " + str1
					+ " , consume " + (nanoTime2 - nanoTime1)/1000 + "us");


			long nanoTime3 = System.nanoTime();
			Date date2 = null;
			try {
				date2 = customCronSequenceGenerator.next(date);
			} catch (Exception e) {
				e.printStackTrace();
			}
			long nanoTime4 = System.nanoTime();
			String str2 = null;
			if (date2 != null) {
				str2 = sdf.format(date2);
			}
			System.out.println("new method : result date = " + str2
					+ " , consume " + (nanoTime4 - nanoTime3)/1000 + "us");

			long nanoTime5 = System.nanoTime();
			Date date3 = null;
			try {
				date3 = quartzCronExpression.getNextValidTimeAfter(date);
			} catch (Exception e) {
				e.printStackTrace();
			}
			long nanoTime6 = System.nanoTime();
			String str3 = null;
			if (date3 != null) {
				str3 = sdf.format(date3);
			}
			System.out.println("new method : result date = " + str3
					+ " , consume " + (nanoTime6 - nanoTime5)/1000 + "us");
		}
	}

	public static void main(String[] args) throws Exception {
		Map<String, String> map = new HashMap<>();
		map.put("0 0 8 * * ?", "2011-03-25 13:22:43");
		map.put("0/2 1 * * * ?", "2016-12-25 18:00:45");
		map.put("0 0/5 14,18 * * ?", "2016-01-29 04:01:12");
		map.put("0 15 10 ? * MON-FRI", "2022-08-31 23:59:59");
		map.put("0 26,29,33 * * * ?", "2013-09-12 03:04:05");
		map.put("10-20/4 10,44,30/2 10 ? 3 WED", "1999-10-18 12:00:00");
		map.put("0 0 0 1/2 MAR-AUG ?", "2008-09-11 19:19:19");
		map.put("0 10-50/3,57-59 * ? * WED-FRI", "2003-02-09 06:17:19");
		map.put("0/2 0 1 29 2 ? ", "2016-05-23 09:13:53");
		map.put("0 10,44 14 ? 3 WED", "2016-12-28 19:01:35");
		testCronAlg(map);
	}
}
