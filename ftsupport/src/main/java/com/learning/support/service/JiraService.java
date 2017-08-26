package com.learning.support.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.atlassian.jira.rest.client.api.GetCreateIssueMetadataOptions;
import com.atlassian.jira.rest.client.api.GetCreateIssueMetadataOptionsBuilder;
import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClientFactory;
import com.atlassian.jira.rest.client.api.domain.BasicIssue;
import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.atlassian.jira.rest.client.api.domain.CimFieldInfo;
import com.atlassian.jira.rest.client.api.domain.CimIssueType;
import com.atlassian.jira.rest.client.api.domain.CimProject;
import com.atlassian.jira.rest.client.api.domain.Comment;
import com.atlassian.jira.rest.client.api.domain.CustomFieldOption;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.api.domain.input.ComplexIssueInputFieldValue;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInputBuilder;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.atlassian.util.concurrent.Promise;
import com.learning.support.bean.IssueDetail;
import com.learning.support.service.freemarker.GenerateContentService;

import freemarker.template.TemplateException;

/**
 * This class act as a client for Jira service.
 * 
 * @author sswain
 *
 */
@Service
public class JiraService {

	private static final Logger LOGGER = Logger.getLogger(JiraService.class);

	/**
	 * Jira login url
	 */
	@Value("${zmnt.zira.url}")
	private   String JIRA_URL;

	/**
	 * Jira username
	 */
	@Value("${zmnt.zira.admin.username}")
	private   String JIRA_ADMIN_USERNAME;

	/**
	 * Jira password
	 */
	@Value("${zmnt.zira.admin.passowrd}")
	private   String JIRA_ADMIN_PASSWORD;

	/**
	 * Jira query
	 */
	@Value("${zmnt.zira.search.query}")
	private   String JIRA_JQL_QUERY;

	@Autowired
	private GenerateContentService contentService;
	/**
	 * Invoke different Jira service.
	 * 
	 * @param input
	 *       	Input detail in Map format
	 *       
	 * @return {@link Map}
	 *          Return output in a Map
	 * @throws URISyntaxException 
	 * @throws IOException 
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 * @throws TemplateException 
	 */
	public Map<String, Object> invokeReadService(Map<String, String> input) 
			throws URISyntaxException, IOException, InterruptedException, ExecutionException, TemplateException {
		Map<String, Object> jiraIssueDtl = new HashMap<String, Object>();
		String operation = input.get("operation");
		if(operation.equals("readTask")) {
			jiraIssueDtl = sendJiraTicketDetail(input);
		} else if(operation.equals("createTask")) {
			createIssue(input.get("category_type"), input.get("category"), input);
		} else if(operation.equals("writeComment")) {
			writeComment(input);
		}
		return jiraIssueDtl;
	}

	/**
	 * Read the jira ticket. Get the commentUri and than add comments through {@link JiraRestClient}
	 * instance.
	 * @param input
	 * @throws IOException 
	 * @throws URISyntaxException 
	 */
	private void writeComment(Map<String, String> input) throws URISyntaxException, IOException {
		JiraRestClient jiraRestClient = null;
		try {
			jiraRestClient = loginToJira();
			Issue issue = getIssueDetail(jiraRestClient, input.get("key").toString()).claim();
			jiraRestClient.getIssueClient().addComment(
					issue.getCommentsUri(), Comment.valueOf(input.get("comment"))).claim();
		} catch(URISyntaxException| IOException e) {
			LOGGER.error("Error writing the comment", e);
			throw e;
		} finally {
			logoutJira(jiraRestClient);
		}
	}

	/**
	 * Read the Jira ticket detail.
	 * 
	 * @return {@link Map}
	 *               List of jira detail in Map
	 * @throws URISyntaxException 
	 * @throws IOException 
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	private Map<String, Object> sendJiraTicketDetail(Map<String, String> input) 
			throws URISyntaxException, IOException, InterruptedException, ExecutionException {
		Map<String, Object> jiraDetailMap = new HashMap<String, Object>();
		JiraRestClient client = loginToJira();

		Promise<SearchResult> issues = searchJiraIssues(client, input.get("JIRA_QUERY"));

		for(Issue issue : issues.claim().getIssues()){
			Promise<Issue> issueDtl = getIssueDetail(client, issue.getKey());
			IssueDetail issueDetail = new IssueDetail(issueDtl.get().getSummary(), issueDtl.get().getDescription(), 
					issueDtl.get().getKey(),issueDtl.get().getStatus().getName(), 
					getLableStr(issueDtl.get().getLabels()), listComments(issueDtl.get().getComments()));
			jiraDetailMap.put(issueDtl.get().getKey(), issueDetail);
			jiraDetailMap.put("key", issueDtl.get().getKey());
			break;
		}

		logoutJira(client);

		return jiraDetailMap;
	}

	/**
	 * Format the Label object and return them as string.
	 * @param lables
	 * @return
	 */
	private String getLableStr(Set<String> lables) {
		return null;
	}

	/**
	 * Iterate through comment object and return the last comment object.
	 * @param comments
	 * @return
	 */
	private List<String> listComments(Iterable<Comment> comments) {
		List<String> commentsBody = new ArrayList<String>();
		for(Comment comment:comments) {
			commentsBody.add(comment.getBody());
		}
		return commentsBody;
	}

	/**
	 * Get the issue detail in a expanded format.
	 * @param client
	 * @param issueId
	 * @return
	 */
	private Promise<Issue> getIssueDetail(JiraRestClient client, String issueId) {
		Promise<Issue> issue = (Promise<Issue>) client.getIssueClient().getIssue(issueId,
				Arrays.asList(IssueRestClient.Expandos.CHANGELOG, IssueRestClient.Expandos.TRANSITIONS));
		return issue;
	}

	/**
	 * Search Jira repo for issues.
	 * @param client {@link JiraRestClient}
	 *             Jira connection object
	 * @param JIRA_QUERY
	 *             Jira Query
	 * @return
	 *      
	 */
	private Promise<SearchResult> searchJiraIssues(JiraRestClient client, String JIRA_QUERY) {
		Promise<SearchResult> searchJqlPromise	=	client.getSearchClient().searchJql(JIRA_JQL_QUERY);
		return searchJqlPromise;
	}

	/**
	 * Login to Jira using username/password
	 *  
	 * @return
	 * @throws URISyntaxException
	 * @throws IOException 
	 */
	private JiraRestClient loginToJira() throws URISyntaxException, IOException {
		JiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
		LOGGER.info(JIRA_URL);
		URI uri = new URI(JIRA_URL);
		JiraRestClient client = (JiraRestClient) 
				factory.createWithBasicHttpAuthentication(uri, JIRA_ADMIN_USERNAME, JIRA_ADMIN_PASSWORD);
		return client;
	}

	/**
	 * Logout from a Jira once a particular activity is complete
	 * 
	 * @param client
	 * @throws IOException 
	 */
	private void logoutJira(JiraRestClient client) throws IOException {
		client.close();
	}
	/**
	 * Create issue in Jira
	 * 
	 * @param newIssue
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public BasicIssue createIssue(IssueInput newIssue) throws IOException, URISyntaxException {

		BasicIssue basicIssue = null;
		JiraRestClient jiraRestClient = null;
		try {
			jiraRestClient = loginToJira();
			basicIssue = jiraRestClient.getIssueClient().createIssue(newIssue).claim();
		} catch(IOException | URISyntaxException e) {
			LOGGER.error("Exception in creating the issue", e);
			throw e;
		} finally {
			logoutJira(jiraRestClient);
		}

		return basicIssue;
	}

	/**
	 * Get the attachmentURI and than add the attachment to this URI.
	 * @param issueKey
	 * @return
	 * @throws IOException
	 */
	private URI getAttachmentURI(String issueKey)throws IOException {
		JiraRestClient jiraRestClient = null;
		URI attachmentUri = null;

		try {
			jiraRestClient = loginToJira();
			Promise<Issue> issue = getIssueDetail(jiraRestClient,issueKey);
			Issue i = issue.claim();
			attachmentUri = i.getAttachmentsUri();
		} catch(IOException | URISyntaxException e) {
			LOGGER.error("Exception in creating the issue", e);
			throw new IOException(e);
		} finally {
			logoutJira(jiraRestClient);
		}
		return attachmentUri;
	}

	/**
	 * Add attachment to the JIRA ticket.
	 * @param issueKey
	 * @param content
	 * @param fileName
	 * @throws IOException
	 */
	private void addAttachment(String issueKey, String content, String fileName) throws IOException {
		JiraRestClient jiraRestClient = null;
		URI attachmentUri = getAttachmentURI(issueKey);
		ByteArrayInputStream inputStream = null;
		try {
			Thread.sleep(6000);
			LOGGER.info("AttachmentURI "+attachmentUri);
			jiraRestClient = loginToJira();
			inputStream = new ByteArrayInputStream(content.getBytes());
			jiraRestClient.getIssueClient().addAttachment(
					attachmentUri, inputStream, fileName+".txt").claim();
		} catch(IOException | URISyntaxException | InterruptedException e) {
			LOGGER.error("Exception in creating the issue", e);
			throw new IOException(e);
		} finally {
			inputStream.close();
			logoutJira(jiraRestClient);

		}

	}
	/**
	 * 
	 * @return {@link IssueInput}
	 * @throws IOException 
	 * @throws URISyntaxException 
	 * @throws TemplateException 
	 */
	public IssueInput createIssue(String category, String contentKey, Map<String, String> inputData) throws URISyntaxException, IOException, TemplateException {
		String content = null;
		BasicProject basicProject = findProjects();
		CimIssueType cimIssueType = findIssueTypesByProjectKey(basicProject.getKey());
		Map<String, String> issueFieldValuesMap = buildFieldMap(inputData);

		if(category.indexOf("deploy") >= 0) {
			String projectType = null;
			content = contentService.createContent(basicProject.getKey(), inputData.get("category"), inputData);
			issueFieldValuesMap.put("Description", "Hi Deploy Team, Deploy the instructions mentioned in "+inputData.get("category")+".txt");
			IssueInput issueInput = buildIssueMetadata(cimIssueType,basicProject.getKey(), projectType, issueFieldValuesMap);
			BasicIssue basicIssue = createIssue(issueInput);
			LOGGER.info("Issue created .. "+basicIssue.getKey());
			addAttachment(basicIssue.getKey(),content, inputData.get("category"));
		} else {
			content = contentService.createContent(basicProject.getKey(), inputData.get("category"), inputData);
			issueFieldValuesMap.put("Description", content);
			IssueInput issueInput = buildIssueMetadata(cimIssueType,basicProject.getKey(), "", issueFieldValuesMap);	
			BasicIssue basicIssue = createIssue(issueInput);
			LOGGER.info("Issue created .. "+basicIssue.getKey());
		}

		return null;
	}
	//TODO
	private Map<String, String> buildFieldMap(Map<String, String> inputData) {
		Map<String, String> issueFieldValuesMap = new HashMap<String, String>();
		issueFieldValuesMap.put("Summary", "summary");
		issueFieldValuesMap.put("Issue Type", "\"Deploy Task\"");
		issueFieldValuesMap.put("description", inputData.get("description"));
		issueFieldValuesMap.put("Phase", getPhase(inputData.get("env"),inputData.get("category")));
		issueFieldValuesMap.put("Execution Time", "26-AUG_2016 10:20:20");
		issueFieldValuesMap.put("Assignee", "sswain");
		issueFieldValuesMap.put("Activity", inputData.get("Activity"));
		issueFieldValuesMap.put("Account", "1");
		issueFieldValuesMap.put("DML Present", "No");
		issueFieldValuesMap.put("Access To", "JIRA");
		issueFieldValuesMap.put("Access Type", "Read/Write");
		return issueFieldValuesMap;
	}

	/**
	 * Get the phase of the object
	 * @param category
	 * @return
	 */
	private String getPhase(String env,String category) {
		if(!StringUtils.isEmpty(env)) {
			return env.trim();
		}

		if(category.toLowerCase().indexOf("qa") >= 0) {
			return "UAT";
		} else if(category.toLowerCase().indexOf("deploy_uat") >= 0) {
			return "UAT";
		}
		return null;
	}

	/**
	 * Build the metadata for the issues so that new Jira ticktes can be created.
	 * @param projectKey
	 * @param projectType
	 * @param issueFieldValuesMap
	 * @return
	 */
	public IssueInput buildIssueMetadata(CimIssueType cimIssueType,String projectKey, String projectType, Map<String, String> issueFieldValuesMap) {
		IssueInputBuilder issueBuilder = new IssueInputBuilder(projectKey, cimIssueType.getId());
		for (String key : cimIssueType.getFields().keySet()) {
			CimFieldInfo issueField = cimIssueType.getFields().get(key);
			String fieldID = issueField.getId();
			LOGGER.info("field id "+fieldID+" Field label "+issueField.getName() +" required "+issueField.isRequired());
			if(fieldID.equals("project") || fieldID.equals("issuetype") || issueField.getName().equals("Reason for DML") 
					|| issueField.getName().equals("Actual End Date") || issueField.getName().equals("Actual Start Date") 
					|| issueField.getName().equals("Notify To") || issueField.getName().equals("Account") 
					|| issueField.getName().equals("Actual Cost") || issueField.getName().equals("Execution Time")) { // Skip the project field since it is already filled when issueBuilder was created
				continue;
			} else if(fieldID.equals("assignee")) {

				issueBuilder.setFieldValue(fieldID, 
						ComplexIssueInputFieldValue.with("name", issueFieldValuesMap.get(issueField.getName())));
				continue;
			}

			try {
				if(issueField.getAllowedValues() != null) {
					for(Object object: issueField.getAllowedValues()) {
						if(object instanceof CustomFieldOption) {
							CustomFieldOption customFieldOption = (CustomFieldOption)object;
							LOGGER.info("Allowed values " +object.getClass() + " values "+customFieldOption.getValue());
							if(customFieldOption.getValue().equals(issueFieldValuesMap.get(issueField.getName()))) {
								ComplexIssueInputFieldValue value = ComplexIssueInputFieldValue.with("value", customFieldOption.getValue());
								issueBuilder.setFieldValue(fieldID, value);
								break;
							}
						}

					}
				} else {
					if(issueFieldValuesMap.get(issueField.getName()) != null) { 
						issueBuilder.setFieldValue(fieldID, issueFieldValuesMap.get(issueField.getName()));
					} else {
						issueBuilder.setFieldValue(fieldID, "test");
					}
				}
			} catch (Exception ex) {
				LOGGER.error("Exception in accessing the field", ex);				
			}
		}

		LOGGER.info("Issue Builder detail "+issueBuilder.build().toString());
		return issueBuilder.build();
	}

	/**
	 * Find list of projects from JIRA
	 * @return
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	@SuppressWarnings("rawtypes")
	public BasicProject findProjects() throws URISyntaxException, IOException {
		JiraRestClient jiraRestClient = loginToJira();
		Iterable projects = null;
		try {
			projects = jiraRestClient.getProjectClient().getAllProjects().get();
			for(Object o:projects) {
				BasicProject basicProject = (BasicProject)o;
				if(basicProject.getKey().equals("DHPROJ")) {
					return basicProject; 
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logoutJira(jiraRestClient);
		return null;
	}

	/**
	 * Get the issueType by project Key.
	 * @param projectKey
	 * @return
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	@SuppressWarnings("rawtypes")
	public CimIssueType findIssueTypesByProjectKey(String projectKey) throws URISyntaxException, IOException {
		JiraRestClient jiraRestClient = loginToJira();
		GetCreateIssueMetadataOptions options = new GetCreateIssueMetadataOptionsBuilder()
		.withExpandedIssueTypesFields()
		.withProjectKeys(projectKey)
		.build();

		Iterable cimProjects = jiraRestClient.getIssueClient()
				.getCreateIssueMetadata(options).claim();
		final CimProject project = (CimProject) cimProjects.iterator().next();
		List<CimIssueType> issueTypes = (List<CimIssueType>) project.getIssueTypes();
		CimIssueType cIssueType = null;
		for(CimIssueType cimIssueType:issueTypes) {
			if(cimIssueType.getName().equals("Deploy Task")) {
				cIssueType= cimIssueType;
				break;
			}
		}
		//Iterable issueTypes = project.getIssueTypes();
		logoutJira(jiraRestClient);
		return cIssueType;
	}

}
