//  Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.contrib.table.model;

/**
 * An interface for converting an object to its primary key and back. 
 * Typically used to determine how to store a given object as a hidden 
 * value when rendering a form.
 * 
 * @version $Id$
 * @author mb
 * @since 3.0
 */
public interface IPrimaryKeyConvertor
{
    /**
     * Gets the serializable primary key of the given value
     * 
     * @param objValue the value for which a primary key needs to be extracted
     * @return the serializable primary key of the value
     */
    Object getPrimaryKey(Object objValue);
    
    /**
     * Gets the value corresponding the given primary key 
     *  
     * @param objPrimaryKey the primary key for which a value needs to be generated
     * @return the generated value corresponding to the given primary key
     */
    Object getValue(Object objPrimaryKey); 
}
