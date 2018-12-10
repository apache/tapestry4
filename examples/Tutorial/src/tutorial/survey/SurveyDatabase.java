package tutorial.survey;

import java.util.*;

/**
 *  Acts like an in-memory database of survey information.
 *
 */
 
public class SurveyDatabase
{
	private Map surveys = new HashMap();
	
	/**
	 *  Assigns a primary key to the survey, then stores a clone of it into
	 *  the database.
	 *
	 */
	 
	public void addSurvey(Survey survey)
	{
		Object key;
		
		key = allocateKey();
		survey.setPrimaryKey(key);
		
		surveys.put(key, survey.clone());
	}
	
	/**
	 *  Returns the number of surveys in the database.
	 *
	 */
	 
	public int getSurveyCount()
	{
		return surveys.size();
	}
	
	/**
	 *  Returns an array of <em>copies</em> of all {@link Survey}s stored in the
	 *  database.
	 *
	 */
	 
	public Survey[] getAllSurveys()
	{
		Collection all;
		Survey survey;
		Iterator i;
		Survey[] result;
		int count = 0;
		
		all = surveys.values();
		
		result = new Survey[all.size()];
		i = all.iterator();
		
		while (i.hasNext())
		{
			survey = (Survey)i.next();
			result[count++] = (Survey)survey.clone();
		}
			
		return result;	
	}
	
	/**
	 *  Returns a clone of the survey with the given primary key, or
	 *  null if not found.
	 *
	 */
	 
	public Survey getSurvey(Object primaryKey)
	{
		Survey survey;
	
		survey = (Survey)surveys.get(primaryKey);
		
		if (survey == null)
			return null;
			
		return (Survey)survey.clone();	
	}
	
	public void deleteSurvey(Object primaryKey)
	{
		surveys.remove(primaryKey);
	}
	
	
	/**
	 *  Stores a clone of the survey, replacing any previous version.
	 *
	 */
	 
	public void updateSurvey(Survey survey)
	{
		Object key;
		
		key = survey.getPrimaryKey();
		surveys.put(key, survey.clone());
	}
	
	private static int nextKey = 1000;
	
	private Object allocateKey()
	{
		return new Integer(nextKey++);
	}
}