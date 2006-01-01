// Copyright 2004, 2005, 2006 The Apache Software Foundation
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

/**
 * Lists different types of JavaScript events that can be associated with a {@link Form} via
 * {@link Form#addEventHandler(FormEventType, String)}.
 * 
 * @author Howard Lewis Ship
 * @since 1.0.2
 * @deprecated Managing of form events is now done on the client side; this class may be removed in
 *             a future release of Tapestry.
 */

public class FormEventType
{
    /**
     * Form event triggered when the form is submitted. Allows an event handler to perform any final
     * changes before the results are posted to the server.
     * <p>
     * The JavaScript method should return <code>true</code> or <code>false</code>. If there
     * are multiple event handlers for the form they will be combined using the binary and operator (<code>&amp;&amp;</code>).
     */

    public static final FormEventType SUBMIT = new FormEventType("SUBMIT", "onsubmit");

    /**
     * Form event triggered when the form is reset; this allows an event handler to deal with any
     * special cases related to resetting.
     */

    public static final FormEventType RESET = new FormEventType("RESET", "onreset");

    private final String _name;

    private final String _addHandlerFunctionName;

    private FormEventType(String name, String addHandlerFunctionName)
    {
        _name = name;
        _addHandlerFunctionName = addHandlerFunctionName;
    }

    public String toString()
    {
        return "FormEventType[" + _name + "]";
    }

    /**
     * Returns the name of the function, on the Tapestry object (see Form.js), which should be
     * invoked. The first parameter will be the id of the form, the second will be the handler
     * itself.
     */

    public String getAddHandlerFunctionName()
    {
        return _addHandlerFunctionName;
    }
}