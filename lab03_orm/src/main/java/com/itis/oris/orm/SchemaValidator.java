package com.itis.oris.orm;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SchemaValidator {

    private final Connection connection;

    public SchemaValidator(Connection connection) {
        this.connection = connection;
    }

    public void validate(List<Class<?>> entities) {

        Set<String> tables = loadTables();

        for (Class<?> entity : entities) {

            String tableName = entity.getSimpleName().toLowerCase();

            if (!tables.contains(tableName)) {
                throw new RuntimeException("Table not found: " + tableName);
            }

            validateColumns(entity, tableName);
        }
    }

    private Set<String> loadTables() {

        Set<String> tables = new HashSet<>();

        String sql = """
                SELECT table_name
                FROM information_schema.tables
                WHERE table_type = 'BASE TABLE'
                AND table_schema NOT IN ('pg_catalog','information_schema')
                """;

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                tables.add(rs.getString("table_name"));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return tables;
    }

    private void validateColumns(Class<?> entity, String tableName) {

        Set<String> columns = loadColumns(tableName);

        for (Field field : entity.getDeclaredFields()) {

            if (field.isAnnotationPresent(Id.class)) {

                if (!columns.contains("id")) {
                    throw new RuntimeException(
                            "Column id missing in table " + tableName
                    );
                }

            } else if (field.isAnnotationPresent(Column.class)) {

                if (!columns.contains(field.getName())) {
                    throw new RuntimeException(
                            "Column missing: " + field.getName()
                    );
                }

            } else if (field.isAnnotationPresent(ManyToOne.class)) {

                String column = field.getName() + "_id";

                if (!columns.contains(column)) {
                    throw new RuntimeException(
                            "Column missing: " + column
                    );
                }
            }
        }
    }

    private Set<String> loadColumns(String tableName) {

        Set<String> columns = new HashSet<>();

        String sql = """
                SELECT a.attname
                FROM pg_catalog.pg_attribute a
                WHERE a.attrelid = (
                    SELECT c.oid FROM pg_catalog.pg_class c
                    WHERE c.relname = ?
                )
                AND a.attnum > 0 AND NOT a.attisdropped
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, tableName);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    columns.add(rs.getString("attname"));
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return columns;
    }
}