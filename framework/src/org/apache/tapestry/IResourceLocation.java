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

import java.net.URL;
import java.util.Locale;

/**
 *  Describes the location of a resource, such as a specification
 *  or template.  Resources may be located within the classpath,
 *  or within the Web context root or somewhere else entirely.
 * 
 *  <p>
 *  Resources may be either base or localized.  A localized
 *  version of a base resource may be obtained
 *  via {@link #getLocalization(Locale)}.
 * 
 *  <p>
 *  Resource locations are used as Map keys, they must 
 *  implement {@link java.lang.Object#hashCode()} and
 *  {@link java.lang.Object#equals(java.lang.Object)}
 *  properly.
 * 
 *  <p>
 *  Resource locations are valid even if the corresponding
 *  resource <i>doesn't exist</i>.  To verify if a localization
 *  actually exists, use {@link #getResourceURL()}, which returns
 *  null if the resource doesn't exist.  {@link #getLocalization(Locale)}
 *  returns only real resource locations, where the resource exists.
 * 
 *  <p>
 *  Folders must be represented with a trailing slash.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public interface IResourceLocation
{
    /**
     *  Returns a URL for the resource.
     * 
     *  @return the URL for the resource if it exists, or null if it does not
     * 
     **/
    
    public URL getResourceURL();
    
    /**
     *  Returns the file name portion of the resource location.
     * 
     **/
    
    public String getName();
    
    /**
     *  Returns a localized version of this resource (or this resource, if no
     *  appropriate localization is found).  Should only be invoked
     *  on a base resource.
     * 
     *  @param locale to localize for, or null for no localization.
     *  @return a localized version of this resource, of null if the resource
     *  itself does not exist.
     * 
     **/
    
    public IResourceLocation getLocalization(Locale locale);
    
    /**
     *  Returns at a relative location to this resource.  
     *  The new resource may or may not exist; this can be determined
     *  via {@link #getResourceURL(Locale)}.
     * 
     *  @param name name of new resource, possibly as a relative path, or
     *  as an absolute path (starting with a slash).
     * 
     **/
    
    public IResourceLocation getRelativeLocation(String name);
    
    /**
     *  Returns the path that represents the resource.  This should 
     *  only be used when the type of resource is known.
     * 
     **/
    
    public String getPath();

    /**
     *  Returns the locale for which this resource has been localized 
     *  or null if the resource has not been localized. This should 
     *  only be used when the type of resource is known.
     * 
     *  This locale is the same or more general than the locale for which localization 
     *  was requested. For example, if the requested locale was en_US, but only the file 
     *  Home_en was found, this locale returned would be en. 
     **/
    
    public Locale getLocale();
}
