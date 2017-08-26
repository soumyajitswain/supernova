package com.zyme.support.test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.learning.support.SupportInitializer;
import com.learning.support.service.DeployTaskService;
import com.learning.support.service.DeployTaskTraining;

import freemarker.template.TemplateException;

@Ignore
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SupportInitializer.class)
public class FullAutomationTest {

    private static Logger LOGGER = Logger.getLogger(JiraServiceTest.class);

    @Autowired
    private DeployTaskTraining deployTaskTraining;
    
    @Autowired
    private DeployTaskService deployTaskService;
    
    @Before    
    public void setUp() {
    	deployTaskTraining.trainNaiveClassifier();
	}

    @Test
    public void createTagRequestTest() {
    	try {
			deployTaskService.processDeployTask();
		} catch (URISyntaxException | IOException | InterruptedException
				| ExecutionException | TemplateException e) {
			LOGGER.error("Error in process ", e);
		}
    }
}
