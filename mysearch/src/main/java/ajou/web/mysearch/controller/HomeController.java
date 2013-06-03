package ajou.web.mysearch.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "home";
	}
	
	@RequestMapping(value = "/wordcloud", method = RequestMethod.GET)
	public String wordcloud(Locale locale, Model model) {
		logger.info("Word Cloud!", locale);

		return "d3js";
	}
	
	@RequestMapping(value = "/jsonp", method = RequestMethod.GET)
	public String jsonp(Locale locale, Model model) {
		logger.info("JSONP", locale);

		return "jsonp";
	}
	
	@RequestMapping(value = "/relation_keyword", method = RequestMethod.GET)
	public String relation(Locale locale, Model model) {
		logger.info("relation_keyword", locale);

		ApplicationContext ctx = new GenericXmlApplicationContext("SpringConfig.xml");
		MongoOperations mongoOperation = (MongoOperations)ctx.getBean("mongoTemplate");
		//mongoOperation.group("")
		return "relation_keyword";
	}
	
	@RequestMapping(value ="/parseurl", method = RequestMethod.GET)
	public String parseurl(Locale locale, Model model) {
		logger.info("Parse Html", locale);
		
		return "inputUrlTest";
	}
}
