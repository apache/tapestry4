// Copyright 2004 The Apache Software Foundation
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

import java.io.OutputStream;

import org.apache.tapestry.AbstractPage;
import org.apache.tapestry.IMarkupWriter;

/**
 *  Concrete class for HTML pages. Most pages
 *  should be able to simply subclass this, adding new properties and
 *  methods.  An unlikely exception would be a page that was not based
 *  on a template.
 *
 *  @author Howard Lewis Ship
 **/

public class BasePage extends AbstractPage
{
    /**
     *  Returns a new {@link HTMLWriter}.
     *
     **/

    public IMarkupWriter getResponseWriter(OutputStream out)
    {
        return new HTMLWriter(out, getOutputEncoding());
    }
}
