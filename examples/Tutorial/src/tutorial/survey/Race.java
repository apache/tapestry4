package tutorial.survey;

import com.primix.foundation.Enum;

/**
 *  An enumeration of different races.
 *
 */
 
public class Race extends Enum
{
	public static final Race CAUCASIAN = new Race("CAUCASIAN");
	public static final Race AFRICAN = new Race("AFRICAN");
	public static final Race ASIAN = new Race("ASIAN");
	public static final Race INUIT = new Race("INUIT");
	public static final Race MARTIAN = new Race("MARTIAN");

	private Race(String enumerationId)
	{
		super(enumerationId);
	}

	private Object readResolve()
	{
		return getSingleton();
	}

}
