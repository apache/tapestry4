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

package org.apache.tapestry.parse;

import org.xml.sax.Attributes;

/**
 *  A {@link org.apache.tapestry.parse.SpecificationDigester} rule that executes
 *  at the start and end of the document.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public interface IDocumentRule
{
    public void setDigester(SpecificationDigester digester);

	/**
	 *  Invoked at the time the first element in the document is parsed.
	 * 
	 *  By this time, the publicId will be known.
	 * 
	 **/
	
    public void startDocument(String namespace, String name, Attributes attributes) throws Exception;

    public void endDocument() throws Exception;

    public void finish() throws Exception;
}
