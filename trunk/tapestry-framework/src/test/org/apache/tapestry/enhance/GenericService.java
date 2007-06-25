package org.apache.tapestry.enhance;

/**
 *
 */
public interface GenericService<E> {

    void doFoo(E bar);

    E getCurrentFoo();
}
