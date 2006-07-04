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

import java.util.List;

import org.apache.hivemind.Locatable;
import org.apache.hivemind.Resource;
import org.apache.tapestry.engine.IPropertySource;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.ILibrarySpecification;

/**
 * Organizes different libraries of Tapestry pages, components and services into "frameworks", used
 * to disambiguate names.
 * <p>
 * Tapestry release 3.0 includes dynamic discovery of pages and components; an application or
 * library may contain a page or component that won't be "known" until the name is resolved (because
 * it involves searching for a particular named file).
 * <p>
 * A namespace implements {@link org.apache.tapestry.engine.IPropertySource}, exposing the
 * properties provided in the namespace's specification.
 * 
 * @see org.apache.tapestry.resolver.PageSpecificationResolver
 * @see org.apache.tapestry.resolver.ComponentSpecificationResolver
 * @author Howard Lewis Ship
 * @since 2.2
 */

public interface INamespace extends Locatable, IPropertySource
{
    /**
     * Reserved name of a the implicit Framework library.
     */

    String FRAMEWORK_NAMESPACE = "framework";

    /**
     * Reserved name for the implicit (root) application namespace. Use of this prefix allows page
     * or component defined in the application to be referenced from a library. Is this a good
     * thing? In rare cases, yes. Is it subject to severe abuse? Yes.
     * 
     * @since 4.0
     */

    String APPLICATION_NAMESPACE = "application";

    /**
     * Character used to seperate the namespace prefix from the page name or component type.
     * 
     * @since 2.3
     */

    char SEPARATOR = ':';

    /**
     * Returns an identifier for the namespace. Identifiers are simple names (they start with a
     * letter, and may contain letters, numbers, underscores and dashes). An identifier must be
     * unique among a namespaces siblings.
     * <p>
     * The application namespace has a null id; the framework namespace has an id of "framework".
     */

    String getId();

    /**
     * Returns the extended id for this namespace, which is a dot-seperated sequence of ids.
     */

    String getExtendedId();

    /**
     * Returns a version of the extended id appropriate for error messages. This is the based on
     * {@link #getExtendedId()}, unless this is the application or framework namespace, in which
     * case special strings are returned.
     * 
     * @since 3.0
     */

    String getNamespaceId();

    /**
     * Returns the parent namespace; the namespace which contains this namespace.
     * <p>
     * The application and framework namespaces return null as the parent.
     */

    INamespace getParentNamespace();

    /**
     * Returns a namespace contained by this namespace.
     * 
     * @param id
     *            either a simple name (of a directly contained namespace), or a dot-separated name
     *            sequence
     * @return the child namespace
     * @throws ApplicationRuntimeException
     *             if no such namespace exist.
     */

    INamespace getChildNamespace(String id);

    /**
     * Returns a sorted, immutable list of the ids of the immediate children of this namespace. May
     * return the empty list, but won't return null.
     */

    List getChildIds();

    /**
     * Returns the page specification of the named page (defined within the namespace).
     * 
     * @param name
     *            the name of the page
     * @return the specification
     * @throws ApplicationRuntimeException
     *             if the page specification doesn't exist or can't be loaded
     */

    IComponentSpecification getPageSpecification(String name);

    /**
     * Returns true if this namespace contains the specified page name.
     */

    boolean containsPage(String name);

    /**
     * Returns a sorted list of page names. May return an empty list, but won't return null. The
     * return list is immutable.
     */

    List getPageNames();

    /**
     * Returns the path for the named component (within the namespace).
     * 
     * @param type
     *            the component type
     * @return the specification for the component
     * @throws ApplicationRuntimeException
     *             if the specification doesn't exist or can't be loaded
     */

    IComponentSpecification getComponentSpecification(String type);

    /**
     * Returns true if the namespace contains the indicated component type.
     * 
     * @param type
     *            a simple component type (no namespace prefix is allowed)
     */

    boolean containsComponentType(String type);

    /**
     * Returns the {@link org.apache.tapestry.spec.LibrarySpecification}from which this namespace
     * was created.
     */

    ILibrarySpecification getSpecification();

    /**
     * Constructs a qualified name for the given simple page name by applying the correct prefix (if
     * any).
     * 
     * @since 2.3
     */

    String constructQualifiedName(String pageName);

    /**
     * Returns the location of the resource from which the specification for this namespace was
     * read.
     */

    Resource getSpecificationLocation();

    /**
     * Returns true if the namespace is the special application namespace (which has special search
     * rules for handling undeclared pages and components).
     * 
     * @since 3.0
     */

    boolean isApplicationNamespace();

    /**
     * Used to specify additional pages beyond those that came from the namespace's specification.
     * This is used when pages in the application namespace are dynamically discovered.
     * 
     * @since 3.0
     */

    void installPageSpecification(String pageName, IComponentSpecification specification);

    /**
     * Used to specify additional components beyond those that came from the namespace's
     * specification. This is used when components in the application namespace are dynamically
     * discovered.
     * 
     * @since 3.0
     */

    void installComponentSpecification(String type, IComponentSpecification specification);

}
