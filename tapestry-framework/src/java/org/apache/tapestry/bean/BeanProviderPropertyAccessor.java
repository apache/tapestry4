// Copyright 2004, 2005 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.bean;

import java.util.Map;

import ognl.ObjectPropertyAccessor;
import ognl.OgnlContext;
import ognl.OgnlException;
import ognl.OgnlRuntime;
import ognl.PropertyAccessor;
import ognl.enhance.ExpressionCompiler;
import ognl.enhance.UnsupportedCompilationException;

import org.apache.tapestry.IBeanProvider;

/**
 *  Adapts a {@link org.apache.tapestry.IBeanProvider} to
 *  <a href="http://www.ognl.org">OGNL</a> by exposing the named
 *  beans provided by the provider as read-only properties of
 *  the provider.
 * 
 *  <p>This is registered by {@link org.apache.tapestry.AbstractComponent}.
 *
 *  @author Howard Lewis Ship
 *  @since 2.2
 *
 **/

public class BeanProviderPropertyAccessor extends ObjectPropertyAccessor implements PropertyAccessor
{
    /**
     *  Checks to see if the name matches the name of a bean inside
     *  the provider and returns that bean if so.
     *  Otherwise, invokes the super implementation.
     * 
     **/
    
    public Object getProperty(Map context, Object target, Object name) throws OgnlException
    {
        IBeanProvider provider = (IBeanProvider)target;
        String beanName = (String)name;
        
        if (provider.canProvideBean(beanName))
            return provider.getBean(beanName);
        
        return super.getProperty(context, target, name);
    }

    /**
     *  Returns true if the name matches a bean provided by the provider.
     *  Otherwise invokes the super implementation.
     * 
     **/
    
    public boolean hasGetProperty(Map context, Object target, Object oname) throws OgnlException
    {
        IBeanProvider provider = (IBeanProvider)target;
        String beanName = ((String)oname).replaceAll("\"", "");

        if (provider.canProvideBean(beanName))
            return true;
            
        return super.hasGetProperty(context, target, oname);
    }
    
    public Class getPropertyClass(OgnlContext context, Object target, Object name)
    {
        IBeanProvider provider = (IBeanProvider)target;
        String beanName = ((String)name).replaceAll("\"", "");
        
        if (provider.canProvideBean(beanName))
            return provider.getBean(beanName).getClass();
        
        return super.getPropertyClass(context, target, name);
    }
    
    public String getSourceAccessor(OgnlContext context, Object target, Object name)
    {
        IBeanProvider provider = (IBeanProvider)target;
        String beanName = ((String)name).replaceAll("\"", "");
        
        if (provider.canProvideBean(beanName)) {
            
            Class type = OgnlRuntime.getCompiler().getInterfaceClass(provider.getBean(beanName).getClass());
            
            ExpressionCompiler.addCastString(context, "((" + type.getName() + ")");
            
            context.setCurrentAccessor(IBeanProvider.class);
            context.setCurrentType(type);
            
            return ".getBean(" + name + "))";
        }
        
        return super.getSourceAccessor(context, target, name);
    }
    
    public String getSourceSetter(OgnlContext context, Object target, Object name)
    {
        throw new UnsupportedCompilationException("Can't set beans on IBeanProvider.");
    }
}
