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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import ognl.ClassResolver;
import ognl.DefaultClassResolver;

import org.apache.tapestry.Tapestry;
import org.apache.tapestry.link.DirectLink;
import org.apache.tapestry.parse.TemplateParser;
import org.apache.tapestry.util.prop.OgnlUtils;

/**
 *  Contains common code and methods for all the Tapestry JSP tag implementations.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public abstract class AbstractTapestryTag extends TagSupport
{

    private String _servlet = "/app";

    public String getServlet()
    {
        return _servlet;
    }

    public void setServlet(String servlet)
    {
        _servlet = servlet;
    }

    /**
     *  Implemented in subclasses to provide a 
     *  {@link org.apache.tapestry.jsp.URLRetriever} instance that
     *  can insert the correct URL into the output.
     * 
     **/

    protected abstract URLRetriever getURLRetriever() throws JspException;

    /**
     *  Builds an object array appropriate for use as the service parameters
     *  for the external service. The first object in the array is the name
     *  of the page.  Any additional objects are service parameters to be
     *  supplied to the listener method.
     * 
     *  <p>
     *  The parameters are converted to an array of objects
     *  via {@link #convertParameters(String)}.
     * 
     **/

    protected Object[] constructExternalServiceParameters(String pageName, String parameters)
        throws JspException
    {

        Object[] resolvedParameters = convertParameters(parameters);

        int count = Tapestry.size(resolvedParameters);

        if (count == 0)
            return new Object[] { pageName };

        List list = new ArrayList();
        list.add(pageName);

        for (int i = 0; i < count; i++)
            list.add(resolvedParameters[i]);

        return list.toArray();
    }

    /**
     *  <p>The external service allows service parameters (an array of
     *  objects) to be passed along inside the URL.  This method converts
     *  the input string into an array of parameter objects.
     *  <ul>
     *  <li>If parameters is null, the no parameters are passed
     *  <li>If parameters starts with "ognl:" it is treated as an OGNL expression:
     *     <ul>
     *     <li>The expression is evaluated using the 
     *         {@link javax.servlet.jsp.PageContext page context} as the root object
     *     <li>If the expression value is a Map, then the Map is converted to
     *  	   an array via (@link org.apache.tapestry.Tapestry#convertMapToArray(Map)}
     *     <li>Otherwise, the expression value is converted using
     *  {@link org.apache.tapestry.link.DirectLink#constructServiceParameters(Object)}.
     * 		</ul>
     *   <li>Otherwise, parameters are simply a string, which is included as the lone
     *  service parameter
     *  </ul>
     **/

    protected Object[] convertParameters(String _parameters) throws JspException
    {
        if (_parameters == null)
            return null;

        if (_parameters.startsWith(TemplateParser.OGNL_EXPRESSION_PREFIX))
        {
            String expression =
                _parameters.substring(TemplateParser.OGNL_EXPRESSION_PREFIX.length() + 1);

            return convertExpression(expression);
        }

        return new Object[] { _parameters };
    }

    private Object[] convertExpression(String expression) throws JspException
    {
        Object value = evaluateExpression(expression);

        if (value == null)
            return null;

        if (value instanceof Map)
            return Tapestry.convertMapToArray((Map) value);

        return DirectLink.constructServiceParameters(value);
    }

    private Object evaluateExpression(String expression) throws JspException
    {
        ClassResolver resolver = new DefaultClassResolver();

        try
        {
            return OgnlUtils.get(expression, resolver, pageContext);
        }
        catch (Throwable t)
        {
            throw new JspException(
                Tapestry.format(
                    "AbstractTapestryTag.unable-to-evaluate-expression",
                    expression,
                    t.getMessage()));
        }
    }
}
