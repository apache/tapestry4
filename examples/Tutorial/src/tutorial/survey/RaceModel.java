package tutorial.survey;

import java.util.*;
import com.primix.tapestry.*;
import com.primix.tapestry.components.*;

public class RaceModel implements IPropertySelectionModel
{
	private static final Race[] options =
		new Race[] 
		{ Race.CAUCASIAN, Race.AFRICAN, Race.ASIAN, Race.INUIT, Race.MARTIAN 
		};
	
	
	private Locale locale;
	private String labels[];
	
	public RaceModel(Locale locale)
	{
		this.locale = locale;
	}
	
	public int getOptionCount()
	{
		return options.length;
	}
	
	public Object getOption(int index)
	{
		return options[index];
	}
	
	public String getLabel(int index)
	{
		if (labels == null)
			readLabels();
		
		return labels[index];
	}
	
	private void readLabels()
	{
		ResourceBundle bundle;
		String key;
		int i;
		
		bundle = ResourceBundle.getBundle("tutorial.survey.RaceModelStrings", locale);
		
		labels = new String[options.length];
		for (i = 0; i < options.length; i++)
		{
			key = options[i].getEnumerationId();
			labels[i] = bundle.getString(key);
		}
		
	}
}