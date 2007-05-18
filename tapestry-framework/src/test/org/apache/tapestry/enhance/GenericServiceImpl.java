package org.apache.tapestry.enhance;

/**
 *
 */
public class GenericServiceImpl implements SimpleGenericsInterface {

    private BasicObject _basic = new BasicObject();
    
    public String getObjectName()
    {
        return _basic.getName();
    }

    public void doFoo(BasicObject bar)
    {
    }

    public BasicObject getCurrentFoo()
    {
        return _basic;
    }
}
