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
import org.apache.tapestry.bean.IBeanInitializer;
import org.apache.tapestry.bean.MessageBeanInitializer;

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
	 *  Returns a new instance of {@link IAssetSpecification}.
	 * 
	 *  @since 3.0
	 * 
	 **/
	
	public IAssetSpecification createAssetSpecification()
	{
		return new AssetSpecification();
	}

	/**
	 *  Creates a new instance of {@link IBeanSpecification}.
	 * 
	 *  @since 3.0
	 * 
	 **/
	
	public IBeanSpecification createBeanSpecification()
	{
		return new BeanSpecification();
	}

	public IBindingSpecification createBindingSpecification() 
	{
		return new BindingSpecification();
	}

    /**
     *  Creates a new concrete instance of {@link IListenerBindingSpecification} for the
     *  given language (which is option) and script.
     * 
     *  @since 3.0
     *  
     **/

    public IListenerBindingSpecification createListenerBindingSpecification()
    {
        return new ListenerBindingSpecification();
    }

    /**
     * Creates a concrete instance of {@link IComponentSpecification}.
     **/

    public IComponentSpecification createComponentSpecification()
    {
        return new ComponentSpecification();
    }

    /**
     * Creates a concrete instance of {@link IContainedComponent}.
     **/

    public IContainedComponent createContainedComponent()
    {
        return new ContainedComponent();
    }

    /**
     * Creates a concrete instance of {@link ParameterSpecification}.
     **/

    public IParameterSpecification createParameterSpecification()
    {
        return new ParameterSpecification();
    }

	/**
	 *  Creates a new instance of {@link ExpressionBeanInitializer}.
	 * 
	 *  @since 3.0
	 * 
	 **/
	
	public IBeanInitializer createExpressionBeanInitializer()
	{
		return new ExpressionBeanInitializer();
	}

	/**
	 *  Returns a new instance of {@link MessageBeanInitializer}.
	 * 
	 *  @since 3.0
	 * 
	 **/
	
	public IBeanInitializer createMessageBeanInitializer()
	{
		return new MessageBeanInitializer();
	}

    /**
     *  Creates a concrete instance of {@link org.apache.tapestry.spec.IExtensionSpecification}.
     * 
     *  @since 2.2
     * 
     **/

    public IExtensionSpecification createExtensionSpecification()
    {
        return new ExtensionSpecification();
    }
    
    /**
     *  Creates a concrete instance of {@link org.apache.tapestry.spec.IPropertySpecification}.
     * 
     *  @since 3.0
     * 
     **/
    
    public IPropertySpecification createPropertySpecification()
    {
    	return new PropertySpecification();
    }
}