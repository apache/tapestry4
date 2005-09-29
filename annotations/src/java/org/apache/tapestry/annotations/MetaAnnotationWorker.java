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

package org.apache.tapestry.annotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.tapestry.enhance.EnhancementOperation;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * Recognizes the {@link org.apache.tapestry.annotations.Meta} annotation, and converts it into
 * properties on the specification. It is desirable to have meta data in base classes be "merged"
 * with meta data from sub classes, with the sub classes overriding any conflicting elements from
 * the base class. What we do is work our way up the inheritance tree to java.lang.Object and work
 * with each Meta annotation we find.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class MetaAnnotationWorker implements ClassAnnotationEnhancementWorker
{

    public void performEnhancement(EnhancementOperation op, IComponentSpecification spec,
            Class baseClass, Location location)
    {
        List<Meta> metas = assembleMetas(baseClass);

        for (Meta meta : metas)
            applyPropertiesFromMeta(meta, spec, location);
    }

    private List<Meta> assembleMetas(Class baseClass)
    {
        Class searchClass = baseClass;
        List<Meta> result = new ArrayList<Meta>();
        Meta lastMeta = null;

        while (true)
        {
            Meta meta = (Meta) searchClass.getAnnotation(Meta.class);

            // When reach a class that doesn't define or inherit a @Meta, we're done
            if (meta == null)
                break;

            // Cheap approach, based on annotation inheritance, for determining
            // whether a meta is already in the result list

            if (meta != lastMeta)
                result.add(meta);

            searchClass = searchClass.getSuperclass();
        }

        Collections.reverse(result);

        return result;
    }

    private void applyPropertiesFromMeta(Meta meta, IComponentSpecification spec, Location location)
    {
        String[] pairs = meta.value();

        for (String pair : pairs)
        {
            int equalx = pair.indexOf('=');

            // The only big problem is that the location will be the location of the @Meta in
            // the subclass, even if the @Meta with a problem is from a base class.

            if (equalx < 0)
                throw new ApplicationRuntimeException(AnnotationMessages.missingEqualsInMeta(pair),
                        location, null);

            String key = pair.substring(0, equalx);
            String value = pair.substring(equalx + 1);

            spec.setProperty(key, value);
        }
    }
}
