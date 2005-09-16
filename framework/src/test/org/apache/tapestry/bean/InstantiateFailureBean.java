package org.apache.tapestry.bean;

/**
 * Used by {@link org.apache.tapestry.bean.TestBeanProvider} to check logic that reports errors
 * instantiating beans.
 * 
 * @author Howard M. Lewis Ship
 */
public class InstantiateFailureBean
{

    public InstantiateFailureBean()
    {
        throw new RuntimeException("Boom!");
    }
}
