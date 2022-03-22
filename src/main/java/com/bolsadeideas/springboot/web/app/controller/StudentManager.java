package com.bolsadeideas.springboot.web.app.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.bolsadeideas.springboot.web.app.models.Alumno;
import com.bolsadeideas.springboot.web.app.utils.ConnectionManager;



public class StudentManager {

    private static final String SQL_INSERT = "INSERT INTO alumno (nombre, apellido) VALUES (?, ?)";
    private static final String SQL = "SELECT * FROM alumno";
    private static final String SQL_DELETE = "DELETE FROM alumno WHERE idalumno=?";
    private static final String SQL_MODIFY = "UPDATE alumno SET nombre=?, apellido=? WHERE idalumno=?";

    public List<Alumno> getAllStudents() {

        try (Connection conn = ConnectionManager.getConnection();
                Statement statement = conn.createStatement()) {
            List<Alumno> listaAlumnos = new ArrayList<>();
            
            ResultSet resultSet = statement.executeQuery(SQL);
            
            while (resultSet.next()) {
                Alumno alumno = new Alumno();
                alumno.setIdAlumno(resultSet.getInt("idalumno"));
                alumno.setApellido(resultSet.getString("apellido"));
                alumno.setNombre(resultSet.getString("nombre"));
                
                listaAlumnos.add(alumno);
            }
            resultSet.close();
            return listaAlumnos;
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
        
        return Collections.EMPTY_LIST; 
    }

    public Alumno add(String nombre, String apellido) throws SQLException{

        try (Connection conn = ConnectionManager.getConnection();
                PreparedStatement preparestatement = conn.prepareStatement(SQL_INSERT,
                        Statement.RETURN_GENERATED_KEYS)) {

            preparestatement.setString(1, nombre);
            preparestatement.setString(2, apellido);
            
            
            Alumno alumno = new Alumno();
            
            int affectedRows = preparestatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparestatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                	alumno.setApellido(apellido);
                    alumno.setNombre(nombre);
                    
                    alumno.setIdAlumno(generatedKeys.getInt(1));
                    return alumno;
                }
                else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            throw e;
        }

    }
    //Si no se puede eliminar retorna flase, en caso contrario true si se pudo y tambien si no se encuentra en la base de datos
    public boolean delete(int idAlumno) {

        try (Connection conn = ConnectionManager.getConnection();
                PreparedStatement preparestatement = conn.prepareStatement(SQL_DELETE)) {

            preparestatement.setInt(1, idAlumno);

            preparestatement.executeUpdate();

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            return false;
        }
        return true;
    }
    
    public void modify(int idAlumno, String nombre, String apellido){
        try(Connection conn = ConnectionManager.getConnection();
                PreparedStatement preparestatement = conn.prepareStatement(SQL_MODIFY)){
            
            preparestatement.setString(1, nombre);
            preparestatement.setString(2, apellido);
            preparestatement.setInt(3, idAlumno); 
            
            preparestatement.executeUpdate(); 
            
            
        }catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
    }

    public Alumno getByid(int idAlumno) {

        try (Connection conn = ConnectionManager.getConnection();
                Statement statement = conn.createStatement()) {
            
            ResultSet resultSet = statement.executeQuery(SQL);
            
            while (resultSet.next()) {
                              
                if (resultSet.getInt("idalumno")==idAlumno){
                    Alumno student = new Alumno();
                    student.setIdAlumno(resultSet.getInt("idalumno"));
                    student.setNombre(resultSet.getString("nombre"));
                    student.setApellido(resultSet.getString("apellido"));
                    resultSet.close();
                    return student; 
                }
           
            }
            

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
        return null; 
    }
    

}

