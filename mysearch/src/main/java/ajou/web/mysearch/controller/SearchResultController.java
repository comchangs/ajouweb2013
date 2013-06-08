package ajou.web.mysearch.controller;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapreduce.GroupBy;
import org.springframework.data.mongodb.core.mapreduce.GroupByResults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ajou.web.mysearch.controller.HomeController.Keyword;
import ajou.web.mysearch.model.SearchResult;
import ajou.web.mysearch.model.MySqlConnection;
import ajou.web.mysearch.model.User;
import ajou.web.mysearch.model.Bookmark;

@Controller
public class SearchResultController {
	
	private	MySqlConnection mySqlCon;
	/*	private String key = "";
	private int sta;
	private int numF;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/GetResultJsonData", method = RequestMethod.POST)
	public JSONObject getResultJsonData()
	{
		JSONObject json = new JSONObject();
		json.put("searchKeyword", key);
		json.put("start", sta);
		json.put("numFound", numF);
		
		return json;
	}*/
	
	private void naverParse(String start, String bookmarkUrl, String bookmarkSelect, String searchKeywordNotEncode)
	{		
		if(start.equals("0") && bookmarkUrl.equals("null") && bookmarkSelect.equals("null"))
		{
			ParseNaverDictionaryController naver = new ParseNaverDictionaryController();
			naver.parseNaverDictionary(searchKeywordNotEncode);
		}
		
	}
	
	private void Bookmark(User user, String bookmarkUrl, String bookmarkSelect, String bookmarkName)
	{
		
		if(bookmarkSelect.equals("add"))
		{
			Bookmark bookmark = new Bookmark();
			bookmark.setName(bookmarkName);
			bookmark.setUrl(bookmarkUrl);
			if(bookmarkName.equals(""))
				bookmarkName = "이름 없음";
			mySqlCon.insertDB("INSERT bookmark(user_id, url, name) VALUES ('" + user.getUserId() + "','" + bookmarkUrl + "','" + bookmarkName +  "')");
			user.setUserBookmark(bookmark);
		}
		else if(bookmarkSelect.equals("remove"))
		{
			mySqlCon.insertDB("DELETE FROM bookmark WHERE user_id='" + user.getUserId() + "' AND url='" + bookmarkUrl + "'");
			for(int i = 0; i < user.getBookmark().size(); i++)
			{
				if(user.getBookmark().get(i).getUrl().equals(bookmarkUrl))
				{
					user.getBookmark().remove(i);
					break;
				}
			}
		}
	}
	
	@RequestMapping(value = "/SearchResult", method = RequestMethod.GET)
	public ModelAndView SearchResult(
			@RequestParam(value = "searchKeyword", defaultValue = "") String searchKeyword,
			@RequestParam(value = "start", defaultValue = "0") String start,
			@RequestParam(value = "bookmarkUrl", defaultValue = "null") String bookmarkUrl,
			@RequestParam(value = "bookmarkSelect", defaultValue = "null") String bookmarkSelect,
			@RequestParam(value = "bookmarkName", defaultValue = "") String bookmarkName,
			HttpServletRequest request,
			HttpServletResponse response) {
		/*
		 * String keyword = request.getParameter("searchKeyword"); String
		 * startTemp = (request.getParameter("start") == null) ? "0" :
		 * request.getParameter("start");
		 */
		HttpSession session = request.getSession(true);
		User user = null;
		user = (User) session.getAttribute("user");
		
		int startNum = Integer.parseInt(start);
		String searchKeywordNotEncode = "";
		URL url = null;
		SearchResult[] resultList = new SearchResult[10];
		int numFound = 0;
		mySqlCon = new MySqlConnection();
		ModelAndView mv = new ModelAndView();
		
		if(user == null)
		{
			mv.setViewName("Login");
			return mv;
		}

		try {
			searchKeywordNotEncode = new String(searchKeyword.getBytes("8859_1"),"UTF-8");
			searchKeyword = URLEncoder.encode(searchKeywordNotEncode, "UTF-8");
			bookmarkName = new String(bookmarkName.getBytes("8859_1"),"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		naverParse(start, bookmarkUrl, bookmarkSelect, searchKeywordNotEncode);

		Bookmark(user, bookmarkUrl, bookmarkSelect, bookmarkName);
		
		try {//
			url = new URL(
					"http://ec2-54-249-102-156.ap-northeast-1.compute.amazonaws.com:8983/solr/collection1/select?q="
							+ "*"
							+ searchKeyword
							+ "*"
							+ "&start="
							+ startNum
							+ "&wt=json&indent=true");

			JSONObject searchResult = (JSONObject) JSONValue
					.parse(new InputStreamReader(url.openConnection()
							.getInputStream(), "UTF-8"));

			JSONObject responseObject = (JSONObject) searchResult.get("response");
			numFound = Integer.parseInt(responseObject.get("numFound").toString());
			if (numFound != 0) {
				JSONArray resultArray = (JSONArray) responseObject.get("docs");
				
				for (int i = 0; i < resultArray.size(); i++) {
					JSONObject resultBuff = (JSONObject) resultArray.get(i);
					resultList[i] = new SearchResult();

					resultList[i].setIndex(startNum + i);
					String str = (String) resultBuff.get("content");
					resultList[i].setContent((str.length() > 200 ? str
							.substring(0, 200) : str));
					resultList[i].setContent(resultList[i].getContent()
							+ " ...");
					// sr.setContent((String) resultBuff.get("content"));
					resultList[i].setTitle((String) resultBuff.get("title"));
					resultList[i].setUrl(URLDecoder.decode((String) resultBuff.get("url"), "UTF-8"));
					/*
					 * sr.setBoost((double) resultBuff.get("boost"));
					 * sr.setDigest((String) resultBuff.get("digest"));
					 * sr.setTstamp((String) resultBuff.get("tstamp"));
					 * sr.setId((String) resultBuff.get("id"));
					 * 
					 * JSONArray anchorArray = (JSONArray) resultBuff
					 * .get("anchor"); if (anchorArray != null)
					 * sr.setAnchor(anchorArray.toString());
					 * sr.setVersion((long) resultBuff.get("_version_"));
					 */

					for(int j=0; j < user.getBookmark().size(); j++)
					{
						if(user.getBookmark().get(j).getUrl().equals(resultList[i].getUrl()))
						{
							resultList[i].setBookmark("true");
							break;
						}
						else
							resultList[i].setBookmark("false");
					}
				}
			} else {
				resultList[0] = new SearchResult();
				resultList[0].setContent("검색 결과가 없습니다.");
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// SearchResult sr = new SearchResult(searchKeyword, titleArray,
		// descriptionArray, keywordArray);
		ApplicationContext ctx = new GenericXmlApplicationContext("SpringConfig.xml");
		MongoOperations mongoOperation = (MongoOperations)ctx.getBean("mongoTemplate");
		String json_data = "[ ]";
		GroupByResults<Keyword> results = null;
		results = mongoOperation.group(where("search_keyword").is(searchKeywordNotEncode),"keyword",
		        GroupBy.key("relation_keyword").initialDocument("{ count: 0 }").reduceFunction("function(curr, result) { result.count += 1 }"), 
		        Keyword.class);

		json_data = results.getRawResults().get("retval").toString();

		mv.addObject("resultList", resultList);
		mv.addObject("searchKeyword", searchKeywordNotEncode);
		mv.addObject("start", start);
		mv.addObject("numFound", numFound);
		mv.addObject("cloud", json_data);
		
		mv.setViewName("SearchResult");
		
		return mv;
	}
}
