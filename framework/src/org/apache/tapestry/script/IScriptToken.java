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

package org.apache.tapestry.script;

import org.apache.tapestry.ILocatable;


/**
 *  Defines the responsibilities of a template token used by a
 *  {@link org.apache.tapestry.IScript}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public interface IScriptToken extends ILocatable
{
	/**
	 *  Invoked to have the token
	 *  add its text to the buffer.  A token may need access
	 *  to the symbols in order to produce its output.
	 *
	 *  <p>Top level tokens (such as BodyToken) can expect that
	 *  buffer will be null.
	 *
	 **/

	public void write(StringBuffer buffer, ScriptSession session);

	/**
	 *  Invoked during parsing to add the token parameter as a child
	 *  of this token.
	 *
	 *  @since 0.2.9
	 **/

	public void addToken(IScriptToken token);
}