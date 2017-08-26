package com.learning.support.scheduler;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.learning.support.service.DeployTaskService;
import com.learning.support.service.DeployTaskTraining;

import freemarker.template.TemplateException;

/**
 * All schedulers will be configured in this class. Separate schedulers will be maintained for different task.
 * 
 * @author sswain
 *
 */
@Component
public class SupportScheduler {
	
    private static final Logger LOGGER = Logger.getLogger(SupportScheduler.class);
	
    @Autowired
    private DeployTaskService deployTaskService;
    
	/**
	 * Scheduler to manage the deploy task
	 * 
	 * @return void
	 */
	@Scheduled(cron	= "${training.task.schedular.exp}")
	public void deployTaskScheduler() {
		try {
			deployTaskService.trainClassifier();
			deployTaskService.processDeployTask();
		} catch (URISyntaxException | IOException | InterruptedException
				| ExecutionException | TemplateException e) {
			LOGGER.error("Exception in scheduler. Stopping the scheduler", e);
		}
	}
	
	

}
