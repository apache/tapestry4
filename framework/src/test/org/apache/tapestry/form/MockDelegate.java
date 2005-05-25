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

import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.valid.IFieldTracking;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.valid.IValidator;
import org.apache.tapestry.valid.ValidationConstraint;
import org.apache.tapestry.valid.ValidatorException;

/**
 * Mock implementation of {@link org.apache.tapestry.valid.IValidationDelegate} used for testing
 * (particularily, to test how the delegate decorates fields and field labels.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class MockDelegate implements IValidationDelegate
{
    private IFormComponent _component;

    private String _input;

    private boolean _inError;

    public MockDelegate()
    {
        this(false);
    }

    public MockDelegate(boolean inError)
    {
        _inError = inError;
    }

    public IFormComponent getFormComponent()
    {
        return _component;
    }

    public void setFormComponent(IFormComponent component)
    {
        _component = component;
    }

    public boolean isInError()
    {
        return _inError;
    }

    public String getFieldInputValue()
    {
        return _input;
    }

    public List getFieldTracking()
    {
        return null;
    }

    public void reset()
    {
    }

    public void clear()
    {
    }

    public void recordFieldInputValue(String input)
    {
        _input = input;
    }

    public void record(ValidatorException ex)
    {
    }

    public void record(String message, ValidationConstraint constraint)
    {
    }

    public void record(IRender errorRenderer, ValidationConstraint constraint)
    {
    }

    public void writePrefix(IMarkupWriter writer, IRequestCycle cycle, IFormComponent component,
            IValidator validator)
    {
        writer.begin("span");
        writer.attribute("class", "prefix");
    }

    public void writeAttributes(IMarkupWriter writer, IRequestCycle cycle,
            IFormComponent component, IValidator validator)
    {
        writer.attribute("class", "validation-delegate");
    }

    public void writeSuffix(IMarkupWriter writer, IRequestCycle cycle, IFormComponent component,
            IValidator validator)
    {
        writer.end();
    }

    public void writeLabelPrefix(IFormComponent component, IMarkupWriter writer, IRequestCycle cycle)
    {
    }

    public void writeLabelSuffix(IFormComponent component, IMarkupWriter writer, IRequestCycle cycle)
    {
    }

    public boolean getHasErrors()
    {
        return false;
    }

    public IFieldTracking getCurrentFieldTracking()
    {
        return null;
    }

    public List getErrorRenderers()
    {
        return null;
    }

}
