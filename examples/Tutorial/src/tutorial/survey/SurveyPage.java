package tutorial.survey;

import com.primix.tapestry.*;
import com.primix.tapestry.components.*;
import com.primix.tapestry.spec.*;
import java.util.*;

public class SurveyPage extends BasePage
{
	private Survey survey;
	private String error;
	private String age;
	private IPropertySelectionModel sexModel;
	private IPropertySelectionModel raceModel;
	
	public SurveyPage(IApplication application, ComponentSpecification specification)
	{
		super(application, specification);
	}
	

	public IPropertySelectionModel getRaceModel()
	{
		if (raceModel == null)
			raceModel = new EnumPropertySelectionModel(
				new Race[] 
				{
					Race.CAUCASIAN, Race.AFRICAN, Race.ASIAN, Race.INUIT, Race.MARTIAN
				}, page.getLocale(), "tutorial.survey.SurveyStrings", "Race");
				
		return raceModel;
	}
		
	public IPropertySelectionModel getSexModel()
	{
		if (sexModel == null)
			sexModel = new EnumPropertySelectionModel(
				new Sex[] 
				{
					Sex.MALE, Sex.FEMALE, Sex.TRANSGENDER, Sex.ASEXUAL	
				}, getLocale(), "tutorial.survey.SurveyStrings", "Sex");
				
		return sexModel;
	}	
	
		
	public IActionListener getFormListener()
	{
		return new IActionListener()
		{
			public void actionTriggered(IComponent component, IRequestCycle cycle)
			{
				try
				{
					survey.setAge(Integer.parseInt(age));
					
					survey.validate();
				}
				catch (NumberFormatException e)
				{
					// NumberFormatException doesn't provide any useful data
					
					setError("Value entered for age is not a number.");
					return;
				}
				catch (Exception e)
				{
					setError(e.getMessage());
					return;
				}
				
				// Survey is OK, add it to the database.
				
				((SurveyApplication)getApplication()).getDatabase().addSurvey(survey);
				
				setSurvey(null);	
				
				// Jump to the results page to show the totals.
				
				cycle.setPage("home");
			}	
		};
	}
		
	public Survey getSurvey()
	{
		if (survey == null)
			setSurvey(new Survey());
				
		return survey;
	}
	
	public void setSurvey(Survey value)
	{
		survey = value;
		fireObservedChange("survey", survey);
	}
	
	public void detachFromApplication()
	{
		super.detachFromApplication();
		
		survey = null;
		error = null;
		age = null;
		
		// We keep the models, since they are stateless
	}
	
	public void setError(String value)
	{
		error = value;
	}
	
	public String getError()
	{
		return error;
	}
	
	public String getAge()
	{
		int ageValue;
		
		if (age == null)
			{
			ageValue = getSurvey().getAge();
			
			if (ageValue == 0)
				age = "";
			else 
				age = Integer.toString(ageValue);
		}	
		
		return age;	
	}
	
	public void setAge(String value)
	{
		age = value;
	}
}