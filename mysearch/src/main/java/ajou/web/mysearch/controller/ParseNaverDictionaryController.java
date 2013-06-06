package ajou.web.mysearch.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ajou.web.mysearch.model.MySqlConnection;
import ajou.web.mysearch.model.ParseNaverDictionaryNaverParse;
import ajou.web.mysearch.model.ParseNaverDictionaryStringParse;
import ajou.web.mysearch.model.ParseNaverDictionaryUrlParse;

@Controller
public class ParseNaverDictionaryController {
	private MongoOperations mongoOperation;
	private MySqlConnection userDataMan;
	private String naverKey;
	private ParseNaverDictionaryUrlParse urlParse;
	private ParseNaverDictionaryNaverParse naver;
	private ParseNaverDictionaryStringParse stringParse;
	
	public void init()
	{		
		urlParse = new ParseNaverDictionaryUrlParse();
		naver = new ParseNaverDictionaryNaverParse();
		naverKey = "09eb9f1c30c96d9e1fcae581b0a16325";
		
		stringParse = new ParseNaverDictionaryStringParse();
		
		ApplicationContext ctx = new GenericXmlApplicationContext("SpringConfig.xml");
		mongoOperation = (MongoOperations)ctx.getBean("mongoTemplate");
	}

	@RequestMapping(value = "/parseNaverDictionary", method = RequestMethod.POST)
	public String parseNaverDictionary(@RequestParam("keyword") String searchKeyword)
	{
		String Url = "";
		
		try{
			Url = naver.convertNaverAPIUrl(naverKey, URLEncoder.encode(searchKeyword, "UTF-8"));
			naver.NaverParseInit(Url);
		}
		catch(UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		stringParse.initStringParse(searchKeyword, userDataMan, mongoOperation);
		if (!searchKeyword.isEmpty()) {
			for (;!naver.isLast();naver.setNext()) {
				urlParse.getURL(naver.getContent("link"));
//				stringParse.stringSearchMeta(urlParse.toStringMeta());
				stringParse.stringSearchContent(urlParse.toStringContent());
				stringParse.createRelationWord();
				stringParse.insertMongoDB();
			}
		}
		
		//document.put("keyword", keyword);
		//document.put("relation_keyword", relationWord.get(i));
		/*
		 * 
SELECT keyword, relation_keyword, count(*) as count
FROM relation_keyword
GROUP BY relation_keyword
order by count desc
		 * */
		
		return "";
	}
}
