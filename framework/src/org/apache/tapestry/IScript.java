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

package org.apache.tapestry;

import java.util.Map;

/**
 *  An object that can convert a set of symbols into a collection of JavaScript statements.
 *
 *  <p>IScript implementation must be threadsafe.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.2
 * 
 **/

public interface IScript
{
    /**
     *  Returns the location from which the script was loaded.
     *
     **/

    public IResourceLocation getScriptLocation();

    /**
     * Executes the script, which will read and modify the symbols {@link Map}.  The
     * script works with the {@link IScriptProcessor} to get the generated JavaScript
     * included on the page.
     * 
     * @param cycle the current request cycle
     * @param processor an object that processes the results of the script, typically
     * an instance of {@link org.apache.tapestry.html.Body}
     * @param symbols Map of input symbols; execution of the script may modify the map,
     * creating new output symbols
     * 
     * @see org.apache.tapestry.html.Body#get(IRequestCycle)
     *
     */

    public void execute(IRequestCycle cycle, IScriptProcessor processor, Map symbols);
}