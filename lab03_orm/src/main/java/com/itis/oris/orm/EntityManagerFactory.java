package com.itis.oris.orm;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.io.Closeable;
import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EntityManagerFactory implements Closeable {

    private HikariDataSource dataSource;

    private List<Class<?>> entities = new ArrayList<>();

    public EntityManagerFactory() {

        try {

            Class.forName("org.postgresql.Driver");

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:postgresql://localhost:5432/testing_db");
            config.setUsername("postgres");
            config.setPassword("123");
            config.setMaximumPoolSize(10);

            dataSource = new HikariDataSource(config);

            // сканируем entity
            entities = scanEntities("com.itis.oris.model");

            // создаем таблицы
            createTables();

            validateSchema();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public EntityManager getEntityManager() {

        try {
            Connection connection = dataSource.getConnection();
            return new EntityManagerImpl(connection);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        dataSource.close();
    }

    private List<Class<?>> scanEntities(String packageName) {

        List<Class<?>> entities = new ArrayList<>();

        try {

            ClassLoader loader = getClass().getClassLoader();

            String path = packageName.replace('.', '/');

            URL resource = loader.getResource(path);

            if (resource == null) return entities;

            File directory = new File(resource.toURI());

            for (File file : directory.listFiles()) {

                if (!file.getName().endsWith(".class")) continue;

                String className = packageName + "." + file.getName().replace(".class", "");

                Class<?> clazz = Class.forName(className);

                if (clazz.isAnnotationPresent(Entity.class)) {
                    entities.add(clazz);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return entities;
    }

    private void createTables() {

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            // 1. сначала таблицы без foreign key
            for (Class<?> entity : entities) {

                if (!hasManyToOne(entity)) {

                    String sql = generateCreateTable(entity);
                    statement.execute(sql);
                }
            }

            // 2. потом таблицы с foreign key
            for (Class<?> entity : entities) {

                if (hasManyToOne(entity)) {

                    String sql = generateCreateTable(entity);
                    statement.execute(sql);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String generateCreateTable(Class<?> entity) {

        String tableName = entity.getSimpleName().toLowerCase();

        StringBuilder sql = new StringBuilder();

        sql.append("create table if not exists ").append(tableName).append(" (");

        Field[] fields = entity.getDeclaredFields();

        for (Field field : fields) {

            if (field.isAnnotationPresent(Id.class)) {

                sql.append("id bigserial primary key,");

            } else if (field.isAnnotationPresent(Column.class)) {

                String sqlType = resolveSqlType(field);

                sql.append(field.getName())
                        .append(" ")
                        .append(sqlType)
                        .append(",");

            } else if (field.isAnnotationPresent(ManyToOne.class)) {

                String refTable = field.getType().getSimpleName().toLowerCase();

                sql.append(field.getName()).append("_id bigint references ").append(refTable).append("(id),");
            }
        }

        sql.deleteCharAt(sql.length() - 1);
        sql.append(")");

        return sql.toString();
    }

    private boolean hasManyToOne(Class<?> entity) {

        for (Field field : entity.getDeclaredFields()) {

            if (field.isAnnotationPresent(ManyToOne.class)) {
                return true;
            }
        }

        return false;
    }

    private void validateSchema() {

        try (Connection connection = dataSource.getConnection()) {

            SchemaValidator validator = new SchemaValidator(connection);
            validator.validate(entities);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String resolveSqlType(Field field) {

        Class<?> type = field.getType();

        if (type == String.class) {
            return "varchar(255)";
        }

        if (type == int.class || type == Integer.class) {
            return "integer";
        }

        if (type == long.class || type == Long.class) {
            return "bigint";
        }

        if (type == boolean.class || type == Boolean.class) {
            return "boolean";
        }

        if (type == double.class || type == Double.class) {
            return "double precision";
        }

        if (type == java.time.LocalDate.class) {
            return "date";
        }

        if (type == java.time.LocalDateTime.class) {
            return "timestamp";
        }

        throw new RuntimeException("Unsupported type: " + type);
    }
}