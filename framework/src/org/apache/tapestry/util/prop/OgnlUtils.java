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

package org.apache.tapestry.util.prop;

import java.util.HashMap;
import java.util.Map;

import ognl.ClassResolver;
import ognl.Ognl;
import ognl.OgnlException;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.Tapestry;

/**
 *  Utilities wrappers around <a href="http://www.ognl.org">OGNL</a>.
 * 
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 * 
 **/

public class OgnlUtils
{
    private static final Map _cache = new HashMap();

    private OgnlUtils()
    {
    }

    /**
     *  Gets a parsed OGNL expression from the input string.
     * 
     *  @throws ApplicationRuntimeException if the expression can not be parsed.
     * 
     **/

    public static synchronized Object getParsedExpression(String expression)
    {
        Object result = _cache.get(expression);

        if (result == null)
        {
            try
            {
                result = Ognl.parseExpression(expression);
            }
            catch (Exception ex)
            {
                throw new ApplicationRuntimeException(
                    Tapestry.getString("OgnlUtils.unable-to-parse-expression", expression),
                    ex);
            }

            _cache.put(expression, result);
        }

        return result;
    }

    /**
     *  Parses and caches the expression and uses it to update
     *  the target object with the provided value.
     * 
     *  @throws ApplicationRuntimeException if the expression
     *  can not be parsed, or the target can not be updated.
     * 
     **/

    public static void set(String expression, ClassResolver resolver, Object target, Object value)
    {
        set(getParsedExpression(expression), resolver, target, value);
    }

    /** 
     *  Updates the target object with the provided value.
     * 
     *  @param expression a parsed OGNL expression
     *  @throws ApplicationRuntimeException if the target can not be updated.
     * 
     **/

    public static void set(Object expression, ClassResolver resolver, Object target, Object value)
    {
        try
        {
            Map context = Ognl.createDefaultContext(target, resolver);

            Ognl.setValue(expression, context, target, value);
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.getString(
                    "OgnlUtils.unable-to-update-expression",
                    "<parsed expression>",
                    target,
                    value),
                ex);
        }
    }

    /**
     *   Returns the value of the expression evaluated against
     *   the object.
     * 
     *   @param expression a parsed OGNL expression
     *   @param object the root object
     * 
     *   @throws ApplicationRuntimeException
     *   if the value can not be obtained from the object.
     * 
     **/

    public static Object get(Object expression, ClassResolver resolver, Object object)
    {
        try
        {
            Map context = Ognl.createDefaultContext(object, resolver);

            return Ognl.getValue(expression, context, object);
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.getString(
                    "OgnlUtils.unable-to-read-expression",
                    "<parsed expression>",
                    object),
                ex);
        }
    }

    /**
     *   Returns the value of the expression evaluated against
     *   the object.
     * 
     * 
     *   @throws ApplicationRuntimeException if the
     *   expression can not be parsed, or the value
     *   not obtained from the object.
     **/

    public static Object get(String expression, ClassResolver resolver, Object object)
    {
        return get(getParsedExpression(expression), resolver, object);
    }

}
