/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.spec;

import org.apache.tapestry.bean.ExpressionBeanInitializer;
import org.apache.tapestry.bean.FieldBeanInitializer;
import org.apache.tapestry.bean.IBeanInitializer;
import org.apache.tapestry.bean.StaticBeanInitializer;
import org.apache.tapestry.bean.StringBeanInitializer;

/**
 *  A Factory used by {@link org.apache.tapestry.parse.SpecificationParser} to create Tapestry
 *  domain objects.
 * 
 *  <p>
 *  The default implementation here creates the expected runtime
 *  instances of classes in packages:
 *  <ul>
 *  <li>org.apache.tapestry.spec</li>
 *  <li>org.apache.tapestry.bean</li>
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
     *  Creates a concrete instance of {@link AssetSpecification}.
     * 
     *  @deprecated
     **/

    public AssetSpecification createAssetSpecification(AssetType type, String path)
    {
        return new AssetSpecification(type, path);
    }

	/**
	 *  Returns a new instance of {@link AssetSpecification}.
	 * 
	 *  @since 2.4
	 * 
	 **/
	
	public AssetSpecification createAssetSpecification()
	{
		return new AssetSpecification();
	}

    /**
     *  Creates a concrete instance of {@link BeanSpecification}.
     * 
     *  @deprecated
     **/

    public BeanSpecification createBeanSpecification(String className, BeanLifecycle lifecycle)
    {
        return new BeanSpecification(className, lifecycle);
    }

	/**
	 *  Creates a new instance of {@link BeanSpecification}.
	 * 
	 *  @since 2.4
	 * 
	 **/
	
	public BeanSpecification createBeanSpecification()
	{
		return new BeanSpecification();
	}

    /**
     * Creates a concrete instance of {@link BindingSpecification}.
     * 
     *  @deprecated
     * 
     **/

    public BindingSpecification createBindingSpecification(BindingType type, String value)
    {
        return new BindingSpecification(type, value);
    }

	public BindingSpecification createBindingSpecification()
	{
		return new BindingSpecification();
	}

    /**
     *  Creates a new instance of {@link ListenerBindingSpecification} for the
     *  given language (which is option) and script.
     * 
     *  @since 2.4
     *  
     **/

    public ListenerBindingSpecification createListenerBindingSpecification()
    {
        return new ListenerBindingSpecification();
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

    /** 
     * 
     *  @deprecated
     * 
     *  @since 2.2 
     * 
     **/

    public IBeanInitializer createExpressionBeanInitializer(String propertyName, String expression)
    {
        return new ExpressionBeanInitializer(propertyName, expression);
    }

	/**
	 *  Creates a new instance of {@link ExpressionBeanInitializer}.
	 * 
	 *  @since 2.4
	 * 
	 **/
	
	public IBeanInitializer createExpressionBeanInitializer()
	{
		return new ExpressionBeanInitializer();
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
     *  Default implementation returns an instance of {@link org.apache.tapestry.bean.StringBeanInitializer}.
     * 
     *  @since 2.2
     *  @deprecated
     **/

    public IBeanInitializer createStringBeanInitializer(String propertyName, String key)
    {
        return new StringBeanInitializer(propertyName, key);
    }

	/**
	 *  Returns a new instance of {@link StringBeanInitializer}.
	 * 
	 *  @since 2.4
	 * 
	 **/
	
	public IBeanInitializer createStringBeanInitializer()
	{
		return new StringBeanInitializer();
	}

    /**
     *  Creates a concrete instance of {@link org.apache.tapestry.spec.ExtensionSpecification}.
     * 
     *  @since 2.2
     * 
     **/

    public ExtensionSpecification createExtensionSpecification()
    {
        return new ExtensionSpecification();
    }
    
    /**
     *  Creates a concrete instance of {@link org.apache.tapestry.spec.PropertySpecification}.
     * 
     *  @since 2.4
     * 
     **/
    
    public PropertySpecification createPropertySpecification()
    {
    	return new PropertySpecification();
    }
}