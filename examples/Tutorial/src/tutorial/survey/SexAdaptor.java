package tutorial.survey;

import java.util.*;
import com.primix.tapestry.*;
import com.primix.tapestry.components.*;

public class SexAdaptor extends SelectionAdaptor
{
	private static final Sex[] options =
		new Sex[] 
		{ Sex.MALE, Sex.FEMALE, Sex.TRANSGENDER, Sex.ASEXUAL 
		};
		
	private static List optionsList;
		
	private Survey survey;
	private Locale locale;
	
	private static final int MAP_SIZE = 7;
	
	private Map toLabel;
	
	public SexAdaptor(Survey survey, Locale locale)
	{
		this.survey = survey;
		this.locale = locale;
	}
	
	public List getOptions()
	{
		if (optionsList == null)
		{
			optionsList = new ArrayList(options.length);
		
			for (int i = 0; i < options.length; i++)
				optionsList.add(options[i]);
		}	
			
		return optionsList;
	}
	
	public Object getSelection()
	{
		return survey.getSex();
	}
	
	protected void updateSelection(Object newValue)
	{
		survey.setSex((Sex)newValue);
	}
		
	public String getCurrentSelectionLabel()
	{
		if (toLabel == null)
			readLabels();
		
		return (String)toLabel.get(getCurrentSelection());
	}
	
	private void readLabels()
	{
		ResourceBundle bundle;
		String key;
		String label;
		int i;
		
		bundle = ResourceBundle.getBundle("tutorial.survey.SexStrings", locale);
		toLabel = new HashMap(MAP_SIZE);
		
		for (i = 0; i < options.length; i++)
		{
			key = options[i].getEnumerationId();
			label = bundle.getString(key);
			
			toLabel.put(options[i], label);
		}
	}	
}
