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

package net.sf.tapestry.util.prop;

import java.util.HashMap;
import java.util.Map;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.Tapestry;
import ognl.Ognl;
import ognl.OgnlException;

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
            catch (OgnlException ex)
            {
                throw new ApplicationRuntimeException(ex);
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
            Ognl.setValue(expression, target, value);
        }
        catch (OgnlException ex)
        {
            throw new ApplicationRuntimeException(ex);
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
            return Ognl.getValue(expression, object);
        }
        catch (OgnlException ex)
        {
            throw new ApplicationRuntimeException(ex);
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
     
}
