// Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.param;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.util.PropertyAdaptor;
import org.apache.hivemind.util.PropertyUtils;
import org.apache.tapestry.BindingException;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.spec.Direction;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.IParameterSpecification;

/**
 * Manages a set of {@link IParameterConnector}s for a {@link IComponent}.
 * 
 * @author Howard Lewis Ship
 * @since 2.0.3
 */

public class ParameterManager
{
    private static final Log LOG = LogFactory.getLog(ParameterManager.class);

    private IComponent _component;

    private IParameterConnector[] _connectors;

    public ParameterManager(IComponent component)
    {
        _component = component;
    }

    /**
     * Invoked just before a component renders. Converts bindings to values that are assigned to
     * connected properties.
     */

    public void setParameters(IRequestCycle cycle)
    {
        if (_connectors == null)
            setup(cycle);

        for (int i = 0; i < _connectors.length; i++)
            _connectors[i].setParameter(cycle);
    }

    /**
     * Invoked just after the component renders. Returns component properties back to initial values
     * (unless the corresponding binding is {@link IBinding#isInvariant() invariant}). In addition,
     * for {@link Direction#FORM}parameters, the property is read and the binding is set from the
     * property value (if the cycle is rewinding and the current form is rewinding).
     */

    public void resetParameters(IRequestCycle cycle)
    {
        if (_connectors == null)
            return;

        for (int i = 0; i < _connectors.length; i++)
            _connectors[i].resetParameter(cycle);
    }

    private void setup(IRequestCycle cycle)
    {
        boolean debug = LOG.isDebugEnabled();

        if (debug)
            LOG.debug(_component + ": connecting parameters and properties");

        List list = new ArrayList();
        IComponentSpecification spec = _component.getSpecification();
        ClassResolver resolver = _component.getPage().getEngine().getClassResolver();

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

            IParameterSpecification pspec = spec.getParameter(name);
            Direction direction = pspec.getDirection();

            if (direction != Direction.IN && direction != Direction.FORM)
            {
                if (debug)
                    LOG.debug("Parameter is " + pspec.getDirection().getName() + ".");

                continue;
            }

            if (!direction.getAllowInvariant() && binding.isInvariant())
                throw new ConnectedParameterException(Tapestry.format(
                        "ParameterManager.incompatible-direction-and-binding",
                        new Object[]
                        { name, _component.getExtendedId(), direction.getDisplayName(), binding }),
                        _component, name, null, binding.getLocation(), null);

            String propertyName = pspec.getPropertyName();

            if (debug && !name.equals(propertyName))
                LOG.debug("Connecting to property " + propertyName + ".");

            // Next,verify that there is a writable property with the same
            // name as the parameter.

            PropertyAdaptor adaptor = null;

            try
            {
                adaptor = PropertyUtils.getPropertyAdaptor(_component, propertyName);
            }
            catch (Exception ex)
            {
                throw new ConnectedParameterException(ex.getMessage(), _component, name,
                        propertyName, binding.getLocation(), ex);
            }

            if (!(adaptor.isReadable() && adaptor.isWritable()))
            {
                throw new ConnectedParameterException(Tapestry.format(
                        "ParameterManager.property-not-read-write",
                        _component.getExtendedId(),
                        propertyName), _component, name, propertyName, binding.getLocation(), null);
            }

            // Check if the parameter type matches the property type

            Class propertyType = adaptor.getPropertyType();
            Class parameterType = getType(pspec.getType(), resolver);

            if (parameterType == null)
            {
                throw new ConnectedParameterException(Tapestry.format(
                        "ParameterManager.java-type-not-specified",
                        name,
                        _component.getExtendedId()), _component, name, propertyName, binding
                        .getLocation(), null);
            }

            if (!propertyType.equals(parameterType))
            {
                throw new ConnectedParameterException(Tapestry.format(
                        "ParameterManager.type-mismatch",
                        new String[]
                        { name, _component.getExtendedId(), parameterType.toString(),
                                propertyType.toString() }), _component, name, propertyName, binding
                        .getLocation(), null);
            }

            // Here's where we will sniff it for type, for the moment
            // assume its some form of object (not scalar) type.

            IParameterConnector connector = createConnector(
                    _component,
                    name,
                    binding,
                    propertyType,
                    parameterType);

            // Static bindings are set here and then forgotten
            // about. Dynamic bindings are kept for later.

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
                    throw new ConnectedParameterException(Tapestry.format(
                            "ParameterManager.static-initialization-failure",
                            propertyName,
                            _component.getExtendedId(),
                            binding.toString()), _component, name, propertyName, ex);
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

    private IParameterConnector createConnector(IComponent component, String parameterName,
            IBinding binding, Class propertyType, Class requiredType)
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

    private Class getType(String type, ClassResolver resolver)
    {
        if (Tapestry.isBlank(type))
            return null;

        return resolver.findClass(type);
    }
}