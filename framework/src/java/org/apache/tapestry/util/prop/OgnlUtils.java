// Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.util.prop;

import java.util.HashMap;
import java.util.Map;

import ognl.ClassResolver;
import ognl.Ognl;
import ognl.OgnlException;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.Tapestry;

/**
 *  Utilities wrappers around <a href="http://www.ognl.org">OGNL</a>.
 * 
 *  <p>This has been temporarily hacked for Tapestry 3.1. It's not a good solution,
 *  because it only really supports a single class loader, which could be a problem
 *  if the Tapestry framework is loaded by the system class loader.
 * 
 *  @author Howard Lewis Ship
 *  @since 2.2
 * 
 **/

public class OgnlUtils
{
    private static final Map _cache = new HashMap();

    /**
     * @since 3.1
     */
    private static ClassResolver _ognlClassResolver = new OgnlClassResolver();

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
                    Tapestry.format("OgnlUtils.unable-to-parse-expression", expression),
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

    public static void set(String expression, Object target, Object value)
    {
        set(getParsedExpression(expression), target, value);
    }

    /** 
     *  Updates the target object with the provided value.
     * 
     *  @param expression a parsed OGNL expression
     *  @throws ApplicationRuntimeException if the target can not be updated.
     * 
     **/

    public static void set(Object expression, Object target, Object value)
    {
        try
        {
            Map context = Ognl.createDefaultContext(target, _ognlClassResolver);

            Ognl.setValue(expression, context, target, value);
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.format(
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

    public static Object get(Object expression, Object object)
    {
        try
        {
            Map context = Ognl.createDefaultContext(object, _ognlClassResolver);

            return Ognl.getValue(expression, context, object);
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.format(
                    "OgnlUtils.unable-to-read-expression",
                    "<parsed expression>",
                    object),
                ex);
        }
    }

	/**
	 * @return true if the expession evaluates to a constant or literal value. 
	 *
	 * @since 3.1
	 */
    public static boolean isConstant(String expression)
    {
        return isConstant(getParsedExpression(expression));
    }

	/**
	 * @return true if the parsed expression evaluates to a constant or
	 * literal value
	 * 
	 * @since 3.1
	 */
    public static boolean isConstant(Object expression)
    {
        try
        {
            return Ognl.isConstant(expression);
        }
        catch (OgnlException ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.format("OgnlUtils.unable-to-parse-expression", expression),
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

    public static Object get(String expression, Object object)
    {
        return get(getParsedExpression(expression), object);
    }

    /**
     * @since 3.1
     */
    public static ClassResolver getOgnlClassResolver()
    {
        return _ognlClassResolver;
    }
    /**
     * @since 3.1
     */
    public static void setOgnlClassResolver(ClassResolver resolver)
    {
        _ognlClassResolver = resolver;
    }

}
