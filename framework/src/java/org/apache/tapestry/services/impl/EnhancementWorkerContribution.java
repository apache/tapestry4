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

package org.apache.tapestry.services.impl;

import org.apache.hivemind.impl.BaseLocatable;
import org.apache.tapestry.enhance.EnhancementWorker;

/**
 * Contribution to the <code>xxx.Foo</code> configuration point; wraps an instance of
 * {@link org.apache.tapestry.enhance.EnhancementWorker}with and id and etc. to allow ordering.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class EnhancementWorkerContribution extends BaseLocatable
{
    private String _id;

    private String _before;

    private String _after;

    private EnhancementWorker _worker;

    public String getAfter()
    {
        return _after;
    }

    public void setAfter(String after)
    {
        _after = after;
    }

    public String getBefore()
    {
        return _before;
    }

    public void setBefore(String before)
    {
        _before = before;
    }

    public String getId()
    {
        return _id;
    }

    public void setId(String id)
    {
        _id = id;
    }

    public EnhancementWorker getWorker()
    {
        return _worker;
    }

    public void setWorker(EnhancementWorker worker)
    {
        _worker = worker;
    }
}