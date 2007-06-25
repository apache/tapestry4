package org.apache.tapestry.enhance;

import ognl.Node;
import ognl.Ognl;
import ognl.OgnlContext;
import org.apache.tapestry.TestBase;
import org.apache.tapestry.services.impl.HiveMindExpressionCompiler;
import org.testng.annotations.Test;

/**
 * Tests functionality of {@link org.apache.tapestry.services.impl.HiveMindExpressionCompiler}.
 */
@Test
public class TestHiveMindExpressionCompiler extends TestBase {

    HiveMindExpressionCompiler _compiler = new HiveMindExpressionCompiler(new ClassFactoryImpl());

    public void test_Duplicate_Class_Compiler()
    throws Exception
    {
        OgnlContext context = (OgnlContext) Ognl.createDefaultContext(null);
        BasicObject root = new BasicObject();
        String expr = "name";
        Node expression = (Node) Ognl.parseExpression(expr);

        _compiler.compileExpression(context, expression, root);
        assert expression.getAccessor() != null;
        assertEquals(expression.getAccessor().get(context, root), root.getName());

        Node expression2 = (Node) Ognl.parseExpression(expr);
        
        _compiler.compileExpression(context, expression2, root);
        assert expression2.getAccessor() != null;
        assertEquals(expression2.getAccessor().get(context, root), root.getName());

        assertNotSame(expression2.getAccessor().getClass().getName(), expression.getAccessor().getClass().getName());
    }

    public void test_Divide_By_Zero()
    throws Exception
    {
        OgnlContext context = (OgnlContext) Ognl.createDefaultContext(null);
        String expr = "true ? 1 : 1/0";

        Node expression = (Node) Ognl.parseExpression(expr);
        _compiler.compileExpression(context, expression, expr);
        
        assertEquals(expression.getAccessor().get(context, null), Integer.valueOf(1));
    }
}
