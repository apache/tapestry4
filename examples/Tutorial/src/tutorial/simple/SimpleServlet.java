package tutorial.simple;

import com.primix.tapestry.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class SimpleServlet extends ApplicationServlet
{
	protected IApplication getApplication(RequestContext context)
	{
		String name = "simple.application";
		IApplication application;

		application = (IApplication)context.getSessionAttribute(name);

		if (application == null)
		{
			application = new SimpleTutorialApplication(context, null);
			context.setSessionAttribute(name, application);
		}

		return application;
	}
}

