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

package org.apache.tapestry.services.impl;

import javax.servlet.http.HttpServlet;

import org.apache.tapestry.services.ApplicationInitializer;

/**
 * Stores services and configurations into the
 * {@link org.apache.tapestry.services.ApplicationGlobals tapestry.globals.ApplicationGlobals}
 * service, which is used to see the
 * {@link org.apache.tapestry.services.Infrastructure tapestry.infrastructure}&nbsp;service.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class SetupServletApplicationGlobals extends AbstractSetupApplicationGlobals implements
        ApplicationInitializer
{
    public void initialize(HttpServlet servlet)
    {
        initialize("servlet");
    }
}