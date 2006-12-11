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
package org.apache.tapestry.markup;

import org.apache.tapestry.IMarkupWriter;


/**
 * Represents a DOM style attribute that is used by {@link IMarkupWriter} to 
 * manage rendering attributes.
 * 
 * @author jkuhnert
 */
public interface Attribute
{
    /**
     * Retrieves the current value for the attribute.
     * 
     * @return The current value for the attribute.
     */
    Object getValue();
    
    /**
     * Whether or not this attribute should be written out in raw form 
     * as specified by {@link IMarkupWriter#attribute(String, boolean)} .
     * 
     * @return True if content will be written in raw form, false otherwise.
     */
    boolean isRaw();
}
