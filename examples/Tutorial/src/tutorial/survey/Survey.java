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

package tutorial.survey;

import java.util.*;
import com.primix.tapestry.*;
import java.io.*;

public class Survey implements Serializable, Cloneable
{
	private Object primaryKey;
	private String name;
	private int age = 0;
	private Sex sex = Sex.MALE;
	private Race race = Race.CAUCASIAN;
	
	private boolean likesDogs = true;
	private boolean likesCats;
	private boolean likesFerrits;
	private boolean likesTurnips;
	
	public Object getPrimaryKey()
	{
		return primaryKey;
	}
	
	public void setPrimaryKey(Object value)
	{
		primaryKey = value;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String value)
	{
		name = value;
	}
	
	public int getAge()
	{
		return age;
	}
	
	public void setAge(int value)
	{
		age = value;
	}
 	
	public void setSex(Sex value)
	{
		sex = value;
	}
	
	public Sex getSex()
	{
		return sex;
	}
	
	public void setRace(Race value)
	{
		race = value;
	}
	
	public Race getRace()
	{
		return race;
	}
	
	public boolean getLikesCats()
	{
		return likesCats;
	}

	public void setLikesCats(boolean value)
	{
		likesCats = value;
	}
	
	public boolean getLikesDogs()
	{
		return likesDogs;
	}
	
	public void setLikesDogs(boolean value)
	{
		likesDogs = value;
	}
	
	public boolean getLikesFerrits()
	{
		return likesFerrits;
	}
	
	public void setLikesFerrits(boolean value)
	{
		likesFerrits = value;
	}
	
	public boolean getLikesTurnips()
	{
		return likesTurnips;
	}
	
	public void setLikesTurnips(boolean value)
	{
		likesTurnips = value;
	}
	
	/**
	 *  Validates that the survey is acceptible; throws an {@link IllegalArgumentException}
	 *  if not valid.
	 *
	 */
	 
	public void validate()
	throws IllegalArgumentException
	{
		if (race == null)
			throw new IllegalArgumentException("Race must be specified.");
		
		if (sex == null)
			throw new IllegalArgumentException("Sex must be specified.");
			
		if (age < 1)
			throw new IllegalArgumentException("Age must be at least one.");
	}	
	
	public Object clone()
	{
		try
		{
			return super.clone(); 
		}
		catch (CloneNotSupportedException e)
		{ // It is!
			return null;
		}
	}
}
