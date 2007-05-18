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
package org.apache.tapestry.enhance;

import org.apache.commons.logging.Log;
import org.apache.hivemind.Location;
import org.apache.hivemind.internal.Module;
import org.apache.tapestry.spec.IComponentSpecification;

import java.util.Iterator;
import java.util.List;

/**
 * An enhancement worker which automatically injects HiveMind services
 * into pages/components if exactly one service point exists which is
 * compatible with the read-only property's type.
 * 
 */
public class AutowireWorker implements EnhancementWorker
{
    private final Log _log;

    private final Module _module;

    public AutowireWorker( Module module, Log log)
    {
        _module = module;
        _log = log;
    }

    public void performEnhancement( EnhancementOperation op, IComponentSpecification spec )
    {
        final List propertyNames = op.findUnclaimedAbstractProperties();

        for( Iterator i = propertyNames.iterator(); i.hasNext(); ) {
            
            String propertyName = ( String ) i.next();
            
            Class propertyType = op.getPropertyType( propertyName );
            if( propertyType == null )
                propertyType = Object.class;
            
            if (!op.canClaimAsReadOnlyProperty(propertyName))
                continue;

            if( _module.containsService( propertyType )) {
                
                final Object serviceProxy = _module.getService( propertyType );
                final Location location = spec.getLocation();
                
                _log.debug( EnhanceMessages.autowiring( propertyName, spec, serviceProxy ) );
                
                final String fieldName = op.addInjectedField( "_$" + propertyName, propertyType, serviceProxy );
                
                EnhanceUtils.createSimpleAccessor( op, fieldName, propertyName, propertyType, location );
                op.claimReadonlyProperty( propertyName );
            }

        }
    }
}
