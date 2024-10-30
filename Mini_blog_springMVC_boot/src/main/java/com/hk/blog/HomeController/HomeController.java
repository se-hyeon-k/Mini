package com.hk.blog.HomeController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	
	Logger logger=LoggerFactory.getLogger(getClass());
	
	@GetMapping("/")
	public String home() {
		logger.info("HOME페이지 이동");
		return "home";
	}
	
}
