package tutorial.survey;

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
