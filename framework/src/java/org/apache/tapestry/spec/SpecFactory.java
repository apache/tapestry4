//  Copyright 2004 The Apache Software Foundation
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