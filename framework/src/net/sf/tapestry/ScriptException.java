package net.sf.tapestry;

/**
 *  Exception thrown during the execution of a {@link IScript}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 0.2.9
 * 
 **/

public class ScriptException extends Exception
{
	private Throwable rootCause;
	private ScriptSession session;

	public ScriptException(
		String message,
		ScriptSession session,
		Throwable rootCause)
	{
		super(message);

		this.session = session;
		this.rootCause = rootCause;
	}

	public ScriptException(String message, ScriptSession session)
	{
		this(message, session, null);
	}

	public Throwable getRootCause()
	{
		return rootCause;
	}

	public ScriptSession getSession()
	{
		return session;
	}
}