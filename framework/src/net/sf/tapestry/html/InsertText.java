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

import org.apache.log4j.Category;

import net.sf.tapestry.AbstractComponent;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.Tapestry;

/**
 *  Inserts formatted text (possibly collected using a {@link net.sf.tapestry.form.Text} 
 *  component.
 *  To maintain the line breaks provided originally, this component will
 *  break the input into individual lines and insert additional
 *  HTML to make each line seperate.
 *
 * <p>This can be down more simply, using the &lt;pre&gt; HTML element, but
 * that usually renders the text in a non-proportional font.
 *
 *
 *  <table border=1>
 *  <tr> <th>Parameter</th> 
 *  <th>Type</th>
 *  <th>Direction</th> 
 *  <th>Required</th> 
 *  <th>Default</th> 
 *  <th>Description</th>
 * </tr>
 * <tr>
 *  <td>value</td> 
 *  <td>{@link String}</td> 
 *  <td>in</td>
 *  <td>no</td> 
 *  <td>&nbsp;</td>
 *  <td>The text to be inserted.  If not provided, no output is written.
 * 
 * <p>This parameter can also be accessed using the deprected name "text".
 * 
 * </td> </tr>
 *
 *  <tr>
 *      <td>mode</td>
 *      <td>{@link InsertTextMode}</td>
 *		<td>in</td>
 *      <td>no</td>
 *      <td>{@link InsertTextMode#BREAK}</td>
 *      <td>Defines how each line will be emitted.
 *      </td>
 *  </tr>
 *
 * </table>
 *
 * <p>Informal parameters are not allowed.  The component must not have a body.
 *
 * @author Howard Lewis Ship
 * @version $Id$
 * 
 **/

public class InsertText extends AbstractComponent
{
    private static final Category CAT = Category.getInstance(InsertText.class);

    private boolean warning = true;
    private String value;
    private InsertTextMode mode = InsertTextMode.BREAK;

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {
        StringReader reader = null;
        LineNumberReader lineReader = null;
        int lineNumber = 0;
        String line;

        if (value == null)
            return;

        try
        {
            reader = new StringReader(value);

            lineReader = new LineNumberReader(reader);

            while (true)
            {
                line = lineReader.readLine();

                // Exit loop at end of file.

                if (line == null)
                    break;

                mode.writeLine(lineNumber, line, writer);

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
        return mode;
    }

    public void setMode(InsertTextMode mode)
    {
        this.mode = mode;
    }

    public String getText()
    {
        return getValue();
    }

    public void setText(String text)
    {
        if (warning)
        {
            CAT.warn(Tapestry.getString("deprecated-component-param", getExtendedId(), "text", "value"));
            warning = false;
        }

        setValue(text);
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

}