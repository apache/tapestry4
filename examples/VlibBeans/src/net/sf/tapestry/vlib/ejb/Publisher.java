package net.sf.tapestry.vlib.ejb;

import java.io.Serializable;

/**
 *  A light-weight, read-only version of the {@link IPublisher} bean.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class Publisher implements Serializable
{
    private static final long serialVersionUID = -4137036147085472403L;
    
    private Integer primaryKey;
    private String name;

    public Publisher(Integer primaryKey, String name)
    {
        this.primaryKey = primaryKey;
        this.name = name;
    }

    public Integer getPrimaryKey()
    {
        return primaryKey;
    }

    public String getName()
    {
        return name;
    }

    /**
     *  Name is a writable property of this bean, to support the
     *  applications' EditPublisher's page.
     *
     *  @see IOperations#updatePublishers(Publisher[],Integer[])
     *
     **/

    public void setName(String value)
    {
        name = value;
    }

    public String toString()
    {
        StringBuffer buffer;

        buffer = new StringBuffer("Publisher[");
        buffer.append(primaryKey);
        buffer.append(' ');
        buffer.append(name);
        buffer.append(']');

        return buffer.toString();
    }
}