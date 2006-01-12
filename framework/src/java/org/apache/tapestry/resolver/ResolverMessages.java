// Copyright 2004, 2005, 2006 The Apache Software Foundation
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

package org.apache.tapestry.resolver;

import org.apache.hivemind.HiveMind;
import org.apache.hivemind.Location;
import org.apache.hivemind.Messages;
import org.apache.hivemind.Resource;
import org.apache.hivemind.impl.MessageFormatter;
import org.apache.tapestry.INamespace;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * Messages for the resolver package.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
final class ResolverMessages
{

    private final static Messages MESSAGES = new MessageFormatter(ResolverMessages.class);

    /** @since 4.1 */
    private ResolverMessages()
    {
    }

    static String noSuchComponentType(String type, INamespace namespace)
    {
        return MESSAGES.format("no-such-component-type", type, namespace);
    }

    static String noSuchPage(String name, INamespace namespace)
    {
        return MESSAGES.format("no-such-page", name, namespace.getNamespaceId());
    }

    static String resolvingComponent(String type, INamespace namespace)
    {
        return MESSAGES.format("resolving-component", type, namespace);
    }

    static String checkingResource(Resource resource)
    {
        return MESSAGES.format("checking-resource", resource);
    }

    static String installingComponent(String type, INamespace namespace, IComponentSpecification specification)
    {
        return MESSAGES.format("installing-component", type, namespace, specification);
    }

    static String installingPage(String pageName, INamespace namespace, IComponentSpecification specification)
    {
        return MESSAGES.format("installing-page", pageName, namespace, specification);
    }

    static String resolvingPage(String pageName, INamespace namespace)
    {
        return MESSAGES.format("resolving-page", pageName, namespace);
    }

    static String foundFrameworkPage(String pageName)
    {
        return MESSAGES.format("found-framework-page", pageName);
    }

    static String foundHTMLTemplate(Resource resource)
    {
        return MESSAGES.format("found-html-template", resource);
    }

    public static String componentIsDeprecated(String componentType, Location location)
    {
        return MESSAGES.format("component-is-deprecated", componentType, HiveMind.getLocationString(location));
    }

    static String webInfNotAllowed(String simpleName)
    {
        return MESSAGES.format("web-inf-not-allowed", simpleName);
    }
}
