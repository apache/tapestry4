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

import org.apache.hivemind.Resource;

/**
 * Service interface for the <code>tapestry.TemplateParser</code> service. 
 * Note that this requires a threaded service model.
 * 
 * <p>
 * Note: had to use the 'I' prefix, so that {@link org.apache.tapestry.parse.TemplateParser}
 * could keep its name. Otherwise, it makes Spindle support really, really ugly.
 *
 * @author Howard Lewis Ship
 * @since 3.1
 */
public interface ITemplateParser
{
    /**
     *  Parses the template data into an array of {@link TemplateToken}s.
     *
     *  <p>The parser is <i>decidedly</i> not threadsafe, so care should be taken
     *  that only a single thread accesses it.
     *
     *  @param templateData the HTML template to parse.  Some tokens will hold
     *  a reference to this array.
     *  @param delegate  object that "knows" about defined components
     *  @param resourceLocation a description of where the template originated from,
     *  used with error messages.
     *
     **/
    public abstract TemplateToken[] parse(
        char[] templateData,
        ITemplateParserDelegate delegate,
        Resource resourceLocation)
        throws TemplateParseException;
}