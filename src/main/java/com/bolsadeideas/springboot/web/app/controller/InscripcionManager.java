package com.bolsadeideas.springboot.web.app.controller;

import com.bolsadeideas.springboot.web.app.models.Inscripcion;
import com.bolsadeideas.springboot.web.app.utils.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InscripcionManager {

    private static final String SQL_INSERT = "INSERT INTO inscripciones (idcursohabilitado, idalumno) VALUES (?,?)";
    private static final String SQL = "SELECT * FROM inscripciones";
    private static final String SQL_DELETE = "DELETE FROM inscripciones WHERE idinscripcion=?";
    private static final String SQL_MODIFY = "UPDATE inscripciones SET idcursohabilitado=?, idalumno=? WHERE idinscripcion=?";


    public List<Inscripcion> getAll() {

        try (Connection conn = ConnectionManager.getConnection();
             Statement statement = conn.createStatement()) {
            List<Inscripcion> lista = new ArrayList<>();

            ResultSet resultSet = statement.executeQuery(SQL);

            while (resultSet.next()) {
                Inscripcion inscripcion = new Inscripcion();
                inscripcion.setIdinscripcion(resultSet.getInt("idinscripcion"));
                inscripcion.setIdcursohabilitado(resultSet.getInt("idcursohabilitado"));
                inscripcion.setIdalumno(resultSet.getInt("idalumno"));
                lista.add(inscripcion);
            }
            resultSet.close();
            return lista;
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }

        return Collections.EMPTY_LIST;
    }

    public Inscripcion add(int idcursohabilitado, int idalumno) throws SQLException {

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement preparestatement = conn.prepareStatement(SQL_INSERT,
                     Statement.RETURN_GENERATED_KEYS)) {

            preparestatement.setInt(1, idcursohabilitado);
            preparestatement.setInt(2, idalumno);

            Inscripcion inscripcion = new Inscripcion();

            int affectedRows = preparestatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparestatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {

                    inscripcion.setIdcursohabilitado(idcursohabilitado);
                    inscripcion.setIdalumno(idalumno);

                    inscripcion.setIdinscripcion(generatedKeys.getInt(1));
                    return inscripcion;
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
    public boolean delete(int idinscripcion) {

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement preparestatement = conn.prepareStatement(SQL_DELETE)) {

            preparestatement.setInt(1, idinscripcion);

            preparestatement.executeUpdate();

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            return false;
        }
        return true;
    }

    public void modify(int idinscripcion, int idcursohabilitado, int idalumno){
        try(Connection conn = ConnectionManager.getConnection();
            PreparedStatement preparestatement = conn.prepareStatement(SQL_MODIFY)){

            preparestatement.setInt(1, idcursohabilitado);
            preparestatement.setInt(2, idalumno);
            preparestatement.setInt(3, idinscripcion);

            preparestatement.executeUpdate();


        }catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
    }

    public Inscripcion getByid(int idinscripcion) {

        try (Connection conn = ConnectionManager.getConnection();
             Statement statement = conn.createStatement()) {

            ResultSet resultSet = statement.executeQuery(SQL);

            while (resultSet.next()) {

                if (resultSet.getInt("idinscripcion")==idinscripcion){
                    Inscripcion inscripcion = new Inscripcion();
                    inscripcion.setIdinscripcion(resultSet.getInt("idinscripcion"));
                    inscripcion.setIdcursohabilitado(resultSet.getInt("idcursohabilitado"));
                    inscripcion.setIdalumno(resultSet.getInt("idalumno"));
                    resultSet.close();
                    return inscripcion;
                }

            }


        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
        return null;
    }
}
