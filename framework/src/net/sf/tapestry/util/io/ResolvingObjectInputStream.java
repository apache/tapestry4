package net.sf.tapestry.util.io;

import java.io.*;

import net.sf.tapestry.IResourceResolver;

/**
 *  Specialized subclass of {@link java.io.ObjectInputStream}
 *  that knows how to resolve classes with a non-default
 *  class loader (represented by an instance of
 *  {@link net.sf.tapestry.IResourceResolver}).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class ResolvingObjectInputStream extends ObjectInputStream
{
    private IResourceResolver _resolver;

    public ResolvingObjectInputStream(IResourceResolver resolver, InputStream input) throws IOException
    {
        super(input);

        _resolver = resolver;
    }

    /**
     *  Overrides the default implementation to
     *  have the resource resolver find the class.
     * 
     **/

    protected Class resolveClass(ObjectStreamClass v) throws IOException, ClassNotFoundException
    {
        return _resolver.findClass(v.getName());
    }
}