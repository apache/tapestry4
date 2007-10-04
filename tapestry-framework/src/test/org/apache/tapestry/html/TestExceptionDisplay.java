package org.apache.tapestry.html;

import org.apache.tapestry.BaseComponentTestCase;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

@Test
public class TestExceptionDisplay extends BaseComponentTestCase {

    @Test(dataProvider = "traces")
    public void test_isInPackage(List packages, String trace, boolean expected) {
        ExceptionDisplay display = newInstance(ExceptionDisplay.class,
                "packages", packages,
                "trace", trace);
        assertTrue(display.isInPackage() == expected);
    }

    @DataProvider(name="traces")
    public Object[][] createTraces() {
        return new Object[][] {
          { Arrays.asList("org.apache.tapestry"), "org.apache", false},
          { Arrays.asList("org.apache.tapestry.pages", "org.apache.tapestry.components"),
                  "org.apache.tapestry.components", true},
          { Arrays.asList(), "org.apache", false},
          { null, "org.apache", false}
        };
    }
}
