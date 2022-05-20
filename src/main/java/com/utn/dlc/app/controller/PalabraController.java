package com.utn.dlc.app.controller;
import com.utn.dlc.app.entity.Documento;
import com.utn.dlc.app.entity.Palabra;
import com.utn.dlc.app.repository.PalabraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping(path = "/palabras")

public class PalabraController {

    @Autowired
    private PalabraRepository palabraRepository;

    @PostMapping(path = "/add")
    public @ResponseBody String addNewPalabra(@RequestParam String nombre, int cant_documentos){
        Palabra pal= new Palabra();
        pal.setNombre(nombre);
        pal.setCantDocumentos(cant_documentos);
        palabraRepository.save(pal);
        return "Saved";
    }

    @PostMapping(path = "/add-all")
    public ResponseEntity<List<Palabra>> addAllPalabras(List<Palabra> palabras){
        return new ResponseEntity<>(palabraRepository.saveAll(palabras), HttpStatus.OK);
    }

    @GetMapping(path= "/all")
    public @ResponseBody Iterable<Palabra> getAllPalabras(){
        ResponseEntity.ok().build();
        return palabraRepository.findAll();
    }

    @GetMapping(path = "/id")
    public @ResponseBody Optional<Palabra> getPalabraByID(@RequestParam String nombre) {
        return palabraRepository.findById(nombre);
    }

    @DeleteMapping(path = "/delete")
    public @ResponseBody String deleteByID(@RequestParam String nombre){

        if (palabraRepository.existsById(nombre)) {
            palabraRepository.deleteById(nombre);
            return "Deleted";
        }
        else
            return "404 Not Found";
    }

    @PutMapping(path = "/put")
    public @ResponseBody String putPalabra(@RequestParam String nombre, int cant_documentos){
        Palabra pal = new Palabra();
        pal.setNombre(nombre);
        pal.setCantDocumentos(cant_documentos);
        palabraRepository.saveAndFlush(pal);
        return "Saved";
    }
}
