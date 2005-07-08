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
import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.annotations.Message;

/**
 * Target "component" used to test the
 * {@link org.apache.tapestry.annotations.MessageAnnotationWorker}.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public abstract class MessagesTarget extends AbstractComponent
{
    @Message
    public abstract String noParams();

    @Message
    public abstract String objectParam(String bar);

    @Message
    public abstract String primitiveParam(int bar);
}
