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

package org.apache.tapestry.form;

import java.io.IOException;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.util.io.DataSqueezer;

/**
 *  Implements a hidden field within a {@link Form}.
 *
 *  [<a href="../../../../../ComponentReference/Hidden.html">Component Reference</a>]
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public abstract class Hidden extends AbstractFormComponent
{

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        IForm form = getForm(cycle);
        boolean formRewound = form.isRewinding();

        String name = form.getElementId(this);

        // If the form containing the Hidden isn't rewound, then render.

        if (!formRewound)
        {
            // Optimiziation: if the page is rewinding (some other action or
            // form was submitted), then don't bother rendering.

            if (cycle.isRewinding())
                return;

            String externalValue = null;

            if (getEncode())
            {
                Object value = getValueBinding().getObject();

                try
                {
                    externalValue = getDataSqueezer().squeeze(value);
                }
                catch (IOException ex)
                {
                    throw new ApplicationRuntimeException(ex.getMessage(), this, null, ex);
                }
            }
            else
                externalValue = (String) getValueBinding().getObject("value", String.class);

            String id = getElementId();
            //if we would like to test the IForm.addHiddenValue(name, externalValue) method with
            //Hidden JUnit test the following code must be default. But from the performance issue
            //I don't use the id parameter clauses.  
/*			if(id == null || id.length() == 0){
				form.addHiddenValue(name, externalValue);
            }else{
				form.addHiddenValue(name, id, externalValue);
            }
*/
			form.addHiddenValue(name, id, externalValue);
            

            return;
        }

        String externalValue = cycle.getRequestContext().getParameter(name);
        Object value = null;

        if (getEncode())
        {
            try
            {
                value = getDataSqueezer().unsqueeze(externalValue);
            }
            catch (IOException ex)
            {
                throw new ApplicationRuntimeException(ex.getMessage(), this, null, ex);
            }
        }
        else
            value = externalValue;

        // A listener is not always necessary ... it's easy to code
        // the synchronization as a side-effect of the accessor method.

        getValueBinding().setObject(value);

        IActionListener listener = getListener();

        if (listener != null)
            listener.actionTriggered(this, cycle);
    }

	public String getElementId(){
		String value = null;
		IBinding idBinding = getIdBinding();
		if(idBinding != null){
			value = idBinding.getString();
		}
		return value;
	}

    /** @since 2.2 **/

    private DataSqueezer getDataSqueezer()
    {
        return getPage().getEngine().getDataSqueezer();
    }

    public abstract IActionListener getListener();

    public abstract IBinding getValueBinding();
	public abstract IBinding getIdBinding();

    /**
     * 
     *  Returns false.  Hidden components are never disabled.
     * 
     *  @since 2.2
     * 
     **/

    public boolean isDisabled()
    {
        return false;
    }

    /** 
     * 
     *  Returns true if the compent encodes object values using a
     *  {@link org.apache.tapestry.util.io.DataSqueezer}, false
     *  if values are always Strings.
     * 
     *  @since 2.2
     * 
     **/

    public abstract boolean getEncode();

    public abstract void setEncode(boolean encode);

    /**
     * Sets the encode parameter property to its default, true.
     * 
     * @since 3.0
     */
    protected void finishLoad()
    {
        setEncode(true);
    }
}