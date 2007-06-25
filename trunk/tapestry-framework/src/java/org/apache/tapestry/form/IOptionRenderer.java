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
package org.apache.tapestry.form;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;


/**
 * Interface used by {@link PropertySelection} to render each option.
 */
public interface IOptionRenderer
{

    /**
     * Called after the initial <code>&lt;select&gt;</code> tag has been rendered. It is expected that implementations
     * will then do whatever is necessary to render each option available in the model and defer writing the end 
     * <code>&lt;/select&gt;</code> to the calling component.
     * 
     * @param writer
     *          The markup writer to use.
     * @param cycle
     *          The associated cycle.
     * @param model
     *          Model containing values / labels / etc..
     * @param selected
     *          The currently selected object value, if any. Will be null if no value is currently selected.
     */
    void renderOptions(IMarkupWriter writer, IRequestCycle cycle, IPropertySelectionModel model, Object selected);
}
