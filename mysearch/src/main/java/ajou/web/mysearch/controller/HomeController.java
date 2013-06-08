package ajou.web.mysearch.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
	
	@RequestMapping(value = "/autocomplete", method = RequestMethod.GET)
	public String autocomplete(Locale locale, Model model, @RequestParam("keyword") String search_keyword) {
		logger.info("JSON", locale);
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
}
