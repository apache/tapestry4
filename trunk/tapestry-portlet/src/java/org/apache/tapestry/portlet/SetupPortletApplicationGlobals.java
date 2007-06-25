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

import javax.portlet.PortletConfig;

import org.apache.tapestry.services.impl.AbstractSetupApplicationGlobals;

/**
 * Alternative to
 * {@link org.apache.tapestry.services.impl.SetupServletApplicationGlobals},
 * but for Portlets and the
 * <code>tapestry.portlet.services.FactoryServices</code> configuration point.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class SetupPortletApplicationGlobals extends
        AbstractSetupApplicationGlobals implements
        PortletApplicationInitializer
{

    public void initialize(PortletConfig portletConfig)
    {
        initialize("portlet");
    }

}
