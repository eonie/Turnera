package org.turnera.server.schdule.cron;

import org.springframework.util.StringUtils;

import java.util.*;

public class QuartzStyleCronSequenceGenerator {

	private final String expression;

	private final TimeZone timeZone;

	private final BitSet years = new BitSet(2099);

	private final BitSet months = new BitSet(12);

	private final BitSet daysOfMonth = new BitSet(31);

	private final BitSet daysOfWeek = new BitSet(7);

	private final BitSet hours = new BitSet(24);

	private final BitSet minutes = new BitSet(60);

	private final BitSet seconds = new BitSet(60);


	public QuartzStyleCronSequenceGenerator(String expression) {
		this(expression, TimeZone.getDefault());
	}

	public QuartzStyleCronSequenceGenerator(String expression, TimeZone timeZone) {
		this.expression = expression;
		this.timeZone = timeZone;
		parse(expression);
	}

	private QuartzStyleCronSequenceGenerator(String expression, String[] fields) {
		this.expression = expression;
		this.timeZone = null;
		doParse(fields);
	}


	/**
	 * Return the cron pattern that this sequence generator has been built for.
	 */
	String getExpression() {
		return this.expression;
	}


	/**
	 * Get the next {@link Date} in the sequence matching the Cron pattern and
	 * after the value provided. The return value will have a whole number of
	 * seconds, and will be after the input value.
	 * @param date a seed value
	 * @return the next value matching the pattern
	 */
	public Date next(Date date) {
		/*
		The plan:

		1 Start with whole second (rounding up if necessary)

		2 If seconds match move on, otherwise find the next match:
		2.1 If next match is in the next minute then roll forwards

		3 If minute matches move on, otherwise find the next match
		3.1 If next match is in the next hour then roll forwards
		3.2 Reset the seconds and go to 2

		4 If hour matches move on, otherwise find the next match
		4.1 If next match is in the next day then roll forwards,
		4.2 Reset the minutes and seconds and go to 2
		*/

		Calendar calendar = new GregorianCalendar();
		calendar.setTimeZone(this.timeZone);
		calendar.setTime(date);

		// First, just reset the milliseconds and try to calculate from there...
		calendar.set(Calendar.MILLISECOND, 0);
		long originalTimestamp = calendar.getTimeInMillis();
		doNext(calendar);

		if (calendar.getTimeInMillis() == originalTimestamp) {
			// We arrived at the original timestamp - round up to the next whole second and try again...
			calendar.add(Calendar.SECOND, 1);
			doNext(calendar);
		}

		return calendar.getTime();
	}
	//从calendar开始寻找下一个匹配cron表达式的时间
	private void doNext(Calendar calendar) {
		//calendar中比当前更高的域是否调整过
		boolean changed = false;
		List<Integer> fields = null;
		if(StringUtils.tokenizeToStringArray(expression, " ").length == 7){
			fields = Arrays.asList(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH,
					Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND);
		} else {
			fields = Arrays.asList(Calendar.MONTH, Calendar.DAY_OF_MONTH,
					Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND);
		}

		//依次调整年，月，日，时，分，秒
		for (int field : fields) {
			if (changed) {
				calendar.set(field, field == Calendar.DAY_OF_MONTH ? 1 : 0);
			}
			if (!checkField(calendar, field)) {
				changed = true;
				findNext(calendar, field);
			}
		}
	}

	//检查某个域是否匹配cron表达式
	private boolean checkField(Calendar calendar, int field) {
		switch (field) {
			case Calendar.YEAR: {
				int year = calendar.get(Calendar.YEAR);
				return this.years.get(year);
			}
			case Calendar.MONTH: {
				int month = calendar.get(Calendar.MONTH);
				return this.months.get(month);
			}
			case Calendar.DAY_OF_MONTH: {
				int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
				int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
				return this.daysOfMonth.get(dayOfMonth) && this.daysOfWeek.get(dayOfWeek);
			}
			case Calendar.HOUR_OF_DAY: {
				int hour = calendar.get(Calendar.HOUR_OF_DAY);
				return this.hours.get(hour);
			}
			case Calendar.MINUTE: {
				int minute = calendar.get(Calendar.MINUTE);
				return this.minutes.get(minute);
			}
			case Calendar.SECOND: {
				int second = calendar.get(Calendar.SECOND);
				return this.seconds.get(second);
			}
			default:
				return true;
		}
	}

	//调整某个域到下一个匹配值，使其符合cron表达式
	private void findNext(Calendar calendar, int field) {
		switch (field) {
			case Calendar.YEAR: {
				if (calendar.get(Calendar.YEAR) > 2099) {
					throw new IllegalArgumentException("year exceeds 2099!");
				}
				int year = calendar.get(Calendar.YEAR);
				int nextYear = this.years.nextSetBit(year);
				if (nextYear == -1) {
					calendar.add(Calendar.YEAR, 1);
					calendar.set(Calendar.MONTH, 0);
					nextYear = this.years.nextSetBit(1970);
				}
				if (nextYear != year) {
					calendar.set(Calendar.YEAR, nextYear);
				}
				break;
			}
			case Calendar.MONTH: {
				if (calendar.get(Calendar.YEAR) > 2099) {
					throw new IllegalArgumentException("year exceeds 2099!");
				}
				int month = calendar.get(Calendar.MONTH);
				int nextMonth = this.months.nextSetBit(month);
				if (nextMonth == -1) {
					calendar.add(Calendar.YEAR, 1);
					calendar.set(Calendar.MONTH, 0);
					nextMonth = this.months.nextSetBit(0);
				}
				if (nextMonth != month) {
					calendar.set(Calendar.MONTH, nextMonth);
				}
				break;
			}
			case Calendar.DAY_OF_MONTH: {
				while (!this.daysOfMonth.get(calendar.get(Calendar.DAY_OF_MONTH))
						|| !this.daysOfWeek.get(calendar.get(Calendar.DAY_OF_WEEK) - 1)) {
					int max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
					int nextDayOfMonth = this.daysOfMonth.nextSetBit(calendar.get(Calendar.DAY_OF_MONTH) + 1);
					if (nextDayOfMonth == -1 || nextDayOfMonth > max) {
						calendar.add(Calendar.MONTH, 1);
						findNext(calendar, Calendar.MONTH);
						calendar.set(Calendar.DAY_OF_MONTH, 1);
					} else {
						calendar.set(Calendar.DAY_OF_MONTH, nextDayOfMonth);
					}
				}
				break;
			}
			case Calendar.HOUR_OF_DAY: {
				int hour = calendar.get(Calendar.HOUR_OF_DAY);
				int nextHour = this.hours.nextSetBit(hour);
				if (nextHour == -1) {
					calendar.add(Calendar.DAY_OF_MONTH, 1);
					findNext(calendar, Calendar.DAY_OF_MONTH);
					calendar.set(Calendar.HOUR_OF_DAY, 0);
					nextHour = this.hours.nextSetBit(0);
				}
				if (nextHour != hour) {
					calendar.set(Calendar.HOUR_OF_DAY, nextHour);
				}
				break;
			}
			case Calendar.MINUTE: {
				int minute = calendar.get(Calendar.MINUTE);
				int nextMinute = this.minutes.nextSetBit(minute);
				if (nextMinute == -1) {
					calendar.add(Calendar.HOUR_OF_DAY, 1);
					findNext(calendar, Calendar.HOUR_OF_DAY);
					calendar.set(Calendar.MINUTE, 0);
					nextMinute = this.minutes.nextSetBit(0);
				}
				if (nextMinute != minute) {
					calendar.set(Calendar.MINUTE, nextMinute);
				}
				break;
			}
			case Calendar.SECOND: {
				int second = calendar.get(Calendar.SECOND);
				int nextSecond = this.seconds.nextSetBit(second);
				if (nextSecond == -1) {
					calendar.add(Calendar.MINUTE, 1);
					findNext(calendar, Calendar.MINUTE);
					calendar.set(Calendar.SECOND, 0);
					nextSecond = this.seconds.nextSetBit(0);
				}
				if (nextSecond != second) {
					calendar.set(Calendar.SECOND, nextSecond);
				}
				break;
			}
		}
	}

	// Parsing logic invoked by the constructor

	/**
	 * Parse the given pattern expression.
	 */
	private void parse(String expression) throws IllegalArgumentException {
		String[] fields = StringUtils.tokenizeToStringArray(expression, " ");
		if (!areValidCronFields(fields)) {
			throw new IllegalArgumentException(String.format(
					"Cron expression must consist of 6 or 7 fields (found %d in \"%s\")", fields.length, expression));
		}
		doParse(fields);
	}

	private void doParse(String[] fields) {
		setNumberHits(this.seconds, fields[0], 0, 60);
		setNumberHits(this.minutes, fields[1], 0, 60);
		setNumberHits(this.hours, fields[2], 0, 24);
		setDaysOfMonth(this.daysOfMonth, fields[3]);
		setMonths(this.months, fields[4]);
		setDays(this.daysOfWeek, replaceOrdinals(fields[5], "SUN,MON,TUE,WED,THU,FRI,SAT"), 8);
		if(fields.length == 7) {
			setNumberHits(this.years, fields[6],1970, 2099);
		}
		if (this.daysOfWeek.get(7)) {
			// Sunday can be represented as 0 or 7
			this.daysOfWeek.set(0);
			this.daysOfWeek.clear(7);
		}
	}

	/**
	 * Replace the values in the comma-separated list (case insensitive)
	 * with their index in the list.
	 * @return a new String with the values from the list replaced
	 */
	private String replaceOrdinals(String value, String commaSeparatedList) {
		String[] list = StringUtils.commaDelimitedListToStringArray(commaSeparatedList);
		for (int i = 0; i < list.length; i++) {
			String item = list[i].toUpperCase();
			value = StringUtils.replace(value.toUpperCase(), item, "" + i);
		}
		return value;
	}

	private void setDaysOfMonth(BitSet bits, String field) {
		int max = 31;
		// Days of month start with 1 (in Cron and Calendar) so add one
		setDays(bits, field, max + 1);
		// ... and remove it from the front
		bits.clear(0);
	}

	private void setDays(BitSet bits, String field, int max) {
		if (field.contains("?")) {
			field = "*";
		}
		setNumberHits(bits, field, 0, max);
	}

	private void setMonths(BitSet bits, String value) {
		int max = 12;
		value = replaceOrdinals(value, "FOO,JAN,FEB,MAR,APR,MAY,JUN,JUL,AUG,SEP,OCT,NOV,DEC");
		BitSet months = new BitSet(13);
		// Months start with 1 in Cron and 0 in Calendar, so push the values first into a longer bit set
		setNumberHits(months, value, 1, max + 1);
		// ... and then rotate it to the front of the months
		for (int i = 1; i <= max; i++) {
			if (months.get(i)) {
				bits.set(i - 1);
			}
		}
	}

	private void setNumberHits(BitSet bits, String value, int min, int max) {
		String[] fields = StringUtils.delimitedListToStringArray(value, ",");
		for (String field : fields) {
			if (!field.contains("/")) {
				// Not an incrementer so it must be a range (possibly empty)
				int[] range = getRange(field, min, max);
				bits.set(range[0], range[1] + 1);
			}
			else {
				String[] split = StringUtils.delimitedListToStringArray(field, "/");
				if (split.length > 2) {
					throw new IllegalArgumentException("Incrementer has more than two fields: '" +
							field + "' in expression \"" + this.expression + "\"");
				}
				int[] range = getRange(split[0], min, max);
				if (!split[0].contains("-")) {
					range[1] = max - 1;
				}
				int delta = Integer.parseInt(split[1]);
				if (delta <= 0) {
					throw new IllegalArgumentException("Incrementer delta must be 1 or higher: '" +
							field + "' in expression \"" + this.expression + "\"");
				}
				for (int i = range[0]; i <= range[1]; i += delta) {
					bits.set(i);
				}
			}
		}
	}

	private int[] getRange(String field, int min, int max) {
		int[] result = new int[2];
		if (field.contains("*")) {
			result[0] = min;
			result[1] = max - 1;
			return result;
		}
		if (!field.contains("-")) {
			result[0] = result[1] = Integer.valueOf(field);
		}
		else {
			String[] split = StringUtils.delimitedListToStringArray(field, "-");
			if (split.length > 2) {
				throw new IllegalArgumentException("Range has more than two fields: '" +
						field + "' in expression \"" + this.expression + "\"");
			}
			result[0] = Integer.valueOf(split[0]);
			result[1] = Integer.valueOf(split[1]);
		}
		if (result[0] >= max || result[1] >= max) {
			throw new IllegalArgumentException("Range exceeds maximum (" + max + "): '" +
					field + "' in expression \"" + this.expression + "\"");
		}
		if (result[0] < min || result[1] < min) {
			throw new IllegalArgumentException("Range less than minimum (" + min + "): '" +
					field + "' in expression \"" + this.expression + "\"");
		}
		if (result[0] > result[1]) {
			throw new IllegalArgumentException("Invalid inverted range: '" + field +
					"' in expression \"" + this.expression + "\"");
		}
		return result;
	}


	/**
	 * Determine whether the specified expression represents a valid cron pattern.
	 * @param expression the expression to evaluate
	 * @return {@code true} if the given expression is a valid cron expression
	 * @since 4.3
	 */
	public static boolean isValidExpression(String expression) {
		if (expression == null) {
			return false;
		}
		String[] fields = StringUtils.tokenizeToStringArray(expression, " ");
		if (!areValidCronFields(fields)) {
			return false;
		}
		try {
			new QuartzStyleCronSequenceGenerator(expression, fields);
			return true;
		}
		catch (IllegalArgumentException ex) {
			return false;
		}
	}

	private static boolean areValidCronFields(String[] fields) {
		return (fields != null && (fields.length == 6 || fields.length ==7));
	}


	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof QuartzStyleCronSequenceGenerator)) {
			return false;
		}
		QuartzStyleCronSequenceGenerator otherCron = (QuartzStyleCronSequenceGenerator) other;
		return (this.months.equals(otherCron.months) && this.daysOfMonth.equals(otherCron.daysOfMonth) &&
				this.daysOfWeek.equals(otherCron.daysOfWeek) && this.hours.equals(otherCron.hours) &&
				this.minutes.equals(otherCron.minutes) && this.seconds.equals(otherCron.seconds));
	}

	@Override
	public int hashCode() {
		return (17 * this.months.hashCode() + 29 * this.daysOfMonth.hashCode() + 37 * this.daysOfWeek.hashCode() +
				41 * this.hours.hashCode() + 53 * this.minutes.hashCode() + 61 * this.seconds.hashCode());
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + ": " + this.expression;
	}

}
