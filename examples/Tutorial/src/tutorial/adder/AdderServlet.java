package tutorial.adder;

import com.primix.tapestry.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class AdderServlet extends ApplicationServlet
{
  protected IApplication getApplication(RequestContext context)
  {
    String name = "Adder.application";
    IApplication application;

    application = (IApplication)context.getSessionAttribute(name);

    if (application == null)
    {
      application = new AdderApplication(context, null);
      context.setSessionAttribute(name, application);
    }

    return application;
  }
}
