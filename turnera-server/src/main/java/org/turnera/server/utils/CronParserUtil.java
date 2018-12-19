package org.turnera.server.utils;

import net.redhogs.cronparser.CronExpressionDescriptor;
import org.springframework.context.i18n.LocaleContextHolder;

import java.text.ParseException;
import java.util.Locale;

public class CronParserUtil {
	/**
	 * @param cron           ：对应messages配置的key.
	 * @return string
	 */
	public static String getDescription(String cron ) throws ParseException {
		//这里使用比较方便的方法，不依赖request.
		Locale locale = LocaleContextHolder.getLocale();
		return CronExpressionDescriptor.getDescription(cron, locale);
	}
}