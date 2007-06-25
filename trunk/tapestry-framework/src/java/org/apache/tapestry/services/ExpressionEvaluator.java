// Copyright 2004, 2005 The Apache Software Foundation
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

package org.apache.tapestry.services;

import ognl.OgnlContext;
import ognl.enhance.ExpressionAccessor;

/**
 * Wrapper around the OGNL library.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public interface ExpressionEvaluator
{

    /**
     * Reads a property of the target, defined by the expression.
     * 
     * @throws org.apache.hivemind.ApplicationRuntimeException
     *             if the expression can not be parsed, or if some other error
     *             occurs during evaluation of the expression.
     */
    Object read(Object target, String expression);

    /**
     * Reads a property of the target, defined by the (previously compiled)
     * expression.
     * 
     * @throws org.apache.hivemind.ApplicationRuntimeException
     *             if some other error occurs during evaluation of the
     *             expression.
     */
    Object readCompiled(Object target, Object expression);
    
    /**
     * Reads a property of the target, defined by the (previously compiled)
     * expression.
     * 
     * @param target
     *          The object to resolve the expression against.
     * @param expression
     *          The compiled expression.
     * @return
     *          The result of reading on the expression.
     */
    Object read(Object target, ExpressionAccessor expression);
    
    /**
     * Updates a property of the target, defined by the expression.
     * 
     * @throws org.apache.hivemind.ApplicationRuntimeException
     *             if the expression can not be parsed, or if some other error
     *             occurs during evaluation of the expression.
     */
    void write(Object target, String expression, Object value);

    /**
     * Updates a property of the target, defined by the (previously compiled)
     * expression.
     * 
     * @throws org.apache.hivemind.ApplicationRuntimeException
     *             if some other error occurs during evaluation of the
     *             expression.
     */
    void writeCompiled(Object target, Object expression, Object value);
    
    /**
     * Updates a property of the target, defined by the (previously compiled)
     * expression.
     * 
     * @param target
     *          The target object to set a value on.
     * @param expression
     *          The pre-compiled expression.
     * @param value
     *          The value to set.
     */
    void write(Object target, ExpressionAccessor expression, Object value);
    
    /**
     * Returns true if the expression evaluates to a constant or other literal
     * value.
     * 
     * @throws org.apache.hivemind.ApplicationRuntimeException
     *             if the expression is not valid
     */
    boolean isConstant(String expression);
    
    /**
     * Returns true if the expression evaluates to a constant or other literal
     * value.
     * 
     * @throws org.apache.hivemind.ApplicationRuntimeException
     *             if the expression is not valid
     */
    boolean isConstant(Object target, String expression);
    
    /**
     * Creates a default OGNL context object that can be used against
     * the specified object for expression evaluation.
     * 
     * @param target 
     *          The object to get a context for.
     * @return 
     *          An ognl context map.
     */
    OgnlContext createContext(Object target);
}
