package tests.tapestry;

import com.primix.tapestry.*;
import com.primix.tapestry.components.*;
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
 

/**
 * More complex than it has to be, since it combines collecting
 * survey in a form with presenting the results.
 */
 
public class SurveyPage extends BasePage
{
	private boolean showForm = true;
	
	private Survey survey;
	
	private AgeSelectionAdaptor ageAdaptor;
	
	public class AgeSelectionAdaptor extends SelectionAdaptor
	{
		
		public List getOptions()
		{
			return Survey.AGE_OPTIONS;
		}

		protected Object getSelection()
		{
			return survey.getAge();
		}
		
		protected void updateSelection(Object value)
		{
			survey.setAge(value.toString());
		}

		public String getCurrentSelectionLabel()
		{
			return getCurrentSelection().toString();
		}
	}

	private SexSelectionAdaptor sexAdaptor;

	public class SexSelectionAdaptor extends SelectionAdaptor
	{

		public List getOptions()
		{
			return Survey.SEX_OPTIONS;
		}

		protected Object getSelection()
		{
			return survey.isMale() ? "Male" : "Female";
		}

		protected void updateSelection(Object value)
		{
			survey.setMale(value.equals("Male"));
		}
		
		public String getCurrentSelectionLabel()
		{
			return getCurrentSelection().toString();
		}
	}

	private MagazineSelectionAdaptor magazineAdaptor;

	public class MagazineSelectionAdaptor extends SelectionAdaptor
	{

		public List getOptions()
		{
			return Survey.MAGAZINE_OPTIONS;
		}
		
		protected Object getSelection()
		{
			return survey.getMagazine();
		}

		protected void updateSelection(Object value)
		{
			survey.setMagazine((String)value);
		}
		
		public String getCurrentSelectionLabel()
		{
			return getCurrentSelection().toString();
		}
		
	}

	public void detachFromApplication()
	{
		// We return the page largely to its newly constructed state here.
		// We could also implement ILifeCycleComponent and do it in reset(),
		// but this frees the survey early.

		survey = null;
		showForm = true;

		super.detachFromApplication();
	}
	
	public SelectionAdaptor getAgeAdaptor()
	{
		if (ageAdaptor == null)
			ageAdaptor = new AgeSelectionAdaptor();

		return ageAdaptor;
	}

	public IActionListener getFormListener()
	{
		return new IActionListener()
		{
			public void actionTriggered(IComponent component, IRequestCycle cycle)
			{
				setShowForm(false);
			}
		};
	}
	
	public SelectionAdaptor getMagazineAdaptor()
	{
		if (magazineAdaptor == null)
			magazineAdaptor = new MagazineSelectionAdaptor();

		return magazineAdaptor;
	}
	
	public IActionListener getRetakeListener()
	{
		return new IActionListener()
		{
			public void actionTriggered(IComponent component, IRequestCycle cycle)
			{
				setShowForm(true);
			}
		};
	}
	
	public SelectionAdaptor getSexAdaptor()
	{
		if (sexAdaptor == null)
			sexAdaptor = new SexSelectionAdaptor();

		return sexAdaptor;
	}
	
	public boolean getShowForm()
	{
		return showForm;
	}
	
	public Survey getSurvey()
	{
		if (survey == null)
			setSurvey(new Survey());

		return survey;
	}
	
	public String getSurveySex()
	{
		if (survey.isMale())
			return "Male";

		return "Female";
	}
	public void setShowForm(boolean value)
	{
		showForm = value;

		fireObservedChange("showForm", value);
	}
	
	public void setSurvey(Survey value)
	{
		survey = value;

		fireObservedChange("survey", survey);
	}
}

