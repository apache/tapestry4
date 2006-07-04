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

package org.apache.tapestry.portlet;

import java.util.Iterator;

import javax.portlet.PortletConfig;

import org.apache.tapestry.describe.DescribableStrategy;
import org.apache.tapestry.describe.DescriptionReceiver;
import org.apache.tapestry.web.WebUtils;

/**
 * Adapts {@link javax.portlet.PortletConfig}&nbsp;for describing.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class PortletConfigStrategy implements DescribableStrategy
{

    public void describeObject(Object object, DescriptionReceiver receiver)
    {
        PortletConfig pc = (PortletConfig) object;

        receiver.title("Portlet Config");

        receiver.property("portletName", pc.getPortletName());

        receiver.section("Init Parameters");

        Iterator i = WebUtils.toSortedList(pc.getInitParameterNames())
                .iterator();

        while(i.hasNext())
        {
            String name = (String) i.next();
            receiver.property(name, pc.getInitParameter(name));
        }
    }
}
