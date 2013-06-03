package ajou.web.mysearch.model;

import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;

public class ParseNaverDictionaryUrlParse {
	private Vector<StringBuffer> meta;
	private Vector<StringBuffer> content;
	private StringBuffer metaBuff;
	private StringBuffer contentBuff;
	private StringBuffer buff;
	private int outStreamMeta;
	private int outStreamContent;
	private int endCounter;
	private boolean find[];
	private enum FINDNUM{FINDMETADATA, FINDCONTENTDATA, FINDCONTENTDATA_FIRST, FINDCONTENTDATA_TAG};
	
	public ParseNaverDictionaryUrlParse()
	{
		meta = new Vector<StringBuffer>();
		content = new Vector<StringBuffer>();
		metaBuff = new StringBuffer();
		contentBuff = new StringBuffer();
		buff = new StringBuffer();
		find = new boolean[FINDNUM.values().length];
		outStreamMeta = 0;
		outStreamContent = 0;
	}
	
	protected void initUrlParse()
	{
		for(int i = 0; i < FINDNUM.values().length; i++)
			find[i] = false;
		endCounter = 0;
		metaBuff = new StringBuffer();
		contentBuff = new StringBuffer();
		if(buff.length() != 0)
			buff.delete(0, buff.length());
	}
	
	public void getURL(String Url){
		URLConnection con = null;
		InputStreamReader in = null;
		initUrlParse();
		try {
			URL url = new URL(Url);

			con = url.openConnection();
			con.connect();
			in = new InputStreamReader(con.getInputStream(), "UTF-8");

			int c;
			System.out.println("In Model.UrlParse.getURL(String Url) Finding MetaData And ContentData..");
			while ((c = in.read()) != -1) {
/*				if(find[FINDNUM.FINDMETADATA.ordinal()] == false)
					getMeta((char) c);*/
				if(find[FINDNUM.FINDCONTENTDATA.ordinal()] == false)
					getContent((char) c);
				else
					break;
			}
			metaTagDelete();
			meta.add(metaBuff);
			content.add(contentBuff);
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public StringBuffer toStringMeta()
	{
		if(outStreamMeta < meta.size())
		{
			this.outStreamMeta++;
			return meta.get(outStreamMeta - 1);
		}
		else
			return null;
	}
	
	public StringBuffer toStringContent()
	{
		if(outStreamContent < content.size())
		{
			this.outStreamContent++;
			return content.get(outStreamContent - 1);
		}
		else
			return null;
	}
	
	/* 아래 부분은 해당 html에서 내용을 추출하기 위함임.
	 * meta의 경우는 공통적으로 og:description 다음에, content의 경우는 공통적으로 window.onload = function() 다음에 나오게 된다.
	 * 
	 * 좀 거지같긴한데 우리가 백과사전을 가지고 있었다면 이런짓은 안해도 될거라는 생각으로 작성..*/
	protected void getMeta(char c)
	{
		if(c == '\n')
		{
			if(buff.indexOf("og:description") != -1)
			{
				metaBuff.append(buff);
				find[FINDNUM.FINDMETADATA.ordinal()] = true;
			}
			buff.delete(0, buff.length());
		}
		buff.append(c);
	}
	
	protected void metaTagDelete()
	{
		if(metaBuff.length() != 0)
		{
			if(metaBuff.indexOf("<meta") != 0)
			{
				metaBuff.delete(0, metaBuff.indexOf("content=\"") + 9);
				metaBuff.delete(metaBuff.length()-2, metaBuff.length());
			}
		}
	}
	
	protected void getContent(char c)
	{
		if(find[FINDNUM.FINDCONTENTDATA_FIRST.ordinal()] == true)
		{
			if(c == '\n')
			{
				//buff.append(c);
				deleteTagFromContent();
				if(buff.length() == 0)
					endCounter++;
				else
				{
					endCounter = 0;
					contentBuff.append(buff);
					buff.delete(0, buff.length());
				}
				if(endCounter > 10 && contentBuff.length() > 100)
					find[FINDNUM.FINDCONTENTDATA.ordinal()] = true;
			}
			else
				buff.append(c);
		}
		else
		{
			buff.append(c);
			if(buff.indexOf("window.onload = function()") != -1)
			{
				if(buff.indexOf("</script>") != -1)
				{
					//contentBuff.append(buff);
					buff.delete(0, buff.length());
					find[FINDNUM.FINDCONTENTDATA_FIRST.ordinal()] = true;
				}
			}
			else if(c == '\n')
				buff.delete(0, buff.length());
		}
	}
	
	protected void deleteTagFromContent()
	{
		int index = 0;
		boolean complete = true;
		
		while(complete && buff.length() != 0)
			if((index = buff.indexOf("\t")) != -1)
				buff.delete(index, index + 1);
			else if((index = buff.indexOf("\r")) != -1)
				buff.delete(index, index + 1);
			else if((index = buff.indexOf("<div")) != -1 || (index = buff.indexOf("</div")) != -1)
				deleteTagInIndex(index);
			else if((index = buff.indexOf("<ul")) != -1 || (index = buff.indexOf("</ul")) != -1)
				deleteTagInIndex(index);
			else if((index = buff.indexOf("<p")) != -1 || (index = buff.indexOf("</p")) != -1)
				deleteTagInIndex(index);
			else if((index = buff.indexOf("<li")) != -1 || (index = buff.indexOf("</li")) != -1)
				deleteTagInIndex(index);
			else if((index = buff.indexOf("<a")) != -1 || (index = buff.indexOf("</a")) != -1)
				deleteTagInIndex(index);
			else if((index = buff.indexOf("<img")) != -1 || (index = buff.indexOf("</img")) != -1)
				deleteTagInIndex(index);
			else if((index = buff.indexOf("<span")) != -1 || (index = buff.indexOf("</span")) != -1)
				deleteTagInIndex(index);
			else if((index = buff.indexOf("<strong")) != -1 || (index = buff.indexOf("</strong")) != -1)
				deleteTagInIndex(index);
			else if((index = buff.indexOf("<br")) != -1 || (index = buff.indexOf("</br")) != -1)
				deleteTagInIndex(index);
			else if((index = buff.indexOf("<h")) != -1 || (index = buff.indexOf("</h")) != -1)
				deleteTagInIndex(index);
			else if((index = buff.indexOf("<ol")) != -1 || (index = buff.indexOf("</ol")) != -1)
				deleteTagInIndex(index);
			else
				complete = false;
	}
	
	protected void deleteTagInIndex(int index)
	{
		buff.delete(index, buff.indexOf(">", index) + 1);
		//endCounter = 0;
	}
}
