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

import org.apache.hivemind.LocationHolder;

/** 
 * Specification element used to describe the injection of an application
 * state object into a component class.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public interface InjectStateSpecification extends LocationHolder
{
    /**
     * Returns the name of the property to create.
     * 
     */
    
    public String getProperty();
    
    public void setProperty(String property);
    
    /**
     * Returns the name of the application state object to be injected.
     * 
     */
    
    public String getObjectName();
    
    public void setObjectName(String objectName);   
}
