// Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.services;

/**
 * Starting with 3.1, copmonents do not always have a zero-args constructor; the enhanced subclass
 * may take some parameters used to initialize instance variables. This interface represents a
 * wrapper around a constructor and an array of parameters that can be used to stamp out new
 * instances of a component.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public interface ComponentConstructor
{
    /**
     * Requests that a new instance of the component.
     */

    public Object newInstance();

    /**
     * Returns the class actually instantiated (which may be an enhanced subclass).
     */
    public Class getComponentClass();
}