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

import javax.servlet.http.HttpServlet;

import org.apache.hivemind.impl.BaseLocatable;
import org.apache.tapestry.services.ApplicationInitializer;

/**
 * A contribution to the <code>tapestry.ApplicationInitializers</code> configuration
 * point.  Contributions will be ordered
 *
 * @author Howard Lewis Ship
 * @since 3.1
 */
public class InitializerContribution extends BaseLocatable implements ApplicationInitializer
{
    private String _name;
    private String _before;
    private String _after;
    private ApplicationInitializer _initializer;

    public void initialize(HttpServlet servlet)
    {
        _initializer.initialize(servlet);
    }

    public String getName()
    {
        return _name;
    }

    public String getBefore()
    {
        return _before;
    }

    public String getAfter()
    {
        return _after;
    }

    public void setAfter(String string)
    {
        _after = string;
    }

    public void setBefore(String string)
    {
        _before = string;
    }

    public void setInitializer(ApplicationInitializer initializer)
    {
        _initializer = initializer;
    }

    public void setName(String string)
    {
        _name = string;
    }

}
