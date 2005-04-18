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

import org.apache.tapestry.describe.Describable;

/**
 * Primarily concerned with maintaining server-side state as attributes.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public interface WebSession extends AttributeHolder, Describable
{
    /**
     * Returns a unique string identifier used to identify the session. This value is provided by
     * the container, and is typically incorporated into URLs, or stored as a HTTP cookie.
     * 
     * @see org.apache.tapestry.web.WebResponse#encodeURL(String).
     */
    public String getId();

    /**
     * Returns true if the client does not yet know about the session or if the client chooses not
     * to join the session.
     */
    public boolean isNew();
}