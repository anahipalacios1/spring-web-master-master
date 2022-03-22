package com.bolsadeideas.springboot.web.app.controller;

import com.bolsadeideas.springboot.web.app.models.Cuenta;
import com.bolsadeideas.springboot.web.app.utils.ConfigManager;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cuentas")
public class CuentaController{

    @GetMapping("/listar")
    public String listarCuenta(Model model) {
        CuentaManager cuentaManager = new CuentaManager();
        List<Cuenta> cuentas = cuentaManager.getAll();
        model.addAttribute("titulo", "Lista de Cuentas");
        model.addAttribute("idcuenta", "ID cuenta");
        model.addAttribute("idinscripcion", "ID Inscripcion");
        model.addAttribute("monto", "Monto a Pagar");
        model.addAttribute("fecha", "Fecha");
        model.addAttribute("fechavencimiento", "Fecha Vencimiento");
        model.addAttribute("pagos", "Pagos");
        model.addAttribute("cuentas", cuentas);
        return "cuenta-template/listar";
    }

    @GetMapping("/buscar")
    public String buscarCuenta(Model model) {
        Cuenta cuenta = new Cuenta();
        model.addAttribute("titulo", "Buscar Cuenta");
        model.addAttribute("cuenta", cuenta);
        model.addAttribute("error", new HashMap<>());
        return "cuenta-template/buscar";
    }

    @PostMapping("/buscar")
    public String buscarCuentaPro(@Valid Cuenta cuenta, BindingResult result, Model model,
                            @RequestParam(name= "idcuenta") int idcuenta) throws SQLException {

        if(result.hasGlobalErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(err ->{
                errores.put(err.getField(), "El campo ".concat(err.getField()).concat(" ").concat(err.getDefaultMessage()));
            });
            model.addAttribute("titulo", "Debe ser numero entero");
            model.addAttribute("error", errores);
            return "cuenta-template/buscar";
        }
        CuentaManager cuentaManager = new CuentaManager();
        cuenta = cuentaManager.getByid(idcuenta);
        model.addAttribute("idcuenta", "ID cuenta");
        model.addAttribute("idinscripcion", "ID Inscripcion");
        model.addAttribute("fecha", "Fecha");
        model.addAttribute("fechavencimiento", "Fecha Vencimiento");
        model.addAttribute("monto", "Monto a Pagar");
        model.addAttribute("pagos", "Pagos");
        model.addAttribute("titulo","Cuenta Encontrado");
        model.addAttribute("cuenta", cuenta);
        return "cuenta-template/resultado";

    }
    @GetMapping("/modificar")
    public String modificarCuenta(Model model) {
        Cuenta cuenta = new Cuenta();
        model.addAttribute("titulo", "Modificar inscripcion Habilitado");
        model.addAttribute("cuenta", cuenta);
        model.addAttribute("error", new HashMap<>());
        return "cuenta-template/modificar";
    }

    @PostMapping("/modificar")
    public String modificarCuentaProc(@Valid Cuenta cuenta, BindingResult result, Model model,
                            @RequestParam(name="idcuenta") int idcuenta,
                            @RequestParam(name="idinscripcion") int idinscripcion,
                            @RequestParam(name="fecha") Timestamp fecha,
                            @RequestParam(name="fechavencimiento") Timestamp fechavencimiento,
                            @RequestParam(name="monto") int monto ,
                            @RequestParam(name="pagos") byte pagos ) {
        model.addAttribute("titulo", "Falta datos");
        if(result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(err ->{
                errores.put(err.getField(), "El campo ".concat(err.getField()).concat(" ").concat(err.getDefaultMessage()));
            });
            model.addAttribute("titulo", "Modificar inscripcion Habilitado");
            model.addAttribute("error", errores);
            return "cuenta-template/modificar";
        }
        CuentaManager cuentaManager = new CuentaManager();
        cuentaManager.modify(idcuenta,fechavencimiento,monto,pagos);
        cuenta.setIdcuenta(idcuenta);
        cuenta.setIdinscripcion(idinscripcion);
        cuenta.setFecha(fecha);
        cuenta.setFechavencimiento(fechavencimiento);
        cuenta.setMonto(monto);
        cuenta.setPagos(pagos);
        model.addAttribute("idcuenta", "ID Cuenta");
        model.addAttribute("idinscripcion", "ID Inscripcion");
        model.addAttribute("fecha", "Fecha");
        model.addAttribute("fechavencimiento", "Fecha Vencimiento");
        model.addAttribute("monto", "Monto a Pagar");
        model.addAttribute("pagos", "pagos");
        model.addAttribute("cuenta", cuenta);
        model.addAttribute("titulo","Cuenta modificado ");
        return "cuenta-template/resultado";
    }

    @GetMapping("/eliminar")
    public String eliminarCuenta(Model model) {
        Cuenta cuenta = new Cuenta();
        model.addAttribute("titulo", "Buscar Cuenta");
        model.addAttribute("cuenta", cuenta);
        model.addAttribute("error", new HashMap<>());
        return "cuenta-template/eliminar";
    }

    @PostMapping("/eliminar")
    public String eliminarCuentaPro(@Valid Cuenta cuenta, BindingResult result, Model model,
                            @RequestParam(name= "idcuenta") int idcuenta) throws SQLException {

        if(result.hasGlobalErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(err ->{
                errores.put(err.getField(), "El campo ".concat(err.getField()).concat(" ").concat(err.getDefaultMessage()));
            });
            model.addAttribute("titulo", "Debe ser numero entero");
            model.addAttribute("error", errores);
            return "cuenta-template/eliminar";
        }
        CuentaManager cuentaManager = new CuentaManager();
        cuenta = cuentaManager.getByid(idcuenta);

        if(cuentaManager.delete(idcuenta)==false){
            model.addAttribute("idcuenta", "ID cuenta");
            model.addAttribute("idinscripcion", "ID Inscripcion");
            model.addAttribute("fecha", "Fecha");
            model.addAttribute("fechavencimiento", "Fecha Vencimiento");
            model.addAttribute("monto", "Monto a Pagar");
            model.addAttribute("pagos", "Pagos");
            model.addAttribute("titulo","No se puede eliminar, la cuenta esta referido a otra base de datos");
            model.addAttribute("cuenta", cuenta);
        }else{
            if(cuenta==null){
                model.addAttribute("idcuenta", "");
                model.addAttribute("idinscripcion", "");
                model.addAttribute("fecha", "");
                model.addAttribute("fechavencimiento", "");
                model.addAttribute("monto", "");
                model.addAttribute("pagos", "");
                model.addAttribute("titulo","La cuenta no esta en la bse de datos");
                model.addAttribute("cuenta", cuenta);
            }
            else{
                model.addAttribute("idcuenta", "ID cuenta");
                model.addAttribute("idinscripcion", "ID Inscripcion");
                model.addAttribute("fecha", "Fecha");
                model.addAttribute("fechavencimiento", "Fecha Vencimiento");
                model.addAttribute("monto", "Monto a Pagar");
                model.addAttribute("pagos", "Pagos");
                model.addAttribute("titulo","No se puede eliminar, la cuenta esta referido a otra base de datos");
                model.addAttribute("cuenta", cuenta);
            }
        }

        return "cuenta-template/resultado";

    }

    @GetMapping("/pagar")
    public String pagarCuenta(Model model) {
        Cuenta cuenta = new Cuenta();
        model.addAttribute("titulo", "Buscar Cuenta");
        model.addAttribute("cuenta", cuenta);
        model.addAttribute("error", new HashMap<>());
        return "cuenta-template/pagar";
    }

    @PostMapping("/pagar")
    public String pagarCuentaPro(@Valid Cuenta cuenta, BindingResult result, Model model,
                                  @RequestParam(name= "idcuenta") int idcuenta,
                                  @RequestParam(name= "pagos") byte pagos) throws SQLException {

        if(result.hasGlobalErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(err ->{
                errores.put(err.getField(), "El campo ".concat(err.getField()).concat(" ").concat(err.getDefaultMessage()));
            });
            model.addAttribute("titulo", "Debe ser numero entero");
            model.addAttribute("error", errores);
            return "cuenta-template/pagar";
        }
        CuentaManager cuentaManager = new CuentaManager();
        cuenta = cuentaManager.getByid(idcuenta);
        Timestamp datemodify = new Timestamp(System.currentTimeMillis());
        Timestamp date = new Timestamp(System.currentTimeMillis());
        date = cuenta.getFecha();
        datemodify.setMonth(date.getMonth()+pagos+1);
        int montoConstante = cuentaManager.sacarCuentaConstante();
        int montonew = montoConstante-pagos*(montoConstante/4);
        cuentaManager.modify(idcuenta,datemodify,montonew,pagos);
        cuenta = cuentaManager.getByid(idcuenta);
        model.addAttribute("idcuenta", " ID cuenta");
        model.addAttribute("idinscripcion", " ID Inscripcion");
        model.addAttribute("fecha", "Fecha de cuota generada ");
        model.addAttribute("fechavencimiento", "Fecha Vencimiento siguiente");
        model.addAttribute("monto", " Falta pagar ");
        model.addAttribute("pagos", " Pagos");
        model.addAttribute("titulo"," Pago realizado");
        model.addAttribute("cuenta", cuenta);
        return "cuenta-template/resultado";

    }
}
