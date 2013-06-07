package ajou.web.mysearch.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes
public class InputRelatedKeywordsController {
	
	@RequestMapping("/inputRelatedKeywords")
	public String insertKeywords(String inputKeyword) {
		return "redirect:inputRelatedKeywords";
	}
}
