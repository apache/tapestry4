//  Copyright 2004 The Apache Software Foundation
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

import java.util.List;

import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.ILibrarySpecification;

/**
 *  Organizes different libraries of Tapestry pages, components
 *  and services into "frameworks", used to disambiguate names.
 * 
 *  <p>
 *  Tapestry release 3.0 includes dynamic discovery of pages and components; an application
 *  or library may contain a page or component that won't be "known" until the name
 *  is resolved (because it involves searching for a particular named file).
 * 
 *  @see org.apache.tapestry.resolver.PageSpecificationResolver
 *  @see org.apache.tapestry.resolver.ComponentSpecificationResolver
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 *
 **/

public interface INamespace extends ILocatable
{
    /**
     *  Reserved name of a the implicit Framework library.
     * 
     **/

    public static final String FRAMEWORK_NAMESPACE = "framework";

    /**
     *  Character used to seperate the namespace prefix from the page name
     *  or component type.
     * 
     *  @since 2.3
     * 
     **/

    public static final char SEPARATOR = ':';

    /**
     *  Returns an identifier for the namespace.  Identifiers
     *  are simple names (they start with a letter,
     *  and may contain letters, numbers, underscores and dashes).
     *  An identifier must be unique among a namespaces siblings.
     * 
     *  <p>The application namespace has a null id; the framework
     *  namespace has an id of "framework".
     * 
     **/

    public String getId();

    /**
     *  Returns the extended id for this namespace, which is
     *  a dot-seperated sequence of ids.
     * 
     **/

    public String getExtendedId();

    /**
     *  Returns a version of the extended id appropriate for error
     *  messages.  This is the based on
     *  {@link #getExtendedId()}, unless this is the
     *  application or framework namespace, in which case
     *  special strings are returned.
     *  
     *  @since 3.0
     * 
     **/

    public String getNamespaceId();

    /**
     *  Returns the parent namespace; the namespace which
     *  contains this namespace.
     * 
     *  <p>
     *  The application and framework namespaces return null
     *  as the parent.
     * 
     **/

    public INamespace getParentNamespace();

    /**
     *  Returns a namespace contained by this namespace.
     * 
     *  @param id either a simple name (of a directly contained namespace),
     *  or a dot-seperarated name sequence.
     *  @return the child namespace
     *  @throws ApplicationRuntimeException if no such namespace exist.
     * 
     **/

    public INamespace getChildNamespace(String id);

    /**
     *  Returns a sorted, immutable list of the ids of the immediate
     *  children of this namespace.  May return the empty list,
     *  but won't return null.
     * 
     **/

    public List getChildIds();

    /**
     *  Returns the page specification of the named
     *  page (defined within the namespace).
     * 
     *  @param name the name of the page
     *  @return the specification
     *  @throws ApplicationRuntimeException if the page specification
     *  doesn't exist or can't be loaded
     * 
     **/

    public IComponentSpecification getPageSpecification(String name);

    /**
     *  Returns true if this namespace contains the specified
     *  page name.
     * 
     **/

    public boolean containsPage(String name);

    /**
     *  Returns a sorted list of page names.  May return an empty
     *  list, but won't return null.  The return list is immutable.
     * 
     **/

    public List getPageNames();

    /**
     *  Returns the path for the named component (within the namespace).
     * 
     *  @param type the component alias
     *  @return the specification path of the component
     *  @throws ApplicationRuntimeException if the specification
     *  doesn't exist or can't be loaded
     * 
     **/

    public IComponentSpecification getComponentSpecification(String type);

    /**
     *  Returns true if the namespace contains the indicated component type.
     * 
     *  @param type a simple component type (no namespace prefix is allowed)
     *
     **/

    public boolean containsComponentType(String type);

    /**
     *  Returns a sorted list of component types.  May return 
     *  an empty list, but won't return null.  The return list
     *  is immutable.  Represents just the known component types
     *  (additional types may be discoverred dynamically).
     * 
     *  <p>Is this method even needed?
     * 
     *  @since 3.0
     * 
     **/

    public List getComponentTypes();

    /**
     *  Returns the class name of a service provided by the
     *  namespace.
     * 
     *  @param name the name of the service.
     *  @return the complete class name of the service, or null
     *  if the namespace does not contain the named service.
     * 
     **/

    public String getServiceClassName(String name);

    /**
     *  Returns the names of all services provided by the
     *  namespace, as a sorted, immutable list.  May return
     *  the empty list, but won't return null.
     * 
     **/

    public List getServiceNames();

    /**
     *  Returns the {@link org.apache.tapestry.spec.LibrarySpecification} from which
     *  this namespace was created.
     * 
     **/

    public ILibrarySpecification getSpecification();

    /**
     *  Constructs a qualified name for the given simple page name by
     *  applying the correct prefix (if any).
     * 
     *  @since 2.3
     * 
     **/

    public String constructQualifiedName(String pageName);

    /**
     *  Returns the location of the resource from which the
     *  specification for this namespace was read.
     * 
     **/

    public IResourceLocation getSpecificationLocation();

    /**
     *  Returns true if the namespace is the special
     *  application namespace (which has special search rules
     *  for handling undeclared pages and components).
     * 
     *  @since 3.0
     * 
     **/

    public boolean isApplicationNamespace();

    /**
     *  Used to specify additional pages beyond those that came from
     *  the namespace's specification.  This is used when pages
     *  in the application namespace are dynamically discovered.
     * 
     *  @since 3.0
     * 
     **/

    public void installPageSpecification(String pageName, IComponentSpecification specification);

    /**
     *  Used to specify additional components beyond those that came from
     *  the namespace's specification.  This is used when components
     *  in the application namespace are dynamically discovered.
     * 
     *  @since 3.0
     * 
     **/

    public void installComponentSpecification(String type, IComponentSpecification specification);

}
