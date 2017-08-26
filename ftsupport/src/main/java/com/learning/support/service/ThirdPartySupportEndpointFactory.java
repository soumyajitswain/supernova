package com.learning.support.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import freemarker.template.TemplateException;

/**
 * This is the factory to support multiple type of bug and deployment tracking tools.
 * 
 * @author sswain
 *
 */
@Service
public class ThirdPartySupportEndpointFactory {
	@Autowired
    private JiraService jiraService;
	/**
	 * Based on param it will call the corresponding  third party service
	 * 
	 * @param name
	 *           Name of the tool to be invoked.
	 * @param input
	 *           Map of input required to process the service          
	 * @return {@link Map}
	 *           It contains the data required to process the support request.
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @throws URISyntaxException 
	 * @throws TemplateException 
	 */
	public Map<String, Object> invokeFactory(String name, Map<String, String> input) 
			throws URISyntaxException, IOException, InterruptedException, ExecutionException, TemplateException {
		Map<String, Object> o = null;
		if(name.equals("JIRA")) {
			o = jiraService.invokeReadService(input);
		}
		return o;
	}
}
