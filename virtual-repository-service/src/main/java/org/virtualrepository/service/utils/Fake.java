package org.virtualrepository.service.utils;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

@Qualifier
@Target({PARAMETER,METHOD})
@Retention(RUNTIME)
public @interface Fake {}
