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

package org.apache.tapestry;

import org.apache.hivemind.Resource;

/**
 * Defines methods needed by a {@link org.apache.tapestry.IScript}to execute.
 * 
 * @author Howard Lewis Ship
 * @since 3.0
 * @see org.apache.tapestry.html.Body
 */

public interface IScriptProcessor
{
    /**
     * Adds scripting code to the main body. During the render, multiple scripts may render multiple
     * bodies; all are concatinated together to form a single block. The
     * {@link org.apache.tapestry.html.Body}&nbsp;component will write the body script contents
     * just inside the <code>&lt;body&gt;</code> tag.
     * 
     * @deprecated To be removed sometime after 4.1.
     * @see {@link #addBodyScript(IComponent, String)}
     */

    void addBodyScript(String script);

    /**
     * Adds scripting code to the main body. During the render, multiple scripts may render multiple
     * bodies; all are concatinated together to form a single block. The
     * {@link org.apache.tapestry.html.Body}&nbsp;component will write the body script contents
     * just inside the <code>&lt;body&gt;</code> tag.
     * 
     * @param target
     *          The component this script is being added for.
     * @param script
     *          The script to add to the body response.
     */
    void addBodyScript(IComponent target, String script);
    
    /**
     * Adds initialization script. Initialization script is executed once, when the containing page
     * loads. Initialization script content is written only after all HTML content that could be
     * referenced from the script (in effect, just before the <code>&lt/body&gt;</code> tag).
     * 
     * @deprecated To be removed sometime after 4.1.
     * @see {@link #addInitializationScript(IComponent, String)}
     */
    void addInitializationScript(String script);

    /**
     * Adds initialization script. Initialization script is executed once, when the containing page
     * loads. Initialization script content is written only after all HTML content that could be
     * referenced from the script (in effect, just before the <code>&lt/body&gt;</code> tag).
     * 
     * @param target
     *          The component the script is being added for.
     * @param script
     *          The script to add.
     */
    void addInitializationScript(IComponent target, String script);

    /**
     * Works in the same way as {@link #addInitializationScript(IComponent, String)} - except this
     * method causes the script being added to appear <em>after</em> all of the script content written out
     * from the normal initialization script processing happens.  This is useful if you have some initialization
     * script logic that absolutely must happen at the very end of the rest of things.
     *
     * @see {@link #addInitializationScript(IComponent, String)}.
     * 
     * @param target
     *          The component the script is being added for.
     * @param script
     *          The script to add.
     */
    void addScriptAfterInitialization(IComponent target, String script);

    /**
     * Adds an external script. The processor is expected to ensure that external scripts are only
     * loaded a single time per page.
     * 
     * @deprecated To be removed sometime after 4.1
     * @see {@link #addExternalScript(IComponent, Resource)}
     */

    void addExternalScript(Resource resource);

    /**
     * Adds an external script. The processor is expected to ensure that external scripts are only
     * loaded a single time per page. The target will be checked to filter the scripts
     * added for those types of responses that require them.
     * 
     * @param target
     *          The component the script is being added for.
     * @param resource
     *          The external script to add.
     */
    void addExternalScript(IComponent target, Resource resource);
    
    /**
     * Determines if the specified component should have its javascript 
     * body added to the response.
     * 
     * @param target
     *          The component to allow/disallow body script content from.
     * @return True if the component script should be allowed.
     */
    boolean isBodyScriptAllowed(IComponent target);
    
    /**
     * Determines if the specified component should have its javascript 
     * initialization added to the response.
     * 
     * @param target
     *          The component to allow/disallow initialization script content from.
     * @return True if the component script should be allowed.
     */
    boolean isInitializationScriptAllowed(IComponent target);
    
    /**
     * Determines if the specified component should have its javascript 
     * external resource scripts added to the response.
     * 
     * @param target
     *          The component to check for inclusion/exclusion.
     * @return True if external scripts from this component should be added to
     *          the response.
     */
    boolean isExternalScriptAllowed(IComponent target);
    
    /**
     * Ensures that the given string is unique. The string is either returned unchanged, or a suffix
     * is appended to ensure uniqueness.
     */

    String getUniqueString(String baseValue);
}
