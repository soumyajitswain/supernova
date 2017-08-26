package com.learning.support.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(ignoreResourceNotFound=true, value="classpath:deploytask-training-data.properties")
public class DeployTaskTrainingData {
	 @Value("${category.qa.build}")
     private String qaBuild;

	 @Value("${category.tag.qa.build}")
     private String tagQABuild;

	 @Value("${category.deploy.uat}")
     private String deployUAT;

	 @Value("${category.tag.uat.build}")
     private String tagUATBuild;

	 @Value("${category.deploy.prod}")
     private String deployProd;

	 @Value("${category.comment.all}")
     private String comment;
	 
	public String getQaBuild() {
		return qaBuild;
	}

	public void setQaBuild(String qaBuild) {
		this.qaBuild = qaBuild;
	}

	public String getTagQABuild() {
		return tagQABuild;
	}

	public void setTagQABuild(String tagQABuild) {
		this.tagQABuild = tagQABuild;
	}

	public String getDeployUAT() {
		return deployUAT;
	}

	public void setDeployUAT(String deployUAT) {
		this.deployUAT = deployUAT;
	}

	public String getTagUATBuild() {
		return tagUATBuild;
	}

	public void setTagUATBuild(String tagUATBuild) {
		this.tagUATBuild = tagUATBuild;
	}

	public String getDeployProd() {
		return deployProd;
	}

	public void setDeployProd(String deployProd) {
		this.deployProd = deployProd;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	 
	 
}
