package org.apache.tapestry.vlib.services;

import org.apache.tapestry.form.IPropertySelectionModel;

/**
 * Source for some common Virtual Library property selection models.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public interface ModelSource
{
    /**
     * Returns a model of the known Publishers, in alphabetical order.
     * 
     * @see org.apache.tapestry.vlib.ejb.IOperations#getPublishers()
     */
    IPropertySelectionModel getPublisherModel();

    /**
     * Returns a model of the known Users, in ascending alphbetical order (by last name).
     * 
     * @see org.apache.tapestry.vlib.ejb.IOperations#getPersons()
     */

    IPropertySelectionModel getPersonModel();

    /**
     * Returns a model of the known users, prefixed with an empty label option (used when selecting
     * a person is optional).
     */

    IPropertySelectionModel getOptionalPersonModel();

    /**
     * Clears all cached information; this is invoked after a change to the underlying person or
     * publisher data, or after any kind of exception accessing the backend.
     */

    void clear();
}
