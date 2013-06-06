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
//			System.out.println("In Model.UrlParse.getURL(String Url) Finding MetaData And ContentData..");
			while ((c = in.read()) != -1) {
/*				if(find[FINDNUM.FINDMETADATA.ordinal()] == false)
					getMeta((char) c);*/
				if(find[FINDNUM.FINDCONTENTDATA.ordinal()] == false)
					getContent((char) c);
				else
					break;
			}
//			metaTagDelete();
//			meta.add(metaBuff);
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
	
	/* �꾨옒 遺�텇���대떦 html�먯꽌 �댁슜��異붿텧�섍린 �꾪븿��
	 * meta��寃쎌슦��怨듯넻�곸쑝濡�og:description �ㅼ쓬�� content��寃쎌슦��怨듯넻�곸쑝濡�window.onload = function() �ㅼ쓬���섏삤寃��쒕떎.
	 * 
	 * 醫�嫄곗�媛숆릿�쒕뜲 �곕━媛�諛깃낵�ъ쟾��媛��怨��덉뿀�ㅻ㈃ �대윴吏볦� �덊빐���좉굅�쇰뒗 �앷컖�쇰줈 �묒꽦..*/
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
			else if((index = buff.indexOf("<em")) != -1 || (index = buff.indexOf("</em")) != -1)
				deleteTagInIndex(index);
			else if((index = buff.indexOf("<dl")) != -1 || (index = buff.indexOf("</dl")) != -1)
				deleteTagInIndex(index);
			else if((index = buff.indexOf("<dt")) != -1 || (index = buff.indexOf("</dt")) != -1)
				deleteTagInIndex(index);
			else if((index = buff.indexOf("<dd")) != -1 || (index = buff.indexOf("</dd")) != -1)
				deleteTagInIndex(index);
			else if((index = buff.indexOf("<B")) != -1 || (index = buff.indexOf("</B")) != -1)
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
