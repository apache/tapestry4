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

package org.apache.tapestry.coerce;

import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.form.IPropertySelectionModel;

/**
 * Tests for {@link org.apache.tapestry.coerce.StringToPropertySelectionModelConverter} and
 * {@link org.apache.tapestry.coerce.StringConvertedPropertySelectionModel}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class StringToPropertySelectionModelConverterTest extends HiveMindTestCase
{
    private IPropertySelectionModel newModel(String value)
    {
        return (IPropertySelectionModel) new StringToPropertySelectionModelConverter()
                .convertValue(value);
    }

    private void assertValues(IPropertySelectionModel model, String... values)
    {
        assertEquals(values.length, model.getOptionCount());

        for (int i = 0; i < values.length; i++)
        {
            assertEquals(values[i], model.getValue(i));
            assertEquals(values[i], model.getOption(i));
        }
    }

    private void assertLabels(IPropertySelectionModel model, String... labels)
    {
        assertEquals(labels.length, model.getOptionCount());

        for (int i = 0; i < labels.length; i++)
            assertEquals(labels[i], model.getLabel(i));
    }

    public void testJustLabels()
    {
        IPropertySelectionModel model = newModel("Green,Red,Blue");

        assertValues(model, "Green", "Red", "Blue");
        assertLabels(model, "Green", "Red", "Blue");
    }

    public void testLabelsAndValues()
    {
        IPropertySelectionModel model = newModel("Red=RED,Green=GREEN,Blue=BLUE");

        assertValues(model, "RED", "GREEN", "BLUE");
        assertLabels(model, "Red", "Green", "Blue");
    }

    public void testBlankValue()
    {
        IPropertySelectionModel model = newModel("--Colors--=,Red=RED,Green=GREEN,Blue=BLUE");

        assertValues(model, "", "RED", "GREEN", "BLUE");
        assertLabels(model, "--Colors--", "Red", "Green", "Blue");
    }

    public void testWhiteSpaceTrimmed()
    {
        IPropertySelectionModel model = newModel("--Colors--=\t,\n\tRed\t=\tRED\t,\n\tGreen\t=\tGREEN\t,\n\tBlue\t=\tBLUE\n");

        assertValues(model, "", "RED", "GREEN", "BLUE");
        assertLabels(model, "--Colors--", "Red", "Green", "Blue");
    }
}
