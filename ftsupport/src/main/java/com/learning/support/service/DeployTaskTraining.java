package com.learning.support.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learning.support.automation.classifier.naivebaysian.BayesClassifier;
import com.learning.support.bean.DeployTaskTrainingData;

@Service
public class DeployTaskTraining {
	
	@Autowired
	public BayesClassifier<String, String> classifier;
	
//	@Autowired
//	public NaiveBayesClassifier naiveBayesClassifier;
	
	@Autowired
	private DeployTaskTrainingData deployTaskTrainingData;
	
	public void trainNaiveClassifier() {
	     final String qaString = deployTaskTrainingData.getQaBuild();
	     final String[] features = qaString.split("\\s");
	     classifier.learn("QA_BUILD", Arrays.asList(features));

	     final String tagQABuild = deployTaskTrainingData.getTagQABuild();
	     final String[] tagQABuildFeature = tagQABuild.split("\\s");
	     classifier.learn("TAG_QA_BUILD", Arrays.asList(tagQABuildFeature));

	     final String tagUATBuild = deployTaskTrainingData.getTagUATBuild();
	     final String[] tagUATBuildFeature = tagUATBuild.split("\\s");
	     classifier.learn("TAG_UAT_BUILD", Arrays.asList(tagUATBuildFeature));

	     final String deployUat = deployTaskTrainingData.getDeployUAT();
	     final String[] deployUatFeature = deployUat.split("\\s");
	     classifier.learn("DEPLOY_UAT", Arrays.asList(deployUatFeature));

	     
	     final String deployProd = deployTaskTrainingData.getDeployProd();
	     final String[] deployProdFeature = deployProd.split("\\s");
	     classifier.learn("DEPLOY_PROD", Arrays.asList(deployProdFeature));

	     final String commentAll = deployTaskTrainingData.getComment();
	     final String[] commentAllFeature = commentAll.split("\\s");
	     classifier.learn("COMMENT_ALL", Arrays.asList(commentAllFeature));

//	     final Map<String, String> trainingMap = new HashMap<String, String>();
//	     trainingMap.put("QA_BUILD", deployTaskTrainingData.getQaBuild());
//	     trainingMap.put("TAG_QA_BUILD", deployTaskTrainingData.getTagQABuild());
//	     trainingMap.put("TAG_UAT_BUILD", deployTaskTrainingData.getTagUATBuild());
//	     trainingMap.put("DEPLOY_UAT", deployTaskTrainingData.getDeployUAT());
//	     trainingMap.put("DEPLOY_PROD", deployTaskTrainingData.getDeployProd());
	     

	}

}
