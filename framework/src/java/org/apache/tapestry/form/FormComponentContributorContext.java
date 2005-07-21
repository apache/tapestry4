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

package org.apache.tapestry.form;

import org.apache.tapestry.valid.ValidationConstants;

/**
 * Object that provides support to objects that implement
 * {@link org.apache.tapestry.form.FormComponentContributor}. For the moment, at least, this is all
 * about client-side JavaScript generation.
 * <p>
 * TODO: Having support for regular expressions might be useful (and would allow a single
 * {@link RegexpMatcher to be shared).
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public interface FormComponentContributorContext extends ValidationMessages
{
    /**
     * Returns a client-side DOM reference for the field for which contributions are being rendered.
     * Typically a value such as "document.myform.myfield".
     */

    public String getFieldDOM();

    /**
     * Includes the indicated script; the path is a path on the classpath.
     */

    public void includeClasspathScript(String path);

    /**
     * Adds initialization to register a submitListener on the client side. A submitListener is a
     * JavaScript method that accepts a single parameter, a (JavaScript) FormSubmitEvent.
     * 
     * @param submitListener
     *            either the name of a submit listener ("myListener"), or an inline implementation
     *            of a listener function ("function(event) { ... } ").
     */

    public void addSubmitListener(String submitListener);

    /**
     * Registers a field for automatic focus. The goal is for the first field that is in error to
     * get focus; failing that, the first required field; failing that, any field.
     * 
     * @param priority
     *            a priority level used to determine whether the registered field becomes the focus
     *            field. Constants for this purpose are defined in {@link ValidationConstants}.
     * @see org.apache.tapestry.FormBehavior#registerForFocus(IFormComponent, int)
     */

    public void registerForFocus(int priority);
}
