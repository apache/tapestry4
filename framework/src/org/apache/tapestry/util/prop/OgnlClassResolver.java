package org.apache.tapestry.util.prop;

import java.util.Map;

import ognl.ClassResolver;

/**
 * Implementation of OGNL's ClassResolver (which is unfortunately, named
 * the same as HiveMind's ClassResolver).
 *
 * @author Howard Lewis Ship
 * @version $Id$
 */
public class OgnlClassResolver implements ClassResolver
{
    private ClassLoader _loader;

    public OgnlClassResolver()
    {
        this(Thread.currentThread().getContextClassLoader());
    }

    public OgnlClassResolver(ClassLoader loader)
    {
        _loader = loader;
    }

    public Class classForName(String name, Map context) throws ClassNotFoundException
    {
        return Class.forName(name, true, _loader);
    }

}
