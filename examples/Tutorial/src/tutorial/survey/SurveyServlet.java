package tutorial.survey;

import com.primix.tapestry.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class SurveyServlet extends ApplicationServlet
{
  protected IApplication getApplication(RequestContext context)
  {
    String name = "Survey.application";
    IApplication application;

    application = (IApplication)context.getSessionAttribute(name);

    if (application == null)
    {
      application = new SurveyApplication(context);
      context.setSessionAttribute(name, application);
    }

    return application;
  }
}
