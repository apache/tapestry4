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

package net.sf.tapestry.param;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.tapestry.BindingException;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IComponent;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.IResourceResolver;
import net.sf.tapestry.RequiredParameterException;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.spec.ComponentSpecification;
import net.sf.tapestry.spec.Direction;
import net.sf.tapestry.spec.ParameterSpecification;
import net.sf.tapestry.util.prop.PropertyFinder;
import net.sf.tapestry.util.prop.PropertyInfo;

/**
 *  Manages a set of {@link IParameterConnector}s for a
 *  {@link IComponent}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.0.3
 *
 **/

public class ParameterManager
{
    private static final Log LOG = LogFactory.getLog(ParameterManager.class);

    private static final Map scalarTypeMap = new HashMap();

    static {
        scalarTypeMap.put("boolean", Boolean.TYPE);
        scalarTypeMap.put("short", Short.TYPE);
        scalarTypeMap.put("int", Integer.TYPE);
        scalarTypeMap.put("long", Long.TYPE);
        scalarTypeMap.put("float", Float.class);
        scalarTypeMap.put("double", Double.class);
    }

    private IComponent _component;
    private IParameterConnector[] _connectors;

    public ParameterManager(IComponent component)
    {
        _component = component;
    }

    /**
     *  Invoked just before a component renders.  Converts bindings to values
     *  that are assigned to connected properties.
     * 
     **/
    
    public void setParameters(IRequestCycle cycle) throws RequiredParameterException
    {
        if (_connectors == null)
            setup(cycle);

        for (int i = 0; i < _connectors.length; i++)
            _connectors[i].setParameter(cycle);
    }

    /**
     *  Invoked just after the component renders.  Returns component properties
     *  back to initial values (unless the corresponding binding is
     *  {@link IBinding#isInvariant() invariant}).  In addition, for
     *  {@link Direction#FORM} parameters, the property is read and the binding
     *  is set from the property value (if the cycle is rewinding and the current
     *  form is rewinding).
     * 
     **/
    
    public void resetParameters(IRequestCycle cycle)
    {
        if (_connectors == null)
            return;

        for (int i = 0; i < _connectors.length; i++)
            _connectors[i].resetParameter(cycle);
    }

    private void setup(IRequestCycle cycle) throws RequiredParameterException
    {
        boolean debug = LOG.isDebugEnabled();

        if (debug)
            LOG.debug(_component + ": connecting parameters and properties");

        List list = new ArrayList();
        ComponentSpecification spec = _component.getSpecification();
        IResourceResolver resolver = _component.getPage().getEngine().getResourceResolver();

        IParameterConnector disabledConnector = null;

        Collection names = spec.getParameterNames();
        Iterator i = names.iterator();
        while (i.hasNext())
        {
            String name = (String) i.next();

            if (debug)
                LOG.debug("Connecting parameter " + name + ".");

            IBinding binding = _component.getBinding(name);
            if (binding == null)
            {
                if (debug)
                    LOG.debug("Not bound.");

                continue;
            }

            ParameterSpecification pspec = spec.getParameter(name);

            if (pspec.getDirection() == Direction.CUSTOM)
            {
                if (debug)
                    LOG.debug("Parameter is " + pspec.getDirection().getName() + ".");

                continue;
            }

            String propertyName = pspec.getPropertyName();

            if (debug && !name.equals(propertyName))
                LOG.debug("Connecting to property " + propertyName + ".");

            // Next,verify that there is a writable property with the same
            // name as the parameter.

            PropertyInfo propertyInfo = PropertyFinder.getPropertyInfo(_component.getClass(), propertyName);

            if (propertyInfo == null)
            {
                throw new ConnectedParameterException(
                    Tapestry.getString("ParameterManager.no-accessor", _component.getExtendedId(), propertyName),
                    _component,
                    name,
                    propertyName);
            }

            if (!propertyInfo.isReadWrite())
            {
                throw new ConnectedParameterException(
                    Tapestry.getString(
                        "ParameterManager.property-not-read-write",
                        _component.getExtendedId(),
                        propertyName),
                    _component,
                    name,
                    propertyName);
            }

            // Check if the parameter type matches the property type

            Class propertyType = propertyInfo.getType();
            Class parameterType = getType(pspec.getType(), resolver);

            if (parameterType == null)
            {
                throw new ConnectedParameterException(
                    Tapestry.getString("ParameterManager.java-type-not-specified", name, _component.getExtendedId()),
                    _component,
                    name,
                    propertyName);
            }

            if (!propertyType.equals(parameterType))
            {
                throw new ConnectedParameterException(
                    Tapestry.getString(
                        "ParameterManager.type-mismatch",
                        new String[] {
                            name,
                            _component.getExtendedId(),
                            parameterType.toString(),
                            propertyType.toString()}),
                    _component,
                    name,
                    propertyName);
            }

            // Here's where we will sniff it for type, for the moment
            // assume its some form of object (not scalar) type.

            IParameterConnector connector = createConnector(_component, name, binding, propertyType, parameterType);

            // Static bindings are set here and then forgotten
            // about.  Dynamic bindings are kept for later.

            if (binding.isInvariant())
            {
                if (debug)
                    LOG.debug("Setting invariant value using " + connector + ".");

                try
                {
                    connector.setParameter(cycle);
                }
                catch (BindingException ex)
                {
                    throw new ConnectedParameterException(
                        Tapestry.getString(
                            "ParameterManager.static-initialization-failure",
                            propertyName,
                            _component.getExtendedId(),
                            binding.toString()),
                        _component,
                        name,
                        propertyName,
                        ex);
                }
                
                continue;
            }
            
            
                if (debug)
                    LOG.debug("Adding " + connector + ".");

// To properly support forms elements, the disabled parameter
// must always be processed last.

if (name.equals("disabled"))
    disabledConnector = connector;
    else
                list.add(connector);
  
        }

            if (disabledConnector != null)
                list.add(disabledConnector);

        // Convert for List to array

        _connectors = (IParameterConnector[]) list.toArray(new IParameterConnector[list.size()]);

    }

    private IParameterConnector createConnector(
        IComponent component,
        String parameterName,
        IBinding binding,
        Class propertyType,
        Class requiredType)
    {
        // Could convert this code to use a Decorator, but then I'd need
        // some kind of factory for these parameter connectors.

        if (propertyType.equals(Boolean.TYPE))
            return new BooleanParameterConnector(component, parameterName, binding);

        if (propertyType.equals(Integer.TYPE))
            return new IntParameterConnector(component, parameterName, binding);

        if (propertyType.equals(Double.TYPE))
            return new DoubleParameterConnector(component, parameterName, binding);

        if (propertyType.equals(String.class))
            return new StringParameterConnector(component, parameterName, binding);

        // The default is for any kind of object type

        return new ObjectParameterConnector(component, parameterName, binding, requiredType);
    }

    private Class getType(String name, IResourceResolver resolver)
    {
        if (Tapestry.isNull(name))
            return null;

        Class result = (Class) scalarTypeMap.get(name);

        if (result != null)
            return result;

        return resolver.findClass(name);
    }
}