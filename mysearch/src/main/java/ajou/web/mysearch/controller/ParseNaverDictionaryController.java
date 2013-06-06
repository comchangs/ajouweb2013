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

import ajou.web.mysearch.model.ParseNaverDictionaryNaverParse;
import ajou.web.mysearch.model.ParseNaverDictionaryStringParse;
import ajou.web.mysearch.model.ParseNaverDictionaryUrlParse;

@Controller
public class ParseNaverDictionaryController implements Runnable{
	private MongoOperations mongoOperation;
	private String naverKey;
	private String searchKeyword;
	private ParseNaverDictionaryUrlParse urlParse;
	private ParseNaverDictionaryNaverParse naver;
	private ParseNaverDictionaryStringParse stringParse;
	
	@Override
	public void run()
	{
		init();
		
		String Url = "";
		
		try{
			Url = naver.convertNaverAPIUrl(naverKey, URLEncoder.encode(searchKeyword, "UTF-8"));
			naver.NaverParseInit(Url);
		}
		catch(UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		stringParse.initStringParse(searchKeyword, mongoOperation);
		if (!searchKeyword.isEmpty()) {
			for (;!naver.isLast();naver.setNext()) {
				urlParse.getURL(naver.getContent("link"));
//				stringParse.stringSearchMeta(urlParse.toStringMeta());
				stringParse.stringSearchContent(urlParse.toStringContent());
				stringParse.createRelationWord();
				stringParse.insertMongoDB();
			}
		}
	}
	
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
	public String parseNaverDictionary(@RequestParam("keyword") String Keyword)
	{
		this.searchKeyword = Keyword;
		
		Thread t1 = new Thread(this);
		
		t1.start();
		
		return "";
	}

}
