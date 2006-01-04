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

import java.util.List;

import javax.portlet.PortletConfig;

import org.apache.hivemind.util.Defense;
import org.apache.tapestry.describe.DescriptionReceiver;
import org.apache.tapestry.web.WebActivator;
import org.apache.tapestry.web.WebUtils;

/**
 * Adapts a {@link javax.portlet.PortletConfig}&nbsp; as {@link org.apache.tapestry.web.WebActivator}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class PortletWebActivator implements WebActivator
{
    private final PortletConfig _config;

    public PortletWebActivator(PortletConfig config)
    {
        Defense.notNull(config, "config");

        _config = config;
    }

    public String getActivatorName()
    {
        return _config.getPortletName();
    }

    public List getInitParameterNames()
    {
        return WebUtils.toSortedList(_config.getInitParameterNames());
    }

    public String getInitParameterValue(String name)
    {
        return _config.getInitParameter(name);
    }

    public void describeTo(DescriptionReceiver receiver)
    {
        receiver.describeAlternate(_config);
    }

}