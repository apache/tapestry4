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

package org.apache.tapestry.util;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.apache.tapestry.Tapestry;

/**
 *  A complex key that may be used as an alternative to nested
 *  {@link java.util.Map}s.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class MultiKey implements Externalizable
{
    /**
     *  @since 2.0.4
     * 
     **/
    
    private static final long serialVersionUID = 4465448607415788806L;
    
    private static final int HASH_CODE_UNSET = -1;

    private transient int hashCode = HASH_CODE_UNSET;

    private Object[] keys;

    /**
     *  Public no-arguments constructor needed to be compatible with
     *  {@link Externalizable}; this leaves the new MultiKey in a
     *  non-usable state and shouldn't be used by user code.
     *
     **/

    public MultiKey()
    {
    }

    /**
     *  Builds a <code>MultiKey</code> from an array of keys.  If the array is not
     *  copied, then it must not be modified.
     * 
     *  @param keys The components of the key.
     *  @param makeCopy If true, a copy of the keys is created.  If false,
     *  the keys are simple retained by the <code>MultiKey</code>.
     *
     *  @throws IllegalArgumentException if keys is null, of if the
     *  first element of keys is null.
     *
     **/

    public MultiKey(Object[] keys, boolean makeCopy)
    {
        super();

        if (keys == null || keys.length == 0)
            throw new IllegalArgumentException(Tapestry.getMessage("MultiKey.null-keys"));

        if (keys[0] == null)
            throw new IllegalArgumentException(Tapestry.getMessage("MultiKey.first-element-may-not-be-null"));

        if (makeCopy)
        {
            this.keys = new Object[keys.length];
            System.arraycopy(keys, 0, this.keys, 0, keys.length);
        }
        else
            this.keys = keys;
    }

    /**
     *  Returns true if:
     *  <ul>
     *  <li>The other object is a <code>MultiKey</code>
     *  <li>They have the same number of key elements
     *  <li>Every element is an exact match or is equal
     *  </ul>
     *
     **/

    public boolean equals(Object other)
    {
        int i;

        if (other == null)
            return false;

        if (keys == null)
            throw new IllegalStateException(Tapestry.getMessage("MultiKey.no-keys"));

        // Would a hashCode check be worthwhile here?

        try
        {
            MultiKey otherMulti = (MultiKey) other;

            if (keys.length != otherMulti.keys.length)
                return false;

            for (i = 0; i < keys.length; i++)
            {
                // On an exact match, continue.  This means that null matches
                // null.

                if (keys[i] == otherMulti.keys[i])
                    continue;

                // If either is null, but not both, then
                // not a match.

                if (keys[i] == null || otherMulti.keys[i] == null)
                    return false;

                if (!keys[i].equals(otherMulti.keys[i]))
                    return false;

            }

            // Every key equal.  A match.

            return true;
        }
        catch (ClassCastException e)
        {
        }

        return false;
    }

    /**
     *  Returns the hash code of the receiver, which is computed from all the
     *  non-null key elements.  This value is computed once and
     *  then cached, so elements should not change their hash codes 
     *  once created (note that this
     *  is the same constraint that would be used if the individual 
     *  key elements were
     *  themselves {@link java.util.Map} keys.
     * 
     *
     **/

    public int hashCode()
    {
        if (hashCode == HASH_CODE_UNSET)
        {
            hashCode = keys[0].hashCode();

            for (int i = 1; i < keys.length; i++)
            {
                if (keys[i] != null)
                    hashCode ^= keys[i].hashCode();
            }
        }

        return hashCode;
    }

    /**
    *  Identifies all the keys stored by this <code>MultiKey</code>.
    *
    **/

    public String toString()
    {
        StringBuffer buffer;
        int i;

        buffer = new StringBuffer("MultiKey[");

        for (i = 0; i < keys.length; i++)
        {
            if (i > 0)
                buffer.append(", ");

            if (keys[i] == null)
                buffer.append("<null>");
            else
                buffer.append(keys[i]);
        }

        buffer.append(']');

        return buffer.toString();
    }

    /**
     *  Writes a count of the keys, then writes each individual key.
     *
     **/

    public void writeExternal(ObjectOutput out) throws IOException
    {
        out.writeInt(keys.length);

        for (int i = 0; i < keys.length; i++)
            out.writeObject(keys[i]);
    }

    /**
     *  Reads the state previously written by {@link #writeExternal(ObjectOutput)}.
     *
     **/

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
    {
        int count;

        count = in.readInt();
        keys = new Object[count];

        for (int i = 0; i < count; i++)
            keys[i] = in.readObject();
    }
}