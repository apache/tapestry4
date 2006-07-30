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

import java.util.Map;

import org.apache.tapestry.components.IPrimaryKeyConverter;


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
     * Used to filter a potentially large list of objects.
     * 
     * @param match
     *          The given partial string that should be matched against object
     *          <i>labels</i> in the model being managed.
     * @return
     *        A {@link Map} containing key/value pairs matching the given input label string. 
     *        The map should contain a key compatible with {@link IPrimaryKeyConverter#getPrimaryKey(Object)} 
     *        and value compatible with {@link #getLabelFor(Object)}.
     */
    Map filterValues(String match);
    
}
