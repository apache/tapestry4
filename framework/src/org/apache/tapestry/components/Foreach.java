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

package org.apache.tapestry.components;

import java.util.Iterator;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.RenderOnlyPropertyException;
import org.apache.tapestry.RequestCycleException;
import org.apache.tapestry.Tapestry;

/**
 *  Repeatedly renders its wrapped contents while iterating through
 *  a list of values.
 *
 *  [<a href="../../../../../ComponentReference/Foreach.html">Component Reference</a>]
 *
 *  <p>
 *  While the component is rendering, the property
 *  {@link #getValue() value} (accessed as
 *  <code>components.<i>foreach</i>.value</code>
 *  is set to each successive value from the source,
 *  and the property
 *  {@link #getIndex() index} is set to each successive index
 *  into the source (starting with zero).
 * 
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public abstract class Foreach extends AbstractComponent
{
    private Object _value;
    private int _index;
    private boolean _rendering;

    public abstract IBinding getIndexBinding();


    /**
     *  Gets the source binding and returns an {@link Iterator}
     *  representing
     *  the values identified by the source.  Returns an empty {@link Iterator}
     *  if the binding, or the binding value, is null.
     *
     *  <p>Invokes {@link Tapestry#coerceToIterator(Object)} to perform
     *  the actual conversion.
     *
     **/

    protected Iterator getSourceData()
    {
    	Object source = getSource();
    	
 		if (source == null)
 			return null;
 		
        return Tapestry.coerceToIterator(source);
    }

    public abstract IBinding getValueBinding();

    /**
     *  Gets the source binding and iterates through
     *  its values.  For each, it updates the value binding and render's its wrapped elements.
     *
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
        Iterator dataSource = getSourceData();

        // The dataSource was either not convertable, or was empty.

        if (dataSource == null)
            return;


        try
        {
            _rendering = true;
            _value = null;
            _index = 0;
            
            IBinding indexBinding = getIndexBinding();
            IBinding valueBinding = getValueBinding();
            String element = getElement();

            boolean hasNext = dataSource.hasNext();

            while (hasNext)
            {
                _value = dataSource.next();
                hasNext = dataSource.hasNext();

                if (indexBinding != null)
                    indexBinding.setInt(_index);

                if (valueBinding != null)
                    valueBinding.setObject(_value);

                if (element != null)
                {
                    writer.begin(element);
                    generateAttributes(writer, cycle);
                }

                renderBody(writer, cycle);

                if (element != null)
                    writer.end();

                _index++;
            }
        }
        finally
        {
            _value = null;
            _rendering = false;
        }
    }

    /**
     *  Returns the most recent value extracted from the source parameter.
     *
     *  @throws RenderOnlyPropertyException if the Foreach is not currently rendering.
     *
     **/

    public Object getValue()
    {
        if (!_rendering)
            throw new RenderOnlyPropertyException(this, "value");

        return _value;
    }

    public abstract String getElement();

    public abstract Object getSource();

    /**
     *  The index number, within the {@link #getSource() source}, of the
     *  the current value.
     * 
     *  @throws RenderOnlyPropertyException if the Foreach is not currently rendering.
     *
     *  @since 2.2
     * 
     **/
    
    public int getIndex()
    {
        if (!_rendering)
            throw new RenderOnlyPropertyException(this, "index");
        
        return _index;
    }

}