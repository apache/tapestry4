// Copyright May 2, 2006 The Apache Software Foundation
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

import java.lang.reflect.Modifier;
import java.util.Collections;

import org.apache.hivemind.Location;
import org.apache.hivemind.Registry;
import org.apache.hivemind.impl.RegistryBuilder;
import org.apache.hivemind.service.MethodSignature;
import org.apache.tapestry.spec.IComponentSpecification;
import org.easymock.MockControl;


/**
 * 
 * @author James Carman
 *
 */
public class TestAutowireWorker extends BaseEnhancementTestCase
{

    private static final String HELLO_SERVICE_PROPERTY = "helloService";

    public void testWithNoService() throws Exception
    {
        assertNotAutowired( RegistryBuilder.constructDefaultRegistry() );
    }
    
    public void testWithManyServices() throws Exception
    {        
        assertNotAutowired( buildFrameworkRegistry("autowire-multiple.xml" ) );   
    }
    
    public void testWithOneService() throws Exception
    {
        final Registry registry = buildFrameworkRegistry("autowire-single.xml" );
        Location l = newLocation();
        MockControl opControl = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) opControl.getMock();
        
        op.findUnclaimedAbstractProperties();
        opControl.setReturnValue( Collections.singletonList( HELLO_SERVICE_PROPERTY ) );
        
        op.getPropertyType( HELLO_SERVICE_PROPERTY );
        opControl.setReturnValue( HelloService.class );
        
        op.canClaimAsReadOnlyProperty(HELLO_SERVICE_PROPERTY);
        opControl.setReturnValue(true);
        
        MockControl specControl = newControl( IComponentSpecification.class );
        IComponentSpecification spec = ( IComponentSpecification )specControl.getMock();
        
        spec.getLocation();
        specControl.setReturnValue( l );
        
        spec.getDescription();
        specControl.setReturnValue( "Component1" );
        
        final String fieldName = "_$" + HELLO_SERVICE_PROPERTY;
        final HelloService proxy = ( HelloService )registry.getService( HelloService.class );
        
        op.addInjectedField( fieldName, HelloService.class, proxy );
        opControl.setReturnValue( fieldName );
        
        op.getAccessorMethodName( HELLO_SERVICE_PROPERTY );
        opControl.setReturnValue( "getHelloService" );
        
        op.addMethod(Modifier.PUBLIC, new MethodSignature(HelloService.class, "getHelloService", null,
                null), "return " + fieldName + ";", l);
        op.claimReadonlyProperty( HELLO_SERVICE_PROPERTY );
        opControl.setVoidCallable();
        
        replayControls();
        
        final EnhancementWorker worker = ( EnhancementWorker )registry.getService( "tapestry.enhance.AutowireWorker", EnhancementWorker.class );
        worker.performEnhancement( op, spec );
        
        verifyControls();
    }
    
    private void assertNotAutowired( Registry registry )
    {
        MockControl opControl = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) opControl.getMock();
        
        op.findUnclaimedAbstractProperties();
        opControl.setReturnValue( Collections.singletonList( HELLO_SERVICE_PROPERTY ) );
        
        op.getPropertyType( HELLO_SERVICE_PROPERTY );
        opControl.setReturnValue( HelloService.class );
        
        op.canClaimAsReadOnlyProperty(HELLO_SERVICE_PROPERTY);
        opControl.setReturnValue(true);
        
        replayControls();
        
        final EnhancementWorker worker = ( EnhancementWorker )registry.getService( "tapestry.enhance.AutowireWorker", EnhancementWorker.class );
        worker.performEnhancement( op, null);
        
        verifyControls();
    }
    
}
