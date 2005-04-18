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

package org.apache.tapestry.web;

import java.util.List;

/**
 * Interface for objects that can hold attributes. Attributes are objects that are stored and
 * retrieved using unique string names. There may be constraints on when attributes can be accessed
 * (for example, {@link org.apache.tapestry.web.WebSession}attributes should not be
 * changed once the response is committed).
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public interface AttributeHolder
{
    /**
     * Returns a list of all known attributes in ascending alphabetical order. May be empty (but
     * won't be null).
     * 
     * @returns Unmodifiable list of string attribute names.
     */

    public List getAttributeNames();

    /**
     * Returns the named object, or null if no attribute has been stored with the given name.
     */

    public Object getAttribute(String name);

    /**
     * Updates the attribute, replacing (or removing) its value. For certain implementations, the
     * attribute may need to be serializable (for example, a {@link WebSession}
     * &nbsp;attribute in a clustered application).
     * 
     * @param name
     *            the name of the attribute to update
     * @param attribute
     *            the new value for the attribute, or null to delete the attribute entirely.
     */

    public void setAttribute(String name, Object attribute);
}