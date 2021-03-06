package ajou.web.mysearch.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.data.mongodb.core.MongoOperations;

import com.mongodb.BasicDBObject;

import ajou.web.mysearch.model.MySqlConnection;

public class ParseNaverDictionaryStringParse {
	
	private String keyword;
	private ArrayList<String> word;
	private ArrayList<String> relationWord;
	private ArrayList<String> resultArray;
	private String buff;
	private String DBDictionary;
	private MySqlConnection userDataMan;
	private MongoOperations mongoOperation;
	
	public void initStringParse(String keyword, MongoOperations mongoOperation)
	{
		this.keyword = keyword;
		this.userDataMan = new MySqlConnection();
		this.mongoOperation = mongoOperation;
		buff = "";
		word = new ArrayList<String>();
		relationWord = new ArrayList<String>();
		
		DBDictionary = "dictionary";
	}
	
	public void insertMongoDB(String relation)
	{		
		if(relation != null)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
			String[] utcTime = sdf.format(new Date()).split(" ");
			String timestamp = utcTime[0]+"T"+utcTime[1]+"Z";
			
			Keywords keywords = new Keywords(keyword, relation, timestamp);
			mongoOperation.insert(keywords);
		}
	}
	
	public void createRelationWord()
	{
		resultArray = new ArrayList<String>();
		relationWord.clear();
		if(!word.isEmpty())
		{
			for(int i = 0; i < word.size(); i++)
			{
				if(resultArray != null)
					resultArray.clear();
				buff = word.get(i);
				match(buff);
			}
		}
		buff = "";
		word.clear();
	}
	
	protected void match(String str)
	{
		if(searchRelationWord(str, "WHERE") == true)
			insertRelationKeywordArray();
/*		if(str != null)
		{
			if(str.length() >= 2)
			{
				if(searchRelationWord(str, "LIKE") == true)
					insertRelationKeywordArray();
			}
			else
			{
				if(searchRelationWord(str, "WHERE") == true)
					insertRelationKeywordArray();
			}
		}*/
		if(resultArray == null)
		{
			if(str.length() > 2)
			{
				str = str.substring(0, str.length() - 1);
				match(str);
			}
			else
				return;
		}
	}
	
	protected boolean searchRelationWord(String str, String Query)
	{
		if(Query.equals("WHERE"))
			resultArray = userDataMan.selectDb("SELECT word FROM " + DBDictionary + " WHERE" + " word=" + "'" + str + "'");
		else
			resultArray = userDataMan.selectDb("SELECT word FROM " + DBDictionary + " WHERE" + " word" + " LIKE" + "'%" + str + "%'");
		
		if(resultArray == null)
			return false;
		else
			return true;
	}
	
	protected void insertRelationKeywordArray()
	{
		for(int i = 0; i < resultArray.size(); i++)
		{
			if(!resultArray.get(i).equals(keyword))
			{
				relationWord.add(new String(resultArray.get(i)));
				insertMongoDB(resultArray.get(i));
			}
		}
	}
	
	public void stringSearchMeta(StringBuffer meta)
	{
		stringBufferCheck(meta);
	}
	
	public void stringSearchContent(StringBuffer content)
	{
		stringBufferCheck(content);
	}
	
	protected void stringBufferCheck(StringBuffer str)
	{
		int startIndex = 0, endIndex = 0;
		
		if(str != null)
		{
			while((endIndex = str.indexOf(" ", startIndex)) != -1)
			{
				buff = str.substring(startIndex, endIndex);
				stringCheck();
				startIndex = endIndex + 1;
			}
		}
	}
	
	protected void stringCheck()
	{
		boolean complete = false;
		int index = 0;
		
		while(!complete)
		{
			if((index = buff.indexOf(".")) != -1)
				subStringSave(index, 1);
			else if((index = buff.indexOf("��")) != -1)
				subStringSave(index, 1);
			else if((index = buff.indexOf("[")) != -1)
				subStringSave(index, 1);
			else if((index = buff.indexOf("]")) != -1)
				subStringSave(index, 1);
			else if((index = buff.indexOf("\"")) != -1)
				subStringSave(index, 1);
			else if((index = buff.indexOf(",")) != -1)
				subStringSave(index, 1);
			else if((index = buff.indexOf("(")) != -1)
				subStringSave(index, 1);
			else if((index = buff.indexOf(")")) != -1)
				subStringSave(index,1 );
			else if((index = buff.indexOf("!")) != -1)
				subStringSave(index, 1);
			else if((index = buff.indexOf("?")) != -1)
				subStringSave(index, 1);
			else if((index = buff.indexOf(" ")) != -1)
				subStringSave(index, 1);
			else if((index = buff.indexOf("\t")) != -1)
				subStringSave(index, 1);
			else if((index = buff.indexOf("&middot;")) != -1)
				subStringSave(index, 8);
			else if((index = buff.indexOf("&lsquo;")) != -1)
				subStringSave(index, 7);
			else if((index = buff.indexOf("&rsquo;")) != -1)
				subStringSave(index, 7);
			else
			{
				if(!buff.equals(""))
					word.add(new String(buff));
				complete = true;
			}
		}
	}

	protected void subStringSave(int index)
	{		
		if((index + 1) < buff.length())
		{
			word.add(new String(buff.substring(0, index)));
			buff = buff.substring(index + 1, buff.length());
		}
		else
			buff = buff.substring(0, index);
			
	}
	
	protected void subStringSave(int index, int wordNum)
	{
		if((index + wordNum) < buff.length())
		{
			if(!buff.substring(0, index).equals(""))
				word.add(new String(buff.substring(0, index)));
			buff = buff.substring(index + wordNum, buff.length());
		}
		else
			buff = buff.substring(0, index);
	}
	

}
