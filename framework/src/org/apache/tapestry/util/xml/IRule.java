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

package org.apache.tapestry.util.xml;

import org.xml.sax.Attributes;

/**
 * A rule that may be pushed onto the {@link org.apache.tapestry.util.xml.RuleDirectedParser}'s
 * rule stack.  A rule is associated with an XML element.  It is pushed onto the stack when the
 * open tag for the rule is encountered.  It is is popped off the stack after the end-tag is
 * encountered.  It is notified about any text it directly wraps around.
 * 
 * <p>Rules should be stateless, because a rule instance may appear multiple times in the
 * rule stack (if elements can be recusively nested).
 *
 * @author Howard Lewis Ship
 * @version $Id$
 * @since 3.0
 **/

public interface IRule
{
	/**
	 * Invoked just after the rule is pushed onto the rule stack.  Typically, a Rule will
	 *  use the information to create a new object and push it onto the object stack.
	 *  If the rule needs to know about the element (rather than the attributes), it
	 *  may obtain the URI, localName and qName from the parser.
	 * 
	 */
	public void startElement(RuleDirectedParser parser, Attributes attributes);
	
	/**
	 * Invoked just after the rule is popped off the rule stack.
	 */
	public void endElement(RuleDirectedParser parser);
	
	/**
	 * Invoked when ignorable whitespace is found.
	 */
	
	public void ignorableWhitespace(RuleDirectedParser parser, char[] ch, int start, int length);
	
	/**
	 * Invoked when real content is found.
	 */
	
	public void characters(RuleDirectedParser parser, char[] ch, int start, int length);
}
