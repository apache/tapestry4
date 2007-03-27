//Copyright 2004, 2005 The Apache Software Foundation

//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at

//http://www.apache.org/licenses/LICENSE-2.0

//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
package org.apache.tapestry.services.impl;

import ognl.*;
import ognl.enhance.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.service.ClassFab;
import org.apache.hivemind.service.ClassFactory;
import org.apache.hivemind.service.MethodSignature;
import org.apache.tapestry.IRender;
import org.apache.tapestry.enhance.AbstractFab;

import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Adds to default ognl compiler class pools.
 * @author jkuhnert
 */
public class HiveMindExpressionCompiler extends ExpressionCompiler implements OgnlExpressionCompiler
{
    private static final Log _log = LogFactory.getLog(HiveMindExpressionCompiler.class);

    private ClassFactory _classFactory;

    public HiveMindExpressionCompiler(ClassFactory classfactory)
    {
        _classFactory = classfactory;
    }

    public String castExpression(OgnlContext context, Node expression, String body)
    {
        if (_log.isDebugEnabled()) {
            _log.debug("castExpression() with expression " + expression + " currentType is: " + context.getCurrentType() 
                    + " previousType: " + context.getPreviousType()
                    + " \n current Accessor: " + context.getCurrentAccessor()
                    + " previous Accessor: " + context.getPreviousAccessor()
                    + "\n current object: " + context.getCurrentObject());
        }

        if (context.getCurrentAccessor() == null
                || context.getPreviousType() == null
                || context.getCurrentAccessor().isAssignableFrom(context.getPreviousType())
                || (context.getCurrentType() != null
                        && context.getCurrentObject() != null
                        && context.getCurrentType().isAssignableFrom(context.getCurrentObject().getClass())
                        && context.getCurrentAccessor().isAssignableFrom(context.getPreviousType()))
                        || body == null || body.trim().length() < 1
                        || (context.getCurrentType() != null && context.getCurrentType().isArray())
                        || ASTOr.class.isInstance(expression)
                        || ASTAnd.class.isInstance(expression)
                        || ASTRootVarRef.class.isInstance(expression)
                        || context.getCurrentAccessor() == Class.class
                        || Number.class.isAssignableFrom(context.getCurrentAccessor())
                        || (context.get(ExpressionCompiler.PRE_CAST) != null && ((String)context.get(ExpressionCompiler.PRE_CAST)).startsWith("new"))
                        || ASTStaticField.class.isInstance(expression)
                        || (OrderedReturn.class.isInstance(expression) && ((OrderedReturn)expression).getLastExpression() != null))
            return body;

        if (_log.isDebugEnabled()) {
            _log.debug("castExpression() with expression " + expression + " currentType is: " + context.getCurrentType() 
                    + " previousType: " + context.getPreviousType()
                    + " current Accessor: " + context.getCurrentAccessor()
                    + " previous Accessor: " + context.getPreviousAccessor());
        }

        String castClass = null;
        if (context.getCurrentType() != null && context.getCurrentType().isArray()) {

            castClass = context.getCurrentType().getCanonicalName();
        } else if (context.getCurrentAccessor().isArray()) {

            castClass = context.getCurrentAccessor().getCanonicalName();
        } else {
            castClass = (context.getCurrentAccessor().getName().indexOf("$") > -1)
            ? OgnlRuntime.getCompiler().getInterfaceClass(context.getCurrentAccessor()).getName()
                : context.getCurrentAccessor().getName();
        }

        ExpressionCompiler.addCastString(context, "((" + castClass + ")");

        return ")" + body;
    }

    public String getClassName(Class clazz)
    {
        if (IRender.class.isAssignableFrom(clazz) || Modifier.isPublic(clazz.getModifiers()))
            return clazz.getName();

        if (clazz.getName().equals("java.util.AbstractList$Itr"))
            return Iterator.class.getName();

        if (Modifier.isPublic(clazz.getModifiers()) && clazz.isInterface())
            return clazz.getName();

        Class[] intf = clazz.getInterfaces();

        for (int i=0; i < intf.length; i++) {
            if (intf[i].getName().indexOf("util.List") > 0)
                return intf[i].getName();
            else if (intf[i].getName().indexOf("Iterator") > 0)
                return intf[i].getName();
        }

        if (clazz.getSuperclass() != null && clazz.getSuperclass().getInterfaces().length > 0)
            return getClassName(clazz.getSuperclass());

        return clazz.getName();
    }

    public Class getInterfaceClass(Class clazz)
    {
        if (IRender.class.isAssignableFrom(clazz) || clazz.isInterface()
                || Modifier.isPublic(clazz.getModifiers()))
            return clazz;

        if (clazz.getName().equals("java.util.AbstractList$Itr"))
            return Iterator.class;

        if (Modifier.isPublic(clazz.getModifiers())
                && clazz.isInterface() || clazz.isPrimitive()) {

            return clazz;
        }

        Class[] intf = clazz.getInterfaces();

        for (int i=0; i < intf.length; i++) {

            if (List.class.isAssignableFrom(intf[i]))
                return List.class;
            else if (Iterator.class.isAssignableFrom(intf[i]))
                return Iterator.class;
            else if (Map.class.isAssignableFrom(intf[i]))
                return Map.class;
            else if (Set.class.isAssignableFrom(intf[i]))
                return Set.class;
            else if (Collection.class.isAssignableFrom(intf[i]))
                return Collection.class;
        }

        if (clazz.getSuperclass() != null && clazz.getSuperclass().getInterfaces().length > 0)
            return getInterfaceClass(clazz.getSuperclass());

        return clazz;
    }

    public void compileExpression(OgnlContext context, Node expression, Object root)
    throws Exception
    {
        if (_log.isDebugEnabled())
            _log.debug("Compiling expr class " + expression.getClass().getName() + " and root " + root.getClass().getName() + " with toString:" + expression.toString());

        if (expression.getAccessor() != null)
            return;

        synchronized (expression) {
            
            String getBody = null;
            String setBody = null;

            try {

                ClassFab classFab = _classFactory.newClass(expression.getClass().getName()+expression.hashCode()+"Accessor", Object.class);
                classFab.addInterface(ExpressionAccessor.class);

                MethodSignature valueGetter = new MethodSignature(Object.class, "get", new Class[] {OgnlContext.class, Object.class}, null);
                MethodSignature valueSetter = new MethodSignature(void.class, "set", new Class[] {OgnlContext.class, Object.class, Object.class}, null);

                MethodSignature expressionSetter = new MethodSignature(void.class, "setExpression", new Class[] {Node.class}, null);

                // must evaluate expression value at least once if object isn't null

                if (root != null)
                    Ognl.getValue(expression, context, root);

                try {

                    getBody = generateGetter(context, classFab, valueGetter, expression, root);

                } catch (UnsupportedCompilationException uc) {

                    // The target object may not fully resolve yet because of a partial tree with a null somewhere, we 
                    // don't want to bail out forever because it might be enhancable on another pass eventually
                    
                    return;
                }

                classFab.addMethod(Modifier.PUBLIC, valueGetter, getBody);

                try {

                    setBody = generateSetter(context, classFab, valueSetter, expression, root);

                } catch (UnsupportedCompilationException uc) {

                    if (_log.isDebugEnabled())
                        _log.warn("Unsupported setter compilation caught: " + uc.getMessage() + " for expression: " + expression.toString());

                    setBody = generateOgnlSetter(classFab, valueSetter);

                    if (!classFab.containsMethod(expressionSetter)) {

                        classFab.addField("_node", Node.class);
                        classFab.addMethod(Modifier.PUBLIC, expressionSetter, "{ _node = $1; }");
                    }
                }

                if (setBody != null)
                    classFab.addMethod(Modifier.PUBLIC, valueSetter, setBody);

                classFab.addConstructor(new Class[0], new Class[0], "{}");

                Class clazz = ((AbstractFab)classFab).createClass(true);

                expression.setAccessor((ExpressionAccessor)clazz.newInstance());

                // need to set expression on node if the field was just defined.

                if (classFab.containsMethod(expressionSetter)) {

                    expression.getAccessor().setExpression(expression);
                }

            } catch (Throwable t) {
                throw new ApplicationRuntimeException("Error compiling expression on object " + root
                        + " with expression node " + expression + " getter body: " + getBody 
                        + " setter body: " + setBody, t);
            }
        }
    }

    protected String generateGetter(OgnlContext context, ClassFab newClass, MethodSignature valueGetter, Node expression, Object root)
    throws Exception
    {
        String pre="";
        String post="";
        String body = null;
        String getterCode = null;
        
        context.setRoot(root);
        context.setCurrentObject(root);
        context.remove(PRE_CAST);

        try {
            
            getterCode = expression.toGetSourceString(context, root);
        } catch (NullPointerException e) {
            if (_log.isDebugEnabled())
                _log.warn("NullPointer caught compiling getter, may be normal ognl method artifact.", e);
            
            throw new UnsupportedCompilationException("Statement threw nullpointer.");
        }
        
        if (getterCode == null || getterCode.trim().length() <= 0 && !ASTVarRef.class.isAssignableFrom(expression.getClass()))
            getterCode = "null";

        Class returnType = null;

        if (NodeType.class.isInstance(expression)) {
            NodeType nType = (NodeType)expression;
            returnType = nType.getGetterClass();
            
            if (returnType != null && !String.class.isAssignableFrom(returnType)) {

                pre = pre + " ($w) (";
                post = post + ")";
            }
        }

        String castExpression = (String)context.get(PRE_CAST);

        if (returnType == null) {
            pre = pre + " ($w) (";
            post = post + ")";
        }

        String rootExpr = !getterCode.equals("null") ? getRootExpression(expression, root, false) : "";

        String noRoot = (String)context.remove("_noRoot");
        if (noRoot != null)
            rootExpr = "";

        if (OrderedReturn.class.isInstance(expression) && ((OrderedReturn)expression).getLastExpression() != null) {

            body = "{ "
                + (ASTMethod.class.isInstance(expression) || ASTChain.class.isInstance(expression) ? rootExpr : "")
                + (castExpression != null ? castExpression : "")
                + ((OrderedReturn)expression).getCoreExpression()
                + " return " + pre + ((OrderedReturn)expression).getLastExpression()
                + post
                + ";}";

        } else {

            body = "{ return " + pre
            + (castExpression != null ? castExpression : "")
            + rootExpr
            + getterCode
            + post
            + ";}";
        }

        body = body.replaceAll("\\.\\.", ".");

        if (_log.isDebugEnabled())
            _log.debug("Getter Body: ===================================\n"+body);

        return body;
    }

    protected String generateSetter(OgnlContext context, ClassFab newClass, MethodSignature valueSetter, Node expression, Object root)
    throws Exception
    {
        if (ExpressionNode.class.isInstance(expression) 
                || ASTConst.class.isInstance(expression))
            throw new UnsupportedCompilationException("Can't compile expression/constant setters.");

        context.setRoot(root);
        context.setCurrentObject(root);
        context.remove(PRE_CAST);

        String body = null;

        String setterCode = expression.toSetSourceString(context, root);
        String castExpression = (String)context.get(PRE_CAST);

        if (setterCode == null || setterCode.trim().length() < 1)
            throw new UnsupportedCompilationException("Can't compile null setter body.");

        if (root == null)
            throw new UnsupportedCompilationException("Can't compile setters with a null root object.");

        String pre = getRootExpression(expression, root, false);

        String noRoot = (String)context.remove("_noRoot");
        if (noRoot != null)
            pre = "";

        body = "{" 
            + (castExpression != null ? castExpression : "")
            + pre 
            + setterCode + ";}";

        body = body.replaceAll("\\.\\.", ".");

        if (body.indexOf("$3") < 0)
            body = null;

        if (_log.isDebugEnabled())
            _log.debug("Setter Body: ===================================\n"+body);

        return body;
    }

    String generateOgnlGetter(ClassFab newClass, MethodSignature valueGetter)
    throws Exception
    {
        String body = "{ return _node.getValue($1, $2); }";

        return body;
    }

    String generateOgnlSetter(ClassFab newClass, MethodSignature valueSetter)
    throws Exception
    {
        String body = "{ _node.setValue($1, $2, $3); }";

        return body;
    }
}
