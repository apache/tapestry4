//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.script;

import net.sf.tapestry.ScriptException;
import net.sf.tapestry.ScriptSession;

/**
 *  Defines the responsibilities of a template token used by a
 *  {@link ParsedScript} ... that is, how the token
 *  can assist in generating output.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public interface IScriptToken
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

	public void write(StringBuffer buffer, ScriptSession session)
		throws ScriptException;

	/**
	 *  Invoked during parsing to add the token parameter as a child
	 *  of this token.
	 *
	 *  @since 0.2.9
	 **/

	public void addToken(IScriptToken token);
}