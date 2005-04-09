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

package org.apache.tapestry.wml;

import org.apache.hivemind.HiveMind;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.FormEventType;
import org.apache.tapestry.form.FormSupportImpl;

/**
 * Subclass of {@link org.apache.tapestry.form.FormSupportImpl}&nbsp;that adjusts the output markup to
 * conform to WML.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class GoFormSupportImpl extends FormSupportImpl
{
    public GoFormSupportImpl(IMarkupWriter writer, IRequestCycle cycle, IForm form)
    {
        super(writer, cycle, form);
    }

    protected void writeTag(IMarkupWriter writer, String method, String url)
    {
        writer.begin("go");
        writer.attribute("method", method);
        writer.attribute("href", url);
    }

    protected void writeHiddenField(IMarkupWriter writer, String name, String id, String value)
    {
        writer.beginEmpty("postfield");
        writer.attribute("name", name );
        
        if (HiveMind.isNonBlank(id))
            writer.attribute("id", id);
        
        writer.attribute("value", value);
        writer.println();
    }
    
    public void addEventHandler(FormEventType type, String functionName)
    {
        throw new UnsupportedOperationException(
                "addEventHandler() not supported for WML Go component.");
    }
}