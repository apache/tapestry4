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

import junit.framework.Assert;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

/**
 * Used to test rendering and rewinding the Form component.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 * @see org.apache.tapestry.form.FormTest
 */
public abstract class FormFixture extends Form
{
    public abstract FormSupport getFormSupport();

    public abstract IMarkupWriter getExpectedWriter();

    public abstract IRequestCycle getExpectedRequestCycle();

    @Override
    protected FormSupport newFormSupport(IMarkupWriter writer, IRequestCycle cycle)
    {
        Assert.assertSame(getExpectedWriter(), writer);
        Assert.assertSame(getExpectedRequestCycle(), cycle);

        return getFormSupport();
    }
}
