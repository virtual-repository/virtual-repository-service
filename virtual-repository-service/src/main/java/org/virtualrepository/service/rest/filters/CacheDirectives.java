package org.virtualrepository.service.rest.filters;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target(METHOD)
@Retention(value=RUNTIME)
public @interface CacheDirectives {

	int max_age() default -1;
	boolean no_cache() default false;

}
