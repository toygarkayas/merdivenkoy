package com.example.servingwebcontent;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GreetingController {
	
	@RequestMapping("/")
	public String indexPage() {
	    return "views/index";
	}
	@GetMapping("/greeting")
	public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
		model.addAttribute("name", name);
		return "views/greeting";
	}
	
	@GetMapping("/jsp")
	public String jsp(Map<String, Object> model) {
		return "views/serverPage";
	}

}