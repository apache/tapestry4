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

package org.apache.tapestry.parse;

import org.apache.tapestry.Tapestry;
import org.apache.tapestry.util.IPropertyHolder;
import org.apache.tapestry.util.xml.DocumentParseException;
import org.xml.sax.Attributes;

/**
 *  Handles the &lt;property&gt; element in Tapestry specifications, which is 
 *  designed to hold meta-data about specifications.
 *  Expects the top object on the stack to be a {@link org.apache.tapestry.util.IPropertyHolder}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public class SetMetaPropertyRule extends AbstractSpecificationRule
{
    private String _name;
    private String _value;

    public void begin(String namespace, String name, Attributes attributes) throws Exception
    {
        _name = getValue(attributes, "name");

        // First, get the value from the attribute, if present

        _value = getValue(attributes, "value");

    }

    public void body(String namespace, String name, String text) throws Exception
    {
        if (Tapestry.isNull(text))
            return;

        if (_value != null)
        {
            throw new DocumentParseException(
                Tapestry.getString("SpecificationParser.no-attribute-and-body", "value", name),
                getResourceLocation());
        }

        _value = text.trim();
    }

    public void end(String namespace, String name) throws Exception
    {
        if (_value == null)
            throw new DocumentParseException(
                Tapestry.getString(
                    "SpecificationParser.required-extended-attribute",
                    name,
                    "value"),
                    getResourceLocation());

        IPropertyHolder holder = (IPropertyHolder) digester.peek();

        holder.setProperty(_name, _value);

        _name = null;
        _value = null;
    }

}
