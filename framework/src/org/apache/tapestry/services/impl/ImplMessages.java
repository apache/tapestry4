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

package org.apache.tapestry.services.impl;

import java.util.Locale;

import javax.servlet.http.HttpServlet;

import org.apache.hivemind.Resource;
import org.apache.hivemind.impl.MessageFormatter;

/**
 * 
 *
 * @author Howard Lewis Ship
 * @since 3.1
 */
final class ImplMessages
{
    private static final MessageFormatter _formatter =
        new MessageFormatter(ImplMessages.class, "ImplStrings");

    public static String initializerContribution()
    {
        return _formatter.getMessage("initializer-contribution");
    }

    public static String noApplicationSpecification(HttpServlet servlet)
    {
        return _formatter.format("no-application-specification", servlet.getServletName());
    }

    public static String errorInstantiatingEngine(Class engineClass, Throwable cause)
    {
        return _formatter.format("error-instantiating-engine", engineClass.getName(), cause);
    }

    public static String noTemplateForComponent(String componentId, Locale locale)
    {
        return _formatter.format("no-template-for-component", componentId, locale);
    }

    public static String noTemplateForPage(String pageName, Locale locale)
    {
        return _formatter.format("no-template-for-page", pageName, locale);
    }

    public static String unableToReadTemplate(Object template)
    {
        return _formatter.format("unable-to-read-template", template);
    }
    
    public static String unableToParseTemplate(Resource resource)
    {
    	return _formatter.format("unable-to-parse-template", resource);
    }
}
