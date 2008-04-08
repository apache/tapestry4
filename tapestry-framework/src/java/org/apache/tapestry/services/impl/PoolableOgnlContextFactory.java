package org.apache.tapestry.services.impl;

import ognl.ClassResolver;
import ognl.Ognl;
import ognl.OgnlContext;
import ognl.TypeConverter;
import org.apache.commons.pool.BasePoolableObjectFactory;

/**
 * Used by {@link ExpressionEvaluatorImpl} to pool {@link ognl.OgnlContext} objects.
 */
public class PoolableOgnlContextFactory extends BasePoolableObjectFactory {

    private final ClassResolver _resolver;

    private TypeConverter _typeConverter;

    public PoolableOgnlContextFactory(ClassResolver resolver, TypeConverter typeConverter)
    {
        _resolver = resolver;
        _typeConverter = typeConverter;
    }

    public Object makeObject()
        throws Exception
    {
        return Ognl.createDefaultContext(null, _resolver, _typeConverter);
    }

    public void activateObject(Object obj)
    throws Exception
    {
        OgnlContext context = (OgnlContext)obj;
        
        if (context.getRoot() != null || context.getValues().size() > 0)
        {
            context.clear();
        }
    }
}
