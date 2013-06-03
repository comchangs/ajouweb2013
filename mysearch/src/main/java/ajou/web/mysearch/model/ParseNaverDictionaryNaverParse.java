package ajou.web.mysearch.model;

import java.io.IOException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class ParseNaverDictionaryNaverParse {
	private String url;
	private int index;
	private int length;
	
	private Element element;
	private NodeList list;
	
	public void NaverParseInit(String url)
	{
		this.url = url;
		index = 0;
		length = 0;
		
		if(!url.isEmpty())
			initContents(this.url);
		else
			System.out.println("!!!URL Missing in NaverParseInit!!!");
	}
	
	public String convertNaverAPIUrl(String naverKey, String keyword)
	{
		//10개 이외의 값들은 대부분 쓰레기.
		return "http://openapi.naver.com/search?key=" + naverKey + "&"
				+ "query=" + keyword + "&" + "target=encyc" + "&" + "display=10";
	}
	
	public String getContent(String tagName)
	{
		if(index < length)
		{
			element = (Element)list.item(index);
			return getContent(element, tagName);
		}
		else
			return "";
	}
	
	public boolean isLast()
	{
		if(index < length)
			return false;
		else
			return true;
	}
	
	public void setIndexFirst()
	{
		this.index = 0;
	}
	
	public void setNext()
	{
		this.index++;
	}
	
	public String getURL()
	{
		return this.url;
	}
	
	protected void initContents(String url){
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		try{
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(url);
			Element root = doc.getDocumentElement();
			list = root.getElementsByTagName("item");
			
			length = list.getLength();
		}
		catch(ParserConfigurationException e) {
			e.printStackTrace();
		} catch(SAXException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	protected String getContent(Element element, String tagName) {
		NodeList clist = element.getElementsByTagName(tagName);
		Element cElement = (Element) clist.item(0);
	
		if (cElement.getFirstChild() != null) {
			return cElement.getFirstChild().getNodeValue();
		} else {
			return "";
		}
	}

}
