package com.primix.tapestry.valid;

import com.primix.tapestry.IRender;
import com.primix.tapestry.IRequestCycle;
import com.primix.tapestry.IResponseWriter;
import com.primix.tapestry.RequestCycleException;

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
	 *  If raw is true, uses {@link IResponseWriter#printRaw(String)}, otherwise
	 *  {@link IResponseWriter#print(String)}.
	 * 
	 *
	 **/
	
	public void render(IResponseWriter writer, IRequestCycle cycle)
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
