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
package org.apache.tapestry.enhance;

import javassist.CtClass;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.service.ClassFab;
import org.apache.hivemind.service.ClassFactory;
import org.apache.hivemind.service.InterfaceFab;

/**
 * Implementation of the hivemind core {@link ClassFactory} service to get around some incompatibilities 
 * the current 1.1.1 implementation of hivemind has with the latest (3.4) version of javassist.
 * 
 * @author jkuhnert
 */
public class ClassFactoryImpl implements ClassFactory
{
    static final int EXPIRED_CLASS_COUNT = 100;
    
    /**
     * ClassPool shared by all modules (all CtClassSource instances).
     */
    private HiveMindClassPool _pool = new HiveMindClassPool();

    private CtClassSource _classSource = new CtClassSource(_pool);

    private int _classCounter = 0;
    
    public ClassFab newClass(String name, Class superClass)
    {
        try
        {
            checkPoolExpiration();
            
            CtClass ctNewClass = _classSource.newClass(name, superClass);
            
            return new ClassFabImpl(_classSource, ctNewClass);
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(EnhanceMessages.unableToCreateClass(
                    name,
                    superClass,
                    ex), ex);
        }
    }

    /** @since 1.1 */

    public InterfaceFab newInterface(String name)
    {
        try
        {
            checkPoolExpiration();
            
            CtClass ctNewClass = _classSource.newInterface(name);

            return new InterfaceFabImpl(_classSource, ctNewClass);
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(
                    EnhanceMessages.unableToCreateInterface(name, ex), ex);
        }

    }
    
    void checkPoolExpiration()
    {
        _classCounter++;
        
        synchronized(_classSource) {
            if (_classCounter >= EXPIRED_CLASS_COUNT) {
                _pool.clearImportedPackages();
                
                _classCounter = 0;
            }
        }
    }
}
