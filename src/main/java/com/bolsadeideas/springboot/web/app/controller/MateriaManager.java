package com.bolsadeideas.springboot.web.app.controller;

import com.bolsadeideas.springboot.web.app.models.Materia;
import com.bolsadeideas.springboot.web.app.utils.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MateriaManager {

    private static final String SQL_INSERT = "INSERT INTO materia (descripcion) VALUES (?)";
    private static final String SQL = "SELECT * FROM materia";
    private static final String SQL_DELETE = "DELETE FROM materia WHERE idmateria=?";
    private static final String SQL_MODIFY = "UPDATE materia SET descripcion=? WHERE idmateria=?";


    public List<Materia> getAll() {

        try (Connection conn = ConnectionManager.getConnection();
             Statement statement = conn.createStatement()) {
            List<Materia> lista = new ArrayList<>();

            ResultSet resultSet = statement.executeQuery(SQL);

            while (resultSet.next()) {
                Materia materia = new Materia();
                materia.setIdmateria(resultSet.getInt("idmateria"));
                materia.setDescripcion(resultSet.getString("descripcion"));

                lista.add(materia);
            }
            resultSet.close();
            return lista;
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }

        return Collections.EMPTY_LIST;
    }

    public Materia add(String descripcion) throws SQLException {

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement preparestatement = conn.prepareStatement(SQL_INSERT,
                     Statement.RETURN_GENERATED_KEYS)) {

            preparestatement.setString(1, descripcion);


            Materia materia = new Materia();

            int affectedRows = preparestatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparestatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {

                    materia.setDescripcion(descripcion);
                    materia.setIdmateria(generatedKeys.getInt(1));

                    return materia;
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            throw e;
        }

    }
    //Si no se puede eliminar retorna flase, en caso contrario true si se pudo y tambien si no se encuentra en la base de datos
    public boolean delete(int idmateria) {

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement preparestatement = conn.prepareStatement(SQL_DELETE)) {

            preparestatement.setInt(1, idmateria);

            preparestatement.executeUpdate();

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            return false;
        }
        return true;
    }

    public void modify(int idmateria, String descripcion){
        try(Connection conn = ConnectionManager.getConnection();
            PreparedStatement preparestatement = conn.prepareStatement(SQL_MODIFY)){

            preparestatement.setString(1, descripcion);
            preparestatement.setInt(2, idmateria);

            preparestatement.executeUpdate();


        }catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
    }

    public Materia getByid(int idmateria) {

        try (Connection conn = ConnectionManager.getConnection();
             Statement statement = conn.createStatement()) {

            ResultSet resultSet = statement.executeQuery(SQL);

            while (resultSet.next()) {

                if (resultSet.getInt("idmateria")==idmateria){
                    Materia materia = new Materia();
                    materia.setIdmateria(resultSet.getInt("idmateria"));
                    materia.setDescripcion(resultSet.getString("descripcion"));
                    resultSet.close();
                    return materia;
                }

            }


        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
        return null;
    }
}
