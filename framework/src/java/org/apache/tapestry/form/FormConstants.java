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

/**
 * Constants used by the Form component.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class FormConstants
{
    /**
     * Normal submit of the form, typically by the user clicking a submit control.
     */

    public static final String SUBMIT_NORMAL = "submit";

    /**
     * Indicates that the form was cancelled. A form is cancelled on the client side when the
     * JavaScript function document.<em>form-name</em>.events.cancel() is invoked.
     */

    public static final String SUBMIT_CANCEL = "cancel";

    /**
     * Indicates that the form was submitted to force a refresh. Most client-side submit listeners
     * will have been skipped (particularily, those related to validaton). A form is submitted for
     * refresh on the client side when the JavaScript function document.<em>form-name</em>.events.refresh()
     * is invoked.
     */
    public static final String SUBMIT_REFRESH = "refresh";
}
