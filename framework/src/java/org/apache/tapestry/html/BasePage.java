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

package org.apache.tapestry.html;

import org.apache.tapestry.AbstractPage;
import org.apache.tapestry.util.ContentType;

/**
 * Base class for HTML pages. Most pages should be able to simply subclass this, adding new
 * properties and methods. An unlikely exception would be a page that was not based on a template.
 * <p>
 * Starting in release 3.1, this class is abstract, as will be any subclasses. This is because the
 * {@link org.apache.tapestry.IComponent#getMessages()}method is abstract, and is filled in at
 * runtime.
 * 
 * @author Howard Lewis Ship
 */

public abstract class BasePage extends AbstractPage
{
    /**
     * @return "text/html"
     */

    public ContentType getResponseContentType()
    {
        return new ContentType("text/html");
    }
}