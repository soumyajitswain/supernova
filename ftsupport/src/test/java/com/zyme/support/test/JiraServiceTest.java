package com.zyme.support.test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.learning.support.SupportInitializer;
import com.learning.support.service.JiraService;

import freemarker.template.TemplateException;

@Ignore
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SupportInitializer.class)
public class JiraServiceTest {
	
    private static Logger LOGGER = Logger.getLogger(JiraServiceTest.class);
	
    @Autowired
    private JiraService jiraService;

	@Test
	public void invokeReaderServiceTest() {
		Map<String, String> input = new HashMap<String, String>();
		try {
			input.put("operation", "readTask");
			jiraService.invokeReadService(input);
		} catch (URISyntaxException e) {
			LOGGER.error("Exception ", e);
		} catch (IOException e) {
			LOGGER.error("Exception ", e);
		} catch (InterruptedException e) {
			LOGGER.error("Exception ", e);
		} catch (ExecutionException e) {
			LOGGER.error("Exception ", e);
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCreateIssue() {
		String category = "deploy";
		String content = "test";
		Map<String, String> inputData = new HashMap<String, String>();
		try {
			jiraService.createIssue(category, content, inputData);
		} catch (URISyntaxException | IOException | TemplateException e) {
			LOGGER.error("Exception ", e);
		}
	}
}
