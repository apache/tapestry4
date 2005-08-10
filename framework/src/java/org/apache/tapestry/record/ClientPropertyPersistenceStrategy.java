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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.ServiceEncoding;
import org.apache.tapestry.web.WebRequest;

/**
 * Service tapestry.persist.ClientPropertyPersistenceStrategy. Encodes persistent page properties on
 * the client as query parameters.
 * <p>
 * Uses the threaded model.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 * @see org.apache.tapestry.engine.ILink
 */
public class ClientPropertyPersistenceStrategy implements PropertyPersistenceStrategy
{
    /**
     * Keyed on page name (String), values are
     * {@link org.apache.tapestry.record.PersistentPropertyData}.
     */
    private final Map _data = new HashMap();

    private final PersistentPropertyDataEncoder _encoder;

    private WebRequest _request;

    private ClientPropertyPersistenceScope _scope;

    public ClientPropertyPersistenceStrategy()
    {
        this(new PersistentPropertyDataEncoderImpl());
    }

    // Alternate constructor used for testing
    ClientPropertyPersistenceStrategy(PersistentPropertyDataEncoder encoder)
    {
        _encoder = encoder;
    }

    /**
     * Initializer for this service, invoked every time a service instance is created. This
     * initializer pulls out of the request and query parameters whose prefix is "client:" and
     * expects them to be encoded {@link PersistentPropertyData}, which are stored internally.
     * Because the service model is threaded, this information is specific to a single request, and
     * will be discarded at the end of the request.
     */

    public void initializeService()
    {
        List names = _request.getParameterNames();
        Iterator i = names.iterator();
        while (i.hasNext())
        {
            String name = (String) i.next();

            if (!_scope.isParameterForScope(name))
                continue;

            String pageName = _scope.extractPageName(name);

            String encoded = _request.getParameterValue(name);

            PersistentPropertyData data = new PersistentPropertyData(_encoder);
            data.storeEncoded(encoded);

            _data.put(pageName, data);
        }
    }

    public void store(String pageName, String idPath, String propertyName, Object newValue)
    {
        PersistentPropertyData data = (PersistentPropertyData) _data.get(pageName);
        if (data == null)
        {
            data = new PersistentPropertyData(_encoder);
            _data.put(pageName, data);
        }

        data.store(idPath, propertyName, newValue);
    }

    public Collection getStoredChanges(String pageName, IRequestCycle cycle)
    {
        PersistentPropertyData data = (PersistentPropertyData) _data.get(pageName);

        if (data == null)
            return Collections.EMPTY_LIST;

        return data.getPageChanges();
    }

    public void discardStoredChanges(String pageName, IRequestCycle cycle)
    {
        _data.remove(pageName);
    }

    public void addParametersForPersistentProperties(ServiceEncoding encoding, IRequestCycle cycle, boolean post)
    {
        Defense.notNull(encoding, "encoding");
        Defense.notNull(cycle, "cycle");

        Iterator i = _data.entrySet().iterator();
        while (i.hasNext())
        {
            Map.Entry e = (Map.Entry) i.next();

            String pageName = (String) e.getKey();
            PersistentPropertyData data = (PersistentPropertyData) e.getValue();

            ClientPropertyPersistenceScope scope = getScope();

            if (scope.shouldEncodeState(encoding, cycle, pageName, data))
            {
                String parameterName = _scope.constructParameterName(pageName);
                encoding.setParameterValue(parameterName, data.getEncoded());
            }
        }
    }

    public void setRequest(WebRequest request)
    {
        _request = request;
    }

    public ClientPropertyPersistenceScope getScope()
    {
        return _scope;
    }

    public void setScope(ClientPropertyPersistenceScope scope)
    {
        _scope = scope;
    }
}