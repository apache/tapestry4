// Copyright Jul 30, 2006 The Apache Software Foundation
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
package org.apache.tapestry.dojo.form;

import java.util.List;

import org.apache.tapestry.components.IPrimaryKeyConverter;
import org.apache.tapestry.form.IPropertySelectionModel;


/**
 * Defines the interface used by the {@link Autocompleter} component to filter
 * and match values from a potentially large data set. 
 * 
 * <p>
 *  The roots of this model come from the {@link IPropertySelectionModel} interface, adding
 *  additional logic for filtering where the normal semantics of {@link IPropertySelectionModel} 
 *  would be prohibitively expensive.
 * </p>
 * 
 * @author jkuhnert
 */
public interface IAutocompleteModel extends IPrimaryKeyConverter
{

    /**
     * For the given value, provide a user friendly label that will
     * be presented in a drop down selection list in the browser ui.
     * 
     * @param value
     *          The object to retrieve a label for.
     * @return
     *          The label to use for the given value.
     */
    String getLabelFor(Object value);
    
    /**
     * Expected to return a list of all possible values, filtering out values that
     * match the specified String in the <strong>label</strong> representation of the value.
     * 
     * @param filter 
     *          The string to use to filter the values based on the label representation of objects.
     * 
     * @return A filtered list of values. Expected to be in the full object form such that
     *      {@link IPrimaryKeyConverter#getPrimaryKey(Object)} can be called on each returned value.
     */
    List getValues(String filter);
}
