/*

The Martus(tm) free, social justice documentation and
monitoring software. Copyright (C) 2001-2003, Beneficent
Technology, Inc. (Benetech).

Martus is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either
version 2 of the License, or (at your option) any later
version with the additions and exceptions described in the
accompanying Martus license file entitled "license.txt".

It is distributed WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, including warranties of fitness of purpose or
merchantability.  See the accompanying Martus License and
GPL license for more details on the required license terms
for this software.

You should have received a copy of the GNU General Public
License along with this program; if not, write to the Free
Software Foundation, Inc., 59 Temple Place - Suite 330,
Boston, MA 02111-1307, USA.

*/

package org.martus.amplifier.common;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import org.martus.amplifier.search.SearchConstants;
import org.martus.amplifier.velocity.AmplifierServletRequest;

public class SearchParameters implements SearchResultConstants, SearchConstants
{
	public SearchParameters(AmplifierServletRequest request, HashMap fields)
	{
		searchRequest = request;
		searchFields  = fields;
		loadFromRequest();		
	}
	
	private void loadFromRequest()
	{
		for(int i=0; i< ADVANCED_KEYS.length; i++)
		{
			String value = getParameterValue(ADVANCED_KEYS[i]);
			if (value != null)
			{		
				resultList.put(ADVANCED_KEYS[i], value);				
			}
		}
		
		if (!containsKey(RESULT_START_YEAR_KEY) || 
			!containsKey(RESULT_START_MONTH_KEY) ||
			!containsKey(RESULT_START_DAY_KEY) ||
			!containsKey(RESULT_END_YEAR_KEY) ||
			!containsKey(RESULT_END_MONTH_KEY) ||
			!containsKey(RESULT_END_DAY_KEY))
		{				
				hasEventFields = false;				
		}				
		
		setEventDate();
		setFilterKeyWords();
		setNormalFields();																	
	}	

	private void addField(String key, Object value)
	{
		searchFields.put(key, value);
	}	
	
	private void setFilterKeyWords()
	{
		String filterString = getValue(RESULT_FILTER_BY_KEY);
		if (filterString.equals(ANYWORD_TAG))
			return;

		addField(RESULT_FILTER_BY_KEY, filterString); 		

		String queryString = (String) searchFields.get(RESULT_ADVANCED_QUERY_KEY);
		String newString = convertToQueryString(queryString, filterString);
		
		searchFields.remove(RESULT_ADVANCED_QUERY_KEY);
		addField(RESULT_ADVANCED_QUERY_KEY, newString);

//		newFilterFormat();
	}
	
	// TODO: Leave this as a warning for now. 
	// Chiawei should fix it and remove this comment by 10/03
	private void newFilterFormat()
	{
		parseAdvancedQuery(THESE_WORD_TAG);					
		parseAdvancedQuery(EXACTPHRASE_TAG);	
		parseAdvancedQuery(ANYWORD_TAG);
		parseAdvancedQuery(WITHOUTWORDS_TAG);
	
		if (advancedQuery.length() > 0)
		{			
			addField(RESULT_ADVANCED_QUERY_KEY, advancedQuery);
		}
	}
	
	
	private void parseAdvancedQuery(String key)
	{
		String subQuery = getValue(key);
		if (subQuery.length() >0)
		{			
			
			if (advancedQuery.length() > 1)
			{
				advancedQuery += " ";
				if (!key.equals(WITHOUTWORDS_TAG))							
					advancedQuery += PLUS;
			}
			advancedQuery += convertToQueryString(subQuery, key);
		}
	}
	
	public static String convertToQueryString(String text, String filterType)
	{
		String newString = null;
		if (filterType.equals(WITHOUTWORDS_TAG))
			newString = addSign(NOT, text);			
		else if (filterType.equals(EXACTPHRASE_TAG))
			newString = "\""+text+"\"";
		else if (filterType.equals(THESE_WORD_TAG))
			newString = addSign(PLUS, text);
		else
			newString = "("+text+")";
	
		return newString;
	}
	
//	public static String newConvertToQueryString(String text, String filterType)
//	{
//		String newString = null;
//		if (filterType.equals(RESULT_EXCLUDEWORDS_QUERY_KEY))
//			newString = NOT + "("+text+")";
//		else if (filterType.equals(RESULT_EXACTPHRASE_QUERY_KEY))
//			newString = "\""+text+"\"";
//		else if (filterType.equals(RESULT_ALLWORDS_QUERY_KEY))
//			newString = addSign(PLUS, text);
//		else
//			newString = "("+text+")";
//			
//		return newString;
//			
//	}
	
	private static String addSign(String sign, String queryString)
	{
		String[] words = queryString.split(" ");
		String query = "(";
		
		if (words.length == 1)
			return query+ queryString+ ")";

		for (int i=0;i<words.length;i++)		
			query += sign + words[i]+ " ";

		return query + ")";
	}
	
	private void setEventDate()
	{
		Date startDate	= getStartDate();
		Date endDate	= getEndDate();

		if (startDate != null && endDate != null)
		{			
			addField(SEARCH_EVENT_START_DATE_INDEX_FIELD, startDate);
			addField(SEARCH_EVENT_END_DATE_INDEX_FIELD, endDate);
		}	
	}

	private void setNormalFields()
	{
		addField(RESULT_FIELDS_KEY, getValue(RESULT_FIELDS_KEY));
		
		String languageString = getValue(RESULT_LANGUAGE_KEY);
		if (languageString != null && !languageString.equals(LANGUAGE_ANYLANGUAGE_LABEL))
			addField(RESULT_LANGUAGE_KEY, languageString);
			
		Date entryDate = getEntryDate(getValue(RESULT_ENTRY_DATE_KEY));
		addField(SEARCH_ENTRY_DATE_INDEX_FIELD, entryDate);
		
		String sortByString = getValue(RESULT_SORTBY_KEY);
		if (sortByString != null)
			addField(RESULT_SORTBY_KEY, sortByString);	
	}

	public static Date getEntryDate(String dayString)
	{
		if (dayString.equals(ENTRY_ANYTIME_TAG))
			return getDate(1970, 1,1);
			
		int days = Integer.parseInt(dayString);	
		GregorianCalendar today = new GregorianCalendar();
		today.add(Calendar.DATE, -days);

		return today.getTime();
	}
	
	public boolean containsKey(String key)
	{
		return resultList.containsKey(key);
	}	
		
	private String getParameterValue(String param)
	{
		return searchRequest.getParameter(param);
	}
	
	public String getValue(String key)
	{
		return (String) resultList.get(key);
	}	
	
	public Collection getSearchResultValues()
	{
		return resultList.values();
	}
	
	public boolean hasEventFieldKeys()
	{
		return hasEventFields;
	}
	
	public Date getStartDate()
	{	
		if (hasEventFieldKeys())
		 return getDate(Integer.parseInt(getValue(RESULT_START_YEAR_KEY)),					
		               	Integer.parseInt(getValue(RESULT_START_MONTH_KEY)),
		               	Integer.parseInt(getValue(RESULT_START_DAY_KEY)));
		return null;
	}
		
	public Date getEndDate()
	{	
		if (hasEventFieldKeys())
			return getDate(Integer.parseInt(getValue(RESULT_END_YEAR_KEY)),					
							Integer.parseInt(getValue(RESULT_END_MONTH_KEY)),
							Integer.parseInt(getValue(RESULT_END_DAY_KEY)));		
		return null;
	}
	
	public static Date getDate(int year, int month, int day)
	{
		return new GregorianCalendar(year, month, day).getTime();
	}	

	AmplifierServletRequest searchRequest;
	HashMap resultList 	= new HashMap();
	HashMap	searchFields;
	boolean hasEventFields 		= true;
	String advancedQuery = PLUS;
	final static String PLUS 	= "+";
	final static String NOT 	= "-";
}
