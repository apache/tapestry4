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

package org.apache.tapestry.contrib.form;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;

/**
 *  Defines an object that works with a {@link MultiplePropertySelection} component
 *  to render the individual elements obtained from the {@link IPropertySelectionModel model}.
 *
 *  @version $Id$
 *  @author Sanjay Munjal
 *
 **/

public interface IMultiplePropertySelectionRenderer
{
    /**
     *  Begins the rendering of the {@link MultiplePropertySelection}.
     *
     **/

    public void beginRender(
        MultiplePropertySelection component,
        IMarkupWriter writer,
        IRequestCycle cycle);

    /**
     *  Invoked for each element obtained from the {@link IPropertySelectionModel model}.
     *
     **/

    public void renderOption(
        MultiplePropertySelection component,
        IMarkupWriter writer,
        IRequestCycle cycle,
        IPropertySelectionModel model,
        Object option,
        int index,
        boolean selected);

    /**
     *  Ends the rendering of the {@link MultiplePropertySelection}.
     *
     **/

    public void endRender(
        MultiplePropertySelection component,
        IMarkupWriter writer,
        IRequestCycle cycle);
}