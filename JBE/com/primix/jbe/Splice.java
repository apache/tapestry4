package com.primix.jbe;

import java.io.*;
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
 *  Command for performing a splice of a target file, adding a chunk of text from
 *  a second source file.  The splice either adds a new block to the target, or replaces
 *  and existing block in the target. This is used by the JBE to automate configuration by editting files automatically
 *  (for the Primix Virtual Library demo, this is how the jBoss configuration is
 *  modified).
 *
 *  <p>The block of text to be replaced is marked with leading a trailing delimiters in
 *  the form of specially formatted comments.
 *
 *  <p>A single target file (the file being changed) may have multiple blocks that are editted
 *  at different times, so the delimiter incorporates a tag.
 *
 *  <p>There are three types of delimiters:
 *  <table border=1>
 *  <tr>
 *      <th>Delimiter type</th>
 *      <th>Description</th>
 *      <th>Example</th>
 *  </tr>
 *  <tr>
 *      <td>html (default)</td>
 *      <td>HTML style comment, suitable for XML, SGML as well.</td>
 *      <td>
 *      &lt;!-- BEGIN BLOCK tag --&gt;<br>
 *      &lt;!-- END BLOCK tag --&gt;
 *      </td>
 *  </tr>
 *  <tr>
 *      <td>java</th>
 *      <td>Java or C style comment</td>
 *      <td>
 *          // BEGIN BLOCK tag</br>
 *          // END BLOCK tag
 *      </td>
 *  </tr>
 *  <tr>
 *      <td>properties</td>
 *      <td>Java properties file format</td>
 *      <td># BEGIN BLOCK tag<br>
 *          # END BLOCK tag
 *      </td>
 * </tr>
 *  </table>
 *     
 *
 *  <p>This is some of the ugliest code I've written in years, but it gets the
 *  job done.  For once I'll let it pass and "clean it up later".
 *
 *  @author Howard Ship
 *  @version $Id$
 *
 */
  
public class Splice
implements ICommand
{
    private static final int COPY_PREAMBLE = 0;
    private static final int IGNORE_BODY = 1;
    private static final int COPY_POSTAMBLE = 2;

    private SpliceStyle spliceStyle;


    public String getSelector()
    {
        return "splice";
    }

    public void run(String[] args)
    {
        String target = null;
        String source = null;
        String style = "html";
        String before = null;
        String tag = null;
        File targetFile;
        File temporaryFile;
        File targetDirectory;

        for (int i = 1; i < args.length; i++)
        {
            if (args[i].equals("-tag"))
            {
                tag = args[++i];
                continue;
            }

            if (args[i].equals("-target"))
            {
                target = args[++i];
                continue;
            }

            if (args[i].equals("-source"))
            {
                source = args[++i];
                continue;
            }

            if (args[i].equals("-style"))
            {
                style = args[++i];
                continue;
            }

            if (args[i].equals("-before"))
            {
                before = args[++i];
                continue;
            }

            Util.error("Invalid argument: " + args[i]);
        }

        // TBD:  Check that required stuff is there.

        try
        {
            targetFile = new File(target);
            targetDirectory = targetFile.getParentFile();

            temporaryFile = File.createTempFile("Splice", null, targetDirectory);

            spliceStyle = buildStyle(style, tag);

            // Target is the file to modify.

            LineNumberReader targetReader = 
                new LineNumberReader (
                    new BufferedReader (
                        new FileReader(targetFile)));

            PrintWriter temporaryWriter =
                new PrintWriter(
                    new BufferedWriter (
                        new FileWriter(temporaryFile)));
            
            int state = COPY_PREAMBLE;

            // Step one: copy each line from the targetReader to the temporaryWriter
            // until the begin delimiter is reached.
            
            while (true)
            {
                String line = targetReader.readLine();

                if (line == null)
                    break;

                switch (state)
                {

                    case COPY_PREAMBLE:

                        String trimmedLine = line.trim();
                        if (spliceStyle.isBegin(trimmedLine))
                        {
                            copyBlock(source, temporaryWriter);
                            state = IGNORE_BODY;
                            continue;
                        }

                        if (before != null &&
                            trimmedLine.equalsIgnoreCase(before))
                        {
                            copyBlock(source, temporaryWriter);
                            state = COPY_POSTAMBLE;
                            // Don't continue the loop ... we want to actually write
                            // the line.
                        }

                        temporaryWriter.println(line);
                        continue;

                    case IGNORE_BODY:

                        if (spliceStyle.isEnd(line.trim()))
                            state = COPY_POSTAMBLE;

                        continue;
                        
                    case COPY_POSTAMBLE:
                    
                        temporaryWriter.println(line);
                        continue;
                }
            }                        

            // If never found the block, append it now.

            if (state == COPY_PREAMBLE)
                copyBlock(source, temporaryWriter);
                

            // Close everything.

            targetReader.close();
            temporaryWriter.close();

            // Delete the original file

            targetFile.delete();
           
           // Rename the temporaryFile to the targetFile

            temporaryFile.renameTo(targetFile);

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            System.exit(-1);
        }
    }

    private void copyBlock(String sourcePath, PrintWriter writer)
    throws FileNotFoundException, IOException
    {
        char buffer[] = new char[2000];
        int chars;
        Reader reader;

        spliceStyle.writeBegin(writer);

        reader = new BufferedReader(
                    new FileReader(sourcePath));

        while (true)
        {
            chars = reader.read(buffer);
            if (chars < 0)
                break;

            writer.write(buffer, 0, chars);
        }

        reader.close();

        spliceStyle.writeEnd(writer);
    }

    private SpliceStyle buildStyle(String type, String tag)
    {
        if (type.equals("html"))
            return new SpliceStyle(tag,
                "<!-- BEGIN BLOCK ", " -->",
                "<!-- END BLOCK ", " -->");

        if (type.equals("java"))
            return new SpliceStyle(tag,
                "// BEGIN BLOCK ", null,
                "// END BLOCK ", null);

        if (type.equals("properties"))
            return new SpliceStyle(tag,
                "# BEGIN BLOCK ", null,
                "# END BLOCK ", null);

        Util.error("Unrecognized style: " + type);

        return null;
    }
}
