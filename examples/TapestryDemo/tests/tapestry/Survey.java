package tests.tapestry;

import java.io.*;
import java.util.*;

/*
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
 * but WITHOUT ANY WARRANTY; wihtout even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

/**
 *  @version $Id$
 *  @author Howard Ship
 *
 */
 

public class Survey implements Serializable
{
	private String name;
	private String age;
	private boolean male = true;
	private String magazine;
	private String comment;

	public static final List AGE_OPTIONS = new ArrayList();
	public static final List SEX_OPTIONS = new ArrayList();
	public static final List MAGAZINE_OPTIONS = new ArrayList();
	
	static
	{
		add(AGE_OPTIONS, new Object[]
		{ "0 - 10", "11 - 17", "17 - 25", "26 - 31", "31 - 45", "46 - 60",
		  "60+"});
	
		add(SEX_OPTIONS, new Object[] { "Male", "Female"});	  
		add(MAGAZINE_OPTIONS, new Object[] 
				{ "Java Report", "Java Developers Journal", "Dr. Dobbs",
				"JavaPRO", "ZD-Net"});
	}	  
	
	private static final void add(List list, Object[] objects)
	{
		for (int i = 0; i < objects.length; i++)
			list.add(objects[i]);
	}	


/**
 * 
 * @return java.lang.String
 */
public java.lang.String getAge() {
	return age;
}
public String getComment()
{
	return comment;
}
/**
 * 
 * @return java.lang.String
 */
public java.lang.String getMagazine() {
	return magazine;
}
/**
 * 
 * @return java.lang.String
 */
public java.lang.String getName() {
	return name;
}
public boolean isMale()
{
	return male;
}
/**
 * 
 * @param newAge java.lang.String
 */
public void setAge(java.lang.String newAge) {
	age = newAge;
}
public void setComment(String value)
{
	comment = value;
}
/**
 * 
 * @param newMagazine java.lang.String
 */
public void setMagazine(java.lang.String newMagazine) {
	magazine = newMagazine;
}
public void setMale(boolean value)
{
	male = value;
}
/**
 * 
 * @param newName java.lang.String
 */
public void setName(java.lang.String newName) {
	name = newName;
}
}
