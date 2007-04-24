package org.apache.tapestry.contrib.services;

import com.javaforge.tapestry.testng.TestBase;
import org.apache.tapestry.contrib.services.impl.RoundedCornerGenerator;
import org.testng.annotations.Test;

import java.awt.image.BufferedImage;

/**
 * Tests functionality of {@link org.apache.tapestry.contrib.services.impl.RoundedCornerService}.
 */
@Test
public class TestRoundedCornerService extends TestBase {


    public void test_Build_Corner()
    throws Exception
    {
        RoundedCornerGenerator generator = new RoundedCornerGenerator();

        BufferedImage image = generator.buildCorner("FF7E00", null, 30, 30, "tr", -1, -1);
        assert image.getWidth() == 30;
        assert image.getHeight() == 30;
    }
}
