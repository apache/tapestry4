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

package net.sf.tapestry.html;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.StringReader;

import net.sf.tapestry.AbstractComponent;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.Tapestry;

/**
 *  Inserts formatted text (possibly collected using a {@link net.sf.tapestry.form.TextArea} 
 *  component.
 * 
 *  [<a href="../../../../../ComponentReference/InsertText.html">Component Reference</a>]
 *
 *  <p>To maintain the line breaks provided originally, this component will
 *  break the input into individual lines and insert additional
 *  HTML to make each line seperate.
 *
 * <p>This can be down more simply, using the &lt;pre&gt; HTML element, but
 * that usually renders the text in a non-proportional font.
 *
 * @author Howard Lewis Ship
 * @version $Id$
 * 
 **/

public class InsertText extends AbstractComponent
{
    private String _value;
    private InsertTextMode _mode = InsertTextMode.BREAK;

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
        if (_value == null)
            return;

        StringReader reader = null;
        LineNumberReader lineReader = null;

        try
        {
            reader = new StringReader(_value);

            lineReader = new LineNumberReader(reader);

            int lineNumber = 0;

            while (true)
            {
                String line = lineReader.readLine();

                // Exit loop at end of file.

                if (line == null)
                    break;

                _mode.writeLine(lineNumber, line, writer);

                lineNumber++;
            }

        }
        catch (IOException ex)
        {
            throw new RequestCycleException(Tapestry.getString("InsertText.conversion-error"), this, ex);
        }
        finally
        {
            close(lineReader);
            close(reader);
        }

    }

    private void close(Reader reader)
    {
        if (reader == null)
            return;

        try
        {
            reader.close();
        }
        catch (IOException e)
        {
        }
    }

    public InsertTextMode getMode()
    {
        return _mode;
    }

    public void setMode(InsertTextMode mode)
    {
        _mode = mode;
    }

    public String getValue()
    {
        return _value;
    }

    public void setValue(String value)
    {
        _value = value;
    }

}