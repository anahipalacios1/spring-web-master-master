package com.bolsadeideas.springboot.web.app.controller;

import com.bolsadeideas.springboot.web.app.models.Curso;
import com.bolsadeideas.springboot.web.app.utils.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CourseManager {

    private static final String SQL_INSERT = "INSERT INTO curso (descripcion) VALUES (?)";
    private static final String SQL = "SELECT * FROM curso";
    private static final String SQL_DELETE = "DELETE FROM curso WHERE idcurso=?";
    private static final String SQL_MODIFY = "UPDATE curso SET descripcion=? WHERE idcurso=?";


    public List<Curso> getAllCurso() {

        try (Connection conn = ConnectionManager.getConnection();
             Statement statement = conn.createStatement()) {
            List<Curso> listaCursos = new ArrayList<>();

            ResultSet resultSet = statement.executeQuery(SQL);

            while (resultSet.next()) {
                Curso curso = new Curso();
                curso.setIdcurso(resultSet.getInt("idcurso"));
                curso.setDescripcion(resultSet.getString("descripcion"));

                listaCursos.add(curso);
            }
            resultSet.close();
            return listaCursos;
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }

        return Collections.EMPTY_LIST;
    }

    public Curso add(String descripcion) throws SQLException {

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement preparestatement = conn.prepareStatement(SQL_INSERT,
                     Statement.RETURN_GENERATED_KEYS)) {

            preparestatement.setString(1, descripcion);


            Curso curso = new Curso();

            int affectedRows = preparestatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparestatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {

                    curso.setDescripcion(descripcion);


                    curso.setIdcurso(generatedKeys.getInt(1));
                    return curso;
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
    public boolean delete(int idCurso) {

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement preparestatement = conn.prepareStatement(SQL_DELETE)) {

            preparestatement.setInt(1, idCurso);

            preparestatement.executeUpdate();

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            return false;
        }
        return true;
    }

    public void modify(int idCurso, String descripcion){
        try(Connection conn = ConnectionManager.getConnection();
            PreparedStatement preparestatement = conn.prepareStatement(SQL_MODIFY)){

            preparestatement.setString(1, descripcion);
            preparestatement.setInt(2, idCurso);

            preparestatement.executeUpdate();


        }catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
    }

    public Curso getByid(int idCurso) {

        try (Connection conn = ConnectionManager.getConnection();
             Statement statement = conn.createStatement()) {

            ResultSet resultSet = statement.executeQuery(SQL);

            while (resultSet.next()) {

                if (resultSet.getInt("idcurso")==idCurso){
                    Curso curso = new Curso();
                    curso.setIdcurso(resultSet.getInt("idcurso"));
                    curso.setDescripcion(resultSet.getString("descripcion"));
                    resultSet.close();
                    return curso;
                }

            }


        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
        return null;
    }
}
