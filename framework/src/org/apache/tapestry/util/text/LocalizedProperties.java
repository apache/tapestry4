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

package org.apache.tapestry.util.text;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * A version of java.util.Properties that can read the properties from files
 * using an encoding other than ISO-8859-1. All non-latin characters are read 
 * correctly using the given encoding and no longer need to be quoted using native2ascii.
 * 
 * In addition, the properties may be stored in an arbitrary map, rather than
 * only in Properties. For example, using LinkedHashMap will preserve the order
 * of the properties as defined in the file. 
 * 
 * @author mb
 * @since 3.1
 */
public class LocalizedProperties
{
    private Map _propertyMap;
    
    /**
     * Create a new object with an empty property storage 
     */
    public LocalizedProperties()
    {
        this(new HashMap());
    }

    /**
     * Use the provided argument as the storage location for the properties managed
     * by this object. This allows different types of Map implementations to be used,
     * such as a LinkedHashMap to preserve the order of the keys, for example.
     * The provided map may contain the default property values as well.
     * 
     * @param propertyMap the map where properties are to be stored
     */
    public LocalizedProperties(Map propertyMap)
    {
        _propertyMap = propertyMap;
    }
    
    /**
     * Returns the property value corresponding the provided key. If there is no such property,
     * or the value in the provided map is not of type String, null is returned.
     * 
     * @param key the property key
     * @return the value of the property, or null if there is no such property
     */
    public String getProperty(String key)
    {
       Object value = _propertyMap.get(key);
       if (value instanceof String)
           return (String) value;
       return null;
    }
    
    /**
     * Returns the property value corresponding to the provided key, 
     * or the provided default value if no such property exists.
     * 
     * @param key the property key
     * @param defaultValue the default value of the property
     * @return the value of the property, or the default value if there is no such property
     */
    public String getProperty(String key, String defaultValue)
    {
        String value = getProperty(key);
        if (value != null)
            return value;
        return defaultValue;
    }
    
    /**
     * Stores a property value
     * 
     * @param key the property key
     * @param value the property value
     */
    public void setProperty(String key, String value)
    {
        _propertyMap.put(key, value);
    }
    
    /**
     * Returns the map containing all properties. The map can be used to enumerate the 
     * properties or their keys. 
     * 
     * @return a map containing the properties
     */
    public Map getPropertyMap()
    {
        return _propertyMap;
    }
    
    /**
     * Loads the properties from the given stream using the default character encoding.
     * This method operates in the same way as the equivalent method in {@link java.util.Properties},
     * but it also handles non-ascii symbols. 
     * 
     * @param ins the stream to load the properties from
     * @throws IOException
     */
    public void load(InputStream ins) throws IOException
    {
    	LocalizedPropertiesLoader loader = new LocalizedPropertiesLoader(ins);
        loader.load(_propertyMap);
    }
    
    /**
     * Loads the properties from the given stream using the provided character encoding.
     * This method operates in the same way as the equivalent method in {@link java.util.Properties},
     * but it also handles non-ascii symbols. 
     * 
     * @param ins the stream to load the properties from
     * @param encoding the encoding the use when parsing the stream
     * @throws IOException
     */
    public void load(InputStream ins, String encoding) throws UnsupportedEncodingException, IOException
    {
    	LocalizedPropertiesLoader loader = new LocalizedPropertiesLoader(ins, encoding);
        loader.load(_propertyMap);
    }
    
    /**
     * Loads the properties from the given reader.
     * This method operates in the same way as the equivalent method in {@link java.util.Properties},
     * but it also handles non-ascii symbols. 
     * 
     * @param reader the reader to load the properties from
     * @throws IOException
     */
    public void load(Reader reader) throws IOException
    {
    	LocalizedPropertiesLoader loader = new LocalizedPropertiesLoader(reader);
        loader.load(_propertyMap);
    }
}
