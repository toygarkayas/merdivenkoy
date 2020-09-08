package com.example.servingwebcontent;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RandomIntController {

	@GetMapping("/random")
	public String random(@RequestParam(name="random", required=false, defaultValue="0") int random, Model model) {
		GreetingWebClient gwc = new GreetingWebClient();
		model.addAttribute("random", gwc.getResult());
		return "random.html";
	}

}