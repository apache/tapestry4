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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a new asset. The asset will have the same name as the property (and will be accessible
 * via the "asset:" binding prefix, or using {@link org.apache.tapestry.IComponent#getAsset(String)},
 * with that name).
 * <p>
 * Note: if we ever rename IAsset to Asset, this will cause a naming conflict. Perhaps we should
 * come up with annotation names that are compatible with a potential rename.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */

@Target(
{ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Asset {

    /**
     * The value is the asset path, which may include a prefix to define the module in which it can
     * be resolved.
     */

    String value();
}
