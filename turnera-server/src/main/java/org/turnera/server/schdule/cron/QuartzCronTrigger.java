package org.turnera.server.schdule.cron;

import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;

import java.util.Date;

public class QuartzCronTrigger implements Trigger  {
    /**
     * Determine the next execution time according to the given trigger context.
     *
     * @param triggerContext context object encapsulating last execution times
     *                       and last completion time
     * @return the next execution time as defined by the trigger,
     * or {@code null} if the trigger won't fire anymore
     */
    private  final QuartzCronExpression quartzCronExpression;

    /**
     * Return the cron pattern that this trigger has been built with.
     */
    public String getExpression() {
        return this.quartzCronExpression.getCronExpression();
    }
    public QuartzCronTrigger(String expression) {
        this.quartzCronExpression = new QuartzCronExpression(expression);
    }

    @Override
    public Date nextExecutionTime(TriggerContext triggerContext) {
        Date date = triggerContext.lastCompletionTime();
        if (date != null) {
            Date scheduled = triggerContext.lastScheduledExecutionTime();
            if (scheduled != null && date.before(scheduled)) {
                // Previous task apparently executed too early...
                // Let's simply use the last calculated execution time then,
                // in order to prevent accidental re-fires in the same second.
                date = scheduled;
            }
        }
        else {
            date = new Date();
        }
        return this.quartzCronExpression.getNextValidTimeAfter(date);
    }


    @Override
    public boolean equals(Object other) {
        return (this == other || (other instanceof QuartzCronTrigger &&
                this.quartzCronExpression.equals(((QuartzCronTrigger) other).quartzCronExpression)));
    }

    @Override
    public int hashCode() {
        return this.quartzCronExpression.hashCode();
    }

    @Override
    public String toString() {
        return this.quartzCronExpression.toString();
    }

}
