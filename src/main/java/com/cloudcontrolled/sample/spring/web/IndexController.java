package com.cloudcontrolled.sample.spring.web;

import java.io.IOException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import com.cloudcontrolled.sample.spring.visitcounter.VisitCounter;

@Controller
public class IndexController {

	@RequestMapping("/")
	public String index(ModelMap model) throws IOException {
	    VisitCounter vc = new VisitCounter();
	    vc.updateVisitCount();
	    model.addAttribute("count", vc.getVisitCount());
		return "index";
	}
}