package com.primix.jbe;

import java.io.*;
import java.util.*;

/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000 by Howard Ship and Primix Solutions
 *
 * Primix Solutions
 * One Arsenal Marketplace
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
	/**
	 *  Represents a runable sub-command.
	 *
	 */
	 
	abstract static private class Command
	{		
		abstract void run(String args[]);
	}

	protected static void error(String message)
	{
		System.err.println("JBE Util: " + message);
		System.exit(-1);
	}
	
	/**
	 *  Command for canonicalizing relative pathnames.  It takes as input a list of absolute
	 *  or relative path names and produces (writes to System.out) a list of
	 *  canonicalized (i.e., absolute) pathnames.
	 *
	 *  <p>Usage:
	 *  <pre>
	 *  com.primix.jbe.Util canonicalize [-help] [-root directory] [-classpath] paths
	 *  </pre>
	 *
	 *  <p>The root directory is normally the current directory, but can be overriden
	 *  using the -root option.  This is the directory to which relative paths
	 *  are identified.
	 *
	 *  <p>The paths may be absolute or relative path names. 
	 *
	 *  <p>Normally, each path is written to a seperate line.  Specifying -classpath
	 *  causes output to be written to a single line with classpath seperators
	 *  between pathnames. 
	 *
	 */
	 
	protected static class Canonicalize extends Command
	{
		// Default the root directory to the current working directory.
		
		private String root = System.getProperty("user.dir");
		
		private String seperator = "\n";		
		
		private boolean firstEmit = true;
		
		private Set prior = new HashSet();
		
		void run(String[] args)
		{
			boolean showUsage = false;
			int i;
			String arg;
			
			for (i = 1; i < args.length; i++)
			{
				arg = args[i];
				
				if (arg.equals("-help"))
					error("Usage: com.primix.jbe.Util canonicalize " +
						  "[-help] [-root directory] [-classpath] directory...");				
				
				if (arg.equals("-root"))
				{
					if (++i >= args.length)
						error("Must specify root directory after -root option.");
					
					root = args[i];	
					
					continue;
				}
				
				if (arg.equals("-classpath"))
				{
					seperator = System.getProperty("path.separator");
					continue;
				}
				
				emitPath(arg);
			}
		}
		
		private void emitPath(String path)
		{
			boolean absolute;
			File file;
			String canonicalPath = null;
			String finalPath;
			
			absolute = isAbsolute(path);
			
			if (absolute)
				file = new File(path);
			else
				file = new File(root, path);
				
			try
			{
				canonicalPath  = file.getCanonicalPath();	
			}
			catch (IOException e)
			{
				error(e.getMessage() + " for " + path + ".");
			}
			
			// Filter out duplicates.
			
			if (prior.contains(canonicalPath))
				return;
			
			prior.add(canonicalPath);
				
			if (!firstEmit)
				System.out.print(seperator);
			
			// For NT ... convert backslashes (which screws up GNU Make) with forward
			// slashes (which is acceptible to GNU Make and the JDK tools).
			
			finalPath = canonicalPath.replace('\\', '/');
			
			System.out.print(finalPath);
			
			firstEmit = false;	
		}
		
		private boolean isAbsolute(String path)
		{
			char first;
			
			first = path.charAt(0);
			
			// Starting with a slash is an absolute path name on Unix (/) or NT (\).
			// Also, note that within the JBE, we usually use Unix-style forward slashes
			// on all pathnames anyway.
			
			if (first == '/' || first == '\\')
				return true;
		
			// An NT check:  if the first character is alphabetic (a drive letter) and the
			// second character is a colon, then it's an absolute path.
			
			if (((first >= 'a' && first <= 'z') | (first >= 'A' && first <= 'Z')) &&
				path.length() >= 2 &&
				path.charAt(1) == ':')
				return true;
		
			return false;	
		}	
	}

	/**
	 *  Main entry point.  The first argument identifies a sub-command.  The other arguments are
	 *  interpreted by the sub-command.
	 *
	 */
	 
	public static void main(String args[])
	{
		String name;
		Command command;
		
		if (args.length > 0)
		{
			name = args[0];
			
			if (name.equals("canonicalize"))
			{
				command = new Canonicalize();
				command.run(args);
				return;
			}
			
			if (!name.equals("-help"))
			{
				System.err.println("JBE Util: error, unrecognized sub-command: " + name);
			}
		}
		
		System.err.println("Usage: com.primix.jbe.Util [subcommand|-help]");
		System.err.println("\nAvailable subcommands: canonicalize");

	}
}
