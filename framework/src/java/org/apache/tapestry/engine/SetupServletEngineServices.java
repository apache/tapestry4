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

package org.apache.tapestry.engine;

import java.util.List;

import javax.servlet.http.HttpServlet;

import org.apache.tapestry.services.ApplicationGlobals;
import org.apache.tapestry.services.ApplicationInitializer;

/**
 * @author Howard M. Lewis Ship
 */
public class SetupServletEngineServices implements ApplicationInitializer
{
    private ApplicationGlobals _globals;

    private List _factoryServices;

    private List _applicationServices;

    public void initialize(HttpServlet servlet)
    {
        _globals.storeServices(_factoryServices, _applicationServices);
    }

    public void setApplicationServices(List applicationServices)
    {
        _applicationServices = applicationServices;
    }

    public void setFactoryServices(List factoryServices)
    {
        _factoryServices = factoryServices;
    }

    public void setGlobals(ApplicationGlobals globals)
    {
        _globals = globals;
    }

}