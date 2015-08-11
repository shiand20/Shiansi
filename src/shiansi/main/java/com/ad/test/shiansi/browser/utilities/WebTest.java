package com.ad.test.shiansi.browser.utilities;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;


/**
 * interface creates annotation WebTest and specified parameters<br>
 * Annotation @Webtest will let Grid know to open RC and browser instance, start session
 *
 */
@Retention(RUNTIME)
@Target( { CONSTRUCTOR, METHOD, TYPE })
public @interface WebTest {
	
	String browser() default "";
	
}
