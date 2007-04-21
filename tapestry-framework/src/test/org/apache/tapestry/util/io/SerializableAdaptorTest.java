package org.apache.tapestry.util.io;

import com.javaforge.tapestry.testng.TestBase;
import org.testng.annotations.Test;

import java.util.Locale;

/**
 *
 */
@Test
public class SerializableAdaptorTest extends TestBase {

    public void test_Squeeze()
    {
        SerializableAdaptor squeezer = new SerializableAdaptor();

        String data = squeezer.squeeze(null, Locale.getDefault());

        assert data != null;
        assert data.length() > 0;
    }
}
