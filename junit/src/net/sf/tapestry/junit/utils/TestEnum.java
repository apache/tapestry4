package net.sf.tapestry.junit.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.lang.enum.Enum;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import net.sf.tapestry.junit.TapestryTestCase;
import net.sf.tapestry.spec.AssetType;
import net.sf.tapestry.spec.Direction;

/**
 *  Tests the ability of an {@link net.sf.tapestry.util.Enum} 
 *  to be serialized and deserialized properly.
 * 
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class TestEnum extends TapestryTestCase
{
    public TestEnum(String name)
    {
        super(name);
    }

    public void testSerialization() throws Exception
    {
        AssetType start = AssetType.EXTERNAL;

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);

        oos.writeObject(start);

        oos.close();

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bis);

        AssetType result = (AssetType) ois.readObject();

        assertEquals(start, result);
        assertSame(start, result);
    }

    public void testToString()
    {
        assertEquals("AssetType[EXTERNAL]", AssetType.EXTERNAL.toString());
        assertEquals("Direction[CUSTOM]", Direction.CUSTOM.toString());
    }

}