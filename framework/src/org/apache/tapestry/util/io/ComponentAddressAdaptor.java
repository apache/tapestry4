/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2004 The Apache Software Foundation.  All rights
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

package org.apache.tapestry.util.io;

import java.io.IOException;

import org.apache.tapestry.Tapestry;
import org.apache.tapestry.util.ComponentAddress;

/**
 *  Squeezes a org.apache.tapestry.ComponentAddress.
 * 
 *  @author mindbridge
 *  @version $Id$
 *  @since 2.2
 * 
 **/

public class ComponentAddressAdaptor implements ISqueezeAdaptor
{
    private static final String PREFIX = "A";
    private static final char SEPARATOR = '/';
    
    public String squeeze(DataSqueezer squeezer, Object data) throws IOException
    {
        ComponentAddress address = (ComponentAddress) data;

        // a 'null' id path is encoded as an empty string
        String idPath = address.getIdPath();
        if (idPath == null)
        	idPath = "";

        return PREFIX + address.getPageName() + SEPARATOR + idPath;
    }

    public Object unsqueeze(DataSqueezer squeezer, String string) throws IOException
    {
        int separator = string.indexOf(SEPARATOR);
        if (separator < 0) 
            throw new IOException(Tapestry.getMessage("ComponentAddressAdaptor.no-separator"));

        String pageName = string.substring(1, separator);
        String idPath = string.substring(separator + 1);
        if (idPath.equals(""))
        	idPath = null;

        return new ComponentAddress(pageName, idPath);
    }

    public void register(DataSqueezer squeezer)
    {
        squeezer.register(PREFIX, ComponentAddress.class, this);
    }

}
