package com.itis.oris.orm;

public interface EntityManager {
    <T> T save(T entity);
    <T> T update(T entity);
    <T> T delete(T entity);
}
