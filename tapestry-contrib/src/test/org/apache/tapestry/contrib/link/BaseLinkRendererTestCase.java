// Copyright 2009 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package org.apache.tapestry.contrib.link;

import org.apache.tapestry.*;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.components.ILinkComponent;
import org.apache.tapestry.link.ILinkRenderer;
import org.apache.tapestry.link.DirectLink;
import org.easymock.IAnswer;
import static org.easymock.EasyMock.*;

/**
 * Helper to test {@link ILinkRenderer} instances.
 * @author Andreas Andreou
 */
public class BaseLinkRendererTestCase extends BaseComponentTestCase {
    /**
     * Asserts the direct output (and the scripts written to the body) of an
     * {@link ILinkRenderer}. 
     */
    protected void assertLinkRenderer(ILinkRenderer linkRenderer, ILinkComponent linkComponent,
                                      String output, String bodyScript) {
        StringBuffer sb = new StringBuffer();
        final IMarkupWriter writer = newBufferWriter();
        final IRequestCycle cycle = newCycleAnyTimes(false);
        trainUniqueIdAnyTimes(cycle);
        trainGetSupportAnyTimes(cycle, sb);

        trainGetSetRemoveAttribute(cycle, Tapestry.LINK_COMPONENT_ATTRIBUTE_NAME, null, linkComponent);

        replay();

        linkRenderer.renderLink(writer, cycle, linkComponent);

        assertBuffer(output);
        assertEquals(sb.toString(), bodyScript);

        verify();
    }

    /**
     * Generates a link component to be used along with {@link #assertLinkRenderer(ILinkRenderer, ILinkComponent, String, String)}
     * for testing {@link ILinkRenderer} instances.<p/>
     * Users can provide the url, disabled, target and anchor formal parameters and a key-value list
     * of informal parameters.
     */
    protected ILinkComponent newLinkComponent(String url, boolean disabled, String target, String anchor,
                                            Object...informals) {
        final ILink link = newLink();
        expect(link.getURL(anchor, true)).andReturn(url).anyTimes();
        expect(link.getURL(anchor, false)).andReturn(url).anyTimes();
        expect(link.getURL( (String) anyObject(), (String) anyObject(), anyInt(), (String) anyObject(), anyBoolean() )).andReturn(url).anyTimes();
        expect(link.getAbsoluteURL()).andReturn(url).anyTimes();
        final IComponentSpecification specification = newMock(IComponentSpecification.class);
        TestButtonLinkRenderer.ExtraLink directLink = newInstance(TestButtonLinkRenderer.ExtraLink.class, "disabled", disabled,
                "target", target, "anchor", anchor, "link", link, "specification", specification);

        for (int i=0; i<informals.length; i+=2) {
            if (i+1>=informals.length) {
                break;
            }
            directLink.setBinding((String) informals[i], newBindingAnyTimes(informals[i+1]));
            expect(specification.getParameter((String) informals[i])).andReturn(null).anyTimes();
        }

        return directLink;
    }

    protected IRequestCycle newCycleAnyTimes(boolean rewinding) {
        final IRequestCycle cycle = newCycle();
        expect(cycle.isRewinding()).andReturn(rewinding).anyTimes();
        return cycle;
    }

    protected void trainUniqueIdAnyTimes(IRequestCycle cycle) {
        expect(cycle.getUniqueId(isA(String.class))).andAnswer(new IAnswer<String>(){
            public String answer() throws Throwable {
                return (String) getCurrentArguments()[0];
            }
        }).anyTimes();
    }

    protected void trainGetSetRemoveAttribute(IRequestCycle cycle, String attribute,
                                            Object oldValue, Object newValue) {
        expect(cycle.getAttribute(attribute)).andReturn(oldValue).anyTimes();

        cycle.setAttribute(attribute, newValue);
        expectLastCall().anyTimes();
        
        cycle.removeAttribute(attribute);
        expectLastCall().anyTimes();
    }

    protected void trainGetSupportAnyTimes(IRequestCycle cycle, final StringBuffer sb)
    {
        PageRenderSupport support = newMock(PageRenderSupport.class);
        expect(cycle.getAttribute(TapestryUtils.PAGE_RENDER_SUPPORT_ATTRIBUTE)).andReturn(support).anyTimes();
        expect(support.getUniqueString(isA(String.class))).andAnswer(new IAnswer<String>(){
            private int count = 0;
            public String answer() throws Throwable {
                return getCurrentArguments()[0] + "_" + (count++);
            }
        }).anyTimes();
        support.addBodyScript(isA(IComponent.class), isA(String.class));
        expectLastCall().andStubAnswer(new IAnswer<Object>(){
            public Object answer() throws Throwable {
                String js = (String) getCurrentArguments()[1];
                sb.append(js);
                return null;
            }
        });

    }

    protected IBinding newBindingAnyTimes(Object value) {
        IBinding binding = newMock(IBinding.class);
        checkOrder(binding, false);

        expect(binding.getObject()).andReturn(value).anyTimes();
        expect(binding.getObject(isA(Class.class))).andReturn(value).anyTimes();
        return binding;
    }

    /**
     * A {@link DirectLink} subclass to get around some limitations during testing. It allows
     * defining the link and specification to be used/returned and sets the stateful flag to false.
     */
    public static abstract class ExtraLink extends DirectLink {
        private ILink link;
        IComponentSpecification specification;

        public boolean isStateful() {
            return false;
        }

        public ILink getLink(IRequestCycle cycle) {
            return link;
        }

        public ILink getLink() {
            return link;
        }

        public void setLink(ILink link) {
            this.link = link;
        }

        @Override
        public IComponentSpecification getSpecification() {
            return specification;
        }

        public void setSpecification(IComponentSpecification specification) {
            this.specification = specification;
        }
    }
}
