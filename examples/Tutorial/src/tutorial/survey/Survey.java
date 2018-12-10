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
