//  Copyright 2004 The Apache Software Foundation
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

import org.apache.commons.lang.enum.Enum;

/**
 *  Lists different types of JavaScript events that can be associated
 *  with a {@link Form} via {@link Form#addEventHandler(FormEventType, String)}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.2
 **/

public class FormEventType extends Enum
{
    /**
     *  Form event triggered when the form is submitted.  Allows an event handler
     *  to perform any final changes before the results are posted to the server.
     *
     *  <p>The JavaScript method should return <code>true</code> or
     * <code>false</code>.  If there are multiple event handlers for the form
     * they will be combined using the binary and operator (<code>&amp;&amp;</code>).
     *
     **/

    public static final FormEventType SUBMIT = new FormEventType("SUBMIT", "onsubmit");

    /**
     *  Form event triggered when the form is reset; this allows an event handler
     *  to deal with any special cases related to resetting.
     *
     **/

    public static final FormEventType RESET = new FormEventType("RESET", "onreset");

    private String _propertyName;

    private FormEventType(String name, String propertyName)
    {
        super(name);

        _propertyName = propertyName;
    }

    /** 
     *  Returns the DOM property corresponding to event type (used when generating
     *  client-side scripting).
     *
     **/

    public String getPropertyName()
    {
        return _propertyName;
    }

    /**
     *  Returns true if multiple functions should be combined
     *  with the <code>&amp;&amp;</code> operator.  Otherwise,
     *  the event handler functions are simply invoked
     *  sequentially (as a series of JavaScript statements).
     *
     **/

    public boolean getCombineUsingAnd()
    {
        return this == FormEventType.SUBMIT;
    }
}