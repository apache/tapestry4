package com.primix.tapestry.engine;

import java.net.URL;
import com.primix.tapestry.*;

/**
 *  Used to resolve classes and resources, this class is used to
 *  bridge some troubling class loader problems.  Typically,
 *  the web application is loaded by one class loader, and (unless
 *  Tapestry is bundled with the web application), the Tapestry framework
 *  is loaded by a different class loader.
 *
 *  <p>This trick is:  classes and resources may be 'hosted' by
 *  either class loader.  Generally, the application class loader
 *  has the 'broadest' view, meaning it's a child class loader to the
 *  framework's class loader.  That means we want to search using it
 *  (because child class loaders delegate to their parent class loader).
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */
 
class ResourceResolver implements IResourceResolver
{
	private Class resourceClass;
	private ClassLoader classLoader;

	ResourceResolver(Object hook)
	{
		resourceClass = hook.getClass();
		classLoader = resourceClass.getClassLoader();
	}

	public URL getResource(String name)
	{
		URL result;

		// This class loader is related to the web application being loaded.

		result = resourceClass.getResource(name);

		// This class loader is related to the Tapestry framework.
		// To be honest, I'm in over my head with these security frameworks
		// and class loader issues.

		if (result == null)
			result = getClass().getResource(name);

		return result;	
	}

	public Class findClass(String name)
	{
		try
		{
			return Class.forName(name, true, classLoader);
		}
		catch (Throwable t)
		{
			throw new ApplicationRuntimeException(
				"Could not load class " + name + " from " + classLoader
				+ ": " + t.getMessage(), t);
		}
	}
}

