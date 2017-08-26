package com.learning.support;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * Root class to initialize the support module. This module will use Spring boot annotations and 
 * embeeded tomcat.
 * 
 * @author sswain
 *
 */
@ComponentScan
@EnableAutoConfiguration
@SpringBootApplication
@EnableScheduling
@EnableAspectJAutoProxy
public class SupportInitializer {

	/**
	 * Override the configuration method.
	 * 
	 * @param application
	 * @return
	 */
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SupportInitializer.class);
	}
	
	/**
	 * Application starts from here
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new SpringApplicationBuilder(SupportInitializer.class).run(args);
	}

}
