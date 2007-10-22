package org.apache.tapestry.contrib.ajax;

import org.apache.tapestry.TestBase;
import org.testng.annotations.Test;

/**
 * Tests for {@link XTileService}.
 */
@Test
public class TestXTileService extends TestBase {

    public void test_simple_output()
    {
        XTileService objService = new XTileService();
        String result = objService.generateOutputString(new Object[]
                { "test > work", new Integer(20) });

        System.out.println(result);
        assertTrue(result.contains("<sp>test &gt; work</sp>"));
        assertTrue(result.contains("<sp>20</sp>"));
    }
}
