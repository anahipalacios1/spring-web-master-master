package com.bolsadeideas.springboot.web.app.controller;

import com.bolsadeideas.springboot.web.app.models.Profesor;
import com.bolsadeideas.springboot.web.app.utils.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProfessorManager {

    private static final String SQL_INSERT = "INSERT INTO profesor (nombre, apellido) VALUES (?, ?)";
    private static final String SQL = "SELECT * FROM profesor";
    private static final String SQL_DELETE = "DELETE FROM profesor WHERE idprofesor=?";
    private static final String SQL_MODIFY = "UPDATE profesor SET nombre=?, SET apellido=? WHERE idprofesor=?";

    public List<Profesor> getAllProfessor() {

        try (Connection conn = ConnectionManager.getConnection();
             Statement statement = conn.createStatement()) {
            List<Profesor> listaProfes = new ArrayList<>();

            ResultSet resultSet = statement.executeQuery(SQL);

            while (resultSet.next()) {
                Profesor profe = new Profesor();
                profe.setIdProfesor(resultSet.getInt("idprofesor"));
                profe.setNombre(resultSet.getString("nombre"));
                profe.setApellido(resultSet.getString("apellido"));
                listaProfes.add(profe);
            }
            resultSet.close();
            return listaProfes;
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }

        return Collections.EMPTY_LIST;
    }

    public Profesor add(String nombre, String apellido) throws SQLException {

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement preparestatement = conn.prepareStatement(SQL_INSERT,
                     Statement.RETURN_GENERATED_KEYS)) {

            preparestatement.setString(1, nombre);
            preparestatement.setString(2, apellido);

            Profesor profe = new Profesor();

            int affectedRows = preparestatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparestatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {

                    profe.setApellido(apellido);
                    profe.setNombre(nombre);

                    profe.setIdProfesor(generatedKeys.getInt(1));
                    return profe;
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
    public boolean delete(int idProfe) {

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement preparestatement = conn.prepareStatement(SQL_DELETE)) {

            preparestatement.setInt(1, idProfe);

            preparestatement.executeUpdate();

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            return false;
        }
        return true;
    }

    public void modify(int idProfe, String nombre, String apellido){
        try(Connection conn = ConnectionManager.getConnection();
            PreparedStatement preparestatement = conn.prepareStatement(SQL_MODIFY)){

            preparestatement.setString(1, nombre);
            preparestatement.setString(2, apellido);
            preparestatement.setInt(3, idProfe);

            preparestatement.executeUpdate();


        }catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
    }

    public Profesor getByid(int idProfesor) throws SQLException {

        try (Connection conn = ConnectionManager.getConnection();
             Statement statement = conn.createStatement()) {

            ResultSet resultSet = statement.executeQuery(SQL);

            while (resultSet.next()) {

                if (resultSet.getInt("idprofesor")==idProfesor){

                    Profesor profesor = new Profesor();
                    profesor.setIdProfesor(resultSet.getInt("idprofesor"));
                    profesor.setNombre(resultSet.getString("nombre"));
                    profesor.setNombre(resultSet.getString("apellido"));

                    resultSet.close();
                    return profesor;
                }

            }


        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            throw e;
        }
        return null;
    }
}

