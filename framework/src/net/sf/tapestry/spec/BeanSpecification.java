package net.sf.tapestry.spec;

import java.util.ArrayList;
import java.util.List;

import net.sf.tapestry.bean.IBeanInitializer;
import net.sf.tapestry.util.BasePropertyHolder;

/**
 *  A specification of a helper bean for a component.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.4
 * 
 **/

public class BeanSpecification extends BasePropertyHolder
{
    protected String className;
    protected BeanLifecycle lifecycle;

    /** @since 1.0.9 **/
    private String description;

    /**
     *  A List of {@link IBeanInitializer}.
     *
     **/

    protected List initializers;

    public BeanSpecification(String className, BeanLifecycle lifecycle)
    {
        this.className = className;
        this.lifecycle = lifecycle;
    }

    public String getClassName()
    {
        return className;
    }

    public BeanLifecycle getLifecycle()
    {
        return lifecycle;
    }

    /**
     *  @since 1.0.5
     *
     **/

    public void addInitializer(IBeanInitializer initializer)
    {
        if (initializers == null)
            initializers = new ArrayList();

        initializers.add(initializer);
    }

    /**
     *  Returns the {@link List} of {@link IBeanInitializer}s.  The caller
     *  should not modify this value!.  May return null if there
     *  are no initializers.
     *
     *  @since 1.0.5
     *
     **/

    public List getInitializers()
    {
        return initializers;
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer("BeanSpecification[");

        buffer.append(className);
        buffer.append(", lifecycle ");
        buffer.append(lifecycle.getName());

        if (initializers != null && initializers.size() > 0)
        {
            buffer.append(", ");
            buffer.append(initializers.size());
            buffer.append(" initializers");
        }

        buffer.append(']');

        return buffer.toString();
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String desc)
    {
        description = desc;
    }
}