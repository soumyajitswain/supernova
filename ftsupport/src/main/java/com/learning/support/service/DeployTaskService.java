package com.learning.support.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.learning.support.bean.IssueDetail;

import freemarker.template.TemplateException;

/**
 * This class has all the service information to track and deploy task and take forward step based on
 * deploy task transition event and comments provided into it.
 * 
 * @author sswain
 *
 */
@Service
public class DeployTaskService {
	private static final Logger LOGGER = Logger.getLogger(DeployTaskService.class);
	@Autowired
	private DeployTaskTraining training;
	@Autowired
	private ThirdPartySupportEndpointFactory thirdPartySupportEndpointFactory;

	/**
	 * This is the entry point for the deploy task to start execution.
	 * 
	 * @return void
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @throws URISyntaxException 
	 * @throws TemplateException 
	 */
	public void processDeployTask() throws URISyntaxException, IOException, InterruptedException, ExecutionException, TemplateException {
		Map<String, Object> output = new HashMap<String, Object>();
		try {
			output = startReadingDeployTask();
			IssueDetail issueDtl = analizeInformation(output);
			if(issueDtl != null) {
				createDescrAndFile(issueDtl);
				addComment(output.get("key").toString(), "Comment:Instructions has been executed successfully.");
			}
		} catch(Exception e) {
			LOGGER.error("Error occurred. Comments will be added to the ticket. Analyze the error in log", e);
			addComment(output.get("key").toString(), "Comment:Instruction has been executed with Error"+e.getMessage());
		}
	}
	
	/**
	 * Add comments to the tickets. The comments can be a sucess comments or a error comment.
	 * @param key
	 * @param commentMsg
	 * @throws TemplateException 
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @throws URISyntaxException 
	 */
	public void addComment(String key, String commentMsg) throws URISyntaxException, IOException, InterruptedException, ExecutionException, TemplateException {
		Map<String, String> input = new HashMap<String, String>();
		input.put("operation", "writeComment");
		input.put("key", key);
		input.put("comment", commentMsg);
		Map<String, Object> output = thirdPartySupportEndpointFactory.invokeFactory("JIRA", input);
		
	}

	/**
	 * This method will create the metadata based on ticket description.
	 * 
	 * @param issueDtl
	 *          
	 * @return {@link Map}
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @throws URISyntaxException 
	 * @throws TemplateException 
	 */
	private Map<String, String> createDescrAndFile(IssueDetail issueDtl) 
			throws URISyntaxException, IOException, InterruptedException, ExecutionException, TemplateException {
		Map<String, String> input = new HashMap<String, String>();
		input.put("operation", "createTask");
		input.put("category", issueDtl.getCategory());

		String env = validCommentAttribute(issueDtl,"Env", "No record found for key: %d");
		input.put("env", env);

		if(issueDtl.getCategory().equals("QA_BUILD")) {
			input.put("category_type", env.toLowerCase());
			input.put("Activity", "Build Request");
			input.put("Branch",retriveInfoFrmComment(issueDtl.getComments(),"Branch"));
			input.put("REVISIONNUMBER",retriveInfoFrmComment(issueDtl.getComments(),"REVISION NUMBER"));
		} else if(issueDtl.getCategory().indexOf("TAG") >=0 ) {
			input.put("category_type", env.toLowerCase());
			input.put("Activity", "Tag Request");
			input.put("Branch",retriveInfoFrmComment(issueDtl.getComments(),"Branch"));
			input.put("BuildURL",retriveInfoFrmComment(issueDtl.getComments(),"Build URL"));
			input.put("REVISIONNUMBER",retriveInfoFrmComment(issueDtl.getComments(),"REVISION NUMBER"));
		} else if(issueDtl.getCategory().indexOf("DEPLOY") >= 0 ) {
			input.put("category_type", "deploy");
			input.put("Activity", "Deploy Request");
			input.put("artifacturl",retriveInfoFrmComment(issueDtl.getComments(),"Artifact URL"));
		}
		Map<String, Object> output = thirdPartySupportEndpointFactory.invokeFactory("JIRA", input);

		return null;
	}
	
    /**
     * Return valid values from after parsing the comment
     * 
     * @param issueDetail
     * @param key
     * @param assumedErrorMsg
     * @return
     * @throws IOException
     */
	private String validCommentAttribute(IssueDetail issueDetail,String key, String assumedErrorMsg) throws IOException {
		String value = retriveInfoFrmComment(issueDetail.getComments(),key);
		if(value == null) {
			throw new IOException(String.format(assumedErrorMsg, key));
		}
		return value;
	}
	/**
	 * Analyse information received in support tickets
	 * @param output
	 */
	private IssueDetail analizeInformation(Map<String, Object> supIssueOut) {

		for(Entry<String, Object> entry:supIssueOut.entrySet()) {
			IssueDetail issueDtl = (IssueDetail) entry.getValue();
			pupulateCategory(issueDtl);
			if(issueDtl.getCategory() == null) {
				return null;
			} else {
				return issueDtl;	
			}
		}

		return null;
	}

	/**
	 * Populate the category based on training set.No category will be set if it is a comment from automation
	 * 
	 * @param issueDtl
	 */
	private void pupulateCategory(IssueDetail issueDtl) {
		String textForVerification = null;
		if(issueDtl.getComments().size() > 0) {
			textForVerification = issueDtl.getComments().get(issueDtl.getComments().size()-1); 
		} else {
			textForVerification = issueDtl.getDescription();
		}
		String category = training.classifier.classify(
				Arrays.asList(textForVerification.split("\\s"))).getCategory();
		if(!category.equals("COMMENT_ALL"))
			issueDtl.setCategory(category);
	}

	public void trainClassifier() {
		training.trainNaiveClassifier();
	}
	/**
	 * Read the Jira deploy task by calling the Jira service.
	 * 
	 * @return void
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @throws URISyntaxException 
	 * @throws TemplateException 
	 */
	private Map<String, Object> startReadingDeployTask() throws URISyntaxException, IOException, InterruptedException, ExecutionException, TemplateException {
		Map<String, String> input = new HashMap<String, String>();
		input.put("operation", "readTask");
		Map<String, Object> output = thirdPartySupportEndpointFactory.invokeFactory("JIRA", input);
		return output;
	}

	/**
	 * 
	 * @param comments
	 * @param identifierKey
	 * @return
	 */
	private String retriveInfoFrmComment(List<String> comments, String identifierKey) {
		String regex =  "(?<=KEY:)[^\n]+".replace("KEY", identifierKey);
		if(!CollectionUtils.isEmpty(comments)) {
			String lastComment = comments.get(comments.size() - 1);
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher (lastComment);
			if(m.find()) {
				System.out.println(m.group());
				return m.group();
			}
		}
		return null;
	}
}
