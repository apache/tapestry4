/*
 *  ====================================================================
 *  The Apache Software License, Version 1.1
 *
 *  Copyright (c) 2002 The Apache Software Foundation.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  Apache Software Foundation (http://www.apache.org/)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "Apache" and "Apache Software Foundation" and
 *  "Apache Tapestry" must not be used to endorse or promote products
 *  derived from this software without prior written permission. For
 *  written permission, please contact apache@apache.org.
 *
 *  5. Products derived from this software may not be called "Apache",
 *  "Apache Tapestry", nor may "Apache" appear in their name, without
 *  prior written permission of the Apache Software Foundation.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of the Apache Software Foundation.  For more
 *  information on the Apache Software Foundation, please see
 *  <http://www.apache.org/>.
 */
package net.sf.tapestry.spec;

import net.sf.tapestry.bean.ExpressionBeanInitializer;
import net.sf.tapestry.bean.FieldBeanInitializer;
import net.sf.tapestry.bean.IBeanInitializer;
import net.sf.tapestry.bean.StaticBeanInitializer;
import net.sf.tapestry.bean.StringBeanInitializer;

/**
 *  A Factory used by {@link net.sf.tapestry.parse.SpecificationParser} to create Tapestry
 *  domain objects.
 * 
 *  <p>
 *  The default implementation here creates the expected runtime
 *  instances of classes in packages:
 *  <ul>
 *  <li>net.sf.tapestry.spec</li>
 *  <li>net.sf.tapestry.bean</li>
 *  </ul>
 * 
 *  <p>
 *  This class is extended by Spindle - the Eclipse Plugin for Tapestry
 * 
 *  @author GWL
 *  @since 1.0.9
 * 
 **/

public class SpecFactory
{
    /**
     * Creates a concrete instance of {@link ApplicationSpecification}.
     **/

    public IApplicationSpecification createApplicationSpecification()
    {
        return new ApplicationSpecification();
    }


    /**
     *  Creates an instance of {@link LibrarySpecification}.
     * 
     *  @since 2.2
     * 
     **/
    
    public ILibrarySpecification createLibrarySpecification()
    {
        return new LibrarySpecification();
    }
    
    /**
     * Creates a concrete instance of {@link AssetSpecification}.
     **/

    public AssetSpecification createAssetSpecification(AssetType type, String path)
    {
        return new AssetSpecification(type, path);
    }

    /**
     * Creates a concrete instance of {@link BeanSpecification}.
     **/

    public BeanSpecification createBeanSpecification(String className, BeanLifecycle lifecycle)
    {
        return new BeanSpecification(className, lifecycle);
    }

    /**
     * Creates a concrete instance of {@link BindingSpecification}.
     **/

    public BindingSpecification createBindingSpecification(BindingType type, String value)
    {
        return new BindingSpecification(type, value);
    }

    /**
     * Creates a concrete instance of {@link ComponentSpecification}.
     **/

    public ComponentSpecification createComponentSpecification()
    {
        return new ComponentSpecification();
    }

    /**
     * Creates a concrete instance of {@link ContainedComponent}.
     **/

    public ContainedComponent createContainedComponent()
    {
        return new ContainedComponent();
    }

    /**
     * Creates a concrete instance of {@link ParameterSpecification}.
     **/

    public ParameterSpecification createParameterSpecification()
    {
        return new ParameterSpecification();
    }

    /**
     * Creates a concrete instance of {@link IBeanInitializer}.
     * <p>
     * Default implementation returns an instance of {@link PropertyBeanInitializer}.
     * 
     *  @deprecated To be removed in 2.3.  Use {@link #createExpressionBeanInitializer(String, String)}.
     **/

    public IBeanInitializer createPropertyBeanInitializer(String propertyName, String expression)
    {
        return new ExpressionBeanInitializer(propertyName, expression);
    }

    /** @since 2.2 **/
    
    public IBeanInitializer createExpressionBeanInitializer(String propertyName, String expression)
    {
        return new ExpressionBeanInitializer(propertyName, expression);
    }

    /**
     * Creates a concrete instance of {@link IBeanInitializer}.
     * <p>
     * Default implementation returns an instance of {@link StaticBeanInitializer}.
     **/

    public IBeanInitializer createStaticBeanInitializer(String propertyName, Object staticValue)
    {
        return new StaticBeanInitializer(propertyName, staticValue);
    }

    /**
     * Creates a concrete instance of {@link IBeanInitializer}.
     * <p>
     * Default implementation returns an instance of {@link FieldBeanInitializer}.
     **/

    public IBeanInitializer createFieldBeanInitializer(String propertyName, String fieldName)
    {
        return new FieldBeanInitializer(propertyName, fieldName);
    }

    /**
     *  Creates a concrete instance of {@link IBeanInitializer}.  
     * 
     *  <p>
     *  Default implementation returns an instance of {@link net.sf.tapestry.bean.StringBeanInitializer}.
     * 
     *  @since 2.2
     * 
     **/
    
    public IBeanInitializer createStringBeanInitializer(String propertyName, String key)
    {
        return new StringBeanInitializer(propertyName, key);
    }
    
    /**
     *  Creates a concrete instance of {@link net.sf.tapestry.spec.ExtensionSpecification}.
     * 
     *  @since 2.2
     * 
     **/
    
    public ExtensionSpecification createExtensionSpecification()
    {
        return new ExtensionSpecification();
    }
}