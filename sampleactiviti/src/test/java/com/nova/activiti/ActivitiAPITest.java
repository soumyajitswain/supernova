package com.nova.activiti;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.nova.activiti.ActivitiService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/nova-config-test.xml"})
public class ActivitiAPITest {

	@Autowired
	private ActivitiService service;
	@Test
	public void runActiviti() {
		service.deployProcessFiles("hello.bpmn20.xml");
		service.startProcessInstance("helloProcess");
	}
	
}
