package tutorial.survey;

import com.primix.tapestry.*;
import java.util.*;
import java.text.*;
import java.awt.Color;

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

/**
 *  @version $Id$
 *  @author Howard Ship
 *
 */ 

public class Results extends BasePage
{
	private SurveyDatabase surveyDatabase;
	private boolean oddRow = false;
	private NumberFormat percentFormat;
	
	public SurveyDatabase getDatabase()
	{
		if (surveyDatabase == null)
		{
            SurveyEngine myengine = (SurveyEngine)engine;
			
			surveyDatabase = myengine.getDatabase();
		}
		
		return surveyDatabase;
	}
	
	public void detach()
	{
		super.detach();
		
		surveyDatabase = null;
		oddRow = false;
	}
	
	public String getRowColor()
	{
		Color color;
		String result;
		
		if (oddRow)
			color = Color.lightGray;
		else
			color = Color.white;	
			
		result = RequestContext.encodeColor(color);
		
		oddRow = !oddRow;
		
		return result;
	}
	
	public List getResults()
	{
		int raceAfrican = 0;
		int raceAsian = 0;
		int raceCaucasian = 0;
		int raceInuit = 0;
		int raceMartian = 0;
		int sexAsexual = 0;
		int sexFemale  = 0;
		int sexMale = 0;
		int sexTransgender = 0;
		int likesCats = 0;
		int likesDogs = 0;
		int likesFerrits = 0;
		int likesTurnips = 0;
		int ageToTeen = 0; // 1 - 18
		int ageEarlyAdult = 0; // 19 - 28
		int ageToMiddle  = 0; // 29 - 35
		int ageMiddle = 0; // 36 - 49
		int ageOlder = 0; // 50 - 64
		int ageRetire = 0; // 65 - 80
		int ageOld = 0; // 81 - 100
		Survey[] surveys;
		Survey survey;
		List result;
		Race race;
		Sex sex;
		int count;
		int i;
		int age;
		
		surveys = getDatabase().getAllSurveys();
		if (surveys == null ||
			surveys.length == 0)
			return null;

		count = surveys.length;
		for (i = 0; i < count; i++)
		{
			survey = surveys[i];
			
			race = survey.getRace();
			if (race == Race.AFRICAN)
				raceAfrican++;
			
			if (race == Race.ASIAN)
				raceAsian++;
			
			if (race == Race.CAUCASIAN)
				raceCaucasian++;
				
			if (race == Race.INUIT)
				raceInuit++;
				
			if (race == Race.MARTIAN)
				raceMartian++;
				
			sex = survey.getSex();
			if (sex == Sex.MALE)
				sexMale++;
				
			if (sex == Sex.FEMALE)
				sexFemale++;
				
			if (sex == Sex.TRANSGENDER)
				sexTransgender++;
				
			if (sex == Sex.ASEXUAL)
				sexAsexual++;
			
			if (survey.getLikesCats())
				likesCats++;
				
			if (survey.getLikesDogs())
				likesDogs++;
				
			if (survey.getLikesFerrits())
				likesFerrits++;
				
			if (survey.getLikesTurnips())
				likesTurnips++;	
			
			age = survey.getAge();
			
			if (age < 19)
				ageToTeen++;
			
			if (age >= 19 && age <= 28)
				ageEarlyAdult++;
				
			if (age >= 29 && age <= 35)
				ageToMiddle++;
				
			if (age >= 36 && age <= 49)
				ageMiddle++;
			
			if (age >= 50 && age <= 64)
				ageOlder++;
			
			if (age >= 65 && age <= 80)
				ageRetire++;
			
			if (age >= 81)
				ageOld++;	
					
		}
		
		result = new ArrayList();
		
		result.add(buildResult("Sex : Male", sexMale, count));
		result.add(buildResult("Sex : Female", sexFemale, count));
		result.add(buildResult("Sex : Transgender", sexTransgender, count));
		result.add(buildResult("Sex : Asexual", sexAsexual, count));
		
		result.add(buildResult("Race : Caucasian", raceCaucasian, count));
		result.add(buildResult("Race : African", raceAfrican, count));
		result.add(buildResult("Race : Asian", raceAsian, count));
		result.add(buildResult("Race : Inuit", raceInuit, count));
		result.add(buildResult("Race : Martian", raceMartian, count));
		
		result.add(buildResult("Age: to 18", ageToTeen, count));
		result.add(buildResult("Age: 19 - 28", ageEarlyAdult, count));
		result.add(buildResult("Age: 29 - 35", ageToMiddle, count));
		result.add(buildResult("Age: 36 - 49", ageMiddle, count));
		result.add(buildResult("Age: 50 - 64", ageOlder, count));
		result.add(buildResult("Age: 65 - 80", ageRetire, count));
		result.add(buildResult("Age: 80 and up", ageOld, count));
				
		result.add(buildResult("Likes cats", likesCats, count));
		result.add(buildResult("Likes dogs", likesDogs, count));
		result.add(buildResult("Likes ferrits", likesFerrits, count));
		result.add(buildResult("Likes turnips", likesTurnips, count));
			
		return result;
	}
	
	private Map buildResult(String name, int count, int total)
	{
		Map result;
		
		result = new HashMap(3);
		result.put("name", name);
		result.put("count", new Integer(count));
		
		if (percentFormat == null)
			percentFormat = NumberFormat.getPercentInstance(getLocale());
		
		result.put("percent", percentFormat.format((double)count / (double)total));	
		
		return result;
	}
		
}
