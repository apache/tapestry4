//  Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.jsp;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.tapestry.Tapestry;

/**
 *  Abstract super-class of Tapestry JSP tags that produce a hyperlink
 *  (<code>&lt;a&gt;</code>) tag.  Tags use a
 *  {@link org.apache.tapestry.jsp.URLRetriever} for the <code>href</code>
 *  attribute, and may include a 
 *  <code>class</code> attribute (based on
 *  the {@link #getStyleClass() styleClass property}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public abstract class AbstractLinkTag extends AbstractTapestryTag
{
    private String _styleClass;

    public String getStyleClass()
    {
        return _styleClass;
    }

    public void setStyleClass(String styleClass)
    {
        _styleClass = styleClass;
    }

    /**
    *  Writes a <code>&lt;/a&gt;</code> tag.
    * 
    *  @return {@link javax.servlet.jsp.tagext.Tag#EVAL_PAGE}
    * 
    **/

    public int doEndTag() throws JspException
    {
        JspWriter out = pageContext.getOut();

        try
        {
            out.print("</a>");
        }
        catch (IOException ex)
        {
            throw new JspException(
                Tapestry.format("AbstractLinkTag.io-exception", ex.getMessage()));
        }

        return EVAL_PAGE;
    }

    /**
    *  Writes a <code>&lt;a&gt; tag.  The tag may
    *  have a <code>class</code> attribute if the
    *  {@link #getStyleClass() styleClass property}
    *  is not null.  The <code>href</code>
    *  attribute is provided via
    *  a {@link #getURLRetriever() URLRetriever}.
    * 
    *  @return {@link javax.servlet.jsp.tagext.Tag#EVAL_BODY_INCLUDE}
    * 
    **/

    public int doStartTag() throws JspException
    {
        JspWriter out = pageContext.getOut();

        try
        {
            out.print("<a");

            if (_styleClass != null)
            {
                out.print(" class=\"");
                out.print(_styleClass);
                out.print('"');
            }

            out.print(" href=\"");

            getURLRetriever().insertURL(getServlet());

            // And we're back!  Finish off the tag.

            out.print("\">");
        }
        catch (IOException ex)
        {
            throw new JspException(
                Tapestry.format("AbstractLinkTag.io-exception", ex.getMessage()));
        }

        return EVAL_BODY_INCLUDE;
    }
}
