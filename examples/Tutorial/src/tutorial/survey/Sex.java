package tutorial.survey;

import com.primix.foundation.*;

/**
 *  Provides a list of possible sexes.  I'm in a Science Fiction mood, so I provided
 *  a few more options than typical.
 *
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */
 
public class Sex extends Enum
{
	public static final Sex MALE = new Sex("MALE");
	public static final Sex FEMALE = new Sex("FEMALE");
	public static final Sex TRANSGENDER = new Sex("TRANSGENDER");
	public static final Sex ASEXUAL = new Sex("ASEXUAL");

	private String name;
	
	private Sex(String enumerationId)
	{
		super(enumerationId);
	}

	
	private Object readResolve()
	{
		return getSingleton();
	}

}

