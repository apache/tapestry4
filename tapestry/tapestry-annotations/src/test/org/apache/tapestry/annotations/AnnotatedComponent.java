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

import org.apache.tapestry.BaseComponent;

/**
 * Used by {@link TestParameterAnnotationWorker}.
 * 
 * @author Andreas Andreou
 * @since 4.1.1
 */
public abstract class AnnotatedComponent extends BaseComponent
{
    @Parameter
    public abstract String getSimpleParameter();

    @Parameter(required = true)
    public abstract String getRequiredParameter();

    @Parameter(cache = false)
    public abstract Object getNonCachedParameter();

    @Parameter(aliases = "fred")
    public abstract String getAliasedParameter();

    @Parameter
    @Deprecated
    public abstract int getDeprecatedParameter();

    @Parameter(name = "fred")
    public abstract double getNamedParameter();

    @Parameter(defaultValue = "myDefault")
    public abstract String getDefaultValue();    
}
