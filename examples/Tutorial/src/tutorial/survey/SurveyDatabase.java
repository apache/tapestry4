/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000 by Howard Ship and Primix Solutions
 *
 * Primix Solutions
 * One Arsenal Marketplace
 * Watertown, MA 02472
 * http://www.primix.com
 * mailto:hship@primix.com
 * 
 * This library is free software.
 * 
 * You may redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation.
 *
 * Version 2.1 of the license should be included with this distribution in
 * the file LICENSE, as well as License.html. If the license is not
 * included with this distribution, you may find a copy at the FSF web
 * site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
 * Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

package tutorial.survey;

import java.util.*;

/**
 *  @version $Id$
 *  @author Howard Ship
 *
 */ 

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