package org.martus.amplifier.service.search;

/**
 * @author Daniel Chu
 *
 * This interface holds all the shared constants and imports
 * for the Martus Amplifier searching code
 * 
 */
public interface IBulletinConstants
{

	public static final String DEFAULT_FILES_LOCATION = 
		"amplifierdata";
	public static final String DEFAULT_INDEX_LOCATION = 
		"C:\\amplifierindex";
		
	// Martus Amplifier Field Constants
	public static final String AUTHOR_FIELD = "author";
	public static final String VISIBLE_AUTHOR_FIELD = "Author";
	public static final String KEYWORDS_FIELD = "keywords";
	public static final String VISIBLE_KEYWORDS_FIELD = "Keywords";
	public static final String TITLE_FIELD = "title";
	public static final String VISIBLE_TITLE_FIELD = "Title";
	public static final String EVENT_DATE_FIELD = "event_date";
	public static final String VISIBLE_EVENT_DATE_FIELD = "Event Date";
	public static final String PUBLIC_INFO_FIELD = "public_info";
	public static final String VISIBLE_PUBLIC_INFO_FIELD = "Public Info";
	public static final String SUMMARY_FIELD = "summary";
	public static final String VISIBLE_SUMMARY_FIELD = "Summary";
	public static final String LOCATION_FIELD = "location";
	public static final String VISIBLE_LOCATION_FIELD = "Location";
	public static final String ENTRY_DATE_FIELD = "entry_date";
	public static final String VISIBLE_ENTRY_DATE_FIELD = "Entry Date";	
	
	public static final String[] BULLETIN_FIELDS = 
		{AUTHOR_FIELD, KEYWORDS_FIELD, TITLE_FIELD, EVENT_DATE_FIELD, 
			PUBLIC_INFO_FIELD, SUMMARY_FIELD, LOCATION_FIELD, ENTRY_DATE_FIELD};
	public static final String[] VISIBLE_BULLETIN_FIELDS = 
		{VISIBLE_AUTHOR_FIELD, VISIBLE_KEYWORDS_FIELD, VISIBLE_TITLE_FIELD, VISIBLE_EVENT_DATE_FIELD, 
			VISIBLE_PUBLIC_INFO_FIELD, VISIBLE_SUMMARY_FIELD, VISIBLE_LOCATION_FIELD, VISIBLE_ENTRY_DATE_FIELD};

}
