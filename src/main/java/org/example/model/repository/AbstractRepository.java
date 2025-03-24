package org.example.model.repository;


import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AbstractRepository<T> {
    protected static final Logger LOGGER = Logger.getLogger(AbstractRepository.class.getName());
    private final Class<T> type;

    @SuppressWarnings("unchecked")
    public AbstractRepository() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    private String createSelectQuery(String field) {
        return "SELECT * FROM " + type.getSimpleName() + (field.equals("*") ? "" : " WHERE " + field + " = ?");
    }

    public T findById(int id) {
        String query = createSelectQuery(type.getSimpleName().toLowerCase() + "_id");
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<T> objects = createObjects(resultSet);
                return objects.isEmpty() ? null : objects.get(0);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
            return null;
        }
    }

    public List<T> getTableContent() {
        String query = createSelectQuery("*");
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            return createObjects(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:getTableContent " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public int insert(T object) {
        StringBuilder fields = new StringBuilder("INSERT INTO ").append(type.getSimpleName()).append(" (");
        StringBuilder values = new StringBuilder("VALUES (");
        Field[] fieldArray = type.getDeclaredFields();
        List<Object> params = new ArrayList<>();

        for (int i = 1; i < fieldArray.length; i++) {
            fieldArray[i].setAccessible(true);
            if (i > 1) {
                fields.append(", ");
                values.append(", ");
            }
            fields.append(fieldArray[i].getName());
            values.append("?");
            try {
                params.add(fieldArray[i].get(object));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        fields.append(") ");
        values.append(")");
        String query = fields.toString() + values.toString();


        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            setStatementParameters(statement, params);
            System.out.println(query);
            statement.executeUpdate();

            try (ResultSet rs = statement.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Error executing insert: " + e.getMessage());
            return 0;
        }
    }

    public int update(T object) {
        StringBuilder query = new StringBuilder("UPDATE ").append(type.getSimpleName()).append(" SET ");
        Field[] fields = type.getDeclaredFields();
        List<Object> params = new ArrayList<>();

        for (int i = 1; i < fields.length; i++) {
            fields[i].setAccessible(true);
            if (i > 1) query.append(", ");
            query.append(fields[i].getName()).append(" = ?");
            try {
                params.add(fields[i].get(object));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        query.append(" WHERE ").append(fields[0].getName()).append(" = ?");
        try {
            fields[0].setAccessible(true);
            params.add(fields[0].get(object));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(query.toString())) {
            setStatementParameters(statement, params);
            return statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Error executing update: " + e.getMessage());
            return 0;
        }
    }

    public int delete(T object) {
        Field primaryKeyField = type.getDeclaredFields()[0];
        primaryKeyField.setAccessible(true); // Permite accesul la câmpurile private

        String query = "DELETE FROM " + type.getSimpleName() + " WHERE " + primaryKeyField.getName() + " = ?";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            Object primaryKeyValue = primaryKeyField.get(object); // Obține valoarea ID-ului obiectului
            statement.setObject(1, primaryKeyValue);

            return statement.executeUpdate();
        } catch (SQLException | IllegalAccessException e) {
            LOGGER.log(Level.WARNING, "Error executing delete: " + e.getMessage());
            return 0;
        }
    }

    private List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<>();
        try {
            Constructor<T> ctor = type.getDeclaredConstructor();
            ctor.setAccessible(true);
            while (resultSet.next()) {
                T instance = ctor.newInstance();
                for (Field field : type.getDeclaredFields()) {
                    field.setAccessible(true);
                    Object value = resultSet.getObject(field.getName());
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), type);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }
                list.add(instance);
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error creating objects: " + e.getMessage());
        }
        return list;
    }

    private void setStatementParameters(PreparedStatement statement, List<Object> params) throws SQLException {
        for (int i = 0; i < params.size(); i++) {
            statement.setObject(i + 1, params.get(i));
        }
    }
}