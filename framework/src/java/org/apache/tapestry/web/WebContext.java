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

import java.net.URL;

import org.apache.tapestry.describe.Describable;

/**
 * A representation of a set of servlets (or portlets) packaged together as a web application
 * archive. Attributes stored within the context are global to all 'lets (but not distributed across
 * a server cluster).
 * 
 * @author Howard M. Lewis Ship
 */
public interface WebContext extends AttributeHolder, InitializationParameterHolder, Describable
{
    /**
     * Returns a URL to the resource that is mapped to a specified path. The path must begin with a
     * "/" and is interpreted as relative to the current context root.
     */

    public URL getResource(String path);

    /**
     * Returns the MIME type of the specified file, or null if the MIME type is not known.
     */
    public String getMimeType(String resourcePath);
}