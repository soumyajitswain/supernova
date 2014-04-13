package com.spring.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.WebInvocationPrivilegeEvaluator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * @author sswain
 *
 */
@Controller
public class WelcomeController {
	private final WebInvocationPrivilegeEvaluator webInvocationPriviledgeEvaluator;
	 @Autowired
	    public WelcomeController(WebInvocationPrivilegeEvaluator webPrivEvaluator) {
	        this.webInvocationPriviledgeEvaluator = webPrivEvaluator;
	    }
	@RequestMapping("/")
	public String welcome() {
		return "index";
	}
	@RequestMapping("/admin")
	public String admin() {
		return "admin";
	}
	@RequestMapping("/user")
	public String user() {
		return "user";
	}
	@ModelAttribute
	public boolean showAdminLink(Authentication authentication) {
		return webInvocationPriviledgeEvaluator.
				isAllowed("/admin/", authentication);
	}

}