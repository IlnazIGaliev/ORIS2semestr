package com.itis.oris.orm;

import java.io.Closeable;
import java.util.List;

public interface EntityManager extends Closeable {
    <T> T save(T entity); // insert, update

    void remove(Object entity); // delete ... where id =

    <T> T find(Class<T> entityType, Object key); // select ... where id =

    <T> List<T> findAll(Class<T> entityType); // select ...
}
