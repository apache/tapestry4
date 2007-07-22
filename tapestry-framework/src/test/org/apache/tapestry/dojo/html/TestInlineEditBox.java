package org.apache.tapestry.dojo.html;

import org.apache.tapestry.*;
import org.apache.tapestry.engine.IEngineService;
import static org.easymock.EasyMock.*;
import org.testng.annotations.Test;

import java.util.Map;

/**
 * Tests functionality of {@link InlineEditBox}. 
 */
@Test
public class TestInlineEditBox extends BaseComponentTestCase {

    public void test_Render_Widget()
    {
        IRequestCycle cycle = newCycle();
        PageRenderSupport prs = newPageRenderSupport();
        IScript script = newMock(IScript.class);
        IEngineService engine = newMock(IEngineService.class);

        IMarkupWriter writer = newBufferWriter();

        InlineEditBox comp = newInstance(InlineEditBox.class,
                                         "templateTagName", "div",
                                         "clientId", "inline",
                                         "value", "Foo",
                                         "mode", InlineEditBox.TEXT_MODE,
                                         "stateful", false,
                                         "disabled", false,
                                         "doFade", false,
                                         "minWidth", 100,
                                         "minHeight", 200,
                                         "engine", engine,
                                         "script", script);

        expect(cycle.isRewinding()).andReturn(false).anyTimes();
        trainGetPageRenderSupport(cycle, prs);

        script.execute(eq(comp), eq(cycle), eq(prs), isA(Map.class));
        
        replay();

        comp.renderWidget(writer, cycle);

        verify();

        assertBuffer("<div id=\"inline\"></div>");
    }

    public void test_Rewind()
    {
        IRequestCycle cycle = newCycle();
        IMarkupWriter writer = newMock(IMarkupWriter.class);

        InlineEditBox comp = newInstance(InlineEditBox.class,
                                         "templateTagName", "div",
                                         "clientId", "inline",
                                         "value", "Foo",
                                         "mode", InlineEditBox.TEXT_MODE);

        expect(cycle.isRewinding()).andReturn(true).anyTimes();

        replay();

        comp.renderWidget(writer, cycle);

        verify();
    }
}
