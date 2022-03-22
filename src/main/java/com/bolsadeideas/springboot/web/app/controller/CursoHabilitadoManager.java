package com.bolsadeideas.springboot.web.app.controller;

import com.bolsadeideas.springboot.web.app.models.CursoHabilitado;
import com.bolsadeideas.springboot.web.app.utils.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CursoHabilitadoManager {

    private static final String SQL_INSERT = "INSERT INTO cursohabilitado (idcurso, idmateria, idprofesor) VALUES (?,?,?)";
    private static final String SQL = "SELECT * FROM cursohabilitado";
    private static final String SQL_DELETE = "DELETE FROM cursohabilitado WHERE idcursohabilitado=?";
    private static final String SQL_MODIFY = "UPDATE cursohabilitado SET idcurso=?, idmateria=?, idprofesor=? WHERE idcursohabilitado=?";


    public List<CursoHabilitado> getAll() {

        try (Connection conn = ConnectionManager.getConnection();
             Statement statement = conn.createStatement()) {
            List<CursoHabilitado> lista = new ArrayList<>();

            ResultSet resultSet = statement.executeQuery(SQL);

            while (resultSet.next()) {
                CursoHabilitado cursohabilitado = new CursoHabilitado();
                cursohabilitado.setIdcursohabilitado(resultSet.getInt("idcursohabilitado"));
                cursohabilitado.setIdcurso(resultSet.getInt("idcurso"));
                cursohabilitado.setIdmateria(resultSet.getInt("idmateria"));
                cursohabilitado.setIdprofesor(resultSet.getInt("idprofesor"));
                lista.add(cursohabilitado);
            }
            resultSet.close();
            return lista;
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }

        return Collections.EMPTY_LIST;
    }

    public CursoHabilitado add(int idcurso, int idmateria, int idprofesor) throws SQLException {

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement preparestatement = conn.prepareStatement(SQL_INSERT,
                     Statement.RETURN_GENERATED_KEYS)) {

            preparestatement.setInt(1, idcurso);
            preparestatement.setInt(2, idmateria);
            preparestatement.setInt(3, idprofesor);

            CursoHabilitado cursohabilitado = new CursoHabilitado();

            int affectedRows = preparestatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparestatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {

                    cursohabilitado.setIdcurso(idcurso);
                    cursohabilitado.setIdmateria(idmateria);
                    cursohabilitado.setIdprofesor(idprofesor);

                    cursohabilitado.setIdcursohabilitado(generatedKeys.getInt(1));
                    return cursohabilitado;
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
    public boolean delete(int idCursoHabilitado) {

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement preparestatement = conn.prepareStatement(SQL_DELETE)) {

            preparestatement.setInt(1, idCursoHabilitado);

            preparestatement.executeUpdate();

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            return false;
        }
        return true;
    }

    public void modify(int idcursohabilitado, int idcurso, int idmateria, int idprofesor){
        try(Connection conn = ConnectionManager.getConnection();
            PreparedStatement preparestatement = conn.prepareStatement(SQL_MODIFY)){

            preparestatement.setInt(1, idcurso);
            preparestatement.setInt(2, idmateria);
            preparestatement.setInt(3, idprofesor);
            preparestatement.setInt(4, idcursohabilitado);
            preparestatement.executeUpdate();


        }catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
    }

    public CursoHabilitado getByid(int idCursoHabilitado) {

        try (Connection conn = ConnectionManager.getConnection();
             Statement statement = conn.createStatement()) {

            ResultSet resultSet = statement.executeQuery(SQL);

            while (resultSet.next()) {

                if (resultSet.getInt("idcursohabilitado")==idCursoHabilitado){
                    CursoHabilitado cursohabilitado = new CursoHabilitado();
                    cursohabilitado.setIdcursohabilitado(resultSet.getInt("idcursohabilitado"));
                    cursohabilitado.setIdcurso(resultSet.getInt("idcurso"));
                    cursohabilitado.setIdmateria(resultSet.getInt("idmateria"));
                    cursohabilitado.setIdprofesor(resultSet.getInt("idprofesor"));
                    resultSet.close();
                    return cursohabilitado;
                }

            }


        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
        return null;
    }
}
