/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

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
                Tapestry.format("AbstractLinkTag.io-exception", ex.getMessage()),
                ex);
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
                Tapestry.format("AbstractLinkTag.io-exception", ex.getMessage()),
                ex);
        }

        return EVAL_BODY_INCLUDE;
    }
}
