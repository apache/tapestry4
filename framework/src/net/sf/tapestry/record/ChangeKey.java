/*
 *  ====================================================================
 *  The Apache Software License, Version 1.1
 *
 *  Copyright (c) 2002 The Apache Software Foundation.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  Apache Software Foundation (http://www.apache.org/)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "Apache" and "Apache Software Foundation" and
 *  "Apache Tapestry" must not be used to endorse or promote products
 *  derived from this software without prior written permission. For
 *  written permission, please contact apache@apache.org.
 *
 *  5. Products derived from this software may not be called "Apache",
 *  "Apache Tapestry", nor may "Apache" appear in their name, without
 *  prior written permission of the Apache Software Foundation.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of the Apache Software Foundation.  For more
 *  information on the Apache Software Foundation, please see
 *  <http://www.apache.org/>.
 */
package net.sf.tapestry.record;

/**
 *  Used to identify a property change.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class ChangeKey
{
    String componentPath;
    String propertyName;

    public ChangeKey(String componentPath, String propertyName)
    {
        this.componentPath = componentPath;
        this.propertyName = propertyName;
    }

    public boolean equals(Object object)
    {
        boolean propertyNameIdentical;
        boolean componentPathIdentical;

        if (object == null)
            return false;

        if (this == object)
            return true;

        try
        {
            ChangeKey other = (ChangeKey) object;

            propertyNameIdentical = propertyName == other.propertyName;
            componentPathIdentical = componentPath == other.componentPath;

            if (propertyNameIdentical && componentPathIdentical)
                return true;

            // If not identical, then see if equal. If not equal, then
            // we don't equal the other key.

            if (!propertyNameIdentical)
                if (!propertyName.equals(other.propertyName))
                    return false;

            // If this far, that propertyName is equal

            if (componentPathIdentical)
                return true;

            // If one's null and the other isn't then they can't
            // be equal.

            if (componentPath == null || other.componentPath == null)
                return false;

            // So it comes down to this ... are the two (non-null)
            // componentPath's equal?

            return componentPath.equals(other.componentPath);
        }
        catch (ClassCastException e)
        {
            return false;
        }
    }

    public String getComponentPath()
    {
        return componentPath;
    }

    public String getPropertyName()
    {
        return propertyName;
    }

    /**
     *
     *  Returns the propertyName's hash code, xor'ed with the
     *  componentPath hash code (if componentPath is non-null).
     *
     **/

    public int hashCode()
    {
        int result;

        result = propertyName.hashCode();

        if (componentPath != null)
            result ^= componentPath.hashCode();

        return result;
    }
}