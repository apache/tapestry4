package net.sf.tapestry.valid;

import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRender;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;

/**
 *  A wrapper around {@link String} that allows the String to
 *  be renderred.  This is primarily used to present
 *  error messages.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class RenderString implements IRender
{
	private String string;
	private boolean raw = false;
	
	public RenderString(String string)
	{
		this.string = string;
	}
	
	/**
	 *  @param string the string to render
	 *  @param raw if true, the String is rendered as-is, with no filtering.
	 *  If false (the default), the String is filtered.
	 *
	 **/
	
	public RenderString(String string, boolean raw)
	{
		this.string = string;
		this.raw = raw;
	}

	/**
	 *  Renders the String to the writer.  Does nothing if the string is null.
	 *  If raw is true, uses {@link IMarkupWriter#printRaw(String)}, otherwise
	 *  {@link IMarkupWriter#print(String)}.
	 * 
	 *
	 **/
	
	public void render(IMarkupWriter writer, IRequestCycle cycle)
		throws RequestCycleException
	{
		if (string == null)
			return;
			
		if (raw)
			writer.printRaw(string);
		else
			writer.print(string);
	}
	
	public String getString()
	{
		return string;
	}
	
	public boolean isRaw()
	{
		return raw;
	}
	
	public String toString()
	{
		StringBuffer buffer = new StringBuffer("RenderString@");
		
		buffer.append(Integer.toHexString(hashCode()));
		buffer.append('[');
		buffer.append(string);
		
		if (raw)
		buffer.append(" (raw)");
		
		buffer.append(']');
		
		return buffer.toString();
	}
		

}
