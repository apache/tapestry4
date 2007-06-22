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

package org.apache.tapestry.pageload;

import org.apache.hivemind.HiveMind;
import org.apache.hivemind.Location;
import org.apache.hivemind.impl.MessageFormatter;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.spec.IContainedComponent;

/**
 * Messages for the pageload package.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
final class PageloadMessages
{
    private static final MessageFormatter _formatter = new MessageFormatter(PageloadMessages.class);

    /* defeat instantiation */
    private PageloadMessages()
    {
    }

    static String parameterMustHaveNoDefaultValue(IComponent component,
            String name)
    {
        return _formatter.format("parameter-must-have-no-default-value",
                component.getExtendedId(), name);
    }

    static String unableToInitializeProperty(String propertyName,
            IComponent component, Throwable cause)
    {
        return _formatter.format("unable-to-initialize-property", propertyName,
                component, cause);
    }

    static String requiredParameterNotBound(String name, IComponent component)
    {
        return _formatter.format("required-parameter-not-bound", name,
                component.getExtendedId());
    }

    static String inheritInformalInvalidComponentFormalOnly(IComponent component)
    {
        return _formatter.format(
                "inherit-informal-invalid-component-formal-only", component
                        .getExtendedId());
    }

    static String inheritInformalInvalidContainerFormalOnly(
            IComponent container, IComponent component)
    {
        return _formatter.format(
                "inherit-informal-invalid-container-formal-only", container
                        .getExtendedId(), component.getExtendedId());
    }

    static String formalParametersOnly(IComponent component,
            String parameterName)
    {
        return _formatter.format("formal-parameters-only", component
                .getExtendedId(), parameterName);
    }

    static String unableToInstantiateComponent(IComponent container,
            Throwable cause)
    {
        return _formatter.format("unable-to-instantiate-component", container
                .getExtendedId(), cause);
    }

    static String classNotComponent(Class componentClass)
    {
        return _formatter.format("class-not-component", componentClass
                .getName());
    }

    static String unableToInstantiate(String className, Throwable cause)
    {
        return _formatter.format("unable-to-instantiate", className, cause);
    }

    static String pageNotAllowed(String componentId)
    {
        return _formatter.format("page-not-allowed", componentId);
    }

    static String classNotPage(Class componentClass)
    {
        return _formatter.format("class-not-page", componentClass.getName());
    }

    static String defaultParameterName(String name)
    {
        return _formatter.format("default-parameter-name", name);
    }

    static String initializerName(String propertyName)
    {
        return _formatter.format("initializer-name", propertyName);
    }

    static String parameterName(String name)
    {
        return _formatter.format("parameter-name", name);
    }

    static String duplicateParameter(String parameterName, IBinding binding)
    {
        return _formatter.format("duplicate-parameter", parameterName, HiveMind
                .getLocationString(binding));
    }

    public static String usedParameterAlias(IContainedComponent contained,
            String name, String parameterName, Location bindingLocation)
    {
        return _formatter.format("used-parameter-alias", new Object[] {
                HiveMind.getLocationString(bindingLocation),
                contained.getType(), name, parameterName });
    }

    public static String deprecatedParameter(String parameterName,
            Location location, String componentType)
    {
        return _formatter.format("deprecated-parameter", parameterName,
                HiveMind.getLocationString(location), componentType);
    }

    public static String componentNotFound(String id)
    {
        return _formatter.format("component-not-found", id);
    }

    public static String recursiveComponent(IComponent component)
    {
        return _formatter.format("recursive-component", component);
    }

    public static String errorPagePoolGet(Object key)
    {
        return _formatter.format("error-page-pool-borrow", key);
    }
}
