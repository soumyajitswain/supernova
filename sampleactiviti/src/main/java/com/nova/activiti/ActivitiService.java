package com.nova.activiti;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivitiService {

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private HistoryService historyService;

	@Autowired
	private ManagementService managementService;

	@Autowired
	private RepositoryService repositoryService;

	public String deployProcessFiles(String filePath) {
		String deploymentId = repositoryService
				.createDeployment()
				.addClasspathResource(filePath)
				.deploy()
				.getId();
		return deploymentId;
	}
	
	public void startProcessInstance(String processDefName) {
		runtimeService.startProcessInstanceByKey(processDefName);
	}
	
	public void completeTask() {

	}
}
