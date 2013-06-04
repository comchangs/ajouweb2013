package ajou.web.mysearch.controller;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ajou.web.mysearch.model.SearchResult;

public class SearchResultController {

	@RequestMapping(value = "/SearchResult", method = RequestMethod.POST)
	public ModelAndView SearchResult(
			@RequestParam("searchKeyword") String searchKeyword,
			@RequestParam("start") String start) {
		/*
		 * String keyword = request.getParameter("searchKeyword"); String
		 * startTemp = (request.getParameter("start") == null) ? "0" :
		 * request.getParameter("start");
		 */
		int startNum = Integer.parseInt((start == null) ? "0" : start);

		try {
			searchKeyword = URLEncoder.encode(searchKeyword, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		// keyword = new String(keyword.getBytes("EUC-KR"), "UTF-8");

		URL url = null;
		SearchResult[] resultList = new SearchResult[10];
		try {//
			url = new URL(
					"http://ec2-54-249-102-156.ap-northeast-1.compute.amazonaws.com:8983/solr/collection1/select?q="
							+ searchKeyword
							+ "&start="
							+ startNum
							+ "&wt=json&indent=true");

			JSONObject searchResult = (JSONObject) JSONValue
					.parse(new InputStreamReader(url.openConnection()
							.getInputStream(), "UTF-8"));

			if (searchResult != null) {
				// int numFound = (int) searchResult.get("numFound");
				JSONObject responseObject = (JSONObject) searchResult.get("response");
				JSONArray resultArray = (JSONArray) responseObject.get("docs");

				for (int i = 0; i < resultArray.size(); i++) {
					JSONObject resultBuff = (JSONObject) resultArray.get(i);

					String str = (String) resultBuff.get("content");
					resultList[i].setTitle((str.length() > 300 ? str.substring(0, 300) : str));
					// sr.setContent((String) resultBuff.get("content"));
					resultList[i].setTitle((String) resultBuff.get("title"));
					resultList[i].setUrl((String) resultBuff.get("url"));
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
				}
			} else
				;
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
		mv.setViewName("SearchResult");
		return mv;
	}
}
