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

package org.apache.tapestry.script;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.hivemind.Location;
import org.apache.tapestry.Tapestry;

/**
 *  A looping operator, modeled after the Foreach component.  It takes
 *  as its source as property and iterates through the values, updating
 *  a symbol on each pass.
 * 
 *  <p>As of 3.0, the index attribute has been added to foreach to keep 
 *  track of the current index of the iterating collection.</p>
 *
 *  @author Howard Lewis Ship, Harish Krishnaswamy
 *  @version $Id$
 *  @since 1.0.1
 * 
 **/

class ForeachToken extends AbstractToken
{
    private String _key;
    private String _index;
    private String _expression;

    ForeachToken(String key, String index, String expression, Location location)
    {
        super(location);

        _key = key;
        _index = index;
        _expression = expression;
    }

    public void write(StringBuffer buffer, ScriptSession session)
    {
        Map symbols = session.getSymbols();

        Object rawSource = evaluate(_expression, session);

        Iterator i = Tapestry.coerceToIterator(rawSource);
        
        if (i == null)
            return;

        int index = 0;

        while (i.hasNext())
        {
            Object newValue = i.next();

            symbols.put(_key, newValue);
            
            if (_index != null)
            	symbols.put(_index, String.valueOf(index));

            writeChildren(buffer, session);
            
            index++;
        }

        // We leave the last value as a symbol; don't know if that's
        // good or bad.
    }
}