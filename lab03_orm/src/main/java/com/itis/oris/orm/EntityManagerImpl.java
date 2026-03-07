package com.itis.oris.orm;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.io.Closeable;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EntityManagerImpl implements EntityManager, Closeable {

    private final Connection connection;

    public EntityManagerImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Field getIdField(Class<?> clazz) {

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                field.setAccessible(true);
                return field;
            }
        }
        throw new RuntimeException();
    }

    @Override
    public <T> T save(T entity) {

        try {

            Class<?> clazz = entity.getClass();
            String table = clazz.getSimpleName().toLowerCase();

            Field idField = getIdField(clazz);
            Object id = idField.get(entity);

            if (id == null) {
                insert(entity, clazz, table, idField);
            } else {
                update(entity, clazz, table, idField);
            }

            return entity;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <T> void insert(T entity, Class<?> clazz, String table, Field idField) {

        try {

            List<String> columns = new ArrayList<>();
            List<Object> values = new ArrayList<>();

            for (Field field : clazz.getDeclaredFields()) {

                field.setAccessible(true);

                if (field.isAnnotationPresent(Column.class)) {

                    columns.add(field.getName());
                    values.add(field.get(entity));

                } else if (field.isAnnotationPresent(ManyToOne.class)) {

                    Object ref = field.get(entity);

                    if (ref != null) {

                        Field refId = getIdField(ref.getClass());

                        columns.add(field.getName() + "_id");
                        values.add(refId.get(ref));
                    }
                }
            }

            String columnPart = String.join(",", columns);
            String placeholders = String.join(",", columns.stream().map(c -> "?").toList());

            String sql = "insert into " + table +
                    " (" + columnPart + ") values (" + placeholders + ") returning id";

            try (PreparedStatement ps = connection.prepareStatement(sql)) {

                for (int i = 0; i < values.size(); i++) {
                    ps.setObject(i + 1, values.get(i));
                }

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        idField.set(entity, rs.getObject(1));
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <T> void update(T entity, Class<?> clazz, String table, Field idField) {

        try {

            List<String> columns = new ArrayList<>();
            List<Object> values = new ArrayList<>();

            for (Field field : clazz.getDeclaredFields()) {

                field.setAccessible(true);

                if (field.isAnnotationPresent(Column.class)) {

                    columns.add(field.getName() + "=?");
                    values.add(field.get(entity));

                } else if (field.isAnnotationPresent(ManyToOne.class)) {

                    Object ref = field.get(entity);

                    if (ref != null) {

                        Field refId = getIdField(ref.getClass());

                        columns.add(field.getName() + "_id=?");
                        values.add(refId.get(ref));
                    }
                }
            }

            String setPart = String.join(",", columns);

            String sql = "update " + table +
                    " set " + setPart +
                    " where id=?";

            try (PreparedStatement ps = connection.prepareStatement(sql)) {

                for (int i = 0; i < values.size(); i++) {
                    ps.setObject(i + 1, values.get(i));
                }

                ps.setObject(values.size() + 1, idField.get(entity));

                ps.executeUpdate();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(Object entity) {

        try {

            Class<?> clazz = entity.getClass();
            String table = clazz.getSimpleName().toLowerCase();

            Field idField = getIdField(clazz);
            Object id = idField.get(entity);

            String sql = "delete from " + table + " where id=?";

            try (PreparedStatement ps = connection.prepareStatement(sql)) {

                ps.setObject(1, id);
                ps.executeUpdate();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T find(Class<T> entityType, Object key) {

        try {

            String table = entityType.getSimpleName().toLowerCase();
            String sql = "select * from " + table + " where id=?";

            try (PreparedStatement ps = connection.prepareStatement(sql)) {

                ps.setObject(1, key);

                try (ResultSet rs = ps.executeQuery()) {

                    if (!rs.next()) {
                        return null;
                    }

                    T entity = entityType.getDeclaredConstructor().newInstance();

                    for (Field field : entityType.getDeclaredFields()) {

                        field.setAccessible(true);

                        if (field.isAnnotationPresent(Id.class)) {

                            field.set(entity, rs.getObject("id"));

                        } else if (field.isAnnotationPresent(Column.class)) {

                            field.set(entity, rs.getObject(field.getName()));
                        }
                    }

                    return entity;
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> List<T> findAll(Class<T> entityType) {

        List<T> result = new ArrayList<>();

        try {

            String table = entityType.getSimpleName().toLowerCase();
            String sql = "select * from " + table;

            try (Statement st = connection.createStatement();
                 ResultSet rs = st.executeQuery(sql)) {

                while (rs.next()) {

                    T entity = entityType.getDeclaredConstructor().newInstance();

                    for (Field field : entityType.getDeclaredFields()) {

                        field.setAccessible(true);

                        if (field.isAnnotationPresent(Id.class)) {

                            field.set(entity, rs.getObject("id"));

                        } else if (field.isAnnotationPresent(Column.class)) {

                            field.set(entity, rs.getObject(field.getName()));
                        }
                    }

                    result.add(entity);
                }
            }

            return result;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}