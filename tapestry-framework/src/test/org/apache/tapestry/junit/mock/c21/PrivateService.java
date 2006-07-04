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

package org.apache.tapestry.junit.mock.c21;

import java.io.IOException;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;

/**
 * Test case for service which can't be instantiated.
 * 
 * @author Howard Lewis Ship
 * @since 3.0
 */
public class PrivateService implements IEngineService
{
    // Can't be instantiated!
    private PrivateService()
    {
    }

    public ILink getLink(boolean post, Object parameter)
    {
        return null;
    }

    public void service(IRequestCycle cycle) throws IOException
    {
    }

    public String getName()
    {
        return null;
    }

}