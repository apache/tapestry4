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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.hivemind.ApplicationRuntimeException;
import org.apache.commons.hivemind.Location;
import org.apache.tapestry.util.prop.OgnlUtils;

/**
 *  Base class for creating tokens which may contain other tokens.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 0.2.9
 * 
 **/

abstract class AbstractToken implements IScriptToken
{
    private List _tokens;
    private Location _location;
    protected AbstractToken(Location location)
    {
        _location = location;
    }

    public Location getLocation()
    {
        return _location;
    }

    public void addToken(IScriptToken token)
    {
        if (_tokens == null)
            _tokens = new ArrayList();

        _tokens.add(token);
    }

    /**
     *  Invokes {@link IScriptToken#write(StringBuffer,ScriptSession)}
     *  on each child token (if there are any).
     *
     **/

    protected void writeChildren(StringBuffer buffer, ScriptSession session)
    {
        if (_tokens == null)
            return;

        Iterator i = _tokens.iterator();

        while (i.hasNext())
        {
            IScriptToken token = (IScriptToken) i.next();

            token.write(buffer, session);
        }
    }

    /**
     * Evaluates the expression against the session's symbols, using
     * {@link OgnlUtils#get(String, ClassResolver, Object)} and
     * returns the result.
     */
    protected Object evaluate(String expression, ScriptSession session)
    {

        try
        {
            return OgnlUtils.get(expression, session.getSymbols());
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(ex.getMessage(), _location, ex);
        }
    }
}