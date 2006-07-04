// Copyright 2004, 2005 The Apache Software Foundation
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
package org.apache.tapestry.form;


/**
 * Used by the {@link BeanPropertySelectionModelTest} to test
 * listing/selecting bean properties.
 *
 * @author jkuhnert
 */
public class SimpleBean
{

    protected int _id;
    protected String _name;
    protected String _description;
    
    /** Default constructor. */
    public SimpleBean() { }
    
    /**
     * Creates a new instance with default values.
     * @param id
     * @param name
     * @param description
     */
    public SimpleBean(int id, String name, String description)
    {
        _id = id;
        _name = name;
        _description = description;
    }
    
    /**
     * @return Returns the description.
     */
    public String getDescription()
    {
        return _description;
    }
    
    /**
     * @return Returns the id.
     */
    public int getId()
    {
        return _id;
    }
    
    /**
     * @return Returns the name.
     */
    public String getName()
    {
        return _name;
    }
}
