package net.sf.tapestry.script;

import net.sf.tapestry.ScriptException;
import net.sf.tapestry.ScriptSession;

/**
 *  Defines the responsibilities of a template token used by a
 *  {@link net.sf.tapestry.IScript}.
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