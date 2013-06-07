package ajou.web.mysearch.controller;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ajou.web.mysearch.model.SearchResult;
import ajou.web.mysearch.model.MySqlConnection;
import ajou.web.mysearch.model.User;

@Controller
public class SearchResultController {
	
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
	
	
	@RequestMapping(value = "/SearchResult", method = RequestMethod.GET)
	public ModelAndView SearchResult(
			@RequestParam(value = "searchKeyword", defaultValue = "") String searchKeyword,
			@RequestParam(value = "start", defaultValue = "0") String start,
			@RequestParam(value = "bookmarkUrl", defaultValue = "null") String bookmarkUrl,
			@RequestParam(value = "bookmarkSelect", defaultValue = "null") String bookmarkSelect,
			@RequestParam(value = "userId", defaultValue = "") String userId) {
		/*
		 * String keyword = request.getParameter("searchKeyword"); String
		 * startTemp = (request.getParameter("start") == null) ? "0" :
		 * request.getParameter("start");
		 */
		if(userId.equals(""))
		{
			ModelAndView mv = new ModelAndView();
			
			mv.setViewName("Login");
			
			return mv;
		}
		
		int startNum = Integer.parseInt(start);
		String searchKeywordNotEncode = "";

		try {
			searchKeywordNotEncode = new String(searchKeyword.getBytes("8859_1"),"UTF-8");
			searchKeyword = URLEncoder.encode(searchKeywordNotEncode, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		// keyword = new String(keyword.getBytes("EUC-KR"), "UTF-8");

		URL url = null;
		SearchResult[] resultList = new SearchResult[10];
		int numFound = 0;
		MySqlConnection mySqlCon = null;
		
		if(start.equals("0") && bookmarkUrl.equals("null") && bookmarkSelect.equals("null"))
		{
			ParseNaverDictionaryController naver = new ParseNaverDictionaryController();
			naver.parseNaverDictionary(searchKeywordNotEncode);
		}
		
		/*
		if(bookmarkSelect.equals("add"))
			mySqlCon.insertDB("INSERT user_bookmark ..." + bookmarkUrl);
		else if(bookmarkSelect.equals("remove"))
			mySqlCon.insertDB("DELETE user_bookmark ..." + bookmarkUrl);*/
		
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
				mySqlCon = new MySqlConnection();
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
					resultList[i].setUrl((String) resultBuff.get("url"));
					
					resultList[i].setBookmark("false");
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
					/*
					ArrayList<String> bookmark = mySqlCon.selectDb("SELECT count(*) "+ 
					"FROM user Join userbookmark on user.userid = userbookmark.userid" +
					"WHERE userid=" + userid + " AND bookmark=" + resultList[i].getUrl());
					if(Integer.parseInt(bookmark.get(0)) > 0)
						resultList[i].setBookmark(true);
					else
						resultList[i].setBookmark(false);*/ 
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

		ModelAndView mv = new ModelAndView();
		mv.addObject("resultList", resultList);
		mv.addObject("searchKeyword", searchKeywordNotEncode);
		mv.addObject("start", start);
		mv.addObject("numFound", numFound);
		
		mv.setViewName("SearchResult");
		
		return mv;
	}
}
