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

import org.apache.tapestry.*;
import org.apache.tapestry.form.Checkbox;
import org.apache.tapestry.form.TextField;
import org.apache.tapestry.html.BasePage;

import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Map;

/**
 * Used by {@link org.apache.tapestry.annotations.AnnotationEnhancementWorkerTest}. Also a chance
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

    @Persist
    @InitialValue("user.naturalName")
    public abstract int getPersistentPropertyWithInitialValue();

    @InjectAsset("stylesheet")
    public abstract IAsset getStylesheetAsset();
    
    @InjectAsset("homageDeFred")
    public abstract IAsset getUnknownAsset();
    
    @InjectComponent("fred")
    public abstract TextField getFredField();

    @InjectState("barneyASO")
    public abstract Map getBarney();

    @InjectStateFlag("barneyASO")
    public abstract boolean getBarneyExists();
    
    @InjectState
    public abstract Map getMyVisit();

    @InjectStateFlag
    public abstract boolean getMyVisitExists();    

    @Parameter
    public abstract String getSimpleParameter();

    @InjectPage("SomePageName")
    public abstract IPage getMyPage();
    
    @Component
    public abstract TextField getUsernameField();

    @Component(type = "TextField")
    public abstract TextField getTextField();

    @Component(type = "Checkbox", id = "email")
    public abstract Checkbox getEmailField();

    @Component(type = "TextField", inheritInformalParameters = true)
    public abstract IComponent getInherit();

    @Component(type = "Conditional", bindings =
    { "condition=message", "element=div" })
    public abstract IComponent getComponentWithBindings();

    @Component(type = "Conditional", bindings =
    { "condition=message", "element=div" }, inheritedBindings = {"title=pageTitle", "email"})
    public abstract IComponent getComponentWithInheritedBindings();

    @Component(type = "TextField", bindings =
    { "value = email", "displayName = message:email-label" })
    public abstract IComponent getWhitespace();
    
    @Component(id = "anEmailCopy", copyOf = "email", type = "Checkbox")
    public abstract IComponent getInvalidEmailCopy();
    
    @Component(id = "aComponentCopy", copyOf = "componentWithBindings")
    public abstract IComponent getComponentWithBindingsCopy();

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
    
    @InjectMeta
    public abstract String getPageTitle();    

    @InjectScript("foo.script")
    public abstract IScript getScript();
    
    @InitialValue("fred")
    public abstract int getPropertyWithInitialValue();
    
    @EventListener(events = { "onClick" }, targets = { "email" }, 
            elements = { "foo" })
    public void eventListener() { }
    
    @EventListener(events = { "onClick" })
    public void brokenTargetListener() { }
    
    @Component(type = "Form", id = "testForm")
    public abstract IForm getForm();
    
    @EventListener(events = { "onClick" }, targets = { "email" }, submitForm = "testForm", focus=true)
    public void formListener() { }
    
    @EventListener(events = { "onClick" }, targets = { "phone" }, submitForm = "testForm")
    public void anotherFormListener() { }
    
    @EventListener(events = { "onClick" }, targets = { "phone" }, submitForm = "form")
    public void yetAnotherFormListener() { }
    
    @EventListener(events = { "onClick" }, targets = { "email" }, submitForm = "notExisting")
    public void brokenFormListener() { }
    
    @EventListener(targets = "foo", events = "onchange", async = false)
    public void submitForm() {}

    @InitialValue("literal:5")
    public abstract int getDefaultPageSize();
    
    @Persist
    public abstract SimpleBean getBean();
    
    @Persist
    public abstract SubSimpleBean getSubBean();

    @Asset("images/test-asset.txt")
    public abstract IAsset getTextAsset();
}
