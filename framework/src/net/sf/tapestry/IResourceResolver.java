package net.sf.tapestry;

import java.net.URL;

import ognl.ClassResolver;

/**
 * An object which is used to resolve classes and class-path resources.
 * This is needed because, in an application server, different class loaders
 * will be loading the Tapestry framework and the specific Tapestry application.
 *
 * <p>The class loader for the framework needs to be able to see resources in
 * the application, but the application's class loader is a descendent of the
 * framework's class loader.  To resolve this, we need a 'hook', an instance
 * that provides access to the application's class loader.
 * 
 * <p>To more easily support OGNL, this interface now extends
 *  {@link ognl.ClassResolver}.
 * 
 * @author Howard Lewis Ship
 * @version $Id$
 **/

public interface IResourceResolver extends ClassResolver
{
    /**
     *  Forwarded, unchanged, to the class loader.  Returns null if the
     *  resource is not found.
     *
     **/

    public URL getResource(String name);

    /**
     *  Forwarded, to the the method
     *  <code>Class.forName(String, boolean, ClassLoader)</code>, using
     *  the application's class loader.
     *
     *  Throws an {@link ApplicationRuntimeException} on any error.
     **/

    public Class findClass(String name);
}