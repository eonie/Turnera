package org.turnera.client.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.TYPE)
public @interface TurneraJob {
	String name() default "";
	String cron() default "";
	String group() default "Default";
	boolean isSharding() default  false;
}
