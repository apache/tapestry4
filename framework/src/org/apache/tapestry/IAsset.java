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

package org.apache.tapestry;

import java.io.InputStream;
import java.util.Locale;

/**
 *  Representation of a asset (GIF, JPEG, etc.) that may be owned by a
 *  {@link IComponent}.
 *
 *  <p>Assets may be completely external (i.e., on some other web site), 
 *  contained by the {@link javax.servlet.ServletContext},  
 *  or stored somewhere in the classpath.
 *
 *  <p>In the latter two cases, the resource may be localized.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public interface IAsset
{
    /**
     *  Returns a URL for the asset, ready to be inserted into the output HTML.
     *  If the asset can be localized, the localized version (matching the
     *  {@link java.util.Locale} of the current {@link IPage page}) is returned.
     * 
     *  @throws ApplicationRuntimeException if the asset does not exist.
     *
     **/

    public String buildURL(IRequestCycle cycle);

    /**
     *  Accesses the localized version of the resource (if possible) and returns it as
     *  an input stream.  A version of the resource localized to the
     *  current {@link IPage page} is returned.
     * 
     *  @throws ApplicationRuntimeException if the asset does not exist, or
     *  can't be read.
     *
     **/

    public InputStream getResourceAsStream(IRequestCycle cycle);
    
    /**
     *  Accesses the localized version of the resource (if possible) and
     *  returns it as an input stream.
     * 
     *  @throws ApplicationRuntimeException if the asset does not exist, or
     *  can't be read.
     * 
     *  @since 2.2
     * 
     **/
    
    public InputStream getResourceAsStream(IRequestCycle cycle, Locale locale);
}