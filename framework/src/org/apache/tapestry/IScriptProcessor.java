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

/**
 * Defines methods needed by a {@link org.apache.tapestry.IScript} to
 * execute.
 *
 * @author Howard Lewis Ship
 * @version $Id$
 * @since 3.0
 * @see org.apache.tapestry.html.Body
 */

public interface IScriptProcessor
{
	/**
	 * Adds scripting code to the main body.  During the render, multiple scripts may
	 * render multiple bodies; all are concatinated together to form
	 * a single block.
	 */
	
	public void addBodyScript(String script);
	
	/**
	 * Adds initialization script.  Initialization script is executed once, when
	 * the containing page loads.  Effectively, this means that initialization script
	 * is stored inside the HTML &lt;body&gt; element's <code>onload</code>
	 * event handler.
	 */
	public void addInitializationScript(String script);
	
	/**
	 * Adds an external script.  The processor is expected to ensure
	 * that external scripts are only loaded a single time per page.
	 */
	
	public void addExternalScript(IResourceLocation location);
	
	/**
	 * Ensures that the given string is unique.  The string
	 * is either returned unchanged, or a suffix is appended to
	 * ensure uniqueness.
	 */
	
	public String getUniqueString(String baseValue);
}
