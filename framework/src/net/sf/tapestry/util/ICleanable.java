package net.sf.tapestry.util;

/**
 *  An interface implemented by objects that can be
 *  cleaned up, which is to say, can release unneeded
 *  object references.  This is useful for many classes which
 *  provide a pooling or caching function.  Over time, 
 *  some pooled or cached objects may no longer be useful
 *  to keep and can be released. 
 *  references to unneeded objects.
 *  This interface is the bridge between
 *  the {@link JanitorThread} class and an object that
 *  wishes to be periodically told to "clean up".
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.5
 *
 **/

public interface ICleanable
{
    /**
     *  Invoked periodically by the {@link JanitorThread}
     *  to perform whatever memory cleanups are reasonable.
     *
     **/

    public void executeCleanup();
}