// Copyright 2005 The Apache Software Foundation
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

package org.apache.tapestry.describe;

import java.util.Iterator;

import javax.servlet.ServletContext;

import org.apache.tapestry.web.WebUtils;

/**
 * Describes a {@link javax.servlet.ServletContext}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class ServletContextDescribableAdapter implements DescribableAdapter
{

    public void describeObject(Object object, DescriptionReceiver receiver)
    {
        ServletContext context = (ServletContext) object;

        receiver.title("ServletContext");

        receiver.property("serverInfo", context.getServerInfo());
        receiver.property("version", context.getMajorVersion() + "." + context.getMinorVersion());

        receiver.section("Attributes");

        Iterator i = WebUtils.toSortedList(context.getAttributeNames()).iterator();
        while (i.hasNext())
        {
            String key = (String) i.next();

            receiver.property(key, context.getAttribute(key));
        }

        receiver.section("Initialization Parameters");
        i = WebUtils.toSortedList(context.getInitParameterNames()).iterator();
        while (i.hasNext())
        {
            String key = (String) i.next();

            receiver.property(key, context.getInitParameter(key));
        }

    }

}