package tests.tapestry;

import java.io.*;
import java.util.*;

/**
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
