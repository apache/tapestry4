// Copyright 2005 The Apache Software Foundation
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

package org.apache.tapestry.describe;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.apache.hivemind.Registry;
import org.apache.hivemind.impl.RegistryBuilder;
import org.apache.tapestry.IMarkupWriter;

/**
 * Tests for {@link org.apache.tapestry.describe.HTMLDescriptionReceiver}and
 * {@link org.apache.tapestry.describe.HTMLDescriberImpl}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestHTMLDescriptionReceiver extends BaseDescribeTestCase
{
    protected DescribableStrategy newStrategy()
    {
        return (DescribableStrategy) newMock(DescribableStrategy.class);
    }

    public static class NoOpStrategy implements DescribableStrategy
    {
        public void describeObject(Object object, DescriptionReceiver receiver)
        {
            // Does nothing; sufficient for the tests.
        }
    }

    private void trainForTitle(IMarkupWriter writer, String title)
    {
        writer.begin("div");
        writer.attribute("class", "described-object-title");
        writer.print(title);
        writer.end();
        writer.println();

        writer.begin("table");
        writer.attribute("class", "described-object");
        writer.println();
    }

    private void trainForSection(IMarkupWriter writer, String section)
    {
        writer.begin("tr");
        writer.attribute("class", "section");
        writer.begin("th");
        writer.attribute("colspan", 2);
        writer.print(section);
        writer.end("tr");
        writer.println();
    }

    private void trainForKeyValue(IMarkupWriter writer, String key, String value)
    {
        writer.begin("tr");
        writer.begin("th");
        if (key != null)
            writer.print(key);
        writer.end();
        writer.begin("td");
        writer.print(value);
        writer.end("tr");
        writer.println();
    }

    private void trainForNestedKeyValue(IMarkupWriter writer, String key, String value)
    {
        writer.begin("tr");
        writer.begin("th");
        if (key != null)
            writer.print(key);
        writer.end();
        writer.begin("td");
        writer.print(value);
        writer.println();
        writer.end("tr");
        writer.println();
    }

    public void testSetTitleTwiceFails()
    {
        IMarkupWriter writer = newWriter();
        DescribableStrategy adapter = newStrategy();

        replayControls();

        DescriptionReceiver dr = new HTMLDescriptionReceiver(writer, adapter);

        dr.title("First Title");

        try
        {
            dr.title("Second Title");
            unreachable();

        }
        catch (IllegalStateException ex)
        {
            assertEquals(DescribeMessages.setTitleOnce(), ex.getMessage());
        }

        verifyControls();
    }

    public void testSetSectionBeforeTitleFails()
    {
        IMarkupWriter writer = newWriter();
        DescribableStrategy adapter = newStrategy();

        replayControls();

        DescriptionReceiver dr = new HTMLDescriptionReceiver(writer, adapter);

        try
        {
            dr.section("Section");
            unreachable();
        }
        catch (IllegalStateException ex)
        {
            assertEquals(DescribeMessages.mustSetTitleBeforeSection(), ex.getMessage());
        }

        verifyControls();
    }

    public void testIntProperty()
    {
        IMarkupWriter writer = newWriter();
        DescribableStrategy adapter = newStrategy();

        trainForTitle(writer, "Object Title");
        trainForSection(writer, "Section");
        trainForKeyValue(writer, "intProperty", "97");

        replayControls();

        DescriptionReceiver dr = new HTMLDescriptionReceiver(writer, adapter);

        dr.title("Object Title");
        dr.section("Section");
        dr.property("intProperty", 97);

        verifyControls();
    }

    public void testPropertiesWithoutSection()
    {
        IMarkupWriter writer = newWriter();
        DescribableStrategy adapter = newStrategy();

        trainForTitle(writer, "Object Title");
        trainForKeyValue(writer, "first", "1");
        trainForKeyValue(writer, "second", "2");

        replayControls();

        DescriptionReceiver dr = new HTMLDescriptionReceiver(writer, adapter);

        dr.title("Object Title");
        dr.property("first", 1);
        dr.property("second", 2);

        verifyControls();

        trainForSection(writer, "Section 1");
        trainForKeyValue(writer, "s1", "1");
        trainForKeyValue(writer, "s2", "2");

        replayControls();

        dr.section("Section 1");
        dr.property("s1", 1);
        dr.property("s2", 2);

        verifyControls();
    }

    public void testFinishWithProperties()
    {
        IMarkupWriter writer = newWriter();
        DescribableStrategy adapter = newStrategy();

        trainForTitle(writer, "Object Title");
        trainForKeyValue(writer, "first", "1");

        replayControls();

        HTMLDescriptionReceiver dr = new HTMLDescriptionReceiver(writer, adapter);

        dr.title("Object Title");
        dr.property("first", 1);

        verifyControls();

        writer.end("table");
        writer.println();

        replayControls();

        dr.finishUp(null);

        verifyControls();
    }

    public void testFinishNoPropertiesNoTitle()
    {
        IMarkupWriter writer = newWriter();
        DescribableStrategy adapter = newStrategy();

        String object = "Fred";

        writer.print("Fred");
        writer.println();

        replayControls();

        HTMLDescriptionReceiver dr = new HTMLDescriptionReceiver(writer, adapter);

        dr.finishUp(object);

        verifyControls();
    }

    public void testFinishNoPropertiesWithTitle()
    {
        IMarkupWriter writer = newWriter();
        DescribableStrategy adapter = newStrategy();

        String object = "Fred";

        writer.print("Fred Title");
        writer.println();

        replayControls();

        HTMLDescriptionReceiver dr = new HTMLDescriptionReceiver(writer, adapter);

        dr.title("Fred Title");

        dr.finishUp(object);

        verifyControls();
    }

    public void testArray()
    {
        IMarkupWriter writer = newWriter();
        DescribableStrategy adapter = new NoOpStrategy();

        Object[] array = new Object[]
        { "Fred", "Barney" };

        trainForTitle(writer, "Array");
        trainForNestedKeyValue(writer, "list", "Fred");
        trainForNestedKeyValue(writer, null, "Barney");

        replayControls();

        HTMLDescriptionReceiver dr = new HTMLDescriptionReceiver(writer, adapter);

        dr.title("Array");
        dr.array("list", array);

        verifyControls();
    }

    public void testCollection()
    {
        IMarkupWriter writer = newWriter();
        DescribableStrategy adapter = new NoOpStrategy();

        Object[] array = new Object[]
        { "Fred", "Barney" };
        Collection collection = Arrays.asList(array);

        trainForTitle(writer, "Collection");
        trainForNestedKeyValue(writer, "list", "Fred");
        trainForNestedKeyValue(writer, null, "Barney");

        replayControls();

        HTMLDescriptionReceiver dr = new HTMLDescriptionReceiver(writer, adapter);

        dr.title("Collection");
        dr.collection("list", collection);

        verifyControls();
    }

    public void testArrayNullAndEmpty()
    {
        IMarkupWriter writer = newWriter();
        DescribableStrategy strategy = newStrategy();

        replayControls();

        HTMLDescriptionReceiver dr = new HTMLDescriptionReceiver(writer, strategy);

        dr.title("Array");
        dr.array("null", null);
        dr.array("empty", new Object[0]);

        verifyControls();
    }

    public void testCollectionNullAndEmpty()
    {
        IMarkupWriter writer = newWriter();
        DescribableStrategy strategy = newStrategy();

        replayControls();

        HTMLDescriptionReceiver dr = new HTMLDescriptionReceiver(writer, strategy);

        dr.title("Collection");
        dr.collection("null", null);
        dr.collection("empty", Collections.EMPTY_LIST);

        verifyControls();
    }

    public void testScalarProperties()
    {
        IMarkupWriter writer = newWriter();
        DescribableStrategy strategy = newStrategy();

        trainForTitle(writer, "Scalars");
        trainForKeyValue(writer, "boolean", "true");
        trainForKeyValue(writer, "byte", "22");
        trainForKeyValue(writer, "char", "*");
        trainForKeyValue(writer, "short", "-8");
        trainForKeyValue(writer, "int", "900");
        trainForKeyValue(writer, "long", "200020");
        trainForKeyValue(writer, "float", "3.14");
        trainForKeyValue(writer, "double", "-2.7");

        replayControls();

        HTMLDescriptionReceiver dr = new HTMLDescriptionReceiver(writer, strategy);

        dr.title("Scalars");
        dr.property("boolean", true);
        dr.property("byte", (byte) 22);
        dr.property("char", '*');
        dr.property("short", (short) -8);
        dr.property("int", 900);
        dr.property("long", 200020l);
        dr.property("float", (float) 3.14);
        dr.property("double", -2.7);

        verifyControls();
    }

    public void testNullRoot()
    {
        IMarkupWriter writer = newWriter();
        DescribableStrategy strategy = newStrategy();

        writer.print(HTMLDescriptionReceiver.NULL_VALUE);

        replayControls();

        HTMLDescriptionReceiver dr = new HTMLDescriptionReceiver(writer, strategy);

        dr.describe(null);

        verifyControls();
    }

    public void testNullProperty()
    {
        IMarkupWriter writer = newWriter();
        DescribableStrategy strategy = newStrategy();

        trainForTitle(writer, "Null Property");
        trainForKeyValue(writer, "null", HTMLDescriptionReceiver.NULL_VALUE);

        replayControls();

        HTMLDescriptionReceiver dr = new HTMLDescriptionReceiver(writer, strategy);

        dr.title("Null Property");
        dr.property("null", null);

        verifyControls();

    }

    public void testHTMLDescriber()
    {
        IMarkupWriter writer = newWriter();
        DescribableStrategy strategy = new NoOpStrategy();

        String object = "Tapestry";

        writer.print("Tapestry");
        writer.println();

        replayControls();

        HTMLDescriberImpl d = new HTMLDescriberImpl();
        d.setStrategy(strategy);

        d.describeObject(object, writer);

        verifyControls();
    }

    public void testDescribeAlternate()
    {
        IMarkupWriter writer = newWriter();
        DescribableStrategy strategy = newStrategy();

        Object alternate = new Object();

        HTMLDescriptionReceiver dr = new HTMLDescriptionReceiver(writer, strategy);

        strategy.describeObject(alternate, dr);

        replayControls();

        dr.describeAlternate(alternate);

        verifyControls();
    }

    public void testIntegration() throws Exception
    {
        IMarkupWriter writer = newWriter();

        Registry r = RegistryBuilder.constructDefaultRegistry();
        // The Portlet code, which may be in the classpath under Eclipse, adds a second
        // implementation.
        HTMLDescriber d = (HTMLDescriber) r.getService(
                "tapestry.describe.HTMLDescriber",
                HTMLDescriber.class);

        writer.print("Tapestry");
        writer.println();

        replayControls();

        d.describeObject("Tapestry", writer);

        verifyControls();

        writer.print("Anonymous Describable");
        writer.println();

        replayControls();

        d.describeObject(new Describable()
        {
            public void describeTo(DescriptionReceiver receiver)
            {
                receiver.title("Anonymous Describable");
            }
        }, writer);

        verifyControls();
    }
}