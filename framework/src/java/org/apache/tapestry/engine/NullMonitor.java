// Copyright 2004, 2005, 2006 The Apache Software Foundation
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

/**
 * Null implementation of {@link org.apache.tapestry.engine.IMonitor}.
 * 
 * @author Howard Lewis Ship
 * @since 3.0
 * @deprecated To be removed in 4.1 with no direct replacement
 */

public class NullMonitor implements IMonitor
{
    public void pageCreateBegin(String pageName)
    {
    }

    public void pageCreateEnd(String pageName)
    {
    }

    public void pageLoadBegin(String pageName)
    {
    }

    public void pageLoadEnd(String pageName)
    {
    }

    public void pageRenderBegin(String pageName)
    {
    }

    public void pageRenderEnd(String pageName)
    {
    }

    public void pageRewindBegin(String pageName)
    {
    }

    public void pageRewindEnd(String pageName)
    {
    }

    public void serviceBegin(String serviceName, String detailMessage)
    {
    }

    public void serviceEnd(String serviceName)
    {
    }

    public void serviceException(Throwable exception)
    {
    }

    public void sessionBegin()
    {
    }

}