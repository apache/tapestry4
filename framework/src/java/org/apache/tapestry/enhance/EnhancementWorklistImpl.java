// Copyright 2004 The Apache Software Foundation
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

import java.util.ArrayList;
import java.util.List;

import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.service.ClassFab;
import org.apache.hivemind.service.ClassFactory;

/**
 *
 *  @author Mindbridge
 *  @since 3.0
 *
 */
public class EnhancementWorklistImpl implements EnhancementWorklist
{
    /**
     *  List of {@link IEnhancer}.
     * 
     */
    private List _enhancers;

    private String _className;
    private Class _parentClass;
    private ClassResolver _classResolver;
    private ClassFactory _classFactory;

    public EnhancementWorklistImpl(
        String className,
        Class parentClass,
        ClassResolver classResolver,
        ClassFactory classFactory)
    {
        _className = className;
        _parentClass = parentClass;
        _classResolver = classResolver;
        _classFactory = classFactory;
    }

    public String getClassName()
    {
        return _className;
    }

    public Class createEnhancedSubclass()
    {
        ClassFab classFab =
            _classFactory.newClass(_className, _parentClass, _classResolver.getClassLoader());

        performEnhancement(classFab);

        return classFab.createClass();
    }

    protected List getEnhancers()
    {
        return _enhancers;
    }

    public void addEnhancer(IEnhancer enhancer)
    {
        if (_enhancers == null)
            _enhancers = new ArrayList();

        _enhancers.add(enhancer);
    }

    public boolean hasModifications()
    {
        return _enhancers != null && !_enhancers.isEmpty();
    }

    public void performEnhancement(ClassFab classFab)
    {
        List enhancers = getEnhancers();

        if (enhancers == null)
            return;

        int count = enhancers.size();

        for (int i = 0; i < count; i++)
        {
            IEnhancer enhancer = (IEnhancer) enhancers.get(i);

            enhancer.performEnhancement(classFab);
        }
    }

}
