/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.contrib.form;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;

/**
 *  Implementation of {@link IMultiplePropertySelectionRenderer} that
 *  produces a table of checkbox (&lt;input type=checkbox&gt;) elements.
 *
 *  @version $Id$
 *  @author Sanjay Munjal
 *
 **/

public class CheckBoxMultiplePropertySelectionRenderer
    implements IMultiplePropertySelectionRenderer
{

    /**
     *  Writes the &lt;table&gt; element.
     *
     **/

    public void beginRender(
        MultiplePropertySelection component,
        IMarkupWriter writer,
        IRequestCycle cycle)
    {
        writer.begin("table");
        writer.attribute("border", 0);
        writer.attribute("cellpadding", 0);
        writer.attribute("cellspacing", 2);
    }

    /**
     *  Closes the &lt;table&gt; element.
     *
     **/

    public void endRender(
        MultiplePropertySelection component,
        IMarkupWriter writer,
        IRequestCycle cycle)
    {
        writer.end(); // <table>
    }

    /**
     *  Writes a row of the table.  The table contains two cells; the first is the checkbox,
     *  the second is the label for the checkbox.
     *
     **/

    public void renderOption(
        MultiplePropertySelection component,
        IMarkupWriter writer,
        IRequestCycle cycle,
        IPropertySelectionModel model,
        Object option,
        int index,
        boolean selected)
    {
        writer.begin("tr");
        writer.begin("td");

        writer.beginEmpty("input");
        writer.attribute("type", "checkbox");
        writer.attribute("name", component.getName());
        writer.attribute("value", model.getValue(index));

        if (component.isDisabled())
            writer.attribute("disabled");

        if (selected)
            writer.attribute("checked");

        writer.end(); // <td>

        writer.println();

        writer.begin("td");
        writer.print(model.getLabel(index));
        writer.end(); // <td>
        writer.end(); // <tr>

        writer.println();
    }
}