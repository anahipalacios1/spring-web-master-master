package com.bolsadeideas.springboot.web.app.controller;

import com.bolsadeideas.springboot.web.app.models.Materia;
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
@RequestMapping("/materias")
public class MateriaController {

    @GetMapping("/listar")
    public String listar(Model model) {
        MateriaManager materiaManager = new MateriaManager();
        List<Materia> materias = materiaManager.getAll();
        model.addAttribute("titulo", "Lista de Materias");
        model.addAttribute("idMateria", "iDMateria");
        model.addAttribute("descripcion", "Descripcion");
        model.addAttribute("materias", materias);
        return "materia-template/listar";
    }

    @GetMapping("/agregar")
    public String agregarMateria(Model model) {
        Materia materia = new Materia();
        model.addAttribute("titulo", "Agregar Materia");
        model.addAttribute("materia", materia);
        model.addAttribute("error", new HashMap<>());
        return "materia-template/agregar";
    }

    @PostMapping("/agregar")
    public String agregarMateriaProc(@Valid Materia materia, BindingResult result, Model model,
                                   @RequestParam(name="descripcion") String descripcion) throws SQLException {
        model.addAttribute("titulo", "Falta datos");
        if(result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(err ->{
                errores.put(err.getField(), "El campo ".concat(err.getField()).concat(" ").concat(err.getDefaultMessage()));
            });
            model.addAttribute("error", errores);
            return "materia-template/agregar";
        }
        MateriaManager materiaManager = new MateriaManager();
        materia = materiaManager.add(descripcion);
        model.addAttribute("idmateria", "ID Materia");
        model.addAttribute("descripcion", "Descripcion");
        model.addAttribute("titulo", "Materia Agregado");
        model.addAttribute("materia", materia);
        return "materia-template/resultado";
    }
    @GetMapping("/buscar")
    public String buscarMateria(Model model) {
        Materia materia = new Materia();
        model.addAttribute("titulo", "Buscar Materia");
        model.addAttribute("materia", materia);
        model.addAttribute("error", new HashMap<>());
        return "materia-template/buscar";
    }

    @PostMapping("/buscar")
    public String buscarMateriaPro(@Valid Materia materia, BindingResult result, Model model,
                                 @RequestParam(name= "idmateria") int idmateria) throws SQLException {

        if(result.hasGlobalErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(err ->{
                errores.put(err.getField(), "El campo ".concat(err.getField()).concat(" ").concat(err.getDefaultMessage()));
            });
            model.addAttribute("titulo", "Debe ser numero entero");
            model.addAttribute("error", errores);
            return "materia-template/buscar";
        }
        MateriaManager materiaManager = new MateriaManager();
        materia = materiaManager.getByid(idmateria);
        model.addAttribute("idmateria", "ID Materia");
        model.addAttribute("descripcion", "Descripcion");
        model.addAttribute("titulo", "Materia Encontrado");
        model.addAttribute("materia", materia);
        return "materia-template/resultado";

    }
    @GetMapping("/modificar")
    public String modificar(Model model) {
        Materia materia = new Materia();
        model.addAttribute("titulo", "Modificar Materia");
        model.addAttribute("materia", materia);
        model.addAttribute("error", new HashMap<>());
        return "materia-template/modificar";
    }

    @PostMapping("/modificar")
    public String modificar(@Valid Materia materia, BindingResult result, Model model,
                            @RequestParam(name="idmateria") int idmateria,
                            @RequestParam(name="descripcion") String descripcion) {
        model.addAttribute("titulo", "Falta datos");
        if(result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(err ->{
                errores.put(err.getField(), "El campo ".concat(err.getField()).concat(" ").concat(err.getDefaultMessage()));
            });
            model.addAttribute("titulo", "Modificar Materia");
            model.addAttribute("error", errores);
            return "materia-template/modificar";
        }
        MateriaManager materiaManager = new MateriaManager();
        materiaManager.modify(idmateria, descripcion);
        materia.setIdmateria(idmateria);
        materia.setDescripcion(descripcion);
        model.addAttribute("idmateria", "Idmateria");
        model.addAttribute("descripcion", "descripcion");
        model.addAttribute("materia", materia);
        return "materia-template/resultado";
    }

    @GetMapping("/eliminar")
    public String eliminarMateria(Model model) {
        Materia materia = new Materia();
        model.addAttribute("titulo", "Materia a eliminar");
        model.addAttribute("materia", materia);
        model.addAttribute("error", new HashMap<>());
        return "materia-template/eliminar";
    }

    @PostMapping("/eliminar")
    public String eliminarMateriaPro(@Valid Materia materia, BindingResult result, Model model,
                                   @RequestParam(name= "idmateria") int idmateria) throws SQLException {

        if(result.hasGlobalErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(err ->{
                errores.put(err.getField(), "El campo ".concat(err.getField()).concat(" ").concat(err.getDefaultMessage()));
            });
            model.addAttribute("titulo", "Debe ser numero entero");
            model.addAttribute("error", errores);
            return "materia-template/eliminar";
        }
        MateriaManager materiaManager = new MateriaManager();
        materia = materiaManager.getByid(idmateria);
        if(materiaManager.delete(idmateria)==false){
            model.addAttribute("idmateria", "ID Materia");
            model.addAttribute("descripcion", "Descripcion");
            model.addAttribute("titulo", "No se puede eliminar la materia, esta referido a otra base de datos");
            model.addAttribute("materia", materia);
        }else{
            if(materia==null){
                model.addAttribute("idmateria", "ID Materia");
                model.addAttribute("descripcion", "Descripcion");
                model.addAttribute("titulo", "No se encuentra el id de la materia");
                model.addAttribute("materia", materia);
            }
            else{
                model.addAttribute("idmateria", "ID Materia");
                model.addAttribute("descripcion", "Descripcion");
                model.addAttribute("titulo", "Materia Eliminado");
                model.addAttribute("materia", materia);
            }
        }
        return "materia-template/resultado";
    }
}
