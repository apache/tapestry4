package org.apache.tapestry.contrib.services;

import com.javaforge.tapestry.testng.TestBase;
import org.apache.tapestry.contrib.services.impl.RoundedCornerGenerator;
import org.testng.annotations.Test;

/**
 * Tests functionality of {@link org.apache.tapestry.contrib.services.impl.RoundedCornerService}.
 */
@Test
public class TestRoundedCornerService extends TestBase {

    public void test_Build_Corner()
    throws Exception
    {
        RoundedCornerGenerator generator = new RoundedCornerGenerator();

        byte[] data = generator.buildCorner("FF7E00", null, 30, 30, "tr");

        assert data != null;
        assert data.length > 0;        
    }

    public void test_Corner_Cached()
    throws Exception
    {
        RoundedCornerGenerator generator = new RoundedCornerGenerator();

        byte[] data1 = generator.buildCorner("FF7E00", null, 30, 30, "tr");
        byte[] data2 = generator.buildCorner("FF7E00", null, 30, 30, "tr");

        assert data1 == data2;
    }
}
