package ajou.web.mysearch.controller;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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
import org.springframework.web.bind.annotation.RequestParam;

import ajou.web.mysearch.model.MySqlConnection;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	ApplicationContext ctx = new GenericXmlApplicationContext("SpringConfig.xml");
	MongoOperations mongoOperation = (MongoOperations)ctx.getBean("mongoTemplate");
	
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
		
		return "SearchResultTest";
	}
	
	@RequestMapping(value = "/wordcloud", method = RequestMethod.GET)
	public String wordcloud(Locale locale, Model model) {
		logger.info("Word Cloud!", locale);

		return "d3js";
	}
	
	@RequestMapping(value = "/cloud", method = RequestMethod.GET)
	public String cloud(Locale locale, Model model, @RequestParam("keyword") String search_keyword) {
		logger.info("Word Cloud!", locale);
		String json_data = "[ ]";
		logger.info(search_keyword, locale);
		if (search_keyword==null && search_keyword.trim().equals("")) {
			search_keyword = "";
		} else {
			GroupByResults<Keyword> results = null;
			try {
				results = mongoOperation.group(where("search_keyword").is(new String(search_keyword.getBytes("8859_1"),"UTF-8")),"keyword",
				        GroupBy.key("relation_keyword").initialDocument("{ count: 0 }").reduceFunction("function(curr, result) { result.count += 1 }"), 
				        Keyword.class);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			json_data = results.getRawResults().get("retval").toString();
			model.addAttribute("json_data", json_data );
		}
		return "cloud";
	}
	
	@RequestMapping(value = "/autocomplete", method = RequestMethod.GET)
	public String jsonp(Locale locale, Model model, @RequestParam("keyword") String search_keyword) {
		logger.info("JSONP", locale);
		MySqlConnection mysql = new MySqlConnection();
		ArrayList<String> list = null;
		try {
			list = mysql.getAutoComplete(new String(search_keyword.getBytes("8859_1"),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JSONArray array = new JSONArray();
        for(String item : list) {
            JSONObject obj = new JSONObject();
            obj.put("label", item);
            array.add(obj);
        }

        model.addAttribute("json_data", array.toJSONString() );

		return "autocomplete";
	}
	
	@RequestMapping(value = "/relation_keyword", method = RequestMethod.GET)
	public String relation(Locale locale, Model model) {
		logger.info("relation_keyword", locale);

		//mongoOperation.group("")
		/*
		GroupByResults<XObject> results = mongoOperation.group(where("search_keyword").is("man"),"", 
                GroupBy.key("relation_keyword").initialDocument("{ count: 0 }").reduceFunction("function(curr, result) { result.count += 1 }"), 
                XObject.class);
                */
		GroupByResults<Keyword> results = mongoOperation.group(where("search_keyword").is("in"),"keyword",
                GroupBy.key("relation_keyword").initialDocument("{ count: 0 }").reduceFunction("function(curr, result) { result.count += 1 }"), 
                Keyword.class);
		String json_data = results.getRawResults().get("retval").toString();
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
