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

package org.apache.tapestry.engine;

/**
 *  A source for configuration properties.
 *
 *  @author Howard Lewis Ship
 *  @since 2.3
 *
 **/

public interface IPropertySource
{
    /**
     *  Returns the value for a given property, or null if the
     *  source does not provide a value for the named property.
     *  Implementations of IPropertySource may use delegation
     *  to resolve the value (that is, if one property source returns null,
     *  it may forward the request to another source).
     * 
     **/
    
    public String getPropertyValue(String propertyName);
}
