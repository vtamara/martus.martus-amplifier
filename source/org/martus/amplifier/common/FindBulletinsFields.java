
package org.martus.amplifier.common;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

import org.martus.amplifier.search.SearchConstants;


public class FindBulletinsFields implements SearchConstants, SearchResultConstants
{
	public static Vector getFindWordFilterDisplayNames()
	{
		Vector filters = new Vector();
		filters.add(new ChoiceEntry(THESE_WORD_KEY, THESE_WORD_LABEL));
		filters.add(new ChoiceEntry(EXACTPHRASE_KEY, EXACTPHRASE_LABEL));
		filters.add(new ChoiceEntry(ANYWORD_KEY, ANYWORD_LABEL));
		filters.add(new ChoiceEntry(WITHOUTWORDS_KEY, WITHOUTWORDS_LABEL));

		return filters;	
	}
	
	public static Vector getFindEntryDatesDisplayNames()
	{
		Vector dates = new Vector();
		dates.add(new ChoiceEntry(ENTRY_ANYTIME_KEY, ENTRY_ANYTIME_LABEL));
		dates.add(new ChoiceEntry(ENTRY_PAST_WEEK_KEY, ENTRY_PAST_WEEK_DAYS_LABEL));
		dates.add(new ChoiceEntry(ENTRY_PAST_MONTH_KEY, ENTRY_PAST_MONTH_DAYS_LABEL));
		dates.add(new ChoiceEntry(ENTRY_PAST_3_MONTH_KEY,ENTRY_PAST_3_MONTH_DAYS_LABEL ));
		dates.add(new ChoiceEntry(ENTRY_PAST_6_MONTH_KEY, ENTRY_PAST_6_MONTH_DAYS_LABEL));
		dates.add(new ChoiceEntry(ENTYR_PAST_YEAR_KEY, ENTRY_PAST_YEAR_DAYS_LABEL));
		
		return dates;		
	}
	
	public static Vector getBulletinFieldDisplayNames()
	{
		Vector fields = new Vector();
		fields.add(new ChoiceEntry(ANYWHERE_IN_BULLETIN_KEY, IN_ALL_FIELDS));
		fields.add(new ChoiceEntry(IN_TITLE_KEY, SEARCH_TITLE_INDEX_FIELD));
		fields.add(new ChoiceEntry(IN_KEYWORDS_KEY, SEARCH_KEYWORDS_INDEX_FIELD));
		fields.add(new ChoiceEntry(IN_SUMMARY_KEY,SEARCH_SUMMARY_INDEX_FIELD ));
		fields.add(new ChoiceEntry(IN_AUTHOR_KEY, SEARCH_AUTHOR_INDEX_FIELD));
		fields.add(new ChoiceEntry(IN_DETAIL_KEY, SEARCH_DETAILS_INDEX_FIELD ));
		fields.add(new ChoiceEntry(IN_LOCATION_KEY, SEARCH_LOCATION_INDEX_FIELD ));
		fields.add(new ChoiceEntry(IN_ORGANIZATION_KEY, SEARCH_ORGANIZATION_INDEX_FIELD));
		
		return fields;
	}
	
	public static Vector getLanguageFieldDisplayNames()
	{
		Vector fields = new Vector();
		fields.add(new ChoiceEntry(LANGUAGE_ANYLANGUAGE_KEY, LANGUAGE_ANYLANGUAGE_KEY));
		fields.add(new ChoiceEntry(LANGUAGE_ENGLISH_KEY, "en"));
		fields.add(new ChoiceEntry(LANGUAGE_FRENCH_KEY, "fr"));
		fields.add(new ChoiceEntry(LANGUAGE_GERMAN_KEY, "de"));
		fields.add(new ChoiceEntry(LANGUAGE_INDONESIAN_KEY, "id" ));
		fields.add(new ChoiceEntry(LANGUAGE_RUSSIAN_KEY, "ru"));
		fields.add(new ChoiceEntry(LANGUAGE_SPANISH_KEY, "es" ));		
		
		return fields;
	}
	
	public static Vector getMonthFieldDisplayNames()
	{
		Vector fields = new Vector();
		for (int i=0;i< MONTH_NAMES.length;i++)	
			fields.add(new ChoiceEntry(MONTH_NAMES[i], new Integer(i).toString()));
					
		return fields;
	}
	
	public static Object getToday()
	{
		return new Today();
	}
	
	private static final String[] MONTH_NAMES = new String[] {
		"January", "February", "March", "April", "May", "June",
		"July", "August", "September", "October", "November", "December"
	};

	public static class Today
	{
		public static int getYear()
		{	
			return new GregorianCalendar().get(Calendar.YEAR);
		}
	
		public static String getMonth()
		{	
			int month = new GregorianCalendar().get(Calendar.MONTH);
			return new Integer(month).toString();
		}
	
		public static int getDay()
		{
			return new GregorianCalendar().get(Calendar.DATE);
		}				
		
		public Today(){}
		
	}

	public static class ChoiceEntry
	{
		public String getTag()	{return tagString;}
		public String getLabel() {return labelString;}

		public ChoiceEntry(String label, String tag)
		{
			labelString = label;
			tagString 	= tag;
		}		

		private String tagString;
		private String labelString;
	}	
}
