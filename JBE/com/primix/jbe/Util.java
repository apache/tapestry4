package com.primix.jbe;

import java.util.*;

/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000, 2001 by Howard Ship and Primix
 *
 * Primix
 * 311 Arsenal Street
 * Watertown, MA 02472
 * http://www.primix.com
 * mailto:hship@primix.com
 * 
 * This library is free software.
 * 
 * You may redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation.
 *
 * Version 2.1 of the license should be included with this distribution in
 * the file LICENSE, as well as License.html. If the license is not
 * included with this distribution, you may find a copy at the FSF web
 * site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
 * Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

/** 
 *  Utility classes for the JBE, used to canonicalize  relative pathnames.
 *  This is <em>very</em> quick and dirty.
 *
 *  @version $Id$
 *  @author Howard Ship
 */

public class Util
{
	public static void error(String message)
	{
		System.err.println("JBE Util: " + message);
		System.exit(-1);
	}
	
    private static Map commands = new HashMap();

    static
    {
        addCommand(new Canonicalize());
        addCommand(new Splice());
    }

    private static void addCommand(ICommand command)
    {
        commands.put(command.getSelector(), command);
    }

	/**
	 *  Main entry point.  The first argument identifies a sub-command.  The other arguments are
	 *  interpreted by the sub-command.
	 *
	 */
	 
	public static void main(String args[])
	{
		String name;
		ICommand command;
		
		if (args.length > 0)
		{
		    name = args[0];

            command = (ICommand)commands.get(name);

            if (command != null)
            {
                command.run(args);
                return;
            }
		    else if (!name.equals("-help"))
		    {
            
			    System.err.println("JBE Util: error, unrecognized sub-command: " + name);
		    }

		}
		
		System.err.println("Usage: com.primix.jbe.Util [subcommand|-help]");
		System.err.println("\nAvailable subcommands: " + getSelectors());

	}

    private static String getSelectors()
    {
        StringBuffer buffer = new StringBuffer();
        boolean first = true;
        Iterator i;
        String name;

        i = commands.keySet().iterator();
        while (i.hasNext())
        {
            name = (String)i.next();

            if (!first)
                buffer.append(' ');

            buffer.append(name);
        }

        return buffer.toString();
    }
}
