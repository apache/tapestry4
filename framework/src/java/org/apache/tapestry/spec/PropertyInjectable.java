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

package org.apache.tapestry.spec;

/**
 * Interface extended by several specification interfaces to indicate that the runtime object
 * constructed from the specification can be injected into the component class.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public interface PropertyInjectable
{
    /**
     * Returns the name of the property to be created for this component, or null if no property
     * should be created.
     */
    public String getPropertyName();

    public void setPropertyName(String propertyName);

}