//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.spec;

import net.sf.tapestry.bean.ExpressionBeanInitializer;
import net.sf.tapestry.bean.FieldBeanInitializer;
import net.sf.tapestry.bean.IBeanInitializer;
import net.sf.tapestry.bean.PropertyBeanInitializer;
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