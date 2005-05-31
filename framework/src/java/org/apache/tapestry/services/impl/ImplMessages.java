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

package org.apache.tapestry.services.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServlet;

import org.apache.hivemind.HiveMind;
import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.hivemind.impl.MessageFormatter;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.INamespace;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.parse.OpenToken;
import org.apache.tapestry.services.Infrastructure;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.IContainedComponent;

/**
 * @author Howard Lewis Ship
 * @since 4.0
 */
class ImplMessages
{
    protected static MessageFormatter _formatter = new MessageFormatter(ImplMessages.class,
            "ImplStrings");

    static String initializerContribution()
    {
        return _formatter.getMessage("initializer-contribution");
    }

    static String noApplicationSpecification(HttpServlet servlet)
    {
        return _formatter.format("no-application-specification", servlet.getServletName());
    }

    static String errorInstantiatingEngine(Class engineClass, Throwable cause)
    {
        return _formatter.format("error-instantiating-engine", engineClass.getName(), cause);
    }

    static String noTemplateForComponent(String componentId, Locale locale)
    {
        return _formatter.format("no-template-for-component", componentId, locale);
    }

    static String noTemplateForPage(String pageName, Locale locale)
    {
        return _formatter.format("no-template-for-page", pageName, locale);
    }

    static String unableToReadTemplate(Object template)
    {
        return _formatter.format("unable-to-read-template", template);
    }

    static String unableToParseTemplate(Resource resource)
    {
        return _formatter.format("unable-to-parse-template", resource);
    }

    static String unableToParseSpecification(Resource resource)
    {
        return _formatter.format("unable-to-parse-specification", resource);
    }

    static String unableToReadInfrastructureProperty(String propertyName, Infrastructure service,
            Throwable cause)
    {
        return _formatter.format(
                "unable-to-read-infrastructure-property",
                propertyName,
                service,
                cause);
    }

    static String multipleComponentReferences(IComponent component, String id)
    {
        return _formatter.format("multiple-component-references", component.getExtendedId(), id);
    }

    static String dupeComponentId(String id, IContainedComponent containedComponent)
    {
        return _formatter.format("dupe-component-id", id, HiveMind
                .getLocationString(containedComponent));
    }

    static String unbalancedCloseTags()
    {
        return _formatter.getMessage("unbalanced-close-tags");
    }

    static String templateBindingForInformalParameter(IComponent loadComponent,
            String parameterName, IComponent component)
    {
        return _formatter.format("template-binding-for-informal-parameter", loadComponent
                .getExtendedId(), parameterName, component.getExtendedId());
    }

    static String templateBindingForReservedParameter(IComponent loadComponent,
            String parameterName, IComponent component)
    {
        return _formatter.format("template-binding-for-reserved-parameter", loadComponent
                .getExtendedId(), parameterName, component.getExtendedId());
    }

    static String missingComponentSpec(IComponent component, Collection ids)
    {
        StringBuffer buffer = new StringBuffer();
        List idList = new ArrayList(ids);
        int count = idList.size();

        for (int i = 0; i < count; i++)
        {
            if (i > 0)
                buffer.append(", ");

            buffer.append(idList.get(i));
        }

        return _formatter.format("missing-component-spec", component.getExtendedId(), new Integer(
                count), buffer.toString());
    }

    static String bodylessComponent()
    {
        return _formatter.getMessage("bodyless-component");
    }

    static String dupeTemplateBinding(String name, IComponent component, IComponent loadComponent)
    {
        return _formatter.format(
                "dupe-template-binding",
                name,
                component.getExtendedId(),
                loadComponent.getExtendedId());
    }

    static String unableToLoadProperties(URL url, Throwable cause)
    {
        return _formatter.format("unable-to-load-properties", url, cause);
    }

    static String noSuchService(String name)
    {
        return _formatter.format("no-such-service", name);
    }

    static String dupeService(String name, EngineServiceContribution existing)
    {
        return _formatter.format("dupe-service", name, HiveMind.getLocationString(existing));
    }

    static String unableToParseExpression(String expression, Throwable cause)
    {
        return _formatter.format("unable-to-parse-expression", expression, cause);
    }

    static String parsedExpression()
    {
        return _formatter.getMessage("parsed-expression");
    }

    static String unableToReadExpression(String expression, Object target, Throwable cause)
    {
        return _formatter.format("unable-to-read-expression", expression, target, cause);
    }

    static String unableToWriteExpression(String expression, Object target, Object value,
            Throwable cause)
    {
        return _formatter.format("unable-to-write-expression", new Object[]
        { expression, target, value, cause });
    }

    static String isConstantExpressionError(String expression, Exception ex)
    {
        return _formatter.format("is-constant-expression-error", expression, ex);
    }

    static String templateParameterName(String name)
    {
        return _formatter.format("template-parameter-name", name);
    }

    static String componentPropertySourceDescription(IComponentSpecification spec)
    {
        return _formatter.format("component-property-source-description", spec
                .getSpecificationLocation());
    }

    static String namespacePropertySourceDescription(INamespace namespace)
    {
        return _formatter
                .format("namespace-property-source-description", namespace.getExtendedId());
    }

    static String invalidEncoding(String encoding, Throwable cause)
    {
        return _formatter.format("invalid-encoding", encoding, cause);
    }

    static String errorResetting(Throwable cause)
    {
        return _formatter.format("error-resetting", cause);
    }

    static String engineServiceInnerProxyToString(String serviceName)
    {
        return _formatter.format("engine-service-inner-proxy-to-string", serviceName);
    }

    static String engineServiceOuterProxyToString(String serviceName)
    {
        return _formatter.format("engine-service-outer-proxy-to-string", serviceName);
    }

    static String serviceNameMismatch(IEngineService service, String expectedName, String actualName)
    {
        return _formatter.format("service-name-mismatch", service, expectedName, actualName);
    }

    static String infrastructureAlreadyInitialized(String newMode, String initializedMode)
    {
        return _formatter.format("infrastructure-already-initialized", newMode, initializedMode);
    }

    static String duplicateInfrastructureContribution(InfrastructureContribution conflict,
            Location existingLocation)
    {
        return _formatter.format(
                "duplicate-infrastructure-contribution",
                conflict.getProperty(),
                conflict.getMode(),
                existingLocation);
    }

    static String infrastructureNotInitialized()
    {
        return _formatter.getMessage("infrastructure-not-initialized");
    }

    static String missingInfrastructureProperty(String propertyName)
    {
        return _formatter.format("missing-infrastructure-property", propertyName);
    }

    public static String usedTemplateParameterAlias(OpenToken token, String attributeName,
            String parameterName)
    {
        return _formatter.format("used-template-parameter-alias", new Object[]
        { HiveMind.getLocationString(token), token.getType(), attributeName, parameterName });
    }
}