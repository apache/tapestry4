package net.sf.tapestry.junit.valid;

import java.math.BigDecimal;
import java.math.BigInteger;

import junit.framework.TestCase;
import net.sf.tapestry.valid.IField;
import net.sf.tapestry.valid.NumberValidator;
import net.sf.tapestry.valid.ValidatorException;

/**
 *  Test the {@link NumberValidator}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.8
 *
 **/

public class TestNumberValidator extends TestCase
{
    private NumberValidator v = new NumberValidator();

    public TestNumberValidator(String name)
    {
        super(name);
    }

    private void testPassThru(String displayName, Class valueType, Number input)
        throws ValidatorException
    {
        testPassThru(new TestingField(displayName, valueType), input);
    }

    private void testPassThru(IField field, Number input) throws ValidatorException
    {
        String s = v.toString(field, input);

        Object o = v.toObject(field, s);

        assertEquals("Input and output.", input, o);
    }

    public void testShort() throws ValidatorException
    {
        testPassThru("testShort", Short.class, new Short((short) 1000));
    }

    public void testInteger() throws ValidatorException
    {
        testPassThru("testInteger", Integer.class, new Integer(373));
    }

    public void testByte() throws ValidatorException
    {
        testPassThru("testByte", Byte.class, new Byte((byte) 131));
    }

    public void testFloat() throws ValidatorException
    {
        testPassThru("testFloat", Float.class, new Float(3.1415));
    }

    public void testDouble() throws ValidatorException
    {
        testPassThru("testDouble", Double.class, new Double(348348.484854848));
    }

    public void testBigInteger() throws ValidatorException
    {
        testPassThru(
            "testBigInteger",
            BigInteger.class,
            new BigInteger("234905873490587234905724908252390487590234759023487523489075"));
    }

    public void testBigDecimal() throws ValidatorException
    {
        testPassThru(
            "testBigDecimal",
            BigDecimal.class,
            new BigDecimal("-29574923857342908743.29058734289734907543289752345897234590872349085"));
    }
}