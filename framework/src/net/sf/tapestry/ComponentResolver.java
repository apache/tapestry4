package net.sf.tapestry;

import net.sf.tapestry.spec.ComponentSpecification;

/**
 *  Utililtiy class that understands the rules of component types (which
 *  may optionally have a library prefix) and can resolve 
 *  the type to a {@link net.sf.tapestry.INamespace} and a 
 *  {@link net.sf.tapestry.spec.ComponentSpecification}.
 * 
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since NEXT_RELEASE
 *
 **/

public class ComponentResolver
{
    private ISpecificationSource _specificationSource;
    private INamespace _namespace;
    private ComponentSpecification _spec;

    public ComponentResolver(ISpecificationSource source)
    {
        _specificationSource = source;
    }

    /**
     *  Passed the namespace of a container (to resolve the type in)
     *  and the type to resolve, performs the processing.  A "bare type"
     *  (without a library prefix) may be in the containerNamespace,
     *  or the framework namespace
     *  (a search occurs in that order).
     * 
     *  @param containerNamespace namespace that may contain
     *  a library referenced in the type
     *  @param type the component specification
     *  to  find, either a simple name, or prefixed with a library id
     *  (defined for the container namespace)
     * 
     *  @see #getNamespace()
     *  @see #getSpecification()
     *  @throws PageLoaderException if the type cannot be resolved
     * 
     **/

    public void resolve(INamespace containerNamespace, String type) throws PageLoaderException
    {
        // For compatibility with the 1.1 and 1.2 specifications, which allow
        // the component type to be a complete specification path.

        if (type.startsWith("/"))
        {
            _namespace = _specificationSource.getApplicationNamespace();
            _spec = _specificationSource.getComponentSpecification(type);
            return;
        }

        int colonx = type.indexOf(':');

        if (colonx > 0)
        {
            String libraryId = type.substring(0, colonx);
            String simpleType = type.substring(colonx + 1);

            resolve(containerNamespace, libraryId, simpleType);
            return;
        }

        resolve(containerNamespace, null, type);
    }

    /**
     *  Like {@link #resolve(INamespace, String)}, but used when the type has already
     *  been parsed into a library id and a simple type.
     * 
     *  @param containerNamespace namespace that may contain
     *  a library referenced in the type
     *  @param libraryId the library id within the container namespace, or null
     *  @param type the component specification
     *  to  find as a simple name
     *  @throws PageLoaderException if the type cannot be resolved
     * 
     **/

    public void resolve(INamespace containerNamespace, String libraryId, String type) throws PageLoaderException
    {

        if (libraryId != null)
        {
            _namespace = containerNamespace.getChildNamespace(libraryId);

            if (_namespace == null)
                throw new PageLoaderException(
                    Tapestry.getString("ComponentResolver.invalid-library-id", containerNamespace, libraryId));

            _spec = _namespace.getComponentSpecification(type);

            if (_spec == null)
                throw new PageLoaderException(Tapestry.getString("ComponentResolver.invalid-type", _namespace, type));

            return;
        }

        // A bare component type may be in the namespace of the container
        // (typically the application namespace, but possibly a 
        // library namespace).  Check there first and, if not found,
        // check the framework namespace.

        if (containerNamespace.containsAlias(type))
            _namespace = containerNamespace;
        else
            _namespace = _specificationSource.getFrameworkNamespace();

        _spec = _namespace.getComponentSpecification(type);

        if (_spec == null)
            throw new PageLoaderException(Tapestry.getString("ComponentResolver.invalid-type", _namespace, type));
    }

    /**
     *  Returns the namespace for the component most recently resolved
     *  by {@link #resolve(INamespace, String)}.
     * 
     **/

    public INamespace getNamespace()
    {
        return _namespace;
    }

    /**
     *  Returns the specification for the component most recently resolved
     *  by {@link #resolve(INamespace, String)}.
     * 
     **/

    public ComponentSpecification getSpecification()
    {
        return _spec;
    }
}
