package com.learning.support.bean;

import java.util.List;
/**
 * This class holds all the issue detail present in Jira or any other bug tracking tool.
 * 
 * @author sswain
 *
 */
public class IssueDetail {
	/**
	 * Summary of the ticket
	 */
	private String summary;
	/**
	 * Description of the ticket
	 */
	private String description;
	/**
	 * Issue Key
	 */
	private String issueKey;
	/**
	 * Lables of the ticket
	 */
	private String label;
	/**
	 * Status of the ticket
	 */
	private String status;
	/**
	 * List of comments for the ticket
	 */
	private List<String> comments;
	
	/**
	 * 
	 */
	private String category;

	public IssueDetail(String summary, String description, String issueKey,String status, String label, List<String> comments) {
		this.summary = summary;
		this.description = description;
		this.issueKey = issueKey;
		this.status = status;
		this.label = label;
		this.comments = comments;
	}
	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIssueKey() {
		return issueKey;
	}

	public void setIssueKey(String issueKey) {
		this.issueKey = issueKey;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<String> getComments() {
		return comments;
	}

	public void setComments(List<String> comments) {
		this.comments = comments;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}

}
