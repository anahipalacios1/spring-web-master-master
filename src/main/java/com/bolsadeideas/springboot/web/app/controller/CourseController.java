package com.bolsadeideas.springboot.web.app.controller;

import com.bolsadeideas.springboot.web.app.models.Curso;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cursos")
public class CourseController {
    
        @GetMapping("/listar")
        public String listaCurso(Model model) {
            CourseManager courseManager = new CourseManager();
            List<Curso> cursos = courseManager.getAllCurso();
            model.addAttribute("titulo", "Lista de Cursos");
            model.addAttribute("idcurso", "iDCurso");
            model.addAttribute("descripcion", "Descripcion");
            model.addAttribute("cursos", cursos);
            return "curso-template/listar";
        }

        @GetMapping("/agregar")
        public String agregarCurso(Model model) {
            Curso curso = new Curso();
            model.addAttribute("titulo", "Agregar Curso");
            model.addAttribute("curso", curso);
            model.addAttribute("error", new HashMap<>());
            return "curso-template/agregar";
        }

        @PostMapping("/agregar")
        public String agregarCursoProc(@Valid Curso curso, BindingResult result, Model model,
                                       @RequestParam(name="descripcion") String descripcion) throws SQLException{
            model.addAttribute("titulo", "Falta datos");
            if(result.hasErrors()) {
                Map<String, String> errores = new HashMap<>();
                result.getFieldErrors().forEach(err ->{
                    errores.put(err.getField(), "El campo ".concat(err.getField()).concat(" ").concat(err.getDefaultMessage()));
                });
                model.addAttribute("error", errores);
                return "curso-template/agregar";
            }
            CourseManager cursoManager = new CourseManager();
            curso = cursoManager.add(descripcion);
            model.addAttribute("idcurso", "ID curso");
            model.addAttribute("descripcion", "Descripcion");
            model.addAttribute("titulo", "curso Agregado");
            model.addAttribute("curso", curso);
            return "curso-template/resultado";
        }
        @GetMapping("/buscar")
        public String buscarCurso(Model model) {
            Curso curso = new Curso();
            model.addAttribute("titulo", "Buscar curso");
            model.addAttribute("curso", curso);
            model.addAttribute("error", new HashMap<>());
            return "curso-template/buscar";
        }

        @PostMapping("/buscar")
        public String buscarCursoPro(@Valid Curso curso, BindingResult result, Model model,
                                     @RequestParam(name= "idcurso") int idcurso) throws SQLException {

            if(result.hasGlobalErrors()) {
                Map<String, String> errores = new HashMap<>();
                result.getFieldErrors().forEach(err ->{
                    errores.put(err.getField(), "El campo ".concat(err.getField()).concat(" ").concat(err.getDefaultMessage()));
                });
                model.addAttribute("titulo", "Debe ser numero entero");
                model.addAttribute("error", errores);
                return "curso-template/buscar";
            }
            CourseManager cursoManager = new CourseManager();
            curso = cursoManager.getByid(idcurso);
            model.addAttribute("idcurso", "Idcurso");
            model.addAttribute("descripcion", "descripcion");
            model.addAttribute("titulo", "Curso Encontrado");
            model.addAttribute("curso", curso);
            return "curso-template/resultado";

        }
        @GetMapping("/modificar")
        public String modificar(Model model) {
            Curso curso = new Curso();
            model.addAttribute("titulo", "Modificar curso");
            model.addAttribute("curso", curso);
            model.addAttribute("error", new HashMap<>());
            return "curso-template/modificar";
        }

        @PostMapping("/modificar")
        public String modificar(@Valid Curso curso, BindingResult result, Model model,
                                @RequestParam(name="idcurso") int idcurso,
                                @RequestParam(name="descripcion") String descripcion) {
            model.addAttribute("titulo", "Falta datos");
            if(result.hasErrors()) {
                Map<String, String> errores = new HashMap<>();
                result.getFieldErrors().forEach(err ->{
                    errores.put(err.getField(), "El campo ".concat(err.getField()).concat(" ").concat(err.getDefaultMessage()));
                });
                model.addAttribute("titulo", "Modificar Curso");
                model.addAttribute("error", errores);
                return "curso-template/modificar";
            }
            CourseManager cursoManager = new CourseManager();
            cursoManager.modify(idcurso, descripcion);
            curso.setIdcurso(idcurso);
            curso.setDescripcion(descripcion);
            model.addAttribute("idcurso", "Idcurso");
            model.addAttribute("descripcion", "descripcion");
            model.addAttribute("curso", curso);
            return "curso-template/resultado";
        }

    @GetMapping("/eliminar")
    public String eliminarCurso(Model model) {
        Curso curso = new Curso();
        model.addAttribute("titulo", "Buscar curso");
        model.addAttribute("curso", curso);
        model.addAttribute("error", new HashMap<>());
        return "curso-template/eliminar";
    }

    @PostMapping("/eliminar")
    public String eliminarCursoPro(@Valid Curso curso, BindingResult result, Model model,
                                 @RequestParam(name= "idcurso") int idcurso) throws SQLException {

        if(result.hasGlobalErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(err ->{
                errores.put(err.getField(), "El campo ".concat(err.getField()).concat(" ").concat(err.getDefaultMessage()));
            });
            model.addAttribute("titulo", "Debe ser numero entero");
            model.addAttribute("error", errores);
            return "curso-template/eliminar";
        }
        CourseManager cursoManager = new CourseManager();
        curso = cursoManager.getByid(idcurso);
        if(cursoManager.delete(idcurso)==false){
            model.addAttribute("idcurso", "Idcurso");
            model.addAttribute("descripcion", "descripcion");
            model.addAttribute("titulo", "No se pude eliminar, el curso esta referido a otra base de datos");
            model.addAttribute("curso", curso);
        }else{
            if(curso==null){
                model.addAttribute("idcurso", "");
                model.addAttribute("descripcion", "");
                model.addAttribute("titulo", "Curso no se encuentra en la base de datos");
                model.addAttribute("curso", curso);
            }
            else{
                model.addAttribute("idcurso", "Idcurso");
                model.addAttribute("descripcion", "descripcion");
                model.addAttribute("titulo", "Curso Eliminado");
                model.addAttribute("curso", curso);
            }
        }

        return "curso-template/resultado";

    }

    }

