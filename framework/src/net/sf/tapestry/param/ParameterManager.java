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

import net.sf.tapestry.BindingException;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IComponent;
import net.sf.tapestry.IResourceResolver;
import net.sf.tapestry.RequiredParameterException;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.spec.ComponentSpecification;
import net.sf.tapestry.spec.Direction;
import net.sf.tapestry.spec.ParameterSpecification;
import net.sf.tapestry.util.prop.IPropertyAccessor;
import net.sf.tapestry.util.prop.PropertyHelper;
import org.apache.log4j.Category;

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
    private static final Category CAT =
        Category.getInstance(ParameterManager.class);

    private static final Map scalarTypeMap = new HashMap();

    static {
        scalarTypeMap.put("boolean", Boolean.TYPE);
        scalarTypeMap.put("short", Short.TYPE);
        scalarTypeMap.put("int", Integer.TYPE);
        scalarTypeMap.put("long", Long.TYPE);
        scalarTypeMap.put("float", Float.class);
        scalarTypeMap.put("double", Double.class);
    }

    private IComponent component;
    private IParameterConnector[] connectors;

    public ParameterManager(IComponent component)
    {
        this.component = component;
    }

    public void setParameters() throws RequiredParameterException
    {
        if (connectors == null)
            setup();

        for (int i = 0; i < connectors.length; i++)
            connectors[i].setParameter();
    }

    public void clearParameters()
    {
        if (connectors == null)
            return;

        for (int i = 0; i < connectors.length; i++)
            connectors[i].clearParameter();
    }

    private void setup() throws RequiredParameterException
    {
        boolean debug = CAT.isDebugEnabled();

        if (debug)
            CAT.debug("Connecting parameters and properties for " + component);

        List list = new ArrayList();
        ComponentSpecification spec = component.getSpecification();
        PropertyHelper helper = PropertyHelper.forInstance(component);
        IResourceResolver resolver =
            component.getPage().getEngine().getResourceResolver();

        Collection names = spec.getParameterNames();
        Iterator i = names.iterator();
        while (i.hasNext())
        {
            String name = (String) i.next();

            if (debug)
                CAT.debug("Connecting parameter " + name + ".");

            IBinding binding = component.getBinding(name);
            if (binding == null)
            {
                if (debug)
                    CAT.debug("Not bound.");

                continue;
            }

            ParameterSpecification pspec = spec.getParameter(name);

            if (pspec.getDirection() != Direction.IN)
            {
                if (debug)
                    CAT.debug("Parameter is " + pspec.getDirection() + ".");

                continue;
            }

            String propertyName = pspec.getPropertyName();

            if (debug && !name.equals(propertyName))
                CAT.debug("Connecting to property " + propertyName + ".");

            // Next,verify that there is a writable property with the same
            // name as the parameter.

            IPropertyAccessor accessor = helper.getAccessor(component, propertyName);
            if (accessor == null)
            {
                throw new ConnectedParameterException(
                    Tapestry.getString(
                        "ParameterManager.no-accessor",
                        component.getExtendedId(),
                        propertyName),
                    component,
                    name,
                    propertyName);
            }

            if (!accessor.isReadWrite())
            {
                throw new ConnectedParameterException(
                    Tapestry.getString(
                        "ParameterManager.property-not-read-write",
                        component.getExtendedId(),
                        propertyName),
                    component,
                    name,
                    propertyName);
            }

            // Check if the parameter type matches the property type

            Class propertyType = accessor.getType();
            Class parameterType = getType(pspec.getType(), resolver);

            if (parameterType == null)
            {
                throw new ConnectedParameterException(
                    Tapestry.getString(
                        "ParameterManager.java-type-not-specified",
                        name,
                        component.getExtendedId()),
                    component,
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
                            component.getExtendedId(),
                            parameterType.toString(),
                            propertyType.toString()}),
                    component,
                    name,
                    propertyName);
            }

            // Here's where we will sniff it for type, for the moment
            // assume its some form of object (not scalar) type.

            IParameterConnector connector =
                createConnector(component, name, binding, propertyType, parameterType);

            // Static bindings are set here and then forgotten
            // about.  Dynamic bindings are kept for later.

            if (binding.isStatic())
            {
                if (debug)
                    CAT.debug("Setting static value using " + connector + ".");

                try
                {
                    connector.setParameter();
                }
                catch (BindingException ex)
                {
                    throw new ConnectedParameterException(
                        Tapestry.getString(
                            "ParameterManager.ParameterManager.static-initialization-failure",
                            propertyName,
                            component.getExtendedId(),
                            binding.toString()),
                        component,
                        name,
                        propertyName,
                        ex);
                }
            }
            else
            {
                if (debug)
                    CAT.debug("Adding " + connector + ".");

                list.add(connector);
            }
        }

        // Convert for List to array

        connectors =
            (IParameterConnector[]) list.toArray(new IParameterConnector[list.size()]);

    }

    private IParameterConnector createConnector(
        IComponent component,
        String parameterName,
        IBinding binding,
        Class propertyType,
        Class requiredType)
    {
        // For double property, need special conversion

        if (propertyType.equals(Boolean.TYPE))
            return new BooleanParameterConnector(component, parameterName, binding);

        if (propertyType.equals(Integer.TYPE))
            return new IntParameterConnector(component, parameterName, binding);

        if (propertyType.equals(Double.TYPE))
            return new DoubleParameterConnector(component, parameterName, binding);

        if (propertyType.equals(String.class))
            return new StringParameterConnector(component, parameterName, binding);

        // The default is for any kind of object type

        return new ObjectParameterConnector(
            component,
            parameterName,
            binding,
            requiredType);
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