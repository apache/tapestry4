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

package org.apache.tapestry.record;

import org.apache.tapestry.web.WebSession;

/**
 * Callback interface used to operate on a subset of attributes stored within a
 * {@link org.apache.tapestry.web.WebSession} whose names match a particular prefix.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public interface WebSessionAttributeCallback
{
    /**
     * Invoked for each matching attribute.
     * 
     * @param session
     *            session containing the attribute
     * @param name
     *            full name of the attribute
     */
    void handleAttribute(WebSession session, String name);
}
