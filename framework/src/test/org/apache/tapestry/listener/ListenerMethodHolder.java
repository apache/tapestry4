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

package org.apache.tapestry.listener;

import java.util.Map;

import org.apache.tapestry.IRequestCycle;

/**
 * Used by {@link org.apache.tapestry.listener.TestListenerMapSource}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class ListenerMethodHolder
{
    private RuntimeException _exception;

    public void wrongTypes(Map map)
    {
    }

    public void fred(String string, int count)
    {

    }

    public void wilma(IRequestCycle cycle, int argument)
    {
    }

    public void noMatch(String argument1, double argument2, long argument3)
    {

    }

    /**
     * Tapestry 3.0 and earlier style.
     */

    public void pebbles(IRequestCycle cycle)
    {
    }

    public void barney(String string)
    {

    }

    public void barney()
    {

    }

    public String toString()
    {
        return "ListenerMethodHolder";
    }

    public void throwsException()
    {
        throw _exception;
    }

    public void setException(RuntimeException ex)
    {
        _exception = ex;
    }
}