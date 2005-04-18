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
     *             if the expression can not be parsed, or if some other error occurs during
     *             evaluation of the expression.
     */
    public Object read(Object target, String expression);

    /**
     * Reads a property of the target, defined by the (previously compiled) expression.
     * 
     * @throws org.apache.hivemind.ApplicationRuntimeException
     *             if some other error occurs during evaluation of the expression.
     */
    public Object readCompiled(Object target, Object expression);

    /**
     * Updates a property of the target, defined by the expression.
     * 
     * @throws org.apache.hivemind.ApplicationRuntimeException
     *             if the expression can not be parsed, or if some other error occurs during
     *             evaluation of the expression.
     */
    public void write(Object target, String expression, Object value);

    /**
     * Updates a property of the target, defined by the (previously compiled) expression.
     * 
     * @throws org.apache.hivemind.ApplicationRuntimeException
     *             if some other error occurs during evaluation of the expression.
     */
    public void writeCompiled(Object target, Object expression, Object value);

    /**
     * Returns true if the expression evaluates to a constant or other literal value.
     * 
     * @throws org.apache.hivemind.ApplicationRuntimeException
     *             if the expression is not valid
     */
    public boolean isConstant(String expression);
}