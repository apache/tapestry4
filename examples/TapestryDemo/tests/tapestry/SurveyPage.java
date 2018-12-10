package tests.tapestry;

import com.primix.tapestry.*;
import com.primix.tapestry.spec.*;
import com.primix.tapestry.components.*;
import java.util.*;

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

	public SurveyPage(IApplication application, ComponentSpecification componentSpecification)
	{
		super(application, componentSpecification);
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

