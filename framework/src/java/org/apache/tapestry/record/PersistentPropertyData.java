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

package org.apache.tapestry.record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.hivemind.util.Defense;

/**
 * Stores persistent property changes concerning a single page. The data may be stored as an encoded
 * string and the PPD can turn between encoded and object form.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class PersistentPropertyData
{
    /**
     * Keyed on {@link org.apache.tapestry.record.ChangeKey}, values are new objects.
     */

    private Map _changes;

    private String _encoded;

    private final PersistentPropertyDataEncoder _encoder;

    /**
     * Creates a new data using the specified encoder. The set of page changes is initially empty.
     */

    public PersistentPropertyData(PersistentPropertyDataEncoder encoder)
    {
        Defense.notNull(encoder, "encoder");

        _encoder = encoder;
        _changes = new HashMap();
    }

    public String getEncoded()
    {
        if (_encoded == null)
            _encoded = encode();

        return _encoded;
    }

    public List getPageChanges()
    {
        if (_changes == null)
        {
            List pageChanges = _encoder.decodePageChanges(_encoded);

            _changes = decode(pageChanges);

            return pageChanges;
        }

        return createPageChangeList();
    }

    public void store(String componentPath, String propertyName, Object newValue)
    {
        Defense.notNull(propertyName, "propertyName");

        if (_changes == null)
            _changes = decode(_encoder.decodePageChanges(_encoded));

        ChangeKey key = new ChangeKey(componentPath, propertyName);

        _changes.put(key, newValue);

        // With the new (or changed) value, the encoded string is no
        // longer valid.

        _encoded = null;
    }

    public void storeEncoded(String encoded)
    {
        Defense.notNull(encoded, "encoded");

        _encoded = encoded;

        // The decoded data is no longer valid now.

        _changes = null;
    }

    private List createPageChangeList()
    {
        List result = new ArrayList();

        Iterator i = _changes.entrySet().iterator();

        while (i.hasNext())
        {
            Map.Entry me = (Map.Entry) i.next();

            ChangeKey changeKey = (ChangeKey) me.getKey();
            Object value = me.getValue();

            PropertyChange change = new PropertyChangeImpl(changeKey.getComponentPath(), changeKey
                    .getPropertyName(), value);
            
            result.add(change);
        }

        return result;
    }

    private String encode()
    {
        List changes = createPageChangeList();

        return _encoder.encodePageChanges(changes);
    }

    private Map decode(List pageChanges)
    {
        Map result = new HashMap();

        Iterator i = pageChanges.iterator();
        while (i.hasNext())
        {
            PropertyChange pc = (PropertyChange) i.next();

            String propertyName = pc.getPropertyName();
            String componentPath = pc.getComponentPath();

            ChangeKey key = new ChangeKey(componentPath, propertyName);

            result.put(key, pc.getNewValue());
        }

        return result;
    }
}