package com.zyme.support.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.learning.support.SupportInitializer;
import com.learning.support.service.freemarker.GenerateContentService;

import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

@Ignore
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SupportInitializer.class)
public class GenerateContentServiceTest {

    private static Logger LOGGER = Logger.getLogger(JiraServiceTest.class);

    @Autowired
    private GenerateContentService contentService;
    
    @Test
    public void testCreateContent() {
    	try {
    		Map<String, String> map = new HashMap<String, String>();
			contentService.createContent("", "tag_ft_qa", map);
		} catch (TemplateNotFoundException e) {
			LOGGER.error("Exception", e);
		} catch (MalformedTemplateNameException e) {
			LOGGER.error("Exception", e);
		} catch (ParseException e) {
			LOGGER.error("Exception", e);
		} catch (IOException e) {
			LOGGER.error("Exception", e);
		} catch (TemplateException e) {
			LOGGER.error("Exception", e);
		}
    }
    @Test
    public void testCreateFile() {
    	try {
    		Map<String, String> map = new HashMap<String, String>();
			contentService.createFile("", "tag_ft_qa", map);
		} catch (TemplateNotFoundException e) {
			LOGGER.error("Exception", e);
		} catch (MalformedTemplateNameException e) {
			LOGGER.error("Exception", e);
		} catch (ParseException e) {
			LOGGER.error("Exception", e);
		} catch (IOException e) {
			LOGGER.error("Exception", e);
		} catch (TemplateException e) {
			LOGGER.error("Exception", e);
		}
    }

}
