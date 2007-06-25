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

import org.apache.hivemind.Location;
import org.apache.hivemind.Registry;
import org.apache.hivemind.impl.RegistryBuilder;
import org.apache.hivemind.service.MethodSignature;
import org.apache.tapestry.spec.IComponentSpecification;
import static org.easymock.EasyMock.expect;
import org.testng.annotations.Test;

import java.lang.reflect.Modifier;
import java.util.Collections;


/**
 * 
 * @author James Carman
 *
 */
@Test(sequential = true)
public class TestAutowireWorker extends BaseEnhancementTestCase
{
    
    private static final String HELLO_SERVICE_PROPERTY = "helloService";
    
    public void test_No_Service() throws Exception
    {
        assertNotAutowired( RegistryBuilder.constructDefaultRegistry() );
    }
    
    public void test_Many_Services() throws Exception
    {        
        assertNotAutowired( buildFrameworkRegistry("autowire-multiple.xml" ) );   
    }
    
    public void test_One_Service() throws Exception
    {
        final Registry registry = buildFrameworkRegistry("autowire-single.xml" );
        Location l = newLocation();
        EnhancementOperation op = newMock(EnhancementOperation.class);
        
        expect(op.findUnclaimedAbstractProperties()).andReturn(Collections.singletonList( HELLO_SERVICE_PROPERTY ));
        expect(op.getPropertyType( HELLO_SERVICE_PROPERTY )).andReturn(HelloService.class);
        expect(op.canClaimAsReadOnlyProperty(HELLO_SERVICE_PROPERTY)).andReturn(true);
        
        IComponentSpecification spec = newMock(IComponentSpecification.class);
        
        expect(spec.getLocation()).andReturn(l);
        expect(spec.getDescription()).andReturn("Component1");
        
        final String fieldName = "_$" + HELLO_SERVICE_PROPERTY;
        final HelloService proxy = ( HelloService )registry.getService( HelloService.class );
        
        expect(op.addInjectedField( fieldName, HelloService.class, proxy )).andReturn( fieldName );
        expect(op.getAccessorMethodName( HELLO_SERVICE_PROPERTY )).andReturn("getHelloService");
        
        op.addMethod(Modifier.PUBLIC, new MethodSignature(HelloService.class, "getHelloService", null,
                null), "return " + fieldName + ";", l);
        op.claimReadonlyProperty( HELLO_SERVICE_PROPERTY );
        
        replay();
        
        final EnhancementWorker worker = ( EnhancementWorker )registry.getService( "tapestry.enhance.AutowireWorker", EnhancementWorker.class );
        worker.performEnhancement( op, spec );
        
        verify();
    }

    private static final String GENERIC_PROPERTY = "genericService";

    public void test_Generic_Service()
    throws Exception
    {
        final Registry registry = buildFrameworkRegistry("autowire-single.xml" );
        Location l = newLocation();
        EnhancementOperation op = newMock(EnhancementOperation.class);

        expect(op.findUnclaimedAbstractProperties()).andReturn(Collections.singletonList( GENERIC_PROPERTY ));
        expect(op.getPropertyType( GENERIC_PROPERTY )).andReturn(SimpleGenericsInterface.class);
        expect(op.canClaimAsReadOnlyProperty(GENERIC_PROPERTY)).andReturn(true);

        IComponentSpecification spec = newMock(IComponentSpecification.class);

        expect(spec.getLocation()).andReturn(l);
        expect(spec.getDescription()).andReturn("Component1");

        final String fieldName = "_$" + GENERIC_PROPERTY;
        final SimpleGenericsInterface proxy = ( SimpleGenericsInterface )registry.getService( SimpleGenericsInterface.class );

        expect(op.addInjectedField( fieldName, SimpleGenericsInterface.class, proxy )).andReturn( fieldName );
        expect(op.getAccessorMethodName( GENERIC_PROPERTY )).andReturn("getGenericService");

        op.addMethod(Modifier.PUBLIC, new MethodSignature(SimpleGenericsInterface.class, "getGenericService", null,
                null), "return " + fieldName + ";", l);
        op.claimReadonlyProperty( GENERIC_PROPERTY );

        replay();

        final EnhancementWorker worker = ( EnhancementWorker )registry.getService( "tapestry.enhance.AutowireWorker", EnhancementWorker.class );
        worker.performEnhancement( op, spec );

        verify();
    }

    private void assertNotAutowired( Registry registry )
    {
        EnhancementOperation op = newMock(EnhancementOperation.class);
        
        expect(op.findUnclaimedAbstractProperties())
        .andReturn(Collections.singletonList( HELLO_SERVICE_PROPERTY ));
        
        expect(op.getPropertyType( HELLO_SERVICE_PROPERTY )).andReturn(HelloService.class);
        
        expect(op.canClaimAsReadOnlyProperty(HELLO_SERVICE_PROPERTY)).andReturn(true);
        
        replay();
        
        final EnhancementWorker worker = ( EnhancementWorker )registry.getService( "tapestry.enhance.AutowireWorker", EnhancementWorker.class );
        
        worker.performEnhancement( op, null);
        
        verify();
    }
    
}
