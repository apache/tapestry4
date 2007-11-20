package org.apache.tapestry.contrib.table.model.simple;

import org.apache.hivemind.Messages;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IPage;
import org.apache.tapestry.components.Block;
import static org.easymock.EasyMock.checkOrder;
import static org.easymock.EasyMock.expect;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

@Test
public class TestSimpleTableColumn extends BaseComponentTestCase
{

    // Test for TAPESTRY-234
    public void test_Alternate_Column_Display_Name()
    {
        SimpleTableColumn column = new SimpleTableColumn("foo.bar");

        assertEquals(column.getColumnName(), "foo.bar");
        assertEquals(column.getDisplayName(), "foo.bar");
        assertEquals(column.getValueRendererSource(), SimpleTableColumn.DEFAULT_VALUE_RENDERER_SOURCE);

        IPage page = newMock(IPage.class);
        checkOrder(page, false);
        expect(page.getPageName()).andReturn("TestPage").anyTimes();

        IComponent container = newComponent();
        Messages msgs = newMock(Messages.class);
        Block valueRenderer = org.easymock.classextension.EasyMock.createMock(Block.class);
        
        expect(valueRenderer.getPage()).andReturn(page);
        expect(valueRenderer.getIdPath()).andReturn("TestPage[foo.bar]");

        Map components = new HashMap();
        components.put("foo_barColumnValue", valueRenderer);

        expect(container.getMessages()).andReturn(msgs);
        expect(msgs.getMessage("foo.bar")).andReturn("[foo.bar]");
        
        expect(container.getComponents()).andReturn(components).anyTimes();

        replay();
        org.easymock.classextension.EasyMock.replay(valueRenderer);

        column.loadSettings(container);
        
        assert column.getValueRendererSource() != null;
        assert !SimpleTableColumn.DEFAULT_VALUE_RENDERER_SOURCE.equals(column.getValueRendererSource());
        
        verify();
        org.easymock.classextension.EasyMock.verify(valueRenderer);
    }
}
