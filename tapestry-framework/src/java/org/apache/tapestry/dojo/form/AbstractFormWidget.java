// Copyright May 4, 2006 The Apache Software Foundation
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
package org.apache.tapestry.dojo.form;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.AbstractFormComponent;


/**
 * Represents a dojo widget that manages an html form input
 * field.
 * 
 * @author jkuhnert
 * @since 4.1
 */
public abstract class AbstractFormWidget extends AbstractFormComponent implements IFormWidget
{
    
    public abstract void setDestroy(boolean destroy);
    
    /**
     * Determined dynamically at runtime during rendering, informs widget implementations
     * if they should destroy their client side widget equivalents or leave them in tact.
     * 
     * @return True if the widget should be destroyed on this render, false otherwise.
     */
    public abstract boolean getDestroy();
    
    /**
     * {@inheritDoc}
     */
    public void renderWidget(IMarkupWriter writer, IRequestCycle cycle)
    {
        renderComponent(writer, cycle);
    }
    
    /**
     * {@inheritDoc}
     */
    protected void renderFormComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        if(!cycle.isRewinding()) {

            if (!cycle.getResponseBuilder().isDynamic()
                    || cycle.getResponseBuilder().explicitlyContains(this)) {

                setDestroy(false);
            } else {

                setDestroy(true);
            }
        }
        
        renderFormWidget(writer, cycle);
    }
    
    /**
     * {@inheritDoc}
     */
    protected void rewindFormComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        rewindFormWidget(writer, cycle);
    }
    
    /**
     * Called when rendering a form widget. 
     * 
     * @param writer
     *          The markup writer to render with.
     * @param cycle
     *          The cycle associated with request.
     */
    protected abstract void renderFormWidget(IMarkupWriter writer, IRequestCycle cycle);
    
    /**
     * Called during form submission to retrieve submitted input values. 
     * Components should do any validation/retrieval of values in this method. 
     * 
     * @param writer
     *          The passed in {@link IMarkupWriter} will be a {@link org.apache.tapestry.engine.NullWriter}, making
     *          any content written ignored. 
     * @param cycle
     *           Typically used to retrieve submitted value via <code>cycle.getParameter(getName())</code>.
     */
    protected abstract void rewindFormWidget(IMarkupWriter writer, IRequestCycle cycle);
}
