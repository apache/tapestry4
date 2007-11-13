package org.apache.tapestry.javascript;

import java.util.Locale;

import org.apache.tapestry.IAsset;
import org.apache.tapestry.TestBase;
import org.apache.tapestry.util.DescribedLocation;
import org.apache.tapestry.asset.AssetSource;
import org.apache.hivemind.Resource;
import org.testng.annotations.Test;

import static org.easymock.EasyMock.*;

@Test
public class TestJavascriptManagerImpl extends TestBase
{
    public void test_null_manager()
    {
        JavascriptManagerImpl impl = createImpl();
        replay();

        assertNullAndEmpty(impl);
        
        verify();
    }

    public void test_empty_manager()
    {
        JavascriptManagerImpl impl = createImpl("", "", "", "", "", "");
        replay();

        assertNullAndEmpty(impl);

        verify();
    }

    public void test_several_files()
    {
        AssetSource source = newMock(AssetSource.class);
        expectFile(source, "a.js");
        expectFile(source, "b.js");
        expectFile(source, "tap");

        replay();

        JavascriptManagerImpl impl = createImpl(source, "a.js, b.js", "", "", "", "tap", "");
        assertEquals(impl.getJsAssets().size(), 2);
        assertNotNull(impl.getMainJsAsset());
        assertNotNull(impl.getJsTapestryAsset());

        verify();
    }

    private void expectFile(AssetSource source, String file) {
        expect(source.findAsset((Resource) isNull(), eq(file),
                (Locale) isNull(), isA(DescribedLocation.class)))
                .andReturn(newMock(IAsset.class));
    }

    private void assertNullAndEmpty(JavascriptManagerImpl impl) {
        assertNull(impl.getJsPath());
        assertNull(impl.getJsTapestryAsset());
        assertNull(impl.getJsTapestryPath());
        assertNull(impl.getMainJsAsset());
        assertNull(impl.getMainJsFormAsset());
        assertNull(impl.getMainJsWidgetAsset());
        assertTrue(impl.getJsAssets().isEmpty());
        assertTrue(impl.getJsFormAssets().isEmpty());
        assertTrue(impl.getJsWidgetAssets().isEmpty());
    }

    private JavascriptManagerImpl createImpl(String...params) {
        return createImpl(newMock(AssetSource.class), params);
    }

    private JavascriptManagerImpl createImpl(AssetSource source, String... params) {
        JavascriptManagerImpl impl = new JavascriptManagerImpl();
        impl.setAssetSource(source);

        if (params.length>0)
            impl.setFiles(params[0]);
        if (params.length>1)
            impl.setFormFiles(params[1]);
        if (params.length>2)
            impl.setWidgetFiles(params[2]);
        if (params.length>3)
            impl.setPath(params[3]);
        if (params.length>4)
            impl.setTapestryFile(params[4]);
        if (params.length>5)
            impl.setTapestryPath(params[5]);
        return impl;
    }
}
