package net.sf.tapestry.junit.spec;

/**
 *  Tests that immediate extensions are, in fact, instantiated
 *  immediately.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 *
 **/

public class ImmediateExtension
{
    private static int _instanceCount = 0;

    public static int getInstanceCount()
    {
        return _instanceCount;
    }

    public ImmediateExtension()
    {
        _instanceCount++;
    }
}
