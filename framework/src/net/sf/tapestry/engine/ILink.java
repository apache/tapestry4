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
package net.sf.tapestry.engine;

/**
 *  Define a link that may be generated as part of a page render.  The vast majority
 *  of links are tied to {@link net.sf.tapestry.IEngineService services} and are, in
 *  fact, callbacks.  A small number, such as those generated by
 *  {@link net.sf.tapestry.link.GenericLink} component, are to arbitrary locations.
 *  In addition, ILink differentiates between the path portion of the link, and any
 *  query parameters encoded into a link, primarily to benefit {@link net.sf.tapestry.form.Form},
 *  which needs to encode the query parameters as hidden form fields.
 *
 *  <p>
 *  In addition, an ILink is responsible for
 *  passing constructed URLs through
 *  {@link net.sf.tapestry.IRequestCycle#encodeURL(String)}
 *  as needed.
 * 
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 * 
 **/

public interface ILink
{
	/**
	 *  Returns the relative URL as a String.  A relative
	 *  URL may include a leading slash, but omits
	 *  the scheme, host and port portions of a full URL.
	 *  
	 *  @returns the relative URL, with no anchor, but including
	 *  query parameters.
	 * 
	 **/
	
	public String getURL();
	
    /**
     *  Returns the relative URL as a String.  This is used
     *  for most links.
     * 
     *  @param anchor if not null, appended to the URL
     *  @param includeParameters if true, parameters are included
     * 
     **/

    public String getURL(String anchor, boolean includeParameters);

	/**
	 *  Returns the absolute URL as a String, using
	 *  default scheme, server and port, including
	 *  parameters, and no anchor.
	 * 
	 **/
	
	public String getAbsoluteURL();

    /**
     *  Returns the absolute URL as a String.  
     * 
     *  @param scheme if not null, overrides the default scheme.
     *  @param server if not null, overrides the default server
     *  @param port if non-zero, overrides the default port
     *  @param anchor if not null, appended to the URL
     *  @param includeParameters if true, parameters are included
     * 
     **/

    public String getAbsoluteURL(
        String scheme,
        String server,
        int port,
        String anchor,
        boolean includeParameters);

    /**
     * 
     *  Returns an array of parameters names (in
     *  no specified order).
     * 
     *  @see #getParameterValue(String)
     * 
     **/

    public String[] getParameterNames();

    /**
     *  Returns the values for the named parameter.
     *  
     *  @throws IllegalArgumentException if the
     *  link does not define values for the
     *  specified name.
     * 
     **/

    public String[] getParameterValues(String name);
}
