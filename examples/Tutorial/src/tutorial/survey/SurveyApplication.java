package tutorial.survey;

import com.primix.tapestry.*;
import com.primix.tapestry.app.*;
import javax.servlet.*;

public class SurveyApplication extends SimpleApplication
{
	private transient SurveyDatabase database;

	public SurveyApplication(RequestContext context)
	{
		super(context, null);
	}

	protected String getSpecificationAttributeName()
	{
		return "Survey.application";
	}
	
	protected String getSpecificationResourceName()
	{
		return "/tutorial/survey/Survey.application";
	}
	
	
	public SurveyDatabase getDatabase()
	{
		return database;
	}
	

	protected void setupForRequest(RequestContext context)
	{
		super.setupForRequest(context);
		
		if (database == null)
		{
			String name = "Survey.database";
			ServletContext servletContext;
			
			servletContext = context.getServlet().getServletContext();
			
			database = (SurveyDatabase)servletContext.getAttribute(name);
			
			if (database == null)
			{
				database = new SurveyDatabase();
				servletContext.setAttribute(name, database);
			}
		}
	}
	
	private static final String[] pageNames = 
	{ "home", "survey", "results" 
	};
	
	public String[] getPageNames()
	{
		return pageNames;
	}
}