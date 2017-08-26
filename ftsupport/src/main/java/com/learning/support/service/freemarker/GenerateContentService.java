package com.learning.support.service.freemarker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

/**
 * 
 * @author sswain
 *
 */
@Service
public class GenerateContentService {
	
	private static final String TEMPLATE_GEN_ENGINE="freemarker";
	
	@Autowired
	private FreeMarkerConfigurer configurer;
	
	public String createContent(String templateGroup,String templateKey, Map<String, String> data) 
			throws TemplateNotFoundException, MalformedTemplateNameException, 
			ParseException, IOException, TemplateException {
		Template template = getTemplate(templateGroup, templateKey);
		
		Writer out = new StringWriter();
		template.process(data, out);
		return out.toString();
	}
	public String createFile(String templateGroup,String templateKey, Map<String, String> data) 
			throws TemplateNotFoundException, MalformedTemplateNameException, 
			ParseException, IOException, TemplateException {
		String outStr = createContent(templateGroup, templateKey, data);
		List<String> lines = Arrays.asList(new String[] {outStr});
		String filePath = generateFileName(templateGroup, templateKey, "txt");
		Path path = Paths.get(filePath);
		Files.write(path, lines, StandardCharsets.UTF_8);
//		try (BufferedWriter bw = new BufferedWriter(
//				new FileWriter(new File(filePath)))) {
//			bw.write(outStr);
//		}catch (FileNotFoundException ex) {
//			System.out.println(ex.toString());
//		}
		return filePath;
	}
	private String generateFileName(String templateGroup,String templateKey, String type) {
		String templateName = templateGroup+"_"+templateKey+"."+type;
		return templateName;
	}
	private Template getTemplate(String templateGroup,String templateKey) 
			throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException {
		Configuration cfg = configurer.getConfiguration();
		String templateName = generateFileName(templateGroup.toLowerCase(), templateKey.toLowerCase(), "ftl");
	
		Template template = cfg.getTemplate(templateName);
		
		return template;
	}

}
