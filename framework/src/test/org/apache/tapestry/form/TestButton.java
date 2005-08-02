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

import org.apache.tapestry.IBinding;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.components.BaseComponentTestCase;
import org.apache.tapestry.spec.ComponentSpecification;

public class TestButton extends BaseComponentTestCase
{
    public void testRender()
    {
        Button b = (Button) newInstance(Button.class, new Object[]
        { "name", "assignedName" });

        IMarkupWriter writer = newBufferWriter();
        IRequestCycle cycle = newCycle();

        replayControls();

        b.renderFormComponent(writer, cycle);

        verifyControls();

        assertBuffer("<button type=\"button\" name=\"assignedName\"></button>");
    }

    public void testRenderLabel()
    {
        Button b = (Button) newInstance(Button.class, new Object[]
        { "name", "assignedName", "label", "Label" });

        IMarkupWriter writer = newBufferWriter();
        IRequestCycle cycle = newCycle();

        replayControls();

        b.renderFormComponent(writer, cycle);

        verifyControls();

        assertBuffer("<button type=\"button\" name=\"assignedName\">Label</button>");
    }

    public void testRenderInformalParameters()
    {
        Button b = (Button) newInstance(Button.class, new Object[]
        { "name", "assignedName", "specification", new ComponentSpecification() });

        IMarkupWriter writer = newBufferWriter();
        IRequestCycle cycle = newCycle();

        IBinding binding = newBinding("informal-value");

        b.setBinding("informal", binding);

        replayControls();

        b.renderFormComponent(writer, cycle);

        verifyControls();

        assertBuffer("<button type=\"button\" name=\"assignedName\" informal=\"informal-value\"></button>");
    }

    public void testRenderWithId()
    {
        Button b = (Button) newInstance(Button.class, new Object[]
        { "idParameter", "foo", "name", "assignedName" });

        IMarkupWriter writer = newBufferWriter();
        IRequestCycle cycle = newCycleGetUniqueId("foo", "foo$unique");

        replayControls();

        b.renderFormComponent(writer, cycle);

        verifyControls();

        assertBuffer("<button type=\"button\" name=\"assignedName\" id=\"foo$unique\"></button>");
    }

    public void testSubmit()
    {
        Button b = (Button) newInstance(Button.class);

        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();

        replayControls();

        b.rewindFormComponent(writer, cycle);

        verifyControls();
    }
}
