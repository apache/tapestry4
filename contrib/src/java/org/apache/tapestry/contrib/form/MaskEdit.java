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

package org.apache.tapestry.contrib.form;

import org.apache.tapestry.BaseComponent;

/**
 * Provides a mask edit HTML &lt;input type="text"&gt; form element.
 * <p>
 * Mask edit field validates the text the user enters against a mask that encodes the valid forms
 * the text can take. The mask can also format text that is displayed to the user.
 * <p>
 * <table border="1" cellpadding="2">
 * <tr>
 * <th>Mask character</th>
 * <th>Meaning in mask</th>
 * </tr>
 * <tr>
 * <td>&nbsp;l</td>
 * <td>&nbsp;Mixed case letter character [a..z, A..Z]</td>
 * </tr>
 * <tr>
 * <td>&nbsp;L</td>
 * <td>&nbsp;Upper case letter character [A..Z]</td>
 * </tr>
 * <tr>
 * <td>&nbsp;a</td>
 * <td>&nbsp;Mixed case alpha numeric character [a..z, A..Z, 0..1]</td>
 * </tr>
 * <tr>
 * <td>&nbsp;A</td>
 * <td>&nbsp;Upper case alpha numeric character [A..Z, 0..9]</td>
 * </tr>
 * <tr>
 * <td>&nbsp;#</td>
 * <td>&nbsp;Numeric character [0..9]</td>
 * </tr>
 * <tr>
 * <td>&nbsp;_</td>
 * <td>&nbsp;Reserved character for display, do not use.</td>
 * </tr>
 * <tr>
 * <td>&nbsp;others</td>
 * <td>&nbsp;Non editable character for display.</td>
 * </tr>
 * </table>
 * <p>
 * This component requires JavaScript to be enabled in the client browser.
 * <p>[ <a href="../../../../../ComponentReference/MaskEdit.html">Component Reference </a>]
 * 
 * @author Malcolm Edgar
 * @since 2.3
 */

public abstract class MaskEdit extends BaseComponent
{
    public abstract String getMask();

    public abstract void setMask(String mask);

    public abstract String getValue();

    public abstract void setValue(String value);

    public abstract boolean isDisabled();

    public abstract void setDisabled(boolean disabled);
}
