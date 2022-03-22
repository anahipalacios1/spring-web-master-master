package com.bolsadeideas.springboot.web.app.controller;

import com.bolsadeideas.springboot.web.app.models.Cuenta;
import com.bolsadeideas.springboot.web.app.models.Inscripcion;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

@Controller
@RequestMapping("/inscripciones")
public class InscripcionController {

    @GetMapping("/listar")
    public String listarInscripcion(Model model) {
        InscripcionManager inscripcionManager = new InscripcionManager();
        List<Inscripcion> inscripciones = inscripcionManager.getAll();
        model.addAttribute("titulo", "Lista de Inscripciones");
        model.addAttribute("idinscripcion", "ID Inscripcion");
        model.addAttribute("idcursohabilitado", "ID CursoHabilitado");
        model.addAttribute("idalumno", "ID Alumno");
        model.addAttribute("inscripciones", inscripciones);
        return "inscripcion-template/listar";
    }

    @GetMapping("/agregar")
    public String agregarInscripcion(Model model) {
        Inscripcion inscripcion = new Inscripcion();
        model.addAttribute("titulo", "Habilitar Inscripcion");
        model.addAttribute("inscripcion", inscripcion);
        model.addAttribute("error", new HashMap<>());
        return "inscripcion-template/agregar";
    }

    @PostMapping("/agregar")
    public String agregarInscripcionProc(@Valid Inscripcion inscripcion, BindingResult result, Model model,
                              @RequestParam(name="idcursohabilitado") int idcursohabilitado,
                              @RequestParam(name="idalumno") int idalumno ) throws SQLException {
        model.addAttribute("titulo", "Falta datos");
        if(result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(err ->{
                errores.put(err.getField(), "El campo ".concat(err.getField()).concat(" ").concat(err.getDefaultMessage()));
            });
            model.addAttribute("error", errores);
            return "inscripcion-template/agregar";
        }
        InscripcionManager inscripcionManager = new InscripcionManager();
        CuentaManager cuentaManager = new CuentaManager();
        inscripcion = inscripcionManager.add(idcursohabilitado, idalumno);  //Agregamos inscripcion
        Timestamp date = new Timestamp(System.currentTimeMillis());
        date = cuentaManager.sacarFecha();
        Timestamp date1 = new Timestamp(System.currentTimeMillis());
        date1.setMonth(date.getMonth()+1);
        Cuenta cuenta = new Cuenta();
        cuenta = cuentaManager.add(inscripcion.getIdinscripcion(), date, date1);     //Una ves agregado la inscripcion sacamos el id inscripcion y generamos la cuenta
        model.addAttribute("idinscripcion", "ID Inscripcion");
        model.addAttribute("idcursohabilitado", "ID Curso Habilitado");
        model.addAttribute("idalumno", "ID Alumno");
        model.addAttribute("titulo", "Inscripciones");
        model.addAttribute("idcuenta", "ID Cuenta");
        model.addAttribute("idcuentaMensaje", cuenta.getIdcuenta());
        model.addAttribute("inscripcion", inscripcion);
        return "inscripcion-template/resultado";
    }
    @GetMapping("/buscar")
    public String buscarInscripcion(Model model) {
        Inscripcion inscripcion = new Inscripcion();
        model.addAttribute("titulo", "Buscar Inscriptos");
        model.addAttribute("inscripcion", inscripcion);
        model.addAttribute("error", new HashMap<>());
        return "inscripcion-template/buscar";
    }

    @PostMapping("/buscar")
    public String buscarInscricionPro(@Valid Inscripcion inscripcion, BindingResult result, Model model,
                            @RequestParam(name= "idinscripcion") int idinscripcion) throws SQLException {

        if(result.hasGlobalErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(err ->{
                errores.put(err.getField(), "El campo ".concat(err.getField()).concat(" ").concat(err.getDefaultMessage()));
            });
            model.addAttribute("titulo", "Debe ser numero entero");
            model.addAttribute("error", errores);
            return "inscripcion-template/buscar";
        }
        InscripcionManager inscripcionManager = new InscripcionManager();
        inscripcion = inscripcionManager.getByid(idinscripcion);
        model.addAttribute("idinscripcion", "ID Inscripcion");
        model.addAttribute("idcursohabilitado", "ID Curso Habilitado");
        model.addAttribute("idalumno", "ID Alumno");
        model.addAttribute("titulo", "Inscripto");
        model.addAttribute("inscripcion", inscripcion);
        return "inscripcion-template/resultado";

    }
    @GetMapping("/modificar")
    public String modificarInscripcion(Model model) {
        Inscripcion inscripcion = new Inscripcion();
        model.addAttribute("titulo", "Modificar la inscripcion");
        model.addAttribute("inscripcion", inscripcion);
        model.addAttribute("error", new HashMap<>());
        return "inscripcion-template/modificar";
    }

    @PostMapping("/modificar")
    public String modificarInscripcionProc(@Valid Inscripcion inscripcion, BindingResult result, Model model,
                            @RequestParam(name="idinscripcion") int idinscripcion,
                            @RequestParam(name="idcursohabilitado") int idcursohabilitado,
                            @RequestParam(name="idalumno") int idalumno ) {
        model.addAttribute("titulo", "Falta datos");
        if(result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(err ->{
                errores.put(err.getField(), "El campo ".concat(err.getField()).concat(" ").concat(err.getDefaultMessage()));
            });
            model.addAttribute("titulo", "Falta Datos");
            model.addAttribute("error", errores);
            return "inscripcion-template/modificar";
        }
        InscripcionManager inscripcionManager = new InscripcionManager();
        inscripcionManager.modify(idinscripcion, idcursohabilitado, idalumno);
        inscripcion.setIdinscripcion(idinscripcion);
        inscripcion.setIdcursohabilitado(idcursohabilitado);
        inscripcion.setIdalumno(idalumno);
        model.addAttribute("idinscripcion", "idinscripcion");
        model.addAttribute("idcursohabilitado", "idcursohabilitado");
        model.addAttribute("idmateria", "idmateria");
        model.addAttribute("inscripcion", inscripcion);
        return "inscripcion-template/resultado";
    }

    @GetMapping("/eliminar")
    public String eliminarInscripcion(Model model) {
        Inscripcion inscripcion = new Inscripcion();
        model.addAttribute("titulo", "Elminar la inscripcion");
        model.addAttribute("inscripcion", inscripcion);
        model.addAttribute("error", new HashMap<>());
        return "inscripcion-template/eliminar";
    }

    @PostMapping("/eliminar")
    public String eliminarInscricionPro(@Valid Inscripcion inscripcion, BindingResult result, Model model,
                                      @RequestParam(name= "idinscripcion") int idinscripcion) throws SQLException {
        if(result.hasGlobalErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(err ->{
                errores.put(err.getField(), "El campo ".concat(err.getField()).concat(" ").concat(err.getDefaultMessage()));
            });
            model.addAttribute("titulo", "Debe ser numero entero");
            model.addAttribute("error", errores);
            return "inscripcion-template/eliminar";
        }
        InscripcionManager inscripcionManager = new InscripcionManager();
        inscripcion = inscripcionManager.getByid(idinscripcion);
        if(inscripcionManager.delete(idinscripcion)==false){
            model.addAttribute("idinscripcion", "ID Inscripcion");
            model.addAttribute("idcursohabilitado", "ID Curso Habilitado");
            model.addAttribute("idalumno", "ID Alumno");
            model.addAttribute("titulo", "No se puede eliminar, esta referido a otra base de datos la inscripcion.");
            model.addAttribute("inscripcion", inscripcion);
        }else{
            if(inscripcion==null){
                model.addAttribute("idinscripcion", "");
                model.addAttribute("idcursohabilitado", "");
                model.addAttribute("idalumno", "");
                model.addAttribute("titulo", "No se encuentra la inscripcion");
                model.addAttribute("inscripcion", inscripcion);
            }
            else{
                model.addAttribute("idinscripcion", "ID Inscripcion");
                model.addAttribute("idcursohabilitado", "ID Curso Habilitado");
                model.addAttribute("idalumno", "ID Alumno");
                model.addAttribute("titulo", "Inscripto");
                model.addAttribute("inscripcion", inscripcion);
            }
        }
        return "inscripcion-template/resultado";
    }
}
