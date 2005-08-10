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

/**
 * Lightweight serialization used to encode values into strings that are stored in query parameters
 * and hidden fields.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public interface DataSqueezer
{
    /**
     * Squeezes the data object into a String by locating an appropriate adaptor that can perform
     * the conversion. data may be null.
     */
    public String squeeze(Object data);

    /**
     * A convenience; invokes {@link #squeeze(Object)}for each element in the data array. If data
     * is null, returns null.
     */
    public String[] squeeze(Object[] data);

    /**
     * Unsqueezes the string. Note that in a special case, where the first character of the string
     * is not a recognized prefix, it is assumed that the string is simply a string, and returned
     * with no change.
     */
    public Object unsqueeze(String string);

    /**
     * Convenience method for unsqueezing many strings (back into objects).
     * <p>
     * If strings is null, returns null.
     */
    public Object[] unsqueeze(String[] strings);
}