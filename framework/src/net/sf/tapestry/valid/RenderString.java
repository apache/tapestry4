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
package net.sf.tapestry.valid;

import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRender;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;

/**
 *  A wrapper around {@link String} that allows the String to
 *  be renderred.  This is primarily used to present
 *  error messages.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class RenderString implements IRender
{
	private String string;
	private boolean raw = false;
	
	public RenderString(String string)
	{
		this.string = string;
	}
	
	/**
	 *  @param string the string to render
	 *  @param raw if true, the String is rendered as-is, with no filtering.
	 *  If false (the default), the String is filtered.
	 *
	 **/
	
	public RenderString(String string, boolean raw)
	{
		this.string = string;
		this.raw = raw;
	}

	/**
	 *  Renders the String to the writer.  Does nothing if the string is null.
	 *  If raw is true, uses {@link IMarkupWriter#printRaw(String)}, otherwise
	 *  {@link IMarkupWriter#print(String)}.
	 * 
	 *
	 **/
	
	public void render(IMarkupWriter writer, IRequestCycle cycle)
		throws RequestCycleException
	{
		if (string == null)
			return;
			
		if (raw)
			writer.printRaw(string);
		else
			writer.print(string);
	}
	
	public String getString()
	{
		return string;
	}
	
	public boolean isRaw()
	{
		return raw;
	}
	
	public String toString()
	{
		StringBuffer buffer = new StringBuffer("RenderString@");
		
		buffer.append(Integer.toHexString(hashCode()));
		buffer.append('[');
		buffer.append(string);
		
		if (raw)
		buffer.append(" (raw)");
		
		buffer.append(']');
		
		return buffer.toString();
	}
		

}
