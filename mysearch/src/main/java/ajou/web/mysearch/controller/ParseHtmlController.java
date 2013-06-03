package ajou.web.mysearch.controller;

import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import ajou.web.mysearch.model.ParseHtml;

import com.ibm.icu.text.CharsetDetector;

@Controller
@SessionAttributes
public class ParseHtmlController {
	private MongoTemplate mongoTemplate;

	@RequestMapping(value = "/parseHtml", method = RequestMethod.POST)
	public ModelAndView parseUrl(@RequestParam("url") String urlString)
			throws IOException {
		String title = null;
		String description = null;
		String keyword = null;
		Document doc;
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

		titleArray = title.replaceAll("\\p{Punct}", "").split("\\s+");
		if (description != null) {
			descriptionArray = description.replaceAll("\\p{Punct}", "").split("\\s+");
		}
		if (keyword != null) {
			keywordArray = keyword.split(",\\s*");
		}

		ParseHtml ph = new ParseHtml(titleArray, descriptionArray, keywordArray);

		return new ModelAndView("addUrl", "command", ph);
	}
}
