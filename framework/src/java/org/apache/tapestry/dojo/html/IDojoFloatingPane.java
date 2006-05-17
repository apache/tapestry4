// Copyright May 17, 2006 The Apache Software Foundation
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
package org.apache.tapestry.dojo.html;

import org.apache.tapestry.IAsset;

/**
 * @author andyhot
 * @since 4.1
 */
public interface IDojoFloatingPane extends IDojoContentPane
{
    /** Title of the FloatingPane. */
    String getTitle();

    /** Window icon. */
    IAsset getIcon();

    /** Min height of the FloatingPane. */
    int getMinWidth();

    /** Min width of the FloatingPane. */
    int getMinHeight();

    /** Has Shadow. */
    boolean getHasShadow();

    /** Should contrain to container. */
    boolean getConstrainToContainer();

    /** The taskBar this pane is connected to. */
    Object getTaskBar();
    
    /** Should display close icon. */
    boolean getDisplayCloseAction();   
    
    /** Should display minimize icon. */
    boolean getDisplayMinimizeAction(); 
    
    /** Should display maximize icon. */
    boolean getDisplayMaximizeAction(); 
}
