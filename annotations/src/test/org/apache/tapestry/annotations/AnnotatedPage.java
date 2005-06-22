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

import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry.IAsset;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IScript;
import org.apache.tapestry.form.Checkbox;
import org.apache.tapestry.form.TextField;
import org.apache.tapestry.html.BasePage;

/**
 * Used by {@link org.apache.tapestry.annotations.TestAnnotationEnhancementWorker}. Also a chance
 * to try each of the annotations out.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public abstract class AnnotatedPage extends BasePage
{
    @Asset("/style/global.css")
    public abstract IAsset getGlobalStylesheet();

    @InjectObject("barney")
    public abstract Object getInjectedObject();

    @Bean
    public abstract HashMap getHashMapBean();

    @Bean(HashMap.class)
    public abstract Map getMapBean();

    @Bean(initializer = "intValue=10")
    public abstract Target getBeanWithInitializer();

    @Bean(value = HashMap.class, lifecycle = Lifecycle.RENDER)
    public abstract Map getRenderLifecycleBean();

    @Persist
    public abstract int getPersistentProperty();

    @Persist("client")
    public abstract String getClientPersistentProperty();

    @InjectAsset("stylesheet")
    public abstract IAsset getStylesheetAsset();

    @InjectComponent("fred")
    public abstract TextField getFredField();

    @InjectState("barneyASO")
    public abstract Map getBarney();

    @Parameter
    public abstract String getSimpleParameter();

    @Parameter(required = true)
    public abstract String getRequiredParameter();

    @Parameter(defaultBinding = "bean")
    public abstract Object getBeanDefaultParameter();

    @Parameter(cache = false)
    public abstract Object getNonCachedParameter();

    @Parameter(aliases = "fred")
    public abstract String getAliasedParameter();

    @Parameter
    @Deprecated
    public abstract int getDeprecatedParameter();

    @Parameter(name = "fred")
    public abstract double getNamedParameter();

    @InjectPage("SomePageName")
    public abstract IPage getMyPage();

    @Component(type = "TextField")
    public abstract TextField getTextField();

    @Component(type = "Checkbox", id = "email")
    public abstract Checkbox getEmailField();

    @Component(type = "TextField", inheritInformalParameters = true)
    public abstract IComponent getInherit();

    @Component(type = "Conditional", bindings =
    { "condition=message", "element=div" })
    public abstract IComponent getComponentWithBindings();

    @Component(type = "TextField", bindings =
    { "value = email", "displayName = message:email-label" })
    public abstract IComponent getWhitespace();

    @Message
    public abstract String noArgsMessage();

    @Message("message-key")
    public abstract String messageWithSpecificKey();

    @Message
    public abstract String messageWithParameters(String foo, Map bar);

    @Message
    public abstract String messageWithPrimitives(int foo, double bar);

    @Message
    public abstract void voidMessage();

    @Message
    public abstract String getLikeGetter();

    @InjectMeta("fred")
    public abstract String getMetaFred();

    @InjectScript("foo.script")
    public abstract IScript getScript();
}
