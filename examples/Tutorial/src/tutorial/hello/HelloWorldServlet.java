package tutorial.hello;

import com.primix.tapestry.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class HelloWorldServlet extends ApplicationServlet
{
  protected IApplication getApplication(RequestContext context)
  {
    String name = "Hello.application";
    IApplication application;

    application = (IApplication)context.getSessionAttribute(name);

    if (application == null)
    {
      application = new HelloWorldApplication(context, null);
      context.setSessionAttribute(name, application);
    }

    return application;
  }
}
