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

package org.apache.tapestry.container;

import java.util.List;

/**
 * Defines methods for accessing initialization parameters.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public interface InitializationParameterHolder
{
    /**
     * Returns a sorted list of the names of all initialization parameters (which may be the empty
     * list).
     * 
     * @return List of String
     */

    public List getInitParameterNames();

    /**
     * Returns the value of the named parameter, or null if the reciever does not have such a
     * parameter.
     * 
     * @param name
     *            the name of the parameter to retrieve
     * @return the corresponding value, or null
     */

    public String getInitParameterValue(String name);
}