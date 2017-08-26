package com.learning.support.spring.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.utility.XmlEscape;

@Configuration
public class FreemarkerConfig {

	@Bean(name ="freemarkerConfig")	
	public FreeMarkerConfigurer freemarkerConfig() {
		FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
		configurer.setTemplateLoaderPath("**classpath:template**/");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("xml_escape", new XmlEscape());
		configurer.setFreemarkerVariables(map);
		return configurer;
	}

}
