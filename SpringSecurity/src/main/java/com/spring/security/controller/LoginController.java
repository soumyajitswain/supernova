package com.spring.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
	@RequestMapping("/login")
	public String login() {
		return "login";
	}
	@RequestMapping("/landing")
	public String landing() {
		return "landing";
	}
	@RequestMapping("/error")
	public String error() {
		return "error";
	}
   
}
