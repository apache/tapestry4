//  Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

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
	 * Invoked when real content is found.  The parser is responsible for aggregating
	 * all content provided by the underlying SAX parser into a single string.
	 */
	
	public void content(RuleDirectedParser parser, String content);
}
