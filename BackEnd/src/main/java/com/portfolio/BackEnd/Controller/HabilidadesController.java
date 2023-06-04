package com.portfolio.BackEnd.Controller;

import com.portfolio.BackEnd.Dto.DtoHabilidades;
import com.portfolio.BackEnd.Entity.Habilidades;
import com.portfolio.BackEnd.Interface.IHabilidadesService;
import com.portfolio.BackEnd.Security.Controller.Mensaje;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:4200")

@RestController
@RequestMapping("/api/habilidades")

public class HabilidadesController {
    
    @Autowired IHabilidadesService iHabilidadesService;
    
    @GetMapping("/get")
    public ResponseEntity<List<Habilidades>> getHabilidades(){
        List<Habilidades> list = iHabilidadesService.getHabilidades();
        return new ResponseEntity(list, HttpStatus.OK);
    }
    
    @PostMapping("/save")
    public ResponseEntity<?> saveHabilidades(@RequestBody DtoHabilidades dtohab){
        
        //vemos q el nombre no este en blanco
        if (StringUtils.isBlank(dtohab.getNombreHabilidad())){
            return new ResponseEntity(new Mensaje("El nombre de la habilidad no puede estar vacía"), HttpStatus.BAD_REQUEST);
        }
        //vemos q no se repita el nombre de institucion
        if (iHabilidadesService.existsByNombreHabilidades(dtohab.getNombreHabilidad())){
            return new ResponseEntity(new Mensaje("Ese nombre de habilidad ya existe"), HttpStatus.BAD_REQUEST);
        }
        // si no hubo errores grabamos datos del DTO a nuevo Objeto para grabarlo
        Habilidades habilidadNueva = new Habilidades(dtohab.getNombreHabilidad(),
                                                dtohab.getPorcentaje());
        iHabilidadesService.saveHabilidades(habilidadNueva);
        
        return new ResponseEntity(new Mensaje("Habilidad Registrada Correctamente"), HttpStatus.OK);
    }
    
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateHabilidades(@PathVariable("id") int id, @RequestBody DtoHabilidades dtoHab){
        // Vemos si existe el id recibido
        if (!iHabilidadesService.existsById(id)){
            return new ResponseEntity(new Mensaje("El id no existe"), HttpStatus.BAD_REQUEST);
        }
        // check si la habilidad se repite en otro ID
        if (iHabilidadesService.existsByNombreHabilidades(dtoHab.getNombreHabilidad()) &&
                iHabilidadesService.getByNombreHabilidades(dtoHab.getNombreHabilidad()).get().getId() != id){
            return new ResponseEntity(new Mensaje("Esa habilidad existe en otro registro"), HttpStatus.OK);
        }
        // revisamos que no este en blanco el nombre
        if (StringUtils.isBlank(dtoHab.getNombreHabilidad())){
            return new ResponseEntity(new Mensaje("El nombre de habilidad no puede estar vacío"), HttpStatus.BAD_REQUEST);
        }
        // armamos objeto nuevo con los actuales previo a actualizar
        Habilidades habilidadActualizada = iHabilidadesService.getOneHabilidades(id).get();
        // asignamos datos del DTO al objeto a modificar
        habilidadActualizada.setNombreHabilidad(dtoHab.getNombreHabilidad());
        habilidadActualizada.setPorcentaje(dtoHab.getPorcentaje());
        // grabamos obejto
        iHabilidadesService.saveHabilidades(habilidadActualizada);
        return new ResponseEntity(new Mensaje("Habilidad Registrada"), HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteHabilidades(@PathVariable("id") int id){
        // Validamos si existe el Id pasado
        if (!iHabilidadesService.existsById(id)){
            return new ResponseEntity(new Mensaje("El ID no existe"), HttpStatus.BAD_REQUEST);
        }
        iHabilidadesService.deleteHabilidades(id);
        
        return new ResponseEntity(new Mensaje("Habilidad borrada"), HttpStatus.OK);
    }
    
    @GetMapping("/detail/{id}")
    public ResponseEntity<Habilidades> getById(@PathVariable("id") int id){
        // Validamos si existe el Id pasado
        if (!iHabilidadesService.existsById(id)){
            return new ResponseEntity(new Mensaje("El ID no existe"), HttpStatus.NOT_FOUND);
        }
        Habilidades habDetail = iHabilidadesService.getOneHabilidades(id).get();
        return new ResponseEntity(habDetail, HttpStatus.OK);
        }
}
