package tutorial.border;

import com.primix.tapestry.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class BorderServlet extends ApplicationServlet
{
  protected IApplication getApplication(RequestContext context)
  {
    String name = "Border.application";
    IApplication application;

    application = (IApplication)context.getSessionAttribute(name);

    if (application == null)
    {
      application = new BorderApplication(context, null);
      context.setSessionAttribute(name, application);
    }

    return application;
  }
}
