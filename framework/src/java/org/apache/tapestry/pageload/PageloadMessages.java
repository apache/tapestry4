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

package org.apache.tapestry.pageload;

import org.apache.hivemind.impl.MessageFormatter;
import org.apache.tapestry.IComponent;

/**
 * Messages for the pageload package
 *
 * @author Howard Lewis Ship
 * @since 3.1
 */
class PageloadMessages
{
    private static final MessageFormatter _formatter =
        new MessageFormatter(PageloadMessages.class, "PageloadStrings");

    public static String parameterMustHaveNoDefaultValue(IComponent component, String name)
    {
        return _formatter.format(
            "parameter-must-have-no-default-value",
            component.getExtendedId(),
            name);
    }

    public static String unableToInitializeProperty(
        String propertyName,
        IComponent component,
        Throwable cause)
    {
        return _formatter.format("unable-to-initialize-property", propertyName, component, cause);
    }

    public static String requiredParameterNotBound(String name, IComponent component)
    {
        return _formatter.format("required-parameter-not-bound", name, component.getExtendedId());
    }

    public static String inheritInformalInvalidComponentFormalOnly(IComponent component)
    {
        return _formatter.format(
            "inherit-informal-invalid-component-formal-only",
            component.getExtendedId());
    }

    public static String inheritInformalInvalidContainerFormalOnly(
        IComponent container,
        IComponent component)
    {
        return _formatter.format(
            "inherit-informal-invalid-container-formal-only",
            container.getExtendedId(),
            component.getExtendedId());
    }

    public static String formalParametersOnly(IComponent component, String parameterName)
    {
        return _formatter.format(
            "formal-parameters-only",
            component.getExtendedId(),
            parameterName);
    }

    public static String unableToInstantiateComponent(IComponent container, Throwable cause)
    {
        return _formatter.format(
            "unable-to-instantiate-component",
            container.getExtendedId(),
            cause);
    }

    public static String classNotComponent(String className)
    {
        return _formatter.format("class-not-component", className);
    }

    public static String unableToInstantiate(String className, Throwable cause)
    {
        return _formatter.format("unable-to-instantiate", className, cause);
    }

    public static String pageNotAllowed(IComponent component)
    {
        return _formatter.format("page-not-allowed", component.getExtendedId());
    }

    public static String classNotPage(String className)
    {
        return _formatter.format("class-not-page", className);
    }

    public static String missingAsset(String name, IComponent component)
    {
        return _formatter.format("missing-asset", name, component.getExtendedId());
    }
}
