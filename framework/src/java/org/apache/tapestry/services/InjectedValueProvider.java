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

package org.apache.tapestry.services;

import org.apache.hivemind.Location;

/**
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public interface InjectedValueProvider
{
    /**
     * Looks up a value in HiveMind using the locator, as with the HiveMind object translator. The
     * module will always be the Tapestry module (which means that most service ids or
     * configurations will have to be fully qualified.
     * 
     * @param objectReference
     *            the reference to the HiveMind object to obtain, with a leading prefix indicating
     *            type (i.e., "service:", "configuration:", etc.
     * @param location
     *            the location of the value, used if an error must be reported.
     * @return the value
     */

    public Object obtainValue(String objectReference, Location location);
}