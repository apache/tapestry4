package net.sf.tapestry.parse;

/**
 *  Exception thrown indicating a problem parsing an HTML template.
 *
 *  @author Howard Ship
 *  @version $Id$
 **/

public class TemplateParseException extends Exception
{
	private String resourcePath;
	private int line = -1;

	public TemplateParseException(String message)
	{
		this(message, -1, null);
	}

	public TemplateParseException(String message, int line, String resourcePath)
	{
		super(message);

		this.line = line;
		this.resourcePath = resourcePath;
	}

	public int getLine()
	{
		return line;
	}

	public String getResourcePath()
	{
		return resourcePath;
	}
}