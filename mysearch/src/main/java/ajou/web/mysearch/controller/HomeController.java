package ajou.web.mysearch.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapreduce.GroupBy;
import org.springframework.data.mongodb.core.mapreduce.GroupByResults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import static org.springframework.data.mongodb.core.query.Criteria.where;

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
		/*
		GroupByResults<XObject> results = mongoOperation.group(where("search_keyword").is("man"),"", 
                GroupBy.key("relation_keyword").initialDocument("{ count: 0 }").reduceFunction("function(curr, result) { result.count += 1 }"), 
                XObject.class);
                */
		GroupByResults<Keyword> results = mongoOperation.group("keyword", 
                GroupBy.key("relation_keyword").initialDocument("{ count: 0 }").reduceFunction("function(curr, result) { result.count += 1 }"), 
                Keyword.class);
		String json_data = results.getRawResults().toString();
		model.addAttribute("json_data", json_data );
		
		return "relation_keyword";
	}
	public class Keyword {

		  private String relation_keyword;

		  private int count;


		  public String getRelation_keyword() {
		    return relation_keyword;
		  }

		  public void setRelation_keyword(String relation_keyword) {
		    this.relation_keyword = relation_keyword;
		  }

		  public float getCount() {
		    return count;
		  }

		  public void setCount(int count) {
		    this.count = count;
		  }

		  @Override
		  public String toString() {
		    return "Keyword [relation_keyword=" + relation_keyword + " count = " + count + "]";
		  }
		}
	
	@RequestMapping(value ="/parseurl", method = RequestMethod.GET)
	public String parseurl(Locale locale, Model model) {
		logger.info("Parse Html", locale);
		
		return "inputUrlTest";
	}
	
	@RequestMapping(value ="/showweather", method = RequestMethod.GET)
	public String showweather(Locale locale, Model model) {
		logger.info("Show weather", locale);
		
		return "showWeather";
	}
}
