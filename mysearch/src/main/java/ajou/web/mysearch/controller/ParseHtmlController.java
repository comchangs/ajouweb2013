package ajou.web.mysearch.controller;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import ajou.web.mysearch.model.Keywords;

import com.ibm.icu.text.CharsetDetector;

@Controller
@SessionAttributes
public class ParseHtmlController {

	@RequestMapping(value = "/parseHtml", method = RequestMethod.GET)
	public String parseUrl(@RequestParam("keyword") String searchKeyword, @RequestParam("url") String urlString)
			throws IOException {
		String title = null;
		String description = null;
		String keyword = null;
		Document doc;
		searchKeyword = new String(searchKeyword.getBytes("8859_1"),"UTF-8");
		urlString = new String(urlString.getBytes("8859_1"),"UTF-8");
		int slashslash = urlString.indexOf("//") + 2;
		String protocol = urlString.substring(0, slashslash);
		String hostname = urlString.substring(slashslash, urlString.indexOf('/', slashslash));
		String etc = urlString.substring(protocol.length() + hostname.length() + 1, urlString.length());
		boolean bln = Pattern.matches("^[a-zA-Z0-9/.]*$", etc);
		if(!bln) {
			etc = URLEncoder.encode(etc);
		}
		urlString = protocol + hostname + "/" + etc;
		URL url = new URL(urlString);
		CharsetDetector charset = new CharsetDetector();
		charset.setText(IOUtils.toByteArray(url.openStream()));
		doc = Jsoup.parse(url.openStream(), charset.detect().getName(),
				urlString);

		title = doc.title();
		if (!doc.select("meta[name=description]").isEmpty()) {
			description = doc.select("meta[name=description]").get(0)
					.attr("content");
		}
		if (!doc.select("meta[name=keywords]").isEmpty()) {
			keyword = doc.select("meta[name=keywords]").get(0).attr("content");
		}

		String[] titleArray = null;
		String[] descriptionArray = null;
		String[] keywordArray = null;

		if(title != null) {
			titleArray = title.replaceAll("\\p{Punct}", "").split("\\s+");
		}
		if (description != null) {
			descriptionArray = description.replaceAll("\\p{Punct}", "").split("\\s+");
		}
		if (keyword != null) {
			keywordArray = keyword.split(",\\s*");
		}
		
		ApplicationContext ctx = new GenericXmlApplicationContext("SpringConfig.xml");
		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		String[] utcTime = sdf.format(new Date()).split(" ");
		String timestamp = utcTime[0]+"T"+utcTime[1]+"Z";
		
		for(int i = 0; i < titleArray.length; i++) {
			Keywords keywords = new Keywords(searchKeyword, titleArray[i], timestamp);
			mongoOperation.insert(keywords);
		}
		if(descriptionArray != null) {
			for(int i = 0; i < descriptionArray.length; i++) {
				Keywords keywords = new Keywords(searchKeyword, descriptionArray[i], timestamp);
				mongoOperation.insert(keywords);
			}
		}
		if(keywordArray != null) {
			for(int i = 0; i < keywordArray.length; i++) {
				Keywords keywords = new Keywords(searchKeyword, keywordArray[i], timestamp);
				mongoOperation.insert(keywords);
			}
		}
		
		
		// ParseHtml ph = new ParseHtml(searchKeyword, titleArray, descriptionArray, keywordArray);
		return new String("redirect:" + urlString);
	}
}
