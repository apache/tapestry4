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

package org.apache.tapestry.vlib;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.form.IPropertySelectionModel;

/**
 *  This class is used as a property selection model to select a primary key.
 *  We assume that the primary keys are integers, which makes it easy to
 *  translate between the various representations.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class EntitySelectionModel implements IPropertySelectionModel
{
    private static class Entry
    {
        Integer primaryKey;
        String label;

        Entry(Integer primaryKey, String label)
        {
            this.primaryKey = primaryKey;
            this.label = label;
        }

    }

    private static final int LIST_SIZE = 20;

    private List entries = new ArrayList(LIST_SIZE);

    public void add(Integer key, String label)
    {
        Entry entry;

        entry = new Entry(key, label);
        entries.add(entry);
    }

    public int getOptionCount()
    {
        return entries.size();
    }

    private Entry get(int index)
    {
        return (Entry) entries.get(index);
    }

    public Object getOption(int index)
    {
        return get(index).primaryKey;
    }

    public String getLabel(int index)
    {
        return get(index).label;
    }

    public String getValue(int index)
    {
        Integer primaryKey;

        primaryKey = get(index).primaryKey;

        if (primaryKey == null)
            return "";

        return primaryKey.toString();
    }

    public Object translateValue(String value)
    {
        if (value.equals(""))
            return null;

        try
        {
            return new Integer(value);
        }
        catch (NumberFormatException e)
        {
            throw new ApplicationRuntimeException("Could not convert '" + value + "' to an Integer.", e);
        }
    }
}